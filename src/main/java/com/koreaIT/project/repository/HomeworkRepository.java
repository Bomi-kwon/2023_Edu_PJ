package com.koreaIT.project.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.koreaIT.project.vo.Homework;

@Mapper
public interface HomeworkRepository {

	void insertHw(int memberId, int hwPerfection, String hwMsg, int classId, int relId);

	List<Homework> getHwsByRelId(int relId);

	void doHwDelete(int relId);

	void updateHw(int id, int hwPerfection, String hwMsg);

	
}
