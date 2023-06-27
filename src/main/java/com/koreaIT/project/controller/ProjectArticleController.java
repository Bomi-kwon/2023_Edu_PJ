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

	/**
	 * 게시물 리스트
	 * @param model
	 * @param boardId 어떤 게시판인지가 중요 (공부인증 게시판은 형식 다름)
	 * @return 게시물 리스트
	 */
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
	
	
	/**
	 * 게시물 작성
	 * @param boardId 어떤 게시판인지
	 * @param model 게시판 번호를 담아 보내기
	 * @return 작성 페이지
	 */
	@RequestMapping("/project/article/write")
	public String write(Model model, int boardId) {
		
		model.addAttribute("boardId", boardId);
		
		return "project/article/write";
	}
	
	/**
	 * 게시물 최종 작성
	 * @param title 제목
	 * @param classId 반 번호
	 * @param deadLine 제출 기한(null 가능)
	 * @param body 내용
	 * @param boardId 게시판 번호
	 * @param file 파일(있으면)
	 * @param youTubeLink 동영상 링크 (있으면)
	 * @return 게시물 작성 결과 보여주고 list 페이지로 돌아가기
	 * @throws Exception
	 */
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
		
		// 일단 게시물 먼저 작성!!
		articleService.doWrite(rq.getLoginedMemberId(),title, classId, deadLine, body, boardId);
		int id = articleService.getLastId();
		
		
		// 만약 파일을 첨부했다면 파일 table에 따로 article id를 relId로 해서 insert
		if(!file.isEmpty() || file != null) {
			fileService.saveFile(file, "article", id);
		}
		
		// 만약 동영상 링크 첨부했다면 그 정보만 update
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
				Member member = memberService.getMemberById(visitHistory.getMemberId());
				if(member != null) {
					visitors.add(member);
				}
			}
			model.addAttribute("visitors",visitors);
		}

		model.addAttribute("article",article);
		model.addAttribute("group",group);
		model.addAttribute("replies",replies);
		
		return "project/article/detail";
	}
	
	
	/**
	 * 게시물 삭제
	 * @param id 게시물 번호
	 * @param boardId 게시판 번호(를 이용해서 해당 게시판으로 location)
	 * @param memberId 게시물 작성자(와 현재 로그인된 멤버가 일치하는지 확인하려고)
	 * @return 완료 메시지 보여주고 다시 게시판 리스트 페이지 보여주기
	 */
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
		
		if(boardId == 3) {
			scoreService.doScoreDelete(id);
		}
		
		return Util.jsReplace(Util.f("%d번 게시물을 삭제했습니다.", id),Util.f("list?boardId=%d", boardId));
	}
	
	
	/**
	 * 리스트에서 게시물들 여러개 체크해서 삭제
	 * @param ids 체크한 게시물 번호들 (콤마로 구분해서 한줄의 문자열로 만들어놨음)
	 * @param boardId 어떤 게시판인지
	 * @return 게시물 삭제 여부 alert로 보여주고 페이지 새로고침
	 */
	@RequestMapping("/project/article/doDeleteArticles")
	@ResponseBody
	public String doDeleteArticles(@RequestParam(defaultValue = "") String ids, int boardId) {
		// 체크된 박스들 번호를 나열해서 String으로 받아왔음
		
		if (Util.empty(ids)) {
			return Util.jsHistoryBack("선택한 게시물이 없습니다");
		}
		
		List<Integer> articleIds = new ArrayList<>();
		
		// 콤마 기준으로 다시 구분하고 int로 형변환해서 하나씩 리스트에 담기
		for (String idStr : ids.split(",")) {
			articleIds.add(Integer.parseInt(idStr));
		}
		
		// 삭제할 글 번호들을 모은 리스트를 한꺼번에 보내주기
		articleService.deleteArticles(articleIds);
		
		return Util.jsReplace("선택한 게시물들이 삭제되었습니다", Util.f("list?boardId=%d", boardId) );
	}
	
	
	/**
	 * 게시물 수정
	 * @param id 게시물 번호
	 * @param memberId 게시물 작성자
	 * @param model 게시물 정보 보내주기 (수정 전 원래 내용 보여줘야 하니까)
	 * @return 수정 페이지
	 */
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
		
		if(file != null) {
			model.addAttribute("file",file);
		}
		
		model.addAttribute("article", article);
		
		if(article.getBoardId() == 3) {
			Group group = groupService.getGroupById(article.getClassId());
			
			List<Group> groupList = groupService.getGroupsByGrade(group.getGrade());
			
			model.addAttribute("group", group);
			model.addAttribute("groupList", groupList);
		}
		
		return "project/article/modify";
	}
	
	
	/**
	 * 게시물 최종 수정
	 * @param id 게시물 번호
	 * @param title 제목
	 * @param body 내용
	 * @param memberId 작성자 (권한 체크)
	 * @param boardId 게시판 (권한 없으면 list로 돌아가야되서 필요)
	 * @param fileId 기존 파일 번호
	 * @param file 새로운 파일
	 * @param youTubeLink 유튜브링크
	 * @return 완료메시지 보여주고 수정된 detail 보여주기
	 * @throws IOException
	 */
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
	
	
	
	/**
	 * 성적 게시물 detail
	 * @param relId 게시물번호(를 이용해서 성적 가져오기)
	 * @param model 성적(+평균,최고,최저 점수), 학생 수, 방문자들, 게시물 보내주기
	 * @return 성적 게시물 detail 페이지
	 * 
	 * 왜 성적 게시물만 detail 함수 따로 뺌??
	 * 성적을 입력하는 방식 : 1.게시물 작성 2.성적 작성 이렇게 동기로 처리해서
	 * 그리고 detail 페이지에서 보여줘야 되는 정보도 너무 달라 이것만 따로 빼서 처리
	 */
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
	
	
	/**
	 * 학년 선택하면 반 나오게 하기 ajax 함수
	 * @param grade 학년 입력
	 * @return 그 학년에 맞는 반 리스트
	 * 
	 * 아주 여러곳에서 쓰이는 함수!
	 */
	@RequestMapping("/project/article/setSelectBox")
	@ResponseBody
	public ResultData setSelectBox(String grade) {
		List<Group> groups = groupService.getGroupsByGrade(grade);
		
		if(groups.isEmpty()) {
			return ResultData.from("F-1", "선택한 학년에 맞는 반이 없습니다");
		}
		
		return ResultData.from("S-1", "선택한 학년에 맞는 반을 가져왔습니다", "groups", groups);
	}
	
	
	
	/**
	 * 성적게시물 입력후, '점수' 작성 페이지로 넘겨주기
	 * @param title 시험제목
	 * @param classId 대상 반
	 * @param regDate 시험일자
	 * @param body 교사의견
	 * @param model 성적게시물과 반학생 명단 넘겨주기
	 * @return 점수 작성 페이지
	 */
	@RequestMapping("/project/article/score")
	public String score(Model model, String title, int classId, String regDate, String body) {
		
		List<Member> members = memberService.getStudentsByClass(classId);
		
		if(members.isEmpty()) {
			return rq.jsReturnOnView("이 반에는 학생이 없습니다.", true);
		}
		
		// 게시물 먼저 저장하고 점수 작성 페이지로 바로 넘겨주기
		articleService.doWrite(rq.getLoginedMemberId(), title, classId, regDate, body, 3);
		
		Article article = articleService.getArticleById(articleService.getLastId());
		
		model.addAttribute("article", article);
		model.addAttribute("members", members);
		
		return "project/article/score";
	}
	
	
	/**
	 * 성적 게시물과 연결된 score 최종 작성
	 * @param scorelist score.jsp에서 리스트로 받아온 성적들
	 * @return 완료 메시지 보여주고 성적게시판 리스트 보여주기
	 */
	@RequestMapping("/project/article/doWriteScoreArticle")
	@ResponseBody
	public String doWriteScoreArticle(ScoreList scorelist) {
		
		// 사람마다 한 세트씩 받아온 리스트를 재가공하고
		List<Score> scoreList = scorelist.getScorelist();
		
		for(Score score : scoreList) {
			
			if(score != null) {
				// 하나씩 풀어서 score 테이블에 insert 해주기
				scoreService.insertScore(score.getMemberId(), score.getScore(), score.getClassId(), score.getRelId());
			}
		}
		
		return Util.jsReplace("성적을 입력했습니다.", "list?boardId=3");
	}
	
	
	
	/**
	 * 성적 게시글 수정 후 '점수' 수정 페이지로 넘겨주기
	 * @param id 글 번호
	 * @param memberId 작성자
	 * @param title 제목
	 * @param classId 수정한 대상반
	 * @param regDate 시험일자
	 * @param body 내용
	 * @param model 을 반영해서 게시물을 바꾸고 그 글과 연결된 성적 수정 페이지로 넘겨줌
	 * @return 성적 수정 페이지
	 */
	@RequestMapping("/project/article/scoremodify")
	public String scoremodify(Model model, int id, int memberId, String title, int classId, String regDate, String body) {
		
		if(rq.getLoginedMemberId() != memberId) {
			rq.jsPrintHistoryBack("성적 게시물 수정 권한이 없습니다.");
		}
		
		articleService.doScoreArticleModify(id, title, classId, regDate, body);
		
		Article article = articleService.getArticleById(id);
		
		List<Score> scores = scoreService.getScoresByRelId(id);
		
		model.addAttribute("article", article);
		model.addAttribute("scores", scores);
		
		return "project/article/scoremodify";
	}
	
	
	/**
	 * 성적 '점수' 최종 수정
	 * @param articleId 점수와 연결된 글 번호
	 * @param scorelist form에서 받아온 점수 리스트
	 * @return 완료메시지 보여주고 성적 게시판 리스트 보여주기
	 */
	@RequestMapping("/project/article/doModifyScoreArticle")
	@ResponseBody
	public String doModifyScoreArticle(int articleId, ScoreList scorelist) {
		
		List<Score> scoreList = scorelist.getScorelist();
		
		for(Score score : scoreList) {
			
			if(score != null) {
				scoreService.updateScore(score.getId(), score.getScore());
			}
		}
		
		return Util.jsReplace("성적을 수정했습니다.", "list?boardId=3");
	}
	
	
	/**
	 * 성적 게시물과 숙제검사 모달창에서 해당 반 학생 명단 가져오는 ajax 함수
	 * @param classId 반 번호
	 * @return 로 가져온 학생 리스트
	 */
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

	
	/**
	 * 숙제검사 최종 작성
	 * @param homeworklist 모달창에 적은 숙제 리스트
	 * @return 완료메시지 보낸후 그 게시물 detail 페이지 보여주기
	 */
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
		
		
	/**
	 * 숙제 삭제
	 * @param relId 숙제 게시물 번호
	 * @param memberId 작성자 (권한 체크에 필요)
	 * @return 완료메시지 보여준 후 그 게시물 페이지 다시 보여주기
	 */
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
		
		// 내가 form안에서 리스트로 받아온 숙제정보 다시 꺼내서 리스트에 담기
		List<Homework> homeworkList = homeworklist.getHomeworklist();
		
		if(homeworkList.isEmpty()) {
			return Util.jsHistoryBack("숙제 검사 결과를 작성해주세요.");
		}
		
		// 리스트 돌면서 숙제 정보 하나씩 업데이트 해주기
		for(Homework homework : homeworkList) {
			if(homework != null) {
				homeworkService.updateHw(homework.getId(), homework.getHwPerfection(), homework.getHwMsg());
			}
		}
		
		return Util.jsReplace("숙제를 수정했습니다.", Util.f("detail?id=%d", relId));
	}
	
	
	/**
	 * 게시판 당 게시물 하루 한개 작성 제한. 오늘 내가 쓴 글 있나 확인하기
	 * @param today 오늘날짜
	 * @param boardId 게시판
	 * @param loginedMemberId 현재 로그인한 멤버의 아이디
	 * @return 글 쓸 수 있는지 여부 (type : resultdata)
	 */
	@RequestMapping("/project/article/getArticleNumLimit")
	@ResponseBody
	public ResultData getArticleNumLimit(String today, int boardId, int loginedMemberId) {

		// 오늘 내가 이 게시판에 쓴 글 있나 확인하기
		List<Article> articles = articleService.getArticleNumLimit(today, boardId, loginedMemberId);
		
		if(articles.size() != 0) {
			return ResultData.from("F-1", "공부인증 게시판에는 하루에 글 하나씩만 쓸 수 있습니다.");
		}
		
		return ResultData.from("S-1", "오늘 이 게시판에 쓴 글이 없으므로 글 쓰기가 가능합니다.");
	}
	
	
	
}
