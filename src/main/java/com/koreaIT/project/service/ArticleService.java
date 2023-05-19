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

	public List<Article> getArticles(int boardId) {
		return articleRepository.getArticles(boardId);
	}

	public void doWrite(String title, String body) {
		articleRepository.doWrite(title, body);
	}

	public int getLastId() {
		return articleRepository.getLastId();
	}

	public Article getArticleById(int id) {
		return articleRepository.getArticleById(id);
	}

	public void doHomeworkDelete(int id) {
		articleRepository.doHomeworkDelete(id);
	}

	public void doHomeworkModify(int id, String title, String body) {
		articleRepository.doHomeworkModify(id, title, body);
	}

}
