package com.koreaIT.project.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.koreaIT.project.service.ArticleService;
import com.koreaIT.project.util.Util;
import com.koreaIT.project.vo.Article;

@Controller
public class ProjectArticleController {
	ArticleService articleService;
	
	public ProjectArticleController(ArticleService articleService) {
		this.articleService = articleService;
	}

	@RequestMapping("/project/article/homeworklist")
	public String showList(Model model) {
		
		List<Article> articles = articleService.getArticles();
		model.addAttribute("articles", articles);
		
		return "project/article/homeworklist";
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
		
		return Util.jsReplace(Util.f("%d번 숙제가 등록되었습니다.",id), "homeworklist");
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
		
		return Util.jsReplace(Util.f("%d번 게시물을 삭제했습니다.", id),"homeworklist");
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
