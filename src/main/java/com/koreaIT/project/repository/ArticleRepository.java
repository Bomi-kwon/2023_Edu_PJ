package com.koreaIT.project.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.koreaIT.project.vo.Article;

@Mapper
public interface ArticleRepository {

	public List<Article> getArticles(int boardId);

	public void doHomeworkWrite(String title, String body);

	public int getLastId();

	public Article getArticleById(int id);

	public void doHomeworkDelete(int id);

	public void doHomeworkModify(int id, String title, String body);

}
