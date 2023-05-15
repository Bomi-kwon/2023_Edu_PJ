package com.koreaIT.project.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.koreaIT.project.repository.ArticleRepository;
import com.koreaIT.project.vo.Article;

@Service
public class ArticleService {
	ArticleRepository articleRepository;

	public ArticleService(ArticleRepository articleRepository) {
		this.articleRepository = articleRepository;
	}

	public List<Article> getArticles() {
		return articleRepository.getArticles();
	}

}
