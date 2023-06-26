package com.koreaIT.project.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.koreaIT.project.service.ArticleService;
import com.koreaIT.project.service.BoardService;
import com.koreaIT.project.service.FileService;
import com.koreaIT.project.service.GroupService;
import com.koreaIT.project.service.HomeworkService;
import com.koreaIT.project.service.MemberService;
import com.koreaIT.project.service.ReplyService;
import com.koreaIT.project.service.ScoreService;
import com.koreaIT.project.service.VisitHistoryService;
import com.koreaIT.project.util.Util;
import com.koreaIT.project.vo.Article;
import com.koreaIT.project.vo.Board;
import com.koreaIT.project.vo.FileVO;
import com.koreaIT.project.vo.Group;
import com.koreaIT.project.vo.Homework;
import com.koreaIT.project.vo.HomeworkList;
import com.koreaIT.project.vo.Member;
import com.koreaIT.project.vo.Reply;
import com.koreaIT.project.vo.ResultData;
import com.koreaIT.project.vo.Rq;
import com.koreaIT.project.vo.Score;
import com.koreaIT.project.vo.ScoreList;
import com.koreaIT.project.vo.visitHistory;

@Controller
public class ProjectArticleController {
	private ArticleService articleService;
	private MemberService memberService;
	private BoardService boardService;
	private ScoreService scoreService;
	private HomeworkService homeworkService;
	private GroupService groupService;
	private FileService fileService;
	private ReplyService replyService;
	private VisitHistoryService visitHistoryService;
	private Rq rq;
	
	@Autowired
	public ProjectArticleController(ArticleService articleService, MemberService memberService,
			BoardService boardService, ScoreService scoreService, HomeworkService homeworkService,
			GroupService groupService, FileService fileService, ReplyService replyService, 
			VisitHistoryService visitHistoryService, Rq rq) {
		this.articleService = articleService;
		this.memberService = memberService;
		this.boardService = boardService;
		this.scoreService = scoreService;
		this.homeworkService = homeworkService;
		this.groupService = groupService;
		this.fileService = fileService;
		this.replyService = replyService;
		this.visitHistoryService = visitHistoryService;
		this.rq = rq;
	}

	// 게시물 리스트
	
	@RequestMapping("/project/article/list")
	public String showList(Model model, @RequestParam(defaultValue = "2") int boardId) {
		
		Board board = boardService.getBoardById(boardId);
		
		if(board == null) {
			return rq.jsReturnOnView("존재하지 않는 게시판입니다.", true);
		}
		
		List<Article> articles = articleService.getArticles(boardId);
		
		model.addAttribute("board", board);
		model.addAttribute("articles", articles);
		
		return "project/article/list";
	}
	
	
	// 게시물 작성
	
	@RequestMapping("/project/article/write")
	public String write(Model model, int boardId) {
		
		model.addAttribute("boardId", boardId);
		
		return "project/article/write";
	}
	
