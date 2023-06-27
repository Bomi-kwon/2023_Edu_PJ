// roomlist.jsp


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


    // 화상 채팅 시 1:1 임의로 2명 고정
    $rtcType.change(function() {
        if($rtcType.is(':checked')){
            let number = 2;

            $("#maxUserCnt").val(parseInt(2));
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
    let secretChk = $("#secretChk");


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

    // 최소 방 인원 수는 2, 최대 10명
    if($("#maxUserCnt").val() <= 1){
        alert("채팅은 최소 2명 이상!!");
        return false;
    } else if ($("#maxUserCnt").val() > 10) {
        alert("채팅은 최대 10명까지만!!");
        return false;
    }

    // 채팅 타입 필수
    if ($('input[name=chatType]:checked').val() == null) {
        alert("채팅 타입은 필수입니다")
        return false;
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
    
// 채팅방생성 모달창
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
    