package com.koreaIT.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.koreaIT.project.dto.WebSocketMessage;
import com.koreaIT.project.service.RtcChatService;

@Controller
public class ProjectRtcController {

    private RtcChatService rtcChatService;

    @PostMapping("/project/webrtc/usercount")
    public String webRTC(@ModelAttribute WebSocketMessage webSocketMessage) {
        // log.info("MESSAGE : {}", webSocketMessage.toString());
        return Boolean.toString(rtcChatService.findUserCount(webSocketMessage));
    }
    
    
	// 화상채팅
	
	@RequestMapping("/project/webrtc/WebRTC")
	public String WebRTC() {
		return "project/webrtc/WebRTC";
	}
	
	
}