	@RequestMapping("/project/article/doWrite")
	@ResponseBody
	public String doWrite(String title, int classId, String deadLine, String body, int boardId, 
			MultipartFile file, String youTubeLink) throws Exception {
		
		if(Util.empty(title)) {
			return Util.jsHistoryBack("제목을 입력해주세요");
		}
		
		if(Util.empty(body)) {
			return Util.jsHistoryBack("내용을 입력해주세요");
		}
		
		if(boardId == 2 ) {
			if(Util.empty(deadLine)) {
				return Util.jsHistoryBack("제출 기한을 입력해주세요");
			}
		}
		
		articleService.doWrite(rq.getLoginedMemberId(),title, classId, deadLine, body, boardId);
		int id = articleService.getLastId();
		
		if(!file.isEmpty() || file != null) {
			fileService.saveFile(file, "article", id);
		}
		
		if(!Util.empty(youTubeLink)) {
			articleService.addYouTubeLink(id, youTubeLink);
		}
		
		return Util.jsReplace(Util.f("%d번 게시물이 등록되었습니다.",id), Util.f("list?boardId=%d", boardId));
	}
	
	
	/**
	 * 게시물 상세보기
	 * @param id (id로 article 찾아와야함)
	 * @param model (model로 여러정보들을 물고)
	 * @return detail.jsp 페이지에 정보 뿌리기
	 */
	@RequestMapping("/project/article/detail")
	public String detail(int id, Model model) {
		
		// detail을 누르는 순간 조회수 증가
		articleService.increaseHit(id);
		
		Article article = articleService.getArticleById(id);
		
		if(article == null) {
			rq.jsPrintHistoryBack("해당 번호를 가진 게시물이 없습니다.");
		}
		
		Group group = groupService.getGroupById(article.getClassId());
		
		List<Reply> replies = replyService.getReplies("article", id);
		
		// 숙제 게시판이면 숙제 정보도 jsp로 전달
		if(article.getBoardId() == 2) {
			List<Homework> homeworks = homeworkService.getHwsByRelId(id);
			model.addAttribute("homeworks",homeworks);
		}
		
		// 연결된 파일이 있으면 걔도 jsp로 전달
		FileVO file = fileService.getFileByRelId("article", id);
		if(file != null) {
			model.addAttribute("file",file);
		}
		
		// 만약 현재 로그인된 사람 있으면 detail 클릭시 바로 방문 기록하기
		if(rq.getLoginedMemberId() != 0) {
			visitHistoryService.insertVisit(rq.getLoginedMemberId(),id);
		}
		
		// 이 게시물의 방문자 명단 가져오기
		List<visitHistory> visitorList =  visitHistoryService.getVisitorsByArticleId(id);
		List<Member> visitors = new ArrayList<>();
		if(visitorList.isEmpty() == false) {
			for(visitHistory visitHistory : visitorList) {
				visitors.add(memberService.getMemberById(visitHistory.getMemberId()));
			}
			model.addAttribute("visitors",visitors);
		}

		model.addAttribute("article",article);
		model.addAttribute("group",group);
		model.addAttribute("replies",replies);
		
		return "project/article/detail";
	}
	
	
	// 게시물 삭제
	@RequestMapping("/project/article/doDelete")
	@ResponseBody
	public String doDelete(int id, int boardId, int memberId) {
		
		if(rq.getLoginedMemberId() != memberId) {
			return Util.jsReplace("게시물 삭제 권한이 없습니다.",Util.f("list?boardId=%d", boardId));
		}
		
		Article article = articleService.getArticleById(id);
		if(article == null) {
			return Util.f("%d번 게시물은 존재하지 않습니다.", id);
		}
		
		articleService.doDelete(id);
		
		return Util.jsReplace(Util.f("%d번 게시물을 삭제했습니다.", id),Util.f("list?boardId=%d", boardId));
	}
	
	
	// 게시물들 여러개 체크해서 삭제
	@RequestMapping("/project/article/doDeleteArticles")
	@ResponseBody
	public String doDeleteArticles(@RequestParam(defaultValue = "") String ids, int boardId) {
		// 체크된 박스들 번호를 String으로 받아오기
		
		if (Util.empty(ids)) {
			return Util.jsHistoryBack("선택한 게시물이 없습니다");
		}
		
		List<Integer> articleIds = new ArrayList<>();
		
		// 콤마 기준으로 다시 구분해서 하나씩 리스트에 담기 (int로 형변환해서)
		for (String idStr : ids.split(",")) {
			articleIds.add(Integer.parseInt(idStr));
		}
		
		articleService.deleteArticles(articleIds);
		
		return Util.jsReplace("선택한 게시물이 삭제되었습니다", Util.f("list?boardId=%d", boardId) );
	}
	
	
	// 게시물 수정
	
	@RequestMapping("/project/article/modify")
	public String modify(Model model, int id, int memberId) {
		
		if(rq.getLoginedMemberId() != memberId) {
			return Util.jsHistoryBack("게시물 수정 권한이 없습니다.");
		}
		
		Article article = articleService.getArticleById(id);
		
		if(article == null) {
			return Util.f("%d번 게시물은 존재하지 않습니다.", id);
		}
		
		FileVO file = fileService.getFileByRelId("article", id);
		
		model.addAttribute("article", article);
		model.addAttribute("file",file);
		
		return "project/article/modify";
	}
	
