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
		
		articleService.doHomeworkWrite(title, body);
		int id = articleService.getLastId();
		
		return Util.jsReplace(Util.f("%d번 숙제가 등록되었습니다.",id), "homeworklist");
	}
	
	@RequestMapping("/project/article/homeworkdetail")
	public String homeworkdetail(int id) {
		
		Article article = articleService.getArticleById(id);
		
		return "project/article/homeworkdetail";
	}
}
