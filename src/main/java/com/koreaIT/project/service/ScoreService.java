package com.koreaIT.project.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.koreaIT.project.repository.ScoreRepository;
import com.koreaIT.project.vo.Score;

@Service
public class ScoreService {
	ScoreRepository scoreRepository;

	public ScoreService(ScoreRepository scoreRepository) {
		this.scoreRepository = scoreRepository;
	}

	public List<Score> getScoresByRelId(int relId) {
		return scoreRepository.getScoresByRelId(relId);
	}

}
