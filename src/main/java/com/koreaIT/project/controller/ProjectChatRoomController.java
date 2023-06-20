package com.koreaIT.project.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.koreaIT.project.dto.ChatRoomDto;
import com.koreaIT.project.dto.ChatRoomMap;
import com.koreaIT.project.service.ChatServiceMain;

@Controller
public class ProjectChatRoomController {
	
	// ChatService Bean 가져오기
    @Autowired
    // private ChatService chatService;
    private ChatServiceMain chatServiceMain;

    /*
    // 채팅 리스트 화면
    // /chat 로 요청이 들어오면 전체 채팅룸 리스트를 담아서 return
    @GetMapping("/project/chat")
    public String goChatRoom(Model model){

        model.addAttribute("list", chatService.findAllRoom());
//        model.addAttribute("user", "hey");
        //log.info("SHOW ALL ChatList {}", chatRepository.findAllRoom());
        return "project/chat/roomlist";
    }
    */

    // 채팅방 생성
    // 채팅방 생성 후 다시 /chat 로 return
    @PostMapping("/project/chat/createroom")
    public String createRoom(@RequestParam("roomName") String name,
            @RequestParam("roomPwd") String roomPwd,
            @RequestParam("secretChk") String secretChk,
            @RequestParam(value = "maxUserCnt", defaultValue = "2") String maxUserCnt,
            @RequestParam("chatType") String chatType,
            RedirectAttributes rttr) {
    	
    	// 매개변수 : 방 이름, 패스워드, 방 잠금 여부, 방 인원수
        ChatRoomDto room;

        room = chatServiceMain.createChatRoom(name, roomPwd, Boolean.parseBoolean(secretChk), 
        		Integer.parseInt(maxUserCnt), chatType);
        
        //log.info("CREATE Chat Room {}", room);
        
        rttr.addFlashAttribute("roomName", room);
        return "redirect:/project/chat";
    }

    // 채팅방 입장 화면
    // 파라미터로 넘어오는 roomId 를 확인후 해당 roomId 를 기준으로
    // 채팅방을 찾아서 클라이언트를 chatroom 으로 보낸다.
    @GetMapping("/project/chat/room")
    public String roomDetail(Model model, String roomId){

        //log.info("roomId {}", roomId);
    	
    	ChatRoomDto room = ChatRoomMap.getInstance().getChatRooms().get(roomId);
    	
        model.addAttribute("room", room);
        
        if (ChatRoomDto.ChatType.MSG.equals(room.getChatType())) {
            return "project/chat/chatroom";
        }else{
            model.addAttribute("uuid", UUID.randomUUID().toString());

            return "project/chat/rtcroom";
        }
    }
	
	
}
