package com.koreaIT.project.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {
	private int id;
	private String todayDateAndTime;
	private int classId;
	private int memberId;
	
	
}
