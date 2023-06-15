package com.koreaIT.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.koreaIT.project.repository.ScoreRepository;
import com.koreaIT.project.vo.Score;

@Service
public class ScoreService {
	ScoreRepository scoreRepository;

	@Autowired
	public ScoreService(ScoreRepository scoreRepository) {
		this.scoreRepository = scoreRepository;
	}

	public List<Score> getScoresByRelId(int relId) {
		return scoreRepository.getScoresByRelId(relId);
	}

	public void insertScore(int memberId, int score, int classId, int relId) {
		scoreRepository.insertScore(memberId, score, classId, relId);
	}

	public void doScoreDelete(int relId) {
		scoreRepository.doScoreDelete(relId);
	}

	public void updateScore(int id, int score) {
		scoreRepository.updateScore(id, score);
	}

	public void deleteScores(List<Integer> articleIds) {
		for (int articleId : articleIds) {
			Score score = getScoreByRelId(articleId);
			
			if (score != null) {
				doScoreDelete(articleId);
			}
		}
	}

	private Score getScoreByRelId(int relId) {
		return scoreRepository.getScoreByRelId(relId);
	}

	public int getAverageOfScores(int relId) {
		return scoreRepository.getAverageOfScores(relId);
	}

	public Score getBestScore(int relId) {
		return scoreRepository.getBestScore(relId);
	}

	public Score getWorstScore(int relId) {
		return scoreRepository.getWorstScore(relId);
	}

}
