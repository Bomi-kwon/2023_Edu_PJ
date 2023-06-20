package com.koreaIT.project.dto;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomMap {
	
	// 싱글톤으로 생성
	// 모든 ChatService 에서 ChatRooms가 공통된 필요함으로
	
	private static ChatRoomMap chatRoomMap = new ChatRoomMap();
    private Map<String, ChatRoomDto> chatRooms = new LinkedHashMap<>();

//    @PostConstruct
//    private void init() {
//        chatRooms = new LinkedHashMap<>();
//    }

    private ChatRoomMap(){}

    public static ChatRoomMap getInstance(){
        return chatRoomMap;
    }
}
