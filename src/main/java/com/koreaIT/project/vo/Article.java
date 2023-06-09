package com.koreaIT.project.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {

	private int id;
	private String regDate;
	private String updateDate;
	private int memberId;
	private int boardId;
	private String title;
	private String body;
	private int hit;
	private int classId;
	private String deadLine;
	
	private String groupName;
	private String writerName;
	private String hwChk;
	private String youTubeLink;
	private int fileId;
	
	public String getForPrintBody() {
		return this.body.replaceAll("\n", "<br />");
	}
}
