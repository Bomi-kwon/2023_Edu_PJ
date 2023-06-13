package com.koreaIT.project.repository;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AttendanceRepository {

	void insertAttendance(int classId, int memberId);

	
}