	@RequestMapping("/project/article/doModify")
	@ResponseBody
	public String doModify(int id, String title, String body, int memberId, int boardId, 
			@RequestParam(defaultValue = "0") int fileId, MultipartFile file, String youTubeLink) throws IOException {
		
		if(rq.getLoginedMemberId() != memberId) {
			return Util.jsReplace("게시물 삭제 권한이 없습니다.",Util.f("list?boardId=%d", boardId));
		}
		
		Article article = articleService.getArticleById(id);
		
		if(article == null) {
			return Util.f("%d번 게시물은 존재하지 않습니다.", id);
		}
		
		if(Util.empty(title)) {
			return Util.jsHistoryBack("제목을 입력해주세요");
		}
		
		if(Util.empty(body)) {
			return Util.jsHistoryBack("내용을 입력해주세요");
		}
		
		if(Util.empty(youTubeLink)) {
			articleService.doModify(id, title, body, article.getYouTubeLink());
		}
		
		else {
			articleService.doModify(id, title, body, youTubeLink);
		}
		
		if(file != null && !file.isEmpty()) {
			fileService.updateFile(file, "article", id, fileId);
		}
		
		return Util.jsReplace(Util.f("%d번 게시물을 수정하였습니다.",id), Util.f("detail?id=%d", id));
	}
	
	
	// 성적 게시물 리스트
	
	@RequestMapping("/project/article/scorelist")
	public String scorelist(Model model) {
		
		int boardId = 3;
		
		List<Article> articles = articleService.getArticles(boardId);
		
		model.addAttribute("articles", articles);
		
		return "project/article/scorelist";
	}
	
	
	// 성적 게시물 디테일
	
	@RequestMapping("/project/article/scoredetail")
	public String scoredetail(int relId, Model model) {
		
		List<Score> scores = scoreService.getScoresByRelId(relId);
		if(scores.isEmpty() == false) {
			int averageOfScores = scoreService.getAverageOfScores(relId);
			Score bestScore = scoreService.getBestScore(relId);
			Score worstScore = scoreService.getWorstScore(relId);
			model.addAttribute("averageOfScores", averageOfScores);
			model.addAttribute("bestScore", bestScore);
			model.addAttribute("worstScore", worstScore);
		}
		
		Article article = articleService.getArticleById(relId);
		
		if(rq.getLoginedMemberId() != 0) {
			visitHistoryService.insertVisit(rq.getLoginedMemberId(),relId);
		}
		
		Group group = groupService.getGroupById(article.getClassId());
		if(group != null) {
			int studentNum = groupService.getStudentNumById(group.getId());
			model.addAttribute("studentNum", studentNum);
		}
		
		List<visitHistory> visitorList =  visitHistoryService.getVisitorsByArticleId(relId);
		List<Member> visitors = new ArrayList<>();
		
		if(visitorList.isEmpty() == false) {
			for(visitHistory visitHistory : visitorList) {
				visitors.add(memberService.getMemberById(visitHistory.getMemberId()));
			}
			model.addAttribute("visitors",visitors);
		}
		
		model.addAttribute("scores", scores);
		model.addAttribute("article",article);
		
		return "project/article/scoredetail";
	}
	
	
	// 성적 게시물 대상반 소분류
	
	@RequestMapping("/project/article/setSelectBox")
	@ResponseBody
	public ResultData setSelectBox(String grade) {
		List<Group> groups = groupService.getGroupsByGrade(grade);
		
		if(groups.isEmpty()) {
			return ResultData.from("F-1", "선택한 학년에 맞는 반이 없습니다");
		}
		
		return ResultData.from("S-1", "선택한 학년에 맞는 반을 가져왔습니다", "groups", groups);
	}
	
	
	// 성적 '게시물' 작성
	
	@RequestMapping("/project/article/scorearticlewrite")
	public String scorearticlewrite() {
		return "project/article/scorearticlewrite";
	}
	
	
	// 성적 '점수' 작성
	
