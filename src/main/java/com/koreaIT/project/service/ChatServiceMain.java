package com.koreaIT.project.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.koreaIT.project.dto.ChatRoomDto;
import com.koreaIT.project.dto.ChatRoomMap;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class ChatServiceMain {
	
	private final MsgChatService msgChatService;
    private final RtcChatService rtcChatService;

    // 채팅방 삭제에 따른 채팅방의 사진 삭제를 위한 fileService 선언
    // private final FileService fileService;


    /**
     * roomlist.jsp 보여줄때 채팅방 전체 조회하기
     * @return 조회한 채팅방들
     */
    public List<ChatRoomDto> findAllRoom(){
    	
    	// getInstance() : 객체를 매번 new 하지 않고 딱 한번만 생성할 것임
    	// ChatRoomMap에 저장한 chatRooms(map)에서 value(chatRoomDto)를 뽑아 배열에 넣겠다.
        List<ChatRoomDto> chatRooms = new ArrayList<>(ChatRoomMap.getInstance().getChatRooms().values());
       
        // 채팅방 생성 순서를 최근순으로 반환
        Collections.reverse(chatRooms);
        
        return chatRooms;
    }

    // roomID 기준으로 채팅방 찾기
    public ChatRoomDto findRoomById(String roomId){
        return ChatRoomMap.getInstance().getChatRooms().get(roomId);
    }

    /**
     * 채팅방 생성하기
     * @param roomName 방 이름
     * @param maxUserCnt 최대 인원수
     * @param chatType (MSG/RTC 둘중 하나 고르기)
     * @return 방금 생성한 채팅방
     */
    public ChatRoomDto createChatRoom(String roomName, int maxUserCnt, String chatType){

        ChatRoomDto room;

        // 채팅방 타입에 따라서 사용되는 Service 구분
        if(chatType.equals("msgChat")){
            room = msgChatService.createChatRoom(roomName, maxUserCnt);
        }else{
            room = rtcChatService.createChatRoom(roomName, maxUserCnt);
        }

        return room;
    }


    // 채팅방 인원+1
    public void plusUserCnt(String roomId){
        ChatRoomDto room = ChatRoomMap.getInstance().getChatRooms().get(roomId);
        room.setUserCount(room.getUserCount()+1);
    }

    // 채팅방 인원-1
    public void minusUserCnt(String roomId){
        ChatRoomDto room = ChatRoomMap.getInstance().getChatRooms().get(roomId);
        room.setUserCount(room.getUserCount()-1);
    }

    
    
    /**
     * 이 방에 내가 들어간다면 최대 인원수 넘는지 안 넘는지 예측하기
     * @param roomId 방 번호
     * @return 방 입장 가능 여부 (true/false)
     */
    public boolean chkRoomUserCnt(String roomId){
        ChatRoomDto room = ChatRoomMap.getInstance().getChatRooms().get(roomId);

        if (room.getUserCount() + 1 > room.getMaxUserCnt()) {
            return false;
        }

        return true;
    }
    

    // 채팅방 삭제
    public void delChatRoom(String roomId){

        try {
            ChatRoomMap.getInstance().getChatRooms().remove(roomId);

        } catch (Exception e) { 
            System.out.println(e.getMessage());
        }
        
    }

}
