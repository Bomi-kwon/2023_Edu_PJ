package com.koreaIT.project.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.koreaIT.project.vo.visitHistory;

@Mapper
public interface VisitHistoryRepository {

	void insertVisit(int memberId, int articleId);

	List<visitHistory> getVisitorsByArticleId(int articleId);

	visitHistory getHistoryByMemberIdAndArticleId(int memberId, int articleId);

	
}
