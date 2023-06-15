package com.koreaIT.project.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.koreaIT.project.dto.ChatRoom;

import lombok.Data;

@Data
@Service
public class ChatService {
	// 나중에 service랑 repository로 구분해야함
	
	 private Map<String, ChatRoom> chatRoomMap;

	 	// 의존성 주입 후 초기화가 필요한 함수.
	    @PostConstruct
	    private void init() {
	        chatRoomMap = new LinkedHashMap<>();
	        // 일반 HashMap과는 다르게 순서대로 저장된다
	    }

	    // 전체 채팅방 조회
	    public List<ChatRoom> findAllRoom(){
	        // 채팅방 생성 순서를 최근순으로 반환
	        List chatRooms = new ArrayList<>(chatRoomMap.values());
	        Collections.reverse(chatRooms);

	        return chatRooms;
	    }

	    // roomID 기준으로 채팅방 찾기
	    public ChatRoom findRoomById(String roomId){
	        return chatRoomMap.get(roomId);
	    }

	    // roomName 로 채팅방 만들기
	    public ChatRoom createChatRoom(String roomName){
	        ChatRoom chatRoom = new ChatRoom().create(roomName); // 채팅룸 이름으로 채팅 룸 생성 후

	        // map 에 채팅룸 아이디와 만들어진 채팅룸을 저장장
	       chatRoomMap.put(chatRoom.getRoomId(), chatRoom);

	        return chatRoom;
	    }

	    // 채팅방 인원+1
	    public void plusUserCnt(String roomId){
	        ChatRoom room = chatRoomMap.get(roomId);
	        room.setUserCount(room.getUserCount()+1);
	    }

	    // 채팅방 인원-1
	    public void minusUserCnt(String roomId){
	        ChatRoom room = chatRoomMap.get(roomId);
	        room.setUserCount(room.getUserCount()-1);
	    }

	    // 채팅방 유저 리스트에 유저 추가
	    public String addUser(String roomId, String userName){
	        ChatRoom room = chatRoomMap.get(roomId);
	        String userUUID = UUID.randomUUID().toString();

	        // 아이디 중복 확인 후 userList 에 추가
	        room.getUserlist().put(userUUID, userName);

	        return userUUID;
	    }

	    // 채팅방 유저 이름 중복 확인
	    public String isDuplicateName(String roomId, String username){
	        ChatRoom room = chatRoomMap.get(roomId);
	        String tmp = username;

	        // 만약 userName 이 중복이라면 랜덤한 숫자를 붙임
	        // 이때 랜덤한 숫자를 붙였을 때 getUserlist 안에 있는 닉네임이라면 다시 랜덤한 숫자 붙이기!
	        while(room.getUserlist().containsValue(tmp)){
	            int ranNum = (int) (Math.random()*100)+1;

	            tmp = username+ranNum;
	        }

	        return tmp;
	    }

	    // 채팅방 유저 리스트 삭제
	    public void delUser(String roomId, String userUUID){
	        ChatRoom room = chatRoomMap.get(roomId);
	        room.getUserlist().remove(userUUID);
	    }

	    // 채팅방 userName 조회
	    public String getUserName(String roomId, String userUUID){
	        ChatRoom room = chatRoomMap.get(roomId);
	        return room.getUserlist().get(userUUID);
	    }

	    // 채팅방 전체 userlist 조회
	    public ArrayList<String> getUserList(String roomId){
	        ArrayList<String> list = new ArrayList<>();

	        ChatRoom room = chatRoomMap.get(roomId);

	        // hashmap 을 for 문을 돌린 후
	        // value 값만 뽑아내서 list 에 저장 후 reutrn
	        room.getUserlist().forEach((key, value) -> list.add(value));
	        return list;
	    }


}