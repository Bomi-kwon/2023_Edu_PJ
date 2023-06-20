<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org" >
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
	<!-- 폰트어썸 불러오기 -->
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.1/css/all.min.css" />
   
    <script src="https://code.jquery.com/jquery-3.6.0.min.js" integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=" crossorigin="anonymous"></script>
    <!-- CSS only -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    
    
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

        // 채팅방 설정 시 비밀번호 확인
        confirmPWD();

        // // 기본은 유저 설정 칸 미활성화
        // $maxUserCnt.hide();

        // // 체크박스 체크에 따라 인원 설정칸 활성화 여부
        // $maxChk.change(function(){
        //     if($maxChk.is(':checked')){
        //         $maxUserCnt.val('');
        //         $maxUserCnt.show();
        //     }else{
        //         $maxUserCnt.hide();
        //     }
        // })

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



    // 채팅방 설정 시 비밀번호 확인 - keyup 펑션 활용
    function confirmPWD(){
        $("#confirmPwd").on("keyup", function(){
            let $confirmPwd = $("#confirmPwd").val();
            const $configRoomBtn = $("#configRoomBtn");
            let $confirmLabel = $("#confirmLabel");

            $.ajax({
                type : "post",
                url : "/chat/confirmPwd/"+roomId,
                data : {
                    "roomPwd" : $confirmPwd
                },
                success : function(result){
                    // console.log("동작완료")

                    // result 의 결과에 따라서 아래 내용 실행
                    if(result){ // true 일때는
                        // $configRoomBtn 를 활성화 상태로 만들고 비밀번호 확인 완료를 출력
                        $configRoomBtn.attr("class", "btn btn-primary");
                        $configRoomBtn.attr("aria-disabled", false);

                        $confirmLabel.html("<span id='confirm'>비밀번호 확인 완료</span>");
                        $("#confirm").css({
                            "color" : "#0D6EFD",
                            "font-weight" : "bold",
                        });

                    }else{ // false 일때는
                        // $configRoomBtn 를 비활성화 상태로 만들고 비밀번호가 틀립니다 문구를 출력
                        $configRoomBtn.attr("class", "btn btn-primary disabled");
                        $configRoomBtn.attr("aria-disabled", true);

                        $confirmLabel.html("<span id='confirm'>비밀번호가 틀립니다</span>");
                        $("#confirm").css({
                            "color" : "#FA3E3E",
                            "font-weight" : "bold",
                        });

                    }
                }
            })
        })
    }

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
    function createRoom() {

        let name = $("#roomName").val();
        let pwd = $("#roomPwd").val();
        let secret = $("#secret").is(':checked');
        let secretChk = $("#secretChk");

        // console.log("name : " + name);
        // console.log("pwd : " + pwd);

        if (name === "") {
            alert("방 이름은 필수입니다")
            return false;
        }
        if ($("#" + name).length > 0) {
            alert("이미 존재하는 방입니다")
            return false;
        }
        if (pwd === "") {
            alert("비밀번호는 필수입니다")
            return false;
        }

        // 최소 방 인원 수는 2, 최대 100명
        if($("#maxUserCnt").val() <= 1){
            alert("채팅은 최소 2명 이상!!");
            return false;
        }else if ($("#maxUserCnt").val() > 100) {
            alert("100명 이상은 서버가 못 버텨요ㅠ.ㅠ");
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

        return true;
    }

    // 채팅방 입장 시 비밀번호 확인
    function enterRoom(){
        let $enterPwd = $("#enterPwd").val();

        $.ajax({
            type : "post",
            url : "/chat/confirmPwd/"+roomId,
            async : false,
            data : {
                "roomPwd" : $enterPwd
            },
            success : function(result){
                // console.log("동작완료")
                // console.log("확인 : "+chkRoomUserCnt(roomId))

                if(result){
                    if (chkRoomUserCnt(roomId)) {
                        location.href = "/chat/room?roomId="+roomId;
                    }
                }else{
                    alert("비밀번호가 틀립니다. \n 비밀번호를 확인해주세요")
                }
            }
        })
    }

    // 채팅방 삭제
    function delRoom(){
        location.href = "/chat/delRoom/"+roomId;
    }

    // 채팅방 입장 시 인원 수에 따라서 입장 여부 결정
    function chkRoomUserCnt(roomId){
        let chk;

        // 비동기 처리 설정 false 로 변경 => ajax 통신이 완료된 후 return 문 실행
        // 기본설정 async = true 인 경우에는 ajax 통신 후 결과가 나올 때까지 기다리지 않고 먼저 return 문이 실행되서
        // 제대로된 값 - 원하는 값 - 이 return 되지 않아서 문제가 발생한다.
        $.ajax({
            type : "GET",
            url : "/chat/chkUserCnt/"+roomId,
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


    </script>
   
    <style>
         a {
            text-decoration: none;
        }

        #table {
            margin-top: 40px;
        }

        h2 {
            margin-top: 40px;
        }

        input#maxUserCnt {
            width: 160px;
        }

        span.input-group-text.input-password-hide {
            height: 40px;
        }

        span.input-group-text.input-password-show {
            height: 40px;
        }
    </style>
</head>
<body>

<section class="mt-8 mx-auto text-xl">
	<div class="container mx-auto px-3">
	
	 <h2>채팅방 리스트</h2>

        <div th:if="${user == null}" class="row">
            <div class="col">
                <a href="/chatlogin"><button type="button" class="btn btn-primary">로그인하기</button></a>
            </div>
        </div>
        <h5 th:if="${user != null}">
            [[${user.nickName}]]
        </h5>

        <table class="table table-hover" id="table">
            <tr>
                <th scope="col">채팅방명</th>
                <th scope="col">잠금 여부</th>
                <th scope="col">참여 인원</th>
                <th scope="col">채팅 종류</th>
                <th scope="col">채팅방 설정</th>
            </tr>
            <th:block th:fragment="content">

                <tr th:each="room : ${list}">
                    <span class="hidden" th:id="${room.roomName}"></span>
                    <td th:if="${room.secretChk}">
                        <a href="#enterRoomModal" data-bs-toggle="modal" data-target="#enterRoomModal" th:data-id="${room.roomId}">[[${room.roomName}]]</a>
                    </td>
                    <td th:if="${!room.secretChk}">
                        <!-- thymeleaf 의 변수를 onclick 에 넣는 방법 -->
                        <a th:href="@{/chat/room(roomId=${room.roomId})}" th:roomId="${room.roomId}" onclick="return chkRoomUserCnt(this.getAttribute('roomId'));">[[${room.roomName}]]</a>
                    </td>
                    <td>
                        <span th:if="${room.secretChk}">
                            🔒︎
                        </span>
                    </td>
                    <td>
                        <span class="badge bg-primary rounded-pill">[[${room.userCount}]]/[[${room.maxUserCnt}]]</span>
                    </td>
                    <td>
                        <span th:if="${#strings.equals(room.chatType, 'MSG')}">일반 채팅</span>
                        <span th:unless="${#strings.equals(room.chatType, 'MSG')}">화상 채팅</span>
                    </td>
                    <td>
                        <button class="btn btn-primary btn-sm" id="configRoom" data-bs-toggle="modal" data-bs-target="#confirmPwdModal" th:data-id="${room.roomId}">채팅방 설정</button>
                    </td>
                </tr>
            </th:block>

        </table>
        <button type="button" class="btn btn-info" data-bs-toggle="modal" data-bs-target="#roomModal">방 생성</button>

    </div>
</div>

<div class="modal fade" id="roomModal" tabindex="-1" aria-labelledby="roomModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">채팅방 생성</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form method="post" action="/chat/createroom" onsubmit="return createRoom()">
                <div class="modal-body">
                    <div class="mb-3">
                        <label for="roomName" class="col-form-label">방 이름</label>
                        <input type="text" class="form-control" id="roomName" name="roomName">
                    </div>
                    <div class="mb-3">
                        <label for="roomPwd" class="col-form-label">방 설정 번호(방 삭제시 필요합니다)</label>
                        <div class="input-group">
                            <input type="password" name="roomPwd" id="roomPwd" class="form-control" data-toggle="password">
                            <div class="input-group-append">
                                <span class="input-group-text"><i class="fa fa-eye"></i></span>
                            </div>
                        </div>
                    </div>
                    <div class="mb-3">
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="chatType" id="msgType" value="msgChat">
                            <label class="form-check-label" for="msgType">
                                일반 채팅(최대 100명)
                            </label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="chatType" id="rtcType" value="rtcChat">
                            <label class="form-check-label" for="rtcType">
                                화상 채팅(1:1 Only)
                            </label>
                        </div>
                    </div>
                    <div class="mb-3">
                        <label for="maxUserCnt" class="col-form-label">채팅방 인원 설정
                            <!--<input class="form-check-input" type="checkbox" id="maxChk">--></label>
                        <input type="text" class="form-control" id="maxUserCnt" name="maxUserCnt">
                    </div>
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" id="secret">
                        <input type="hidden" name="secretChk" id="secretChk" value="">
                        <label class="form-check-label" for="secret">
                            채팅방 잠금
                        </label>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <input type="submit" class="btn btn-primary" value="방 생성하기">
                </div>
            </form>
        </div>
    </div>
</div>

<div class="modal fade" id="enterRoomModal" tabindex="-1" aria-labelledby="enterRoomModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">채팅방 비밀번호를 입력해주세요</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="mb-3">
                    <label for="roomName" class="col-form-label">방 비밀번호</label>
                    <div class="input-group">
                        <input type="password" name="roomPwd" id="enterPwd" class="form-control" data-toggle="password">
                        <div class="input-group-append">
                            <span class="input-group-text"><i class="fa fa-eye"></i></span>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary" onclick="enterRoom()">입장하기</button>
                </div>
                </form>
            </div>
        </div>
    </div>
</div>



<div class="modal fade" id="confirmPwdModal" aria-hidden="true" aria-labelledby="ModalToggleLabel" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">채팅방 설정을 위한 패스워드 확인</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <label for="confirmPwd" class="col-form-label" id="confirmLabel">비밀번호 확인</label>
                <div class="input-group">
                    <input type="password" name="confirmPwd" id="confirmPwd" class="form-control" data-toggle="password">
                    <div class="input-group-append">
                        <span class="input-group-text"><i class="fa fa-eye"></i></span>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button id="configRoomBtn" class="btn btn-primary disabled" data-bs-target="#configRoomModal" data-bs-toggle="modal" aria-disabled="true">채팅방 설정하기</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="configRoomModal" tabindex="-1" aria-labelledby="roomModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">채팅방 설정</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
                <div class="modal-body">
                    <div class="mb-3">
                        <label for="chPwd" class="col-form-label">비밀번호 변경</label>
                        <div class="input-group">
                            <input type="password" name="confirmPwd" id="chPwd" class="form-control" data-toggle="password">
                            <div class="input-group-append">
                                <span class="input-group-text"><i class="fa fa-eye"></i></span>
                            </div>
                        </div>
                    </div>
                    <div class="mb-3">
                        <label for="chRoomName" class="col-form-label">채팅방 이름 변경</label>
                        <input type="text" class="form-control" id="chRoomName" name="chRoomName">
                    </div>
                    <div class="mb-3">
                        <label for="chRoomUserCnt" class="col-form-label">채팅방 인원 변경</label>
                        <input type="text" class="form-control" id="chRoomUserCnt" name="chUserCnt">
                    </div>
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" id="chSecret">
                        <input type="hidden" name="secretChk" id="chSecretChk" value="">
                        <label class="form-check-label" for="secret">
                            채팅방 잠금
                        </label>
                    </div>
                    <div class="mb-3">
                        <button type="button" class="btn btn-primary" onclick="delRoom()">방 삭제</button>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                </div>
        </div>
    
    </div>
    </section>
    
</body>
</html>