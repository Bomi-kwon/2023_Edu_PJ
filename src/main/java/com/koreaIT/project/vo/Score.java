package com.koreaIT.project.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Score {
	private int id;
	private int memberId;
	private int score;
	private int classId;
	private int relId;
}
