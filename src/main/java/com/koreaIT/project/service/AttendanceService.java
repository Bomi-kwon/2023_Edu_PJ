package com.koreaIT.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.koreaIT.project.repository.AttendanceRepository;

@Service
public class AttendanceService {
	AttendanceRepository attendanceRepository;

	@Autowired
	public AttendanceService(AttendanceRepository attendanceRepository) {
		this.attendanceRepository = attendanceRepository;
	}

	public void insertAttendance(int classId, int memberId) {
		attendanceRepository.insertAttendance(classId, memberId);
	}


}
