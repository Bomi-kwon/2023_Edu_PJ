package com.koreaIT.project.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileVO {
	private int id;
	private String orgNm;
	private String savedNm;
	private String savedPath;
	private String relTypecode;
	private int relId;
	
	
	
}
