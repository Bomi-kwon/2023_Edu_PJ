package com.koreaIT.project.dto;

import java.util.Map;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@EqualsAndHashCode
@Getter
@Setter
public class ChatRoomDto {
	
	
	// @Data는 @Getter, @Setter, @RequiredArgsConstructor, @ToString, @EqualsAndHashCode를 한꺼번에 설정!
	// 그러나 @Data의 무분별한 사용은 별로다.
	// @Setter를 열어두면 아무데서나 값을 변형시킬 수 있기 때문
	// 두 객체를 1:N 양방향으로 매핑되어있을때 @ToString을 호출하면 무한 순환 참조가 발생한다.
	// 그래서 exclude를 사용해 특정 항목을 제외시킬 수도 있다.
	// @EqualsAndHashCode를 변경 가능한 필드에 파라미터 없이 사용하면 동일한 객체여도 hashCode가 변경되며 찾을 수 없다.
	// 그래서 @EqualsAndHashCode(of={'필드 명시'}) 이렇게 필드를 명시해줘야 한다.
	
	// Stomp 를 통해 pub/sub 를 사용하면 구독자 관리가 알아서 된다!!
	// 따라서 따로 세션 관리를 하는 코드를 작성할 필도 없고,
	// 메시지를 다른 세션의 클라이언트에게 발송하는 것도 구현 필요가 없다!
	
	// notnull import 확인하기!!!
	// 갑자기 @NOTNULL annotation 빨간줄 뜨고 import도 안되길래 걍 삭제해버림!!!!
	
	private String roomId; // 채팅방 아이디
    private String roomName; // 채팅방 이름 
    private int userCount; // 채팅방 인원수
    private int maxUserCnt; // 채팅방 최대 인원 제한

    private String roomPwd; // 채팅방 삭제시 필요한 pwd
    private boolean secretChk; // 채팅방 잠금 여부
    public enum ChatType{  // 화상 채팅, 문자 채팅
        MSG, RTC
    }
    private ChatType chatType; //  채팅 타입 여부

    // ChatRoomDto 클래스는 하나로 가되 서비스를 나누었음
    // 일반채팅에서는 RoomId(String)과 ChatRoomDto(String)가 들어가고
    // 화상채팅에서는 userUUID(String)과 WebSocketSession(WebSocketSession)이 들어간다.
    private Map<String, ?> userList;
}
