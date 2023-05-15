package com.koreaIT.project.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.koreaIT.project.service.ArticleService;
import com.koreaIT.project.vo.Article;

@Controller
public class ProjectArticleController {
	ArticleService articleService;
	
	public ProjectArticleController(ArticleService articleService) {
		this.articleService = articleService;
	}

	@RequestMapping("/project/article/list")
	public String showList(Model model) {
		
		List<Article> articles = articleService.getArticles();
		model.addAttribute("articles", articles);
		
		return "project/article/list";
	}
}
