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

	public void doWrite(int loginedMemberId, String title, int classId, String deadLine, String body, int boardId) {
		articleRepository.doWrite(loginedMemberId, title, classId, deadLine, body, boardId);
	}

	public int getLastId() {
		return articleRepository.getLastId();
	}

	public Article getArticleById(int id) {
		return articleRepository.getArticleById(id);
	}

	public void doDelete(int id) {
		articleRepository.doDelete(id);
	}

	public void doModify(int id, String title, String body, String youTubeLink) {
		articleRepository.doModify(id, title, body, youTubeLink);
	}

	public void doScoreArticleModify(int id, String title, int classId, String regDate) {
		articleRepository.doScoreArticleModify(id, title, classId, regDate);
	}

	public void addYouTubeLink(int id, String youTubeLink) {
		articleRepository.addYouTubeLink(id, youTubeLink);
	}

	public void deleteArticles(List<Integer> articleIds) {
		for (int articleId : articleIds) {
			Article article = getArticleById(articleId);
			
			if (article != null) {
				doDelete(articleId);
			}
		}
	}

	public List<Article> getArticlesForStudyList() {
		return articleRepository.getArticlesForStudyList();
	}

	public void increaseHit(int id) {
		articleRepository.increaseHit(id);
	}

	public List<Article> getArticleNumLimit(String today, int boardId, int loginedMemberId) {
		return articleRepository.getArticleNumLimit(today, boardId, loginedMemberId);
	}

}
