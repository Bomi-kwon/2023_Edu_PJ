<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
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
</head>
<body>

<style>
	/* 과제검사 모달창 커스텀 */
.roomModal-bg, .roomModal {
	display: none;
}
.roomModal-bg {
	position: absolute;
	top: 0;
	left: 0;
	bottom: 0;
	right: 0;
	background-color: rgba(0,0,0,0.5);
	z-index: 10;
}
.roomModal {
	position: absolute;
	height: 500px;
	width: 400px;
	background-color: white;
	top: 50%;
	left: 50%;
	transform: translateX(-50%) translateY(-50%);
	padding: 20px;
	z-index: 15;
	border: 2px solid black;
}
.roomModal > h1 {
	font-size: 2rem;
	margin-bottom: 10px;
}
.close-btn {
	position: absolute;
	top:20px;
	right: 20px;
	cursor: pointer;
	font-size: 2rem;
}
</style>

<section class="mt-8 mx-auto text-xl">
	<div class="container mx-auto px-3">
	
        <c:if test="${rq.getLoginedMemberId() != 0 }">
        	<h5>사용자 이름 : ${rq.getLoginedMember().getName()}</h5>
        </c:if>
        
    <!-- 채팅방 리스트표 보여주기 -->
	<div class="table-box-type-1">
        <table class="table w-full" id="table">
            <thead>
            	<c:choose>
            		<c:when test="${list.size() != 0 }">
	           			<tr>
			                <th scope="col">채팅방명</th>
			                <th scope="col">참여 인원</th>
			                <th scope="col">채팅 종류</th>
			                <th scope="col">채팅방 설정</th>
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
                <tr>
                    <td>
	                    <span class="hidden" id="${room.roomName}"></span>
                        <a href="/project/chat/room?roomId=${room.roomId}" data-roomId="${room.roomId}" 
                        onclick="return chkRoomUserCnt(${room.roomId});">${room.roomName}</a>
                    </td>
                    <td>
                        <span class="badge badge-primary">현재 ${room.userCount}명 / 최대 ${room.maxUserCnt}명</span>
                    </td>
                    <td>
                       <span>일반채팅/화상채팅</span>
                    </td>
                    <td>
                        <a class="btn btn-primary btn-sm" href="/project/chat/delRoom?roomId=${room.roomId }" 
                        onclick="if(confirm('정말 삭제하시겠습니까?')==false) return false;">채팅방 삭제</a>
                    </td>
                </tr>
                </c:forEach>
            </tbody>

        </table>
        </div>
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
                            <input class="form-check-input radio radio-primary" type="radio" name="chatType" id="msgType" value="msgChat">
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
                            <!--<input class="form-check-input" type="checkbox" id="maxChk">--></div>
                        <input type="text" class="form-control input input-bordered input-info w-full max-w-xs" 
                        id="maxUserCnt" name="maxUserCnt" placeholder="채팅방 최대인원을 설정해주세요.">
                    </div>
                </div>
                <div class="modal-footer flex justify-end">
                    <button type="button" class="btn btn-secondary roomModal-close-btn">Close</button>
                    <input type="submit" class="btn btn-primary" value="방 생성하기">
                </div>
            </form>
        </div>
    </div>
</div>

   
    </div>
    </section>
    
    
    <script>

    let roomId;

    $(function(){
        let $maxChk = $("#maxChk");
        let $maxUserCnt = $("#maxUserCnt");
        let $rtcType = $("#rtcType");
        let $msgType = $("#msgType");

        // 모달창 열릴 때 이벤트 처리 => roomId 가져오기
        $("#enterRoomModal").on("show.bs.modal", function (event) {
            roomId = $(event.relatedTarget).data('id');
            // console.log("roomId: " + roomId);

        });

        $("#confirmPwdModal").on("show.bs.modal", function (e) {
            roomId = $(e.relatedTarget).data('id');
            // console.log("roomId: " + roomId);

        });


        // 화상 채팅 시 1:1 임으로 2명 고정
        $rtcType.change(function() {
            if($rtcType.is(':checked')){
                let number = 2;

                $("#maxUserCnt").val(parseInt(2));
                // $("#maxUserCnt").attr('value', 2)
                $("#maxUserCnt").attr('disabled', true);
            }
        })

        // 문자 채팅 누를 시 disabled 풀림
        $msgType.change(function(){
            if($msgType.is(':checked')){
                $maxUserCnt.attr('disabled', false);
            }
        })
    })




    // 채팅 인원 숫자만 정규식 체크
    function numberChk(){
        let check = /^[0-9]+$/;
        if (!check.test($("#maxUserCnt").val())) {
            alert("채팅 인원에는 숫자만 입력 가능합니다!!")
            return false;
        }
        return true;
    }
    
 	// 채팅방 생성
        function createRoom(){

            let name = $("#roomName").val();
            let pwd = $("#roomPwd").val();
            let secret = $("#secret").is(':checked');
            let secretChk = $("#secretChk");

            // console.log("name : " + name);
            // console.log("pwd : " + pwd);

            if (name === "") {
                alert("방 이름은 필수입니다");
                return false;
            }
            if ($('#' + name).length) {
                alert("이미 존재하는 방입니다");
                return false;
            }
            if (pwd === "") {
                alert("비밀번호는 필수입니다");
                return false;
            }

            // 최소 방 인원 수는 2, 최대 100명
            if($("#maxUserCnt").val() <= 1){
                alert("채팅은 최소 2명 이상!!");
                return false;
            }else if ($("#maxUserCnt").val() > 100) {
                alert("채팅은 최대 100명까지만!!");
                return false;
            }

            // 채팅 타입 필수
            if ($('input[name=chatType]:checked').val() == null) {
                alert("채팅 타입은 필수입니다")
                return false;
            }

            if (secret) {
                secretChk.attr('value', true);
            } else {
                secretChk.attr('value', false);
            }

            if(!numberChk()){
                return false;
            }
            
            console.log($('#' + name).length);

            return true;
        }
        

        // 채팅방 삭제
        function delRoom(roomId){
        	alert('채팅방 삭제 눌림');
            location.href = "/project/chat/delRoom?roomId="+roomId;
            return false;
        }

        // 채팅방 입장 시 인원 수에 따라서 입장 여부 결정
        function chkRoomUserCnt(roomId){
            let chk;

            // 비동기 처리 설정 false 로 변경 => ajax 통신이 완료된 후 return 문 실행
            // 기본설정 async = true 인 경우에는 ajax 통신 후 결과가 나올 때까지 기다리지 않고 먼저 return 문이 실행되서
            // 제대로된 값 - 원하는 값 - 이 return 되지 않아서 문제가 발생한다.
            $.ajax({
                type : "GET",
                url : "/project/chat/chkUserCnt",
                data : {
                	"roomId" : roomId
                },
                async : false,
                success : function(result){

                    // console.log("여기가 먼저")
                    if (!result) {
                        alert("채팅방이 꽉 차서 입장 할 수 없습니다");
                    }

                    chk = result;
                }
            })
            return chk;
        }
        
        <!-- 채팅방생성 모달창 -->
        $('.btn-makeroom').click(function(){
			$('.roomModal-bg, .roomModal').show();
		});
		$('.roomModal-bg').click(function(){
			$('.roomModal-bg, .roomModal').hide();
		});
		$('.roomModal-close-btn').click(function(){
			$('.roomModal-bg, .roomModal').hide();
		});
		
		function openConfigRoom(roomId) {
			alert('채팅방 설정 버튼 눌림');
			$('.configRoomModal').show();
			return false;
		}
        
    </script>
    
</body>
</html>