package com.koreaIT.project.vo;

import lombok.Data;

@Data
public class SendMessageRequest {

	private String phoneNumber;
	private String message;
	
	public SendMessageRequest() {
		
	}
	
	public SendMessageRequest(String phoneNumber, String message) {
		this.phoneNumber = phoneNumber;
		this.message = message;
	}
	
	
	
}
