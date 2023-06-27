package com.koreaIT.project.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.koreaIT.project.dto.ChatRoomDto;
import com.koreaIT.project.dto.ChatRoomMap;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MsgChatService {

	/**
	 * roomlist > chatServiceMain > MsgChatService로 넘어온 방 생성하기
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

        room.setUserList(new HashMap<String, String>());

        // msg 타입이면 ChatType.MSG
        room.setChatType(ChatRoomDto.ChatType.MSG);

        // map 에 채팅룸 아이디와 만들어진 채팅룸을 저장
        ChatRoomMap.getInstance().getChatRooms().put(room.getRoomId(), room);

        return room;
    }


    /**
     * msg 채팅방 userList에 유저 추가
     * @param chatRoomMap 방들의 모임에서
     * @param roomId 방 번호로 방 찾기
     * @param userName 유저이름
     * @return 유저의UUID (중복불가 랜덤 문자열)
     */
    public String addUser(Map<String, ChatRoomDto> chatRoomMap, String roomId, String userName){
        ChatRoomDto room = chatRoomMap.get(roomId);
        String userUUID = UUID.randomUUID().toString();

        HashMap<String, String> userList = (HashMap<String, String>) room.getUserList();
        userList.put(userUUID, userName);


        return userUUID;
    }

    /**
     * 채팅방 유저 이름 중복 확인
     * @param chatRoomMap 방 모임에서
     * @param roomId 번호로 방 찾기
     * @param username 사용하려는 닉네임
     * @return 최종 닉네임
     */
    public String isDuplicateName(Map<String, ChatRoomDto> chatRoomMap, String roomId, String username){
        ChatRoomDto room = chatRoomMap.get(roomId);
        String tmp = username;

        // 만약 userName 이 중복이라면 랜덤한 숫자를 붙임
        // 이때 랜덤한 숫자를 붙였을 때 getUserlist 안에 있는 닉네임이라면 다시 랜덤한 숫자 붙이기!
        while(room.getUserList().containsValue(tmp)){
            int ranNum = (int) (Math.random()*100)+1;

            tmp = username+ranNum;
        }

        return tmp;
    }

    // 채팅방 userName 조회
    public String findUserNameByRoomIdAndUserUUID(Map<String, ChatRoomDto> chatRoomMap, String roomId, String userUUID){
        ChatRoomDto room = chatRoomMap.get(roomId);
        return (String) room.getUserList().get(userUUID);
    }

    // 
    /**
     * 현재 채팅방의 userlist 조회
     * @param chatRoomMap 방 모임에서
     * @param roomId 번호로 방 찾기
     * @return 그 방의 유저 리스트
     */
    public ArrayList<String> getUserList(Map<String, ChatRoomDto> chatRoomMap, String roomId){
        ArrayList<String> list = new ArrayList<>();

        ChatRoomDto room = chatRoomMap.get(roomId);

        // hashmap 을 for 문을 돌린 후
        // value 값만 뽑아내서 list 에 저장 후 return
        room.getUserList().forEach((key, value) -> list.add((String) value));
        return list;
    }

    
    /**
     * 채팅방에서 유저 퇴장시 리스트에서도 그 유저 삭제
     * @param chatRoomMap 방 모임에서
     * @param roomId 번호로 방 찾기
     * @param userUUID 유저마다 갖고있는 userUUID로 유저 삭제
     */
    public void delUser(Map<String, ChatRoomDto> chatRoomMap, String roomId, String userUUID){
        ChatRoomDto room = chatRoomMap.get(roomId);
        room.getUserList().remove(userUUID);
    }


}
