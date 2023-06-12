package com.koreaIT.project.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reply {

	private int id;
	private String regDate;
	private String updateDate;
	private int replymemberId;
	private String relTypeCode;
	private int relId;
	private String replybody;
	private String writerName;
	
	private int fileId;
	
	public String getForPrintReplybody() {
		
		return this.replybody.replaceAll("\n","<br/>" );
	}
}
