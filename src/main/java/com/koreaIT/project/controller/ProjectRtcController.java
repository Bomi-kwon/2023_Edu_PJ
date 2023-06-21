package com.koreaIT.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.koreaIT.project.dto.WebSocketMessage;
import com.koreaIT.project.service.RtcChatService;

@Controller
public class ProjectRtcController {

    private RtcChatService rtcChatService;

    // 자꾸 cannot invoke 어쩌구 because this.rtcChatService is null이라고 뜨길래
    // 검색해봤더니 주입 해주라길래 했더니 되었음
    // 변수 앞에다 final을 붙이면 한번 값 할당 후 수정할 수 없음.
    // 즉 초기화는 한 번만 가능
    @Autowired
    public ProjectRtcController(RtcChatService rtcChatService) {
		this.rtcChatService = rtcChatService;
	}

    
	@PostMapping("/project/webrtc/usercount")
    public String webRTC(@ModelAttribute WebSocketMessage webSocketMessage) {
        // log.info("MESSAGE : {}", webSocketMessage.toString());
		
		try {
    		System.out.println(webSocketMessage.toString());
    	} catch (Exception e) {
    		e.printStackTrace();
		}
		
        return Boolean.toString(rtcChatService.findUserCount(webSocketMessage));
    }
	
}
