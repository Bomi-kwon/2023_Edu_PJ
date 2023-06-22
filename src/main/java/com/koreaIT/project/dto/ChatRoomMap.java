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
