package com.koreaIT.project.service;

import org.springframework.stereotype.Service;

import com.koreaIT.project.repository.HomeworkRepository;

@Service
public class HomeworkService {
	HomeworkRepository homeworkRepository;

	public HomeworkService(HomeworkRepository homeworkRepository) {
		this.homeworkRepository = homeworkRepository;
	}

	public void insertHw(int memberId, int hwPerfection, String hwMsg, int classId, int relId) {
		homeworkRepository.insertHw(memberId, hwPerfection, hwMsg, classId, relId);
	}


}
