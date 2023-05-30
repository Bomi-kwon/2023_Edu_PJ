package com.koreaIT.project.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.koreaIT.project.repository.HomeworkRepository;
import com.koreaIT.project.vo.Homework;

@Service
public class HomeworkService {
	HomeworkRepository homeworkRepository;

	public HomeworkService(HomeworkRepository homeworkRepository) {
		this.homeworkRepository = homeworkRepository;
	}

	public void insertHw(int memberId, int hwPerfection, String hwMsg, int classId, int relId) {
		homeworkRepository.insertHw(memberId, hwPerfection, hwMsg, classId, relId);
	}

	public List<Homework> getHwsByRelId(int relId) {
		return homeworkRepository.getHwsByRelId(relId);
	}

	public void doHwDelete(int relId) {
		homeworkRepository.doHwDelete(relId);
	}

	public void updateHw(int id, int hwPerfection, String hwMsg) {
		homeworkRepository.updateHw(id, hwPerfection, hwMsg);
	}


}
