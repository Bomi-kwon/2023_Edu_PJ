package com.koreaIT.project.repository;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HomeworkRepository {

	void insertHw(int memberId, int hwPerfection, String hwMsg, int classId, int relId);

	
}
