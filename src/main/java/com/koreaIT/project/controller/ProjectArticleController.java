package com.koreaIT.project.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.koreaIT.project.service.ArticleService;
import com.koreaIT.project.service.BoardService;
import com.koreaIT.project.util.Util;
import com.koreaIT.project.vo.Article;
import com.koreaIT.project.vo.Board;
import com.koreaIT.project.vo.Rq;

@Controller
public class ProjectArticleController {
	ArticleService articleService;
	BoardService boardService;
	Rq rq;
	
	public ProjectArticleController(ArticleService articleService, BoardService boardService, Rq rq) {
		this.articleService = articleService;
		this.boardService = boardService;
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
	
	@RequestMapping("/project/article/homeworkwrite")
	public String homeworkwrite() {
		
		return "project/article/homeworkwrite";
	}
	
	@RequestMapping("/project/article/doHomeworkWrite")
	@ResponseBody
	public String doHomeworkWrite(String title, String body) {
		
		if(title == null) {
			return Util.jsHistoryBack("제목을 입력해주세요");
		}
		
		if(body == null) {
			return Util.jsHistoryBack("내용을 입력해주세요");
		}
		
		articleService.doHomeworkWrite(title, body);
		int id = articleService.getLastId();
		
		return Util.jsReplace(Util.f("%d번 숙제가 등록되었습니다.",id), "list");
	}
	
	@RequestMapping("/project/article/homeworkdetail")
	public String homeworkdetail(int id, Model model) {
		
		Article article = articleService.getArticleById(id);
		
		model.addAttribute("article",article);
		
		return "project/article/homeworkdetail";
	}
	
	@RequestMapping("/project/article/doHomeworkDelete")
	@ResponseBody
	public String doHomeworkDelete(int id) {
		
		Article article = articleService.getArticleById(id);
		if(article == null) {
			return Util.f("%d번 게시물은 존재하지 않습니다.", id);
		}
		
		articleService.doHomeworkDelete(id);
		
		return Util.jsReplace(Util.f("%d번 게시물을 삭제했습니다.", id),"list");
	}
	
	@RequestMapping("/project/article/homeworkmodify")
	public String homeworkmodify(int id, Model model) {
		
		Article article = articleService.getArticleById(id);
		if(article == null) {
			return Util.f("%d번 게시물은 존재하지 않습니다.", id);
		}
		model.addAttribute("article", article);
		
		return "project/article/homeworkmodify";
	}
	
	@RequestMapping("/project/article/doHomeworkModify")
	@ResponseBody
	public String doHomeworkModify(int id, String title, String body) {
		
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
		
		return Util.jsReplace(Util.f("%d번 숙제를 수정하였습니다.",id), Util.f("homeworkdetail?id=%d", id));
	}
}
