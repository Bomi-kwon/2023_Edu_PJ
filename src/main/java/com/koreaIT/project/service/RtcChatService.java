package com.koreaIT.project.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import com.koreaIT.project.dto.ChatRoomDto;
import com.koreaIT.project.dto.ChatRoomMap;
import com.koreaIT.project.dto.WebSocketMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RtcChatService {

    // repository substitution since this is a very simple realization

	/**
	 * roomlist > chatServiceMain > RtcChatService로 넘어온 방 생성하기
	 * @param roomName 방 이름
	 * @param maxUserCnt 최대 입장 인원
	 * @return 방금 생성한 방
	 */
    public ChatRoomDto createChatRoom(String roomName, int maxUserCnt) {
        ChatRoomDto room = ChatRoomDto.builder()
                .roomId(UUID.randomUUID().toString().replace("-", ""))
                .roomName(roomName)
                .userCount(0) // 채팅방 참여 인원수
                .maxUserCnt(maxUserCnt) // 최대 인원수 제한
                .build();

        room.setUserList(new HashMap<String, WebSocketSession>());

        // msg 타입이면 ChatType.MSG
        room.setChatType(ChatRoomDto.ChatType.RTC);

        // map 에 채팅룸 아이디와 만들어진 채팅룸을 저장
        ChatRoomMap.getInstance().getChatRooms().put(room.getRoomId(), room);

        return room;
    }

    
    /**
     * 화상채팅방의 유저 리스트 가져오는 기능
     * @param room 방
     * @return 방에 있는 유저 리스트
     */
    public Map<String, WebSocketSession> getClients(ChatRoomDto room) {
        // 공부하기 좋은 기존 코드
        // unmodifiableMap : read-only 객체를 만들고 싶을 때 사용
        // Collections emptyMap() : 결과를 반환할 시 반환할 데이터가 없거나 내부조직에 의해 빈 데이터가 반환되어야 하는 경우
        // NullPointException 을 방지하기 위하여 반환 형태에 따라 List 나 Map 의 인스턴스를 생성하여 반환하여 처리해야하는 경우
        // size 메서드 등을 체크하고 추가적인 값을 변경하지 않는 경우 Collections.emptyMap() 를 사용하면 매번 동일한 정적 인스턴스가
        // 변환되므로 각 호출에 대한 불필요한 인스턴스 생성하지 않게 되어 메모리 사용량을 줄일 수 있다

        Optional<ChatRoomDto> roomDto = Optional.ofNullable(room);
        
        return (Map<String, WebSocketSession>) roomDto.get().getUserList();
    }

    
    /**
     * rtc 채팅방 userList에 유저 추가
     * @param room 방
     * @param userUUID 유저아이디
     * @param session 세션
     * @return 유저 리스트
     */
    public Map<String, WebSocketSession> addClient(ChatRoomDto room, String userUUID, WebSocketSession session) {
        Map<String, WebSocketSession> userList = (Map<String, WebSocketSession>) room.getUserList();
        
        userList.put(userUUID, session);
        
        return userList;
    }

    // userList 에서 클라이언트 삭제
    public void removeClientByName(ChatRoomDto room, String userUUID) {
        room.getUserList().remove(userUUID);
    }

    
    /**
     * 유저가 2명 이상인지 아닌지 판단해주는 기능
     * @param webSocketMessage rtcroom.js > ProjectRtcController > RtcChatService로 넘어온 메시지(방과 유저 정보 담고있음)
     * @return 내가 참여한 room에 나 포함 2명 이상이면 true (그럼 다른 유저와 통신이 필요해진다.)
     */
    public boolean findUserCount(WebSocketMessage webSocketMessage){
        ChatRoomDto room = ChatRoomMap.getInstance().getChatRooms().get(webSocketMessage.getData());
        // log.info("ROOM COUNT : [{} ::: {}]",room.toString(),room.getUserList().size());
        System.out.println("== findUserCount 인포로깅 시작 ==");
        System.out.println("room.toString : "+room.toString()+", room.getUserList().size() : "+room.getUserList().size());
        System.out.println("== findUserCount 인포로깅 끝 ==");
        return room.getUserList().size() > 1;
    }


}
