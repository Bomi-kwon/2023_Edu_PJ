package com.koreaIT.project.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Homework {
	private int id;
	private int memberId;
	private int hwPerfection;
	private String hwMsg;
	private int classId;
	private int relId;
	
	private String name;
	
}
