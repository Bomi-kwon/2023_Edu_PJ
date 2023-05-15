package com.koreaIT.project.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.koreaIT.project.vo.Article;

@Mapper
public interface ArticleRepository {

	public List<Article> getArticles();

}
