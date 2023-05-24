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
		
		if(title == null) {
			return Util.jsHistoryBack("제목을 입력해주세요");
		}
		
		if(body == null) {
			return Util.jsHistoryBack("내용을 입력해주세요");
		}
		
		if(boardId == 2 ) {
			if(deadLine == null) {
				return Util.jsHistoryBack("제출 기한을 입력해주세요");
			}
		}
		
		articleService.doWrite(rq.getLoginedMember().getId(),title, classId, deadLine, body, boardId);
		int id = articleService.getLastId();
		
		return Util.jsReplace(Util.f("%d번 게시물이 등록되었습니다.",id), Util.f("list?boardId=%d", boardId));
	}
	
	@RequestMapping("/project/article/homeworkdetail")
	public String homeworkdetail(int id, Model model) {
		
		Article article = articleService.getArticleById(id);
		
		model.addAttribute("article",article);
		
		return "project/article/homeworkdetail";
	}
	
	@RequestMapping("/project/article/doHomeworkDelete")
	@ResponseBody
	public String doHomeworkDelete(int id, int boardId, int memberId) {
		
		if(rq.getLoginedMember().getId() != memberId) {
			return Util.jsReplace("게시물 삭제 권한이 없습니다.",Util.f("list?boardId=%d", boardId));
		}
		
		Article article = articleService.getArticleById(id);
		if(article == null) {
			return Util.f("%d번 게시물은 존재하지 않습니다.", id);
		}
		
		articleService.doHomeworkDelete(id);
		
		return Util.jsReplace(Util.f("%d번 게시물을 삭제했습니다.", id),Util.f("list?boardId=%d", boardId));
	}
	
	@RequestMapping("/project/article/homeworkmodify")
	public String homeworkmodify(Model model, int id, int memberId) {
		
		if(rq.getLoginedMember().getId() != memberId) {
			return Util.jsHistoryBack("게시물 수정 권한이 없습니다.");
		}
		
		Article article = articleService.getArticleById(id);
		if(article == null) {
			return Util.f("%d번 게시물은 존재하지 않습니다.", id);
		}
		model.addAttribute("article", article);
		
		return "project/article/homeworkmodify";
	}
	
	@RequestMapping("/project/article/doHomeworkModify")
	@ResponseBody
	public String doHomeworkModify(int id, String title, String body, int memberId, int boardId) {
		
		if(rq.getLoginedMember().getId() != memberId) {
			return Util.jsReplace("게시물 삭제 권한이 없습니다.",Util.f("list?boardId=%d", boardId));
		}
		
		Article article = articleService.getArticleById(id);
		if(article == null) {
			return Util.f("%d번 게시물은 존재하지 않습니다.", id);
		}
		
		
		if(title == null) {
			return Util.jsHistoryBack("제목을 입력해주세요");
		}
		
		if(body == null) {
			return Util.jsHistoryBack("내용을 입력해주세요");
		}
		
		articleService.doHomeworkModify(id, title, body);
		
		return Util.jsReplace(Util.f("%d번 게시물을 수정하였습니다.",id), Util.f("homeworkdetail?id=%d", id));
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
		
		if(groups == null) {
			return ResultData.from("F-1", "선택한 학년에 맞는 반이 없습니다");
		}
		
		return ResultData.from("S-1", "선택한 학년에 맞는 반을 가져왔습니다", "groups", groups);
	}
	
	@RequestMapping("/project/article/scorewrite")
	public String scorewrite() {
		return "project/article/scorewrite";
	}
	
	@RequestMapping("/project/article/score")
	public String score(Model model, String title, int classId, String regDate) {
		
		articleService.doWrite(rq.getLoginedMember().getId(), title, classId, regDate, ".", 3);
		
		Article article = articleService.getArticleById(articleService.getLastId());
		
		List<Member> members = memberService.getStudentsByClass(classId);
		
		model.addAttribute("article", article);
		model.addAttribute("members", members);
		
		return "project/article/score";
	}
	
	@RequestMapping("/project/article/getStudentsByClass")
	@ResponseBody
	public ResultData getStudentsByClass(int classId) {
		
		List<Member> members = memberService.getStudentsByClass(classId);
		
		if(members == null) {
			return ResultData.from("F-1", "선택한 반에는 학생이 없습니다.");
		}
		
		return ResultData.from("S-1", "선택한 반에 맞는 학생 리스트를 가져왔습니다", "members", members);
	}
	
	@RequestMapping("/project/article/doWriteScoreArticle")
	@ResponseBody
	public Object doWriteScoreArticle(ScoreList scorelist) {
		
		for(Score score : scorelist) {
			
		}
		
		for(int i = 0; i < scorelist.length(); i++) {
			
		}
		
		for(int i = 0; i < scorelist.size(); i++) {
			
		}
		
		
		
		return scorelist;
	}
}
