package com.koreaIT.project.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.koreaIT.project.vo.Article;

@Mapper
public interface ArticleRepository {

	public List<Article> getArticles(int boardId);

	public void doWrite(int loginedMemberId, String title, int classId, String deadLine, String body, int boardId);

	public int getLastId();

	public Article getArticleById(int id);

	public void doDelete(int id);

	public void doModify(int id, String title, String body, String youTubeLink);

	public void doScoreArticleModify(int id, String title, int classId, String regDate, String body);

	public void addYouTubeLink(int id, String youTubeLink);

	public List<Article> getArticlesForStudyList();

	public void increaseHit(int id);

	public List<Article> getArticleNumLimit(String today, int boardId, int loginedMemberId);

}
