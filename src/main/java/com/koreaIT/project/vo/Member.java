package com.koreaIT.project.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {

	private int id;
	private String regDate;
	private String updateDate;
	private String loginID;
	private String loginPW;
	private int authLevel;
	private String name;
	private String cellphoneNum;
	private String email;
	private int delStatus;
	private String delData;
	private int classId;
	
	private String groupName;
	private String parentPhoneNum;
	private int fileId;
	
	
}
