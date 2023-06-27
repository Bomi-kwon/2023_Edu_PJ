package com.koreaIT.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
    private final ChatServiceMain chatServiceMain;
    private final Rq rq;
    // 해당 빈(Bean) 들이 생성자를 통해 주입되는 시점에 불변성을 보장하도록
    // final 키워드를 붙여주는 것이 좋다.
    
    // 만약 생성자를 사용하기 싫은데 롬복 사용중이면 @RequiredArgsConstructor 어노테이션만 달아줘도 된다.
    // @Autowired는 만약 생성자가 1개이면 굳이 안 붙여도 된다.
    
    @Autowired
    public ProjectChatRoomController(Rq rq, ChatServiceMain chatServiceMain) {
    	this.chatServiceMain = chatServiceMain;
		this.rq = rq;
	}

    /**
     * 채팅방 리스트
     * @param model
     * @return 전체 채팅룸 리스트를 담아서 return
     */
    @GetMapping("/project/chat/roomlist")
    public String goChatRoom(Model model){

    	// 원래 파라미터로 @AuthenticationPrincipal PrincipalDetails principalDetails 
    	// (로그인정보) 가져왔는데 일단 난 빼겠음
    	
        model.addAttribute("list", chatServiceMain.findAllRoom());

        return "project/chat/roomlist";
    }
    
    
    /**
     * 채팅방 생성
     * @param name 채팅방 이름
     * @param maxUserCnt 채팅방 최대 인원
     * @param chatType 일반(MSG)인지 화상(RTC)인지
     * @param rttr
     * @return 채팅방 생성 후 다시 /project/chat/roomlist 로 return
     */
    @PostMapping("/project/chat/createroom")
    public String createRoom(@RequestParam("roomName") String name,
                             @RequestParam(value = "maxUserCnt", defaultValue = "2") String maxUserCnt,
                             @RequestParam("chatType") String chatType,
                             RedirectAttributes rttr) {

        ChatRoomDto room;

        // 매개변수 : 방 이름, 방 인원수, 채팅타입
        room = chatServiceMain.createChatRoom(name, Integer.parseInt(maxUserCnt), chatType);

        
        // post방식. 일회성으로 데이터를 전달하게 해서 새로고침하면 값이 사라진다.
        rttr.addFlashAttribute("roomName", room);
        
        return "redirect:/project/chat/roomlist";
    }

    /**
     * 채팅방 입장 화면
     * @param model 이걸로 room 정보를 같이 보내준다.
     * @param roomId 파라미터로 넘어오는 roomId 를 확인후 해당 roomId 를 기준으로
     * @return 채팅방을 찾아서 클라이언트를 chatroom 또는 rtcroom으로 보낸다.
     */
    @GetMapping("/project/chat/enterRoom")
    public String roomDetail(Model model, String roomId){

    	// 해당 id를 가진 room만 가져온다.
        ChatRoomDto room = ChatRoomMap.getInstance().getChatRooms().get(roomId);
        
        if(room == null ) {
        	return rq.jsReturnOnView("해당 채팅방은 존재하지 않습니다.", true);
        }

        model.addAttribute("room", room);
        

        if (ChatRoomDto.ChatType.MSG.equals(room.getChatType())) {
            return "project/chat/chatroom";
        }else{
            model.addAttribute("uuid", rq.getLoginedMember().getName());

            return "project/chat/rtcroom";
        }
    }


    /**
     * 채팅방 삭제
     * @param roomId 방 번호
     * @return 삭제 후 채팅방 리스트 페이지로 돌려주기
     */
    @GetMapping("/project/chat/delRoom")
    public String delChatRoom(String roomId){

        // roomId 기준으로 chatRoomMap 에서 그 room 삭제
        chatServiceMain.delChatRoom(roomId);

        return "redirect:/project/chat/roomlist";
    }

    /**
     * 유저 카운트해보고 입장 가능한지 알려주는 ajax 함수
     * @param roomId roomlist.jsp에서 방 입장 눌렀을때 그 방 번호
     * @return 방 유저 명수 확인하고 입장 가능 여부를 true/false로 리턴
     */
    @GetMapping("/project/chat/chkUserCnt")
    @ResponseBody
    public boolean chUserCnt(String roomId){

    	boolean canIenterroom = chatServiceMain.chkRoomUserCnt(roomId);
    	
        return canIenterroom;
    }


	


	
}