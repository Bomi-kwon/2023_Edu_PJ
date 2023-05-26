package com.koreaIT.project.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.koreaIT.project.service.ArticleService;
import com.koreaIT.project.service.BoardService;
import com.koreaIT.project.service.GroupService;
import com.koreaIT.project.service.MemberService;
import com.koreaIT.project.service.ScoreService;
import com.koreaIT.project.util.Util;
import com.koreaIT.project.vo.Article;
import com.koreaIT.project.vo.Board;
import com.koreaIT.project.vo.Group;
import com.koreaIT.project.vo.Member;
import com.koreaIT.project.vo.ResultData;
import com.koreaIT.project.vo.Rq;
import com.koreaIT.project.vo.Score;
import com.koreaIT.project.vo.ScoreList;

@Controller
public class ProjectArticleController {
	ArticleService articleService;
	MemberService memberService;
	BoardService boardService;
	ScoreService scoreService;
	GroupService groupService;
	Rq rq;
	
	public ProjectArticleController(ArticleService articleService, MemberService memberService,
			BoardService boardService, ScoreService scoreService, GroupService groupService, Rq rq) {
		this.articleService = articleService;
		this.memberService = memberService;
		this.boardService = boardService;
		this.scoreService = scoreService;
		this.groupService = groupService;
		this.rq = rq;
	}

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
	
	@RequestMapping("/project/article/write")
	public String write(Model model, int boardId) {
		
		model.addAttribute("boardId", boardId);
		
		return "project/article/write";
	}
	
	@RequestMapping("/project/article/doWrite")
	@ResponseBody
	public String doWrite(String title, int classId, String deadLine, String body, int boardId) {
		
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
		
		return Util.jsReplace(Util.f("%d번 게시물이 등록되었습니다.",id), Util.f("list?boardId=%d", boardId));
	}
	
	@RequestMapping("/project/article/detail")
	public String detail(int id, Model model) {
		
		Article article = articleService.getArticleById(id);
		
		model.addAttribute("article",article);
		
		return "project/article/detail";
	}
	
	@RequestMapping("/project/article/doHomeworkDelete")
	@ResponseBody
	public String doHomeworkDelete(int id, int boardId, int memberId) {
		
		if(rq.getLoginedMemberId() != memberId) {
			return Util.jsReplace("게시물 삭제 권한이 없습니다.",Util.f("list?boardId=%d", boardId));
		}
		
		Article article = articleService.getArticleById(id);
		if(article == null) {
			return Util.f("%d번 게시물은 존재하지 않습니다.", id);
		}
		
		articleService.doHomeworkDelete(id);
		
		return Util.jsReplace(Util.f("%d번 게시물을 삭제했습니다.", id),Util.f("list?boardId=%d", boardId));
	}
	
	@RequestMapping("/project/article/modify")
	public String modify(Model model, int id, int memberId) {
		
		if(rq.getLoginedMemberId() != memberId) {
			return Util.jsHistoryBack("게시물 수정 권한이 없습니다.");
		}
		
		Article article = articleService.getArticleById(id);
		if(article == null) {
			return Util.f("%d번 게시물은 존재하지 않습니다.", id);
		}
		model.addAttribute("article", article);
		
		return "project/article/modify";
	}
	
	@RequestMapping("/project/article/doModify")
	@ResponseBody
	public String doModify(int id, String title, String body, int memberId, int boardId) {
		
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
		
		articleService.doModify(id, title, body);
		
		return Util.jsReplace(Util.f("%d번 게시물을 수정하였습니다.",id), Util.f("detail?id=%d", id));
	}
	
	@RequestMapping("/project/article/scorelist")
	public String scorelist(Model model) {
		
		int boardId = 3;
		
		List<Article> articles = articleService.getArticles(boardId);
		
		model.addAttribute("articles", articles);
		
		return "project/article/scorelist";
	}
	
	@RequestMapping("/project/article/scoredetail")
	public String scoredetail(int relId, Model model) {
		
		List<Score> scores = scoreService.getScoresByRelId(relId);
		
		Article article = articleService.getArticleById(relId);
		
		model.addAttribute("scores", scores);
		model.addAttribute("article",article);
		
		
		return "project/article/scoredetail";
	}
	
	@RequestMapping("/project/article/setSelectBox")
	@ResponseBody
	public ResultData setSelectBox(String grade) {
		List<Group> groups = groupService.getGroupsByGrade(grade);
		
		if(groups.isEmpty()) {
			return ResultData.from("F-1", "선택한 학년에 맞는 반이 없습니다");
		}
		
		return ResultData.from("S-1", "선택한 학년에 맞는 반을 가져왔습니다", "groups", groups);
	}
	
	@RequestMapping("/project/article/scorearticlewrite")
	public String scorearticlewrite() {
		return "project/article/scorearticlewrite";
	}
	
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
	
	@RequestMapping("/project/article/getStudentsByClass")
	@ResponseBody
	public ResultData getStudentsByClass(int classId) {
		
		List<Member> members = memberService.getStudentsByClass(classId);
		
		if(members.isEmpty()) {
			return ResultData.from("F-1", "선택한 반에는 학생이 없습니다.");
		}
		
		return ResultData.from("S-1", "선택한 반에 맞는 학생 리스트를 가져왔습니다", "members", members);
	}
	
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
		
		articleService.doHomeworkDelete(id);
		scoreService.doScoreDelete(id);
		
		return Util.jsReplace(Util.f("%d번 게시물을 삭제했습니다.", id), "scorelist");
	}
	
	@RequestMapping("/project/article/scorearticlemodify")
	public String scorearticlemodify(Model model, int id) {
		
		Article article = articleService.getArticleById(id);
		model.addAttribute("article", article);
		
		return "project/article/scorearticlemodify";
	}
	
	@RequestMapping("/project/article/scoremodify")
	public String scoremodify(Model model, int id, int memberId, String title, int classId, String regDate) {
		
		if(rq.getLoginedMemberId() != memberId) {
			rq.jsPrintHistoryBack("성적 게시물 수정 권한이 없습니다.");
		}
		
		//"." 은 내용이 NOT NULL이라서 일단 아무거나 넣어놓은것!!
		articleService.doScoreArticleModify(id, title, classId, regDate);
		
		Article article = articleService.getArticleById(id);
		
		List<Score> scores = scoreService.getScoresByRelId(id);
		
		model.addAttribute("article", article);
		model.addAttribute("scores", scores);
		
		return "project/article/scoremodify";
	}
	
	@RequestMapping("/project/article/doModifyScoreArticle")
	@ResponseBody
	public String doModifyScoreArticle(ScoreList scorelist) {
		
		List<Score> scoreList = scorelist.getScorelist();
		
		for(Score score : scoreList) {
			
			if(score != null) {
				scoreService.updateScore(score.getId(), score.getScore());
			}
		}
		
		return Util.jsReplace("성적을 수정했습니다.", "scorelist");
	}
	
}
