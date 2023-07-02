package com.koreaIT.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.koreaIT.project.repository.VisitHistoryRepository;
import com.koreaIT.project.vo.Member;
import com.koreaIT.project.vo.visitHistory;

@Service
public class NotificationService {
	VisitHistoryRepository visitHistoryRepository;

	@Autowired
	public NotificationService(VisitHistoryRepository visitHistoryRepository) {
		this.visitHistoryRepository = visitHistoryRepository;
	}

	public void insertVisit(int memberId, int articleId) {
		visitHistory visitHistory = visitHistoryRepository.getHistoryByMemberIdAndArticleId(memberId, articleId);
		
		if(visitHistory != null) {
			return;
		}
		
		visitHistoryRepository.insertVisit(memberId, articleId);
	}

	public List<visitHistory> getVisitorsByArticleId(int articleId) {
		return visitHistoryRepository.getVisitorsByArticleId(articleId);
	}


}
