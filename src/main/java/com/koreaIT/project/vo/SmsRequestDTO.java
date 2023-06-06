package com.koreaIT.project.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SmsRequestDTO {
	
	// 네이버 공식문서에 나와있는 JSON 요청 Body를 잘 보고
	// 그중 내가 필요한 것만 갖다 쓰면됨!

	private String type;
	private String contentType;
	private String countryCode;
	private String from;
	private String content;
	private List<MessageDTO> messages;
	
}
