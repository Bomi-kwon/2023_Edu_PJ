package com.koreaIT.project.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

	private int id;
	private String regDate;
	private String title;
	private String body;
	private String link;
	
	private int checked;
	
	
	
}
