package com.koreaIT.project.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Group {

	private int id;
	private String grade;
	private String groupName;
	private String groupDay;
	
	private int studentNum;
}
