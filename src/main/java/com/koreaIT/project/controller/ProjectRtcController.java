package com.koreaIT.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.koreaIT.project.dto.WebSocketMessage;
import com.koreaIT.project.service.RtcChatService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ProjectRtcController {

    private final RtcChatService rtcChatService;
    
	@PostMapping("/project/webrtc/usercount")
    public String webRTC(@ModelAttribute WebSocketMessage webSocketMessage) {

		System.out.println("== usercount 인포로깅 시작 ==");
		System.out.println("MESSAGE : "+webSocketMessage.toString());
		System.out.println("== usercount 인포로깅 끝 ==");
		System.out.println("usercount return 결과 : " + Boolean.toString(rtcChatService.findUserCount(webSocketMessage)));
		
        return Boolean.toString(rtcChatService.findUserCount(webSocketMessage));
    }
	
}
