package com.koreaIT.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketMessage {
	
	// webRTC 연결시 사용되는 클래스
	// websocke 연결 정보를 주고 받을때 사용
	
    private String from; // 보내는 유저 UUID
    private String type; // 메시지 타입
    private String data; // roomId
    private Object candidate; // 상태
    private Object sdp; // sdp 정보
}
