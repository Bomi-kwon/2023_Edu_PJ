package com.koreaIT.project.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class visitHistory {
	private int id;
	private String regDate;
	private int memberId;
	private int articleId;
	
}
