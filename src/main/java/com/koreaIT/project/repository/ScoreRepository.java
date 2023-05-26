package com.koreaIT.project.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.koreaIT.project.vo.Score;

@Mapper
public interface ScoreRepository {

	List<Score> getScoresByRelId(int relId);

	void insertScore(int memberId, int score, int classId, int relId);

	void doScoreDelete(int relId);

	void updateScore(int id, int score);
	
}
