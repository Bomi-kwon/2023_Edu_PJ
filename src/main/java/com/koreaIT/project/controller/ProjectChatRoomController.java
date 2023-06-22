package com.koreaIT.project.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.koreaIT.project.dto.ChatRoomDto;
import com.koreaIT.project.dto.ChatRoomMap;
import com.koreaIT.project.service.ChatServiceMain;
import com.koreaIT.project.vo.Rq;


@Controller
public class ProjectChatRoomController {

    // ChatService Bean 가져오기
    private ChatServiceMain chatServiceMain;
    private Rq rq;
    
    // rq땜에 어차피 생성자 필요해서
    // chatservicemain에 final 빼고 class에 @requiredargscontroller annotation도 뺐음
    
    @Autowired
    public ProjectChatRoomController(Rq rq, ChatServiceMain chatServiceMain) {
    	this.chatServiceMain = chatServiceMain;
		this.rq = rq;
	}

    // 채팅 리스트 화면
    // "/project/chat" 로 요청이 들어오면 전체 채팅룸 리스트를 담아서 return
    // 참고한 블로그 주인은 이 함수를 MainController 만들어서 따로 뺐음.
    @GetMapping("/project/chat")
    public String goChatRoom(Model model){

    	// 원래 파라미터로 @AuthenticationPrincipal PrincipalDetails principalDetails 
    	// (로그인정보) 가져왔는데 일단 난 빼겠음
    	
        model.addAttribute("list", chatServiceMain.findAllRoom());

        
        // principalDetails 가 null 이 아니라면 로그인 된 상태!!
        // if (principalDetails != null) {
            // 세션에서 로그인 유저 정보를 가져옴
            // model.addAttribute("user", principalDetails.getUser());
            // log.debug("user [{}] ",principalDetails);
        // }

//        model.addAttribute("user", "hey");
        // log.debug("SHOW ALL ChatList {}", chatServiceMain.findAllRoom());
        return "project/chat/roomlist";
    }
    
    
    // 채팅방 생성
    // 채팅방 생성 후 다시 / 로 return
    @PostMapping("/project/chat/createroom")
    public String createRoom(@RequestParam("roomName") String name,
                             @RequestParam(value = "maxUserCnt", defaultValue = "2") String maxUserCnt,
                             @RequestParam("chatType") String chatType,
                             RedirectAttributes rttr) {

        // log.info("chk {}", secretChk);

        // 매개변수 : 방 이름, 패스워드(1234로 통일), 방 잠금 여부, 방 인원수
        ChatRoomDto room;

        room = chatServiceMain.createChatRoom(name, "1234", false, Integer.parseInt(maxUserCnt), chatType);


        // log.info("CREATE Chat Room [{}]", room);

        rttr.addFlashAttribute("roomName", room);
        return "redirect:/project/chat";
    }

    // 채팅방 입장 화면
    // 파라미터로 넘어오는 roomId 를 확인후 해당 roomId 를 기준으로
    // 채팅방을 찾아서 클라이언트를 chatroom 으로 보낸다.
    @GetMapping("/project/chat/room")
    public String roomDetail(Model model, String roomId){

    	// 원래 파라미터로 @AuthenticationPrincipal PrincipalDetails principalDetails 
    	// (로그인정보) 가져왔는데 일단 난 빼겠음
    	
    	
        // log.info("roomId {}", roomId);

        // principalDetails 가 null 이 아니라면 로그인 된 상태!!
       // if (principalDetails != null) {
            // 세션에서 로그인 유저 정보를 가져옴
           // model.addAttribute("user", principalDetails.getUser());
       // }

        ChatRoomDto room = ChatRoomMap.getInstance().getChatRooms().get(roomId);
        
        if(room == null ) {
        	return rq.jsReturnOnView("해당 채팅방은 존재하지 않습니다.", true);
        }

        model.addAttribute("room", room);


        if (ChatRoomDto.ChatType.MSG.equals(room.getChatType())) {
            return "project/chat/chatroom";
        }else{
            model.addAttribute("uuid", UUID.randomUUID().toString());

            return "project/chat/rtcroom";
        }
    }


    // 채팅방 삭제
    @GetMapping("/project/chat/delRoom")
    public String delChatRoom(String roomId){

        // roomId 기준으로 chatRoomMap 에서 삭제, 해당 채팅룸 안에 있는 사진 삭제
        chatServiceMain.delChatRoom(roomId);

        return "redirect:/project/chat";
    }

    // 유저 카운트
    @GetMapping("/project/chat/chkUserCnt")
    @ResponseBody
    public boolean chUserCnt(@PathVariable String roomId){

        return chatServiceMain.chkRoomUserCnt(roomId);
    }


	


	
}