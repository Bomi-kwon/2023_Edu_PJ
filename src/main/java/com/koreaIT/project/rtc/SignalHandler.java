package com.koreaIT.project.rtc;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.codec.binary.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koreaIT.project.dto.ChatRoomDto;
import com.koreaIT.project.dto.ChatRoomMap;
import com.koreaIT.project.dto.WebSocketMessage;
import com.koreaIT.project.service.ChatServiceMain;
import com.koreaIT.project.service.RtcChatService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SignalHandler extends TextWebSocketHandler{
	// WebSocketSession : 연결될 때 생기는 연결정보를 담고있는 객체
    // => 핸들러에서 웹소켓 통신에대한 처리를 하기위해 이 세션들을 자바의 컬렉션으로 관리하는 경우가 많다.
    // 연결이 맺어질때 컬렉션에 WebSocketSession 을 추가하고, 연결이 끊어질때 제거하도록 구현을 해놓았다.
    // 이렇게 세션을 관리하면, 모든 클라이언트들에게 메시지를 보내는 것과 같은 처리를 할 수 있게됩니다.

    private final RtcChatService rtcChatService;
    private final ChatServiceMain chatServiceMain;

    // private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper = new ObjectMapper();

    // roomID to room Mapping
    private Map<String, ChatRoomDto> rooms = ChatRoomMap.getInstance().getChatRooms();

    // message types, used in signalling:
    // SDP Offer message
    private static final String MSG_TYPE_OFFER = "offer";
    // SDP Answer message
    private static final String MSG_TYPE_ANSWER = "answer";
    // New ICE Candidate message
    private static final String MSG_TYPE_ICE = "ice";
    
    /*
     * OFFER, ANSWER, ICE 의 경우 roomDTO 에서 roomID 를 기준으로 room 정보를 가져온다. 
     * 해당 room 의 userList key 정보들을 가져와서 확인한다. 
     * userList key 중 현재 유저의 uuid 와 다른 uuid 가 있다면 다른 유저가 존재하는것으로 판단하고 
     * 해당 유저의 session 에(나와 다른 유저의 session 정보) 에 
     * 현재 유저의 UUID, message.type, roomID, 상태, sdp 를 담아서 보내게 된다.
     */
    
    // join room data message
    private static final String MSG_TYPE_JOIN = "join";
    // leave room data message
    private static final String MSG_TYPE_LEAVE = "leave";
    
    /*
     * JOIN 은 유저가 방에 참가했을 때 실행되며
     * roomId 를 기준으로 room 을 가져온 후 현재 유저를 추가한다. 
     * 또한 room 의 userCnt 도 +1한다.
     * LEAVE 는 유저가 방에서 떠났을 때의 이벤트 처리이다.  
     * 떠났을 때의 기준은 웹 브라우저에서 '방을 나간경우' 로 취급한다. 
     * 즉 exit 버튼을 눌렀거나 브라우저를 종료한 경우 모두 방을 떠났다고 판단하여 LEAVE 메시지를 보내게 된다. 
     * 여기에서는 room 정보를 찾아온 후 userList 에서 key 값만 가져와서 떠난 유저와 동일한 uuid 가 있는지 확인한다. 
     * 만약 있다면 해당 유저를 userList 에서 제거한다.
     */
    
    
    /**
     * 참고한 블로그엔 없던 코드였는데
     * 화상채팅방에서만 채팅방 인원수가 바뀌지 않아서 추가했더니
     * 인원수는 잘 바뀐다!
     * 근데 새로고침 해야지 인원수가 바뀜;; 실시간 통신인데
     * 그리고 상대방 비디오가 안 뜸;;
     * 그래서 다시 지워놓겠음
     */
    /*
    @Autowired
	public SignalHandler(RtcChatService rtcChatService, ChatServiceMain chatServiceMain,
			Map<String, ChatRoomDto> rooms) {
		this.rtcChatService = rtcChatService;
		this.chatServiceMain = chatServiceMain;
		this.rooms = rooms;
	}
	*/
    
    // 연결 끊어졌을 때 이벤트처리
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
    	
    	System.out.println("== 소켓 연결 끊어짐 ==");
    	
    }

    // 소켓 연결되었을 때 이벤트 처리
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        /*
        * 웹 소켓이 연결되었을 때 클라이언트 쪽으로 메시지를 발송한다
        * 이때 원본 코드에서는 rooms.isEmpty() 가 false 를 전달한다. 
        * 이 의미는 현재 room 에 아무도 없다는 것을 의미하고 따라서 추가적인 ICE 요청을 하지 않도록 한다.
        *
        * 현재 채팅 코드에서는 chatRoom 안에 userList 안에 user가 저장되기 때문에 
        * rooms가 아닌 userList 에 몇명이 있는지 확인해야 한다.
        * 따라서 js 쪽에서 ajax 요청을 통해 userList 에 몇명이 있는지 확인하고
        * 2명 이상인 경우에만 JS에서 이와 관련된 변수를 true 가 되도록 변경하였다.
        *
        * 이렇게 true 상태가 되면 이후에 들어온 유저가 방안에 또 다른 유저가 있음을 확인하고,
        * P2P 연결을 시작한다.
        * */
        sendMessage(session, new WebSocketMessage("Server", MSG_TYPE_JOIN, Boolean.toString(!rooms.isEmpty()), null, null));
    }

    // 소켓 메시지 처리
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) {
    	/**
    	 * 데이터 통신시 웹소켓 서버에 연결된 다른 사용자에게 나의 접속여부를 전달해주는 기능
    	 * 기존 접속 사용자의 웹소켓 세션을 전부 관리하고 있어야 한다.
    	 * 이 함수를 기준으로 ICE와 SDP 통신이 일어남.
    	 * 이 함수가 실행되며 userUUID와 roomID를 저장함.
    	 * 이후 전달받은 메세지의 타입에 따라 시그널링 서버 기능 시작.
    	 */
    	
        try {
            // 웹 소켓으로부터 전달받은 메시지
            // 소켓쪽에서는 socket.send 로 메시지를 발송한다 => 참고로 JSON 형식으로 변환해서 전달해온다
            WebSocketMessage message = objectMapper.readValue(textMessage.getPayload(), WebSocketMessage.class);
            
            // 로깅 대신 println으로 처리
            // 디버그 아니어도 항상 나올것임
            System.out.println("== 디버그 로깅 시작 ==");
            System.out.println(message.getType());
            System.out.println(message.getFrom());
            System.out.println("== 디버그 로깅 끝 ==");
            
            // 유저 uuid 와 roomID 를 저장
            String userUUID = message.getFrom(); // 유저 uuid
            String roomId = message.getData(); // roomId

            ChatRoomDto room;
            // 메시지 타입에 따라서 서버에서 하는 역할이 달라진다
            switch (message.getType()) {

                // 클라이언트에게서 받은 메시지 타입에 따른 signal 프로세스
                case MSG_TYPE_OFFER:
                case MSG_TYPE_ANSWER:
                case MSG_TYPE_ICE:
                    Object candidate = message.getCandidate();
                    Object sdp = message.getSdp();

                    if(candidate != null) {
                    	System.out.println("== candidate 존재할 때 디버그 로깅 시작 ==");
                    	System.out.println("Signal : "+candidate.toString().substring(0, 64));
                    	System.out.println("== candidate 존재할 때 디버그 로깅 끝 ==");
                    }
                    
                    else {
                    	System.out.println("== candidate 존재 안 할때 디버그 로깅 시작 ==");
                    	System.out.println("Signal : "+sdp.toString().substring(0, 64));
                    	System.out.println("== candidate 존재 안 할 때 디버그 로깅 끝 ==");
                    }

                    /* 여기도 마찬가지 */
                    ChatRoomDto roomDto = rooms.get(roomId);

                    if (roomDto != null) {
                        Map<String, WebSocketSession> clients = rtcChatService.getClients(roomDto);

                        /*
                         * Map.Entry 는 Map 인터페이스 내부에서 Key, Value 를 쌍으로 다루기 위해 정의된 내부 인터페이스
                         * 보통 key 값들을 가져오는 entrySet() 과 함께 사용한다.
                         * entrySet 을 통해서 key 값들을 불러온 후 Map.Entry 를 사용하면서 Key 에 해당하는 Value 를 쌍으로 가져온다
                         *
                         * 여기를 고치면 1:1 대신 1:N 으로 바꿀 수 있지 않을까..?
                         */
                        for(Map.Entry<String, WebSocketSession> client : clients.entrySet())  {

                            // send messages to all clients except current user
                            if (!client.getKey().equals(userUUID)) {
                                // select the same type to resend signal
                                sendMessage(client.getValue(),
                                        new WebSocketMessage(
                                                userUUID,
                                                message.getType(),
                                                roomId,
                                                candidate,
                                                sdp));
                            }
                        }
                    }
                    break;

                // identify user and their opponent
                case MSG_TYPE_JOIN:
                    // message.data contains connected room id
                	
                	System.out.println("== JOIN 디버그로깅 시작 ==");
                	System.out.println(userUUID+" has joined Room : "+message.getData());
                	System.out.println("== JOIN 디버그로깅 끝 ==");

                    room = ChatRoomMap.getInstance().getChatRooms().get(roomId);

                    // room 안에 있는 userList 에 유저 추가
                    rtcChatService.addClient(room, userUUID, session);

                    // 채팅방 입장 후 유저 카운트+1
                    chatServiceMain.plusUserCnt(roomId);

                    // 이 부분에서 session.getID 대신 roomId 를 사용하면 문제 생김
                    // rooms.put(session.getId(), room);
                    // 아님! 오히려 session.getId를 사용하면
                    // 새로고침할 때마다 session이 바뀌어서 그만큼 방 갯수가 늘어난다.
                    rooms.put(roomId, room);
                    break;

                case MSG_TYPE_LEAVE:
                    // message data contains connected room id

                	System.out.println("== LEAVE 인포로깅 시작 ==");
                	System.out.println(userUUID+" is going to leave Room : "+message.getData());
                	System.out.println("== LEAVE 인포로깅 끝 ==");
                	
                    // roomID 기준 채팅방 찾아오기
                    room = rooms.get(message.getData());

                    // room clients list 에서 해당 유저 삭제
                    // 1. room 에서 client List 를 받아와서 keySet 을 이용해서 key 값만 가져온 후 stream 을 사용해서 반복문 실행
                    Optional<String> client = rtcChatService.getClients(room).keySet().stream()
                            // 2. 이때 filter - 일종의 if문 -을 사용하는데 entry 에서 key 값만 가져와서 userUUID 와 비교한다
                            .filter(clientListKeys -> StringUtils.equals(clientListKeys, userUUID))
                            // 3. 하여튼 동일한 것만 가져온다
                            .findAny();

                    // 만약 client 의 값이 존재한다면 - Optional 임으로 isPersent 사용 , null  아니라면 - removeClientByName 을 실행
                    client.ifPresent(userID -> rtcChatService.removeClientByName(room, userID));

                    // 채팅방에서 떠날 시 유저 카운트 -1
                    chatServiceMain.minusUserCnt(roomId);

                    System.out.println("== LEAVE 디버그로깅 시작 ==");
                    System.out.println("삭제 완료 : " + client);
                    System.out.println("== LEAVE 디버그로깅 끝 ==");
                    
                    break;

                // something should be wrong with the received message, since it's type is unrecognizable
                default:
                	
                	System.out.println("== type이 OFFER,ANSWER,ICE,JOIN,LEAVE 중에 없을때 ==");
                	System.out.println("Type of the received message "+message.getType()+" is undefined!");
                	System.out.println("====");
                	
            }

        } catch (IOException e) {
            // logger.debug("An error occured: {}", e.getMessage());
        	System.out.println(e.getMessage());
        }
    }

    
    
    private void sendMessage(WebSocketSession session, WebSocketMessage message) {
    	
			try {
				String json = objectMapper.writeValueAsString(message);
				
				session.sendMessage(new TextMessage(json));
				
			} catch (IOException e) {
				e.printStackTrace();
			}
        
    }

    
}
