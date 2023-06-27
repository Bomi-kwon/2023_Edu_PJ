package com.koreaIT.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.koreaIT.project.dto.WebSocketMessage;
import com.koreaIT.project.service.RtcChatService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ProjectRtcController {

    private final RtcChatService rtcChatService;
    
    
    /**
     * rtcroom의 ajax함수. 상대방과 통신 할지말지 판단해주는 기능
     * @param webSocketMessage rtcroom.js에서 보낸 방과 유저 정보가 담긴 메시지
     * @return 나 포함 2명 이상이면 true, 아니면 false를 문자화해서
     */
	@RequestMapping("/project/webrtc/usercount")
	@ResponseBody
    public String webRTC(WebSocketMessage webSocketMessage) {

		System.out.println("== usercount 인포로깅 시작 ==");
		System.out.println("MESSAGE : "+webSocketMessage.toString());
		System.out.println("== usercount 인포로깅 끝 ==");
		System.out.println("usercount return 결과 : " + Boolean.toString(rtcChatService.findUserCount(webSocketMessage)));
		
        return Boolean.toString(rtcChatService.findUserCount(webSocketMessage));
    }
	
}
