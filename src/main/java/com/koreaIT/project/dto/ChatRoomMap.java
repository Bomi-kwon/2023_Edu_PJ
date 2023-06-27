package com.koreaIT.project.dto;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomMap {
	
	// 싱글톤으로 생성
	// 객체의 인스턴스(class를 바탕으로 구현된 구체적인 실체)가 오직 1개만 생성되는 패턴
	// 모든 ChatService 에서 ChatRooms가 공통되게 필요하므로
	
	// 전역 객체변수로 사용하기 위해 static 객체변수로 생성
	private static ChatRoomMap chatRoomMap;
    private Map<String, ChatRoomDto> chatRooms = new LinkedHashMap<>();

    // 생성자를 private으로 만들어 접근을 막는다
    private ChatRoomMap(){}

    // 이 함수를 통해 한번만 생성된 객체를 가져온다.
    public static ChatRoomMap getInstance(){
    	if(chatRoomMap == null) {
    		chatRoomMap = new ChatRoomMap();
    	}
        return chatRoomMap;
    }
}
