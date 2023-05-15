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

	public void doHomeworkWrite(String title, String body) {
		articleRepository.doHomeworkWrite(title, body);
	}

	public int getLastId() {
		return articleRepository.getLastId();
	}

	public Article getArticleById(int id) {
		return articleRepository.getArticleById(id);
	}

}