	@RequestMapping("/project/article/score")
	public String score(Model model, String title, int classId, String regDate, String body) {
		
		List<Member> members = memberService.getStudentsByClass(classId);
		
		if(members.isEmpty()) {
			return rq.jsReturnOnView("이 반에는 학생이 없습니다.", true);
		}
		
		articleService.doWrite(rq.getLoginedMemberId(), title, classId, regDate, body, 3);
		
		Article article = articleService.getArticleById(articleService.getLastId());
		
		
		model.addAttribute("article", article);
		model.addAttribute("members", members);
		
		return "project/article/score";
	}
	
	
	// 성적 최종 작성
	
	@RequestMapping("/project/article/doWriteScoreArticle")
	@ResponseBody
	public String doWriteScoreArticle(ScoreList scorelist) {
		
		List<Score> scoreList = scorelist.getScorelist();
		
		for(Score score : scoreList) {
			
			if(score != null) {
				scoreService.insertScore(score.getMemberId(), score.getScore(), score.getClassId(), score.getRelId());
			}
		}
		
		return Util.jsReplace("성적을 입력했습니다.", "scorelist");
	}
	
	
	// 성적 점수 + 게시물 삭제
	
	@RequestMapping("/project/article/doScoreDelete")
	@ResponseBody
	public String doScoreDelete(int id, int memberId) {
		
		if(rq.getLoginedMemberId() != memberId) {
			return Util.jsHistoryBack("성적 삭제 권한이 없습니다.");
		}
		
		Article article = articleService.getArticleById(id);
		
		if(article == null) {
			return Util.jsHistoryBack(Util.f("%d번 게시물은 존재하지 않습니다.", id));
		}
		
		articleService.doDelete(id);
		scoreService.doScoreDelete(id);
		
		return Util.jsReplace(Util.f("%d번 게시물을 삭제했습니다.", id), "scorelist");
	}
	
	
	// 성적 점수 + 게시물 삭제 (체크박스로 여러개 선택)
	
	@RequestMapping("/project/article/doDeleteScoresArticles")
	@ResponseBody
	public String doDeleteScoresArticles(@RequestParam(defaultValue = "") String ids) {
		
		// 체크된 박스들 번호를 String으로 받아오기
		
		if (Util.empty(ids)) {
			return Util.jsHistoryBack("선택한 게시물이 없습니다");
		}
		
		List<Integer> articleIds = new ArrayList<>();
		
		// 콤마 기준으로 다시 구분해서 하나씩 리스트에 담기 (int로 형변환해서)
		for (String idStr : ids.split(",")) {
			articleIds.add(Integer.parseInt(idStr));
		}
		
		articleService.deleteArticles(articleIds);
		scoreService.deleteScores(articleIds);
		
		return Util.jsReplace("선택한 성적 게시물들이 삭제되었습니다", "scorelist");
	}
	
	// 성적 '게시물' 수정
	
	@RequestMapping("/project/article/scorearticlemodify")
	public String scorearticlemodify(Model model, int id) {
		
		Article article = articleService.getArticleById(id);
		
		Group group = groupService.getGroupById(article.getClassId());
		
		List<Group> groupList = groupService.getGroupsByGrade(group.getGrade());
		
		model.addAttribute("article", article);
		model.addAttribute("group", group);
		model.addAttribute("groupList", groupList);
		
		return "project/article/scorearticlemodify";
	}
	
	
	//성적 '점수' 수정
	
	@RequestMapping("/project/article/scoremodify")
	public String scoremodify(Model model, int id, int memberId, String title, int classId, String regDate) {
		
		if(rq.getLoginedMemberId() != memberId) {
			rq.jsPrintHistoryBack("성적 게시물 수정 권한이 없습니다.");
		}
		
		articleService.doScoreArticleModify(id, title, classId, regDate);
		
		Article article = articleService.getArticleById(id);
		
		List<Score> scores = scoreService.getScoresByRelId(id);
		
		model.addAttribute("article", article);
		model.addAttribute("scores", scores);
		
		return "project/article/scoremodify";
	}
	
	
	// 성적 최종 수정
	
