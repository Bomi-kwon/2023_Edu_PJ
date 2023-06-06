package com.koreaIT.project.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDTO {
	
	// form에서 to와 content 두가지 정보를 한꺼번에 controller로 보낼건데
	// 걔네의 집단 이름이 MessageDTO인건 form에서는 명시해주지 않아도됨
	// 그냥 두개 보낸다음에 controller에서 파라미터를 messageDTO로 받고
	// 그 messaeDTO 클래스 안에 to랑 content만 있으면 알아서 알아들음!

	private String to;
	private String content;
	
}
