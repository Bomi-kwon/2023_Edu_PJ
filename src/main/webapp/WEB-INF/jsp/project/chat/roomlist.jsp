<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
   <meta charset="UTF-8">
   <!-- 파비콘 불러오기 -->
	<link rel="shortcut icon" href="/resource/images/favicon.ico" />
	<!-- 테일윈드 불러오기 -->
	<!-- 노말라이즈, 라이브러리 -->
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/tailwindcss/2.2.7/tailwind.min.css" />
	<!-- 데이지 UI -->
	<link href="https://cdn.jsdelivr.net/npm/daisyui@2.31.0/dist/full.css" rel="stylesheet" type="text/css" />
	<!-- 제이쿼리 불러오기 -->
	<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.1/jquery.min.js"></script>
	<!-- 폰트어썸 불러오기 -->
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.1/css/all.min.css" />
   
    
    <link rel="stylesheet" href="/resource/common.css" />
	<script src="/resource/common.js" defer="defer"></script>
	<link rel="stylesheet" href="/css/chat/roomlist.css"/> 
</head>

<body>


<section class="mt-8 mx-auto text-xl">
	<div class="container mx-auto px-3">
	
        <c:if test="${rq.getLoginedMemberId() != 0 }">
        	<h5>사용자 이름 : ${rq.getLoginedMember().getName()}</h5>
        </c:if>
        
    	<!-- 채팅방 리스트표 보여주기 -->
        <table class="table w-full" id="table">
            <thead>
            	<c:choose>
            		<c:when test="${list.size() != 0 }">
	           			<tr>
			                <th scope="col">채팅방 이름</th>
			                <th scope="col">채팅방 ID</th>
			                <th scope="col">참여 인원</th>
			                <th scope="col">채팅 종류</th>
			                <th scope="col">채팅방 삭제</th>
			            </tr>
            		</c:when>
            		<c:otherwise>
            			<tr>
            			<td><span>생성된 채팅방이 없습니다.</span></td>
            			</tr>
            		</c:otherwise>
            	</c:choose>
            </thead>
			<tbody>
				<c:forEach var="room" items="${list }">
                <tr class="hover">
                    <td>
	                    <span class="hidden" id="${room.roomName}"></span>
                        <a href="#" onclick="return chkRoomUserCnt('${room.roomId}');">${room.roomName}</a>
                    </td>
                    <td>
                    	<span>${room.roomId }</span>
                    </td>
                    <td>
                        <span class="badge badge-primary">현재 ${room.userCount}명 / 최대 ${room.maxUserCnt}명</span>
                    </td>
                    <td>
                    	<c:choose>
                    		<c:when test="${room.chatType == 'MSG'}">
		                       <span>일반채팅</span>
                    		</c:when>
                    		<c:otherwise>
		                       <span>화상채팅</span>
                    		</c:otherwise>
                    	</c:choose>
                    </td>
                    <td>
                        <a class="btn btn-primary btn-sm" href="/project/chat/delRoom?roomId=${room.roomId }" 
                        onclick="if(confirm('정말 삭제하시겠습니까?')==false) return false;">채팅방 삭제</a>
                    </td>
                </tr>
                </c:forEach>
            </tbody>

        </table>
        <button type="button" class="btn btn-info btn-makeroom">방 생성</button>

		<!-- 방생성 버튼 누르면 나오는 채팅방생성 모달창 -->
		<div class="roomModal-bg"></div>
		<div class="hidden roomModal" id="roomModal">
		    <div class="modal-dialog">
		        <div class="modal-content">
		            <div class="modal-header">
		                <h1 class="modal-title text-2xl mt-5">채팅방 생성</h1>
		                <button type="button" class="btn-close"></button>
		            </div>
		            <form method="post" action="/project/chat/createroom" onsubmit="return createRoom()">
		                <div class="modal-body">
		                    <div class="mb-5">
		                        <div class="col-form-label mb-2">방 이름</div>
		                        <input type="text" class="form-control input input-bordered input-info w-full max-w-xs" 
		                        id="roomName" name="roomName" placeholder="채팅방 이름을 설정해주세요.">
		                    </div>
		                    <div class="mb-5">
		                        <div class="form-check flex items-center">
		                            <input class="form-check-input radio radio-primary" type="radio" name="chatType" id="msgType" value="msgChat" checked>
		                            &nbsp;&nbsp;
		                            <label class="form-check-label " for="msgType">
		                                일반 채팅(최대 100명)
		                            </label>
		                        </div>
		                        <div class="form-check flex items-center">
		                            <input class="form-check-input radio radio-primary" type="radio" name="chatType" id="rtcType" value="rtcChat">
		                            &nbsp;&nbsp;
		                            <label class="form-check-label" for="rtcType">
		                                화상 채팅(1:1 Only)
		                            </label>
		                        </div>
		                    </div>
		                    <div class="mb-7">
		                        <div class="col-form-label mb-2">채팅방 최대인원 설정
		                        <input type="text" class="form-control input input-bordered input-info w-full max-w-xs" 
		                        id="maxUserCnt" name="maxUserCnt" placeholder="채팅방 최대인원을 설정해주세요.">
		                    </div>
		                </div>
		                <div class="modal-footer flex justify-end">
		                    <button type="button" class="btn btn-secondary roomModal-close-btn">Close</button>
		                    <input type="submit" class="btn btn-primary" value="방 생성하기">
		                </div>
		            </div>
		            </form>
		        </div>
		    </div>
		</div>
   
    </div>
</section>
    
<script src="/js/chat/roomlist.js"></script>
    
</body>
</html>