	@RequestMapping("/project/article/doModifyScoreArticle")
	@ResponseBody
	public String doModifyScoreArticle(int articleId, ScoreList scorelist, String body) {
		
		List<Score> scoreList = scorelist.getScorelist();
		
		for(Score score : scoreList) {
			
			if(score != null) {
				scoreService.updateScore(score.getId(), score.getScore());
			}
		}
		
		Article article = articleService.getArticleById(articleId);
		
		if(article != null) {
			articleService.doModify(articleId, article.getTitle(), body, null);
		}
		
		return Util.jsReplace("성적을 수정했습니다.", "scorelist");
	}
	
	
	// 성적 게시물과 숙제검사 모달창에서 해당 반 학생 명단 가져오기
	
	@RequestMapping("/project/article/getStudentsByClass")
	@ResponseBody
	public ResultData getStudentsByClass(int classId) {
		rq.jsReturnOnView("컨트롤러로 잘 넘어옴", true);
		
		List<Member> members = memberService.getStudentsByClass(classId);
		
		if(members.isEmpty()) {
			return ResultData.from("F-1", "선택한 반에는 학생이 없습니다.");
		}
		
		return ResultData.from("S-1", "선택한 반에 맞는 학생 리스트를 가져왔습니다", "members", members);
	}

	
	// 숙제검사 최종 작성
	
	@RequestMapping("/project/article/doHwCheck")
	@ResponseBody
	public String doHwCheck(HomeworkList homeworklist) {
		
		List<Homework> homeworkList = homeworklist.getHomeworklist();
		
		int relId = 0;
		for(Homework homework : homeworkList) {
			
			if(homework != null) {
				homeworkService.insertHw(homework.getMemberId(), homework.getHwPerfection(), homework.getHwMsg(),
						homework.getClassId(), homework.getRelId());
				relId = homework.getRelId();
			}
		}
		
		return Util.jsReplace("숙제 검사를 완료했습니다.", Util.f("detail?id=%d", relId));
	}
		
		
	// 숙제 삭제
	
	@RequestMapping("/project/article/doHwDelete")
	@ResponseBody
	public String doHwDelete(int relId, int memberId) {
		
		if(rq.getLoginedMemberId() != memberId) {
			return Util.jsHistoryBack("숙제 삭제 권한이 없습니다.");
		}
		
		List<Homework> homeworks = homeworkService.getHwsByRelId(relId);
		
		if(homeworks.isEmpty()) {
			return Util.jsHistoryBack(Util.f("%d번 숙제는 존재하지 않습니다.", relId));
		}
		
		homeworkService.doHwDelete(relId);
		
		return Util.jsReplace(Util.f("%d번 숙제를 삭제했습니다.", relId), Util.f("detail?id=%d", relId));
	}
	
	
	/**
	 * 숙제 수정
	 * @param relId (article번호 가져오기)
	 * @param homeworklist (숙제 수정 내용 리스트로 가져오기)
	 * @return (숙제 업데이트 후 가져온 relId로 다시 detail 페이지 그려주기)
	 */
	@RequestMapping("/project/article/doHwModify")
	@ResponseBody
	public String doHwModify(int relId, HomeworkList homeworklist) {
		
		List<Homework> homeworkList = homeworklist.getHomeworklist();
		
		if(homeworkList.isEmpty()) {
			return Util.jsHistoryBack("숙제 검사 결과를 작성해주세요.");
		}
		
		for(Homework homework : homeworkList) {
			
			if(homework != null) {
				homeworkService.updateHw(homework.getId(), homework.getHwPerfection(), homework.getHwMsg());
			}
		}
		
		return Util.jsReplace("숙제를 수정했습니다.", Util.f("detail?id=%d", relId));
	}
	
	
	// 게시판 당 게시물 하루 한개 작성 제한 - 오늘 내가 쓴 글 있나 확인하기
	
		@RequestMapping("/project/article/getArticleNumLimit")
		@ResponseBody
		public ResultData getArticleNumLimit(String today, int boardId, int loginedMemberId) {

			List<Article> articles = articleService.getArticleNumLimit(today, boardId, loginedMemberId);
			
			if(articles.size() != 0) {
				return ResultData.from("F-1", "게시판 당 하루에 게시물 하나씩만 쓸 수 있습니다.");
			}
			
			return ResultData.from("S-1", "오늘 이 게시판에 쓴 글이 없으므로 글 쓰기가 가능합니다.");
		}
	
	
	
}
