'use strict';
const addr = "localhost:8443"

// create and run Web Socket connection
// 웹 소켓 연결 정보
const socket = new WebSocket("wss://" + window.location.host + "/signal");

// UI elements
const videoButtonOff = document.querySelector('#video_off');
const videoButtonOn = document.querySelector('#video_on');
const audioButtonOff = document.querySelector('#audio_off');
const audioButtonOn = document.querySelector('#audio_on');
const exitButton = document.querySelector('#exit');
const localRoom = document.querySelector('input#id').value;
const localVideo = document.getElementById('local_video');
const remoteVideo = document.getElementById('remote_video');
// const localUserName = localStorage.getItem("uuid");
const localUserName = document.querySelector("#uuid").value

document.querySelector('#view_on').addEventListener('click', startScreenShare);
document.querySelector('#view_off').addEventListener('click', stopScreenShare);


// WebRTC STUN servers
// WebRTC STUN 서버 정보
const peerConnectionConfig = {
    'iceServers': [
        {'urls': 'stun:stun.stunprotocol.org:3478'},
        {'urls': 'stun:stun.l.google.com:19302'},
    ]
};

// WebRTC media
const mediaConstraints = {
    audio: true,
    video: true
};

// WebRTC variables
let localStream;
let localVideoTracks;
let myPeerConnection;

// on page load runner
$(function(){
    start();
});

function start() {
	
    // 페이지 시작시 실행되는 메서드 -> socket 을 통해 server 와 통신한다
    socket.onmessage = function(msg) {
        let message = JSON.parse(msg.data);
        switch (message.type) {
            case "offer":
			    // 두번째 참여자 입장하면 바로 OFFER signal
                console.log('Signal OFFER received');
                handleOfferMessage(message);
                break;

            case "answer":
                console.log('Signal ANSWER received');
                handleAnswerMessage(message);
                break;

            case "ice":
				// OFFER signal 관련 함수 다 실행되기 전에 ICE signal
                console.log('Signal ICE Candidate received');
                handleNewICECandidateMessage(message);
                break;

            case "join":
                // ajax 요청을 보내서 userList 를 다시 확인함
                message.data = chatListCount();

				if(message.data === "true") {
					console.log('Client is starting to negotiate');
				} else {
					console.log('Client is starting to wait for a peer');
				}
				
                console.log("messageDATA : "+message.data)
                
                // 나 말고 상대방 있다? -> 상대방과 통신하기
                handlePeerConnection(message);
                
                break;

            case "leave":
                stop();
                break;

            default:
                handleErrorMessage('Wrong type message received from server');
        }
    };


    // ICE 를 위한 chatList 인원 확인
    function chatListCount(){
		console.log('localRoom 값 : '+localRoom);
		console.log('lacalUserName 값 : '+localUserName);

        let data;

        $.ajax({
            url : "/project/webrtc/usercount",
            type : "POST",
            async : false,
            data : {
                "from" : localUserName,
                "type" : "findCount",
                "data" : localRoom,
                "candidate" : null,
                "sdp" : null
            },
            success(result){
                data = result;
                console.log('usercount 아작스 요청 성공');
            },
            error(result){
                console.log('usercount 아작스 요청 실패');
                console.log("error : "+result);
            }
        });
        
		return data;
    }


    /**
	 * add an event listener to get to know when a connection is open
	 * 웹 소켓 연결 되었을 때 (open 상태)일때 이벤트 처리
	 * 소켓 열리자마자 가장 먼저 실행되는 함수!!
	 * 웹 콘솔창에 두번째로 찍히는 로그 (첫번째꺼는 화면 공유 열린거라
	 * 사실상 이게 가장 처음 실행되는 것임)
	 */
    socket.onopen = function() {
        console.log('WebSocket connection opened to Room: #' + localRoom);
        /**
		 * send a message to the server to join selected room with Web Socket
		 * 여기서 방에 들어가고 나서 웹소켓이 서버한테 join이라고
		 * 메시지 보내는 거였음!!! 이제 signalhandler가 웹소켓 통신 처리
		 */
        sendToServer({
            from: localUserName,
            type: 'join',
            data: localRoom
        });
    };

    // a listener for the socket being closed event
    // 소켓이 끊겼을 때 이벤트처리
    socket.onclose = function(message) {
        console.log('Socket has been closed');
    };

    // an event listener to handle socket errors
    // 에러 발생 시 이벤트 처리
    socket.onerror = function(message) {
        handleErrorMessage("Error: " + message);
    };
}

// 브라우저 종료 시 이벤트
// 그냥...브라우저 종료 시 stop 함수를 부르면 된다ㅠㅠ
window.addEventListener('unload', stop);

// 브라우저 뒤로가기 시 이벤트
window.onhashchange = function(){
    stop();
}


function stop() {
    // send a message to the server to remove this client from the room clients list
    console.log("Send 'leave' message to server");
    sendToServer({
        from: localUserName,
        type: 'leave',
        data: localRoom
    });

    if (myPeerConnection) {
        console.log('Close the RTCPeerConnection');

        // disconnect all our event listeners
        myPeerConnection.onicecandidate = null;
        myPeerConnection.ontrack = null;
        myPeerConnection.onnegotiationneeded = null;
        myPeerConnection.oniceconnectionstatechange = null;
        myPeerConnection.onsignalingstatechange = null;
        myPeerConnection.onicegatheringstatechange = null;
        myPeerConnection.onnotificationneeded = null;
        myPeerConnection.onremovetrack = null;

        // Stop the videos
        // 비디오 정지
        if (remoteVideo.srcObject) {
            remoteVideo.srcObject.getTracks().forEach(track => track.stop());
        }
        if (localVideo.srcObject) {
            localVideo.srcObject.getTracks().forEach(track => track.stop());
        }

        remoteVideo.src = null;
        localVideo.src = null;

        // close the peer connection
        // myPeerConnection 초기화
        myPeerConnection.close();
        myPeerConnection = null;

        console.log('Close the socket');
        if (socket != null) {
            socket.close();
        }
    }
}

/*
 UI Handlers
  */
// mute video buttons handler
videoButtonOff.onclick = () => {
    localVideoTracks = localStream.getVideoTracks();
    localVideoTracks.forEach(track => localStream.removeTrack(track));
    $(localVideo).css('display', 'none');
    console.log('Video Off');
};
videoButtonOn.onclick = () => {
    localVideoTracks.forEach(track => localStream.addTrack(track));
    $(localVideo).css('display', 'inline');
    console.log('Video On');
};

// mute audio buttons handler
audioButtonOff.onclick = () => {
    localVideo.muted = true;
    console.log('Audio Off');
};
audioButtonOn.onclick = () => {
    localVideo.muted = false;
    console.log('Audio On');
};

// room exit button handler
exitButton.onclick = () => {
    stop();
};

function log(message) {
    console.log(message);
}

function handleErrorMessage(message) {
    console.error(message);
}

/**
 * JSON 포맷을 사용해서 WebSocket 메시지를
 * 서버로 보내기
 */
function sendToServer(msg) {
    let msgJSON = JSON.stringify(msg);
    socket.send(msgJSON);
}

/**
 * media stream(오디오, 비디오) 시작
 */
function getMedia(constraints) {
    if (localStream) {
        localStream.getTracks().forEach(track => {
            track.stop();
        });
    }
    navigator.mediaDevices.getUserMedia(constraints)
        .then(getLocalMediaStream).catch(handleGetUserMediaError);
}


// create peer connection, get media, start negotiating when second participant appears
// 두번째 클라이언트가 들어오면 피어 연결을 생성 + 미디어 생성
function handlePeerConnection(message) {
	
    createPeerConnection();

	// media stream(vedio와 audio) 생성
    getMedia(mediaConstraints);

    if (message.data === "true") {
        myPeerConnection.onnegotiationneeded = handleNegotiationNeededEvent;
    }
}

/**
 * 상대방 peer와 연결
 */
function createPeerConnection() {
    myPeerConnection = new RTCPeerConnection(peerConnectionConfig);

    // event handlers for the ICE negotiation process
    myPeerConnection.onicecandidate = handleICECandidateEvent;
    myPeerConnection.ontrack = handleTrackEvent;

    // the following events are optional and could be realized later if needed
    // myPeerConnection.onremovetrack = handleRemoveTrackEvent;
    
    myPeerConnection.oniceconnectionstatechange = handleICEConnectionStateChangeEvent;
    // myPeerConnection.onicegatheringstatechange = handleICEGatheringStateChangeEvent;
    // myPeerConnection.onsignalingstatechange = handleSignalingStateChangeEvent;
}


/** peerConnection 과 관련된 이벤트 처리
 * 다른 peer 와 연결되었을 때는 remote_video show 상태로, 끊어졌을때는 remote_video 를 hide 상태로 변경
 * **/
function handleICEConnectionStateChangeEvent(){
    let status = myPeerConnection.iceConnectionState;

    if(status === "connected"){
        console.log("status : "+status)
        $("#remote_video").show();
    }else if(status === "disconnected"){
        console.log("status : "+status)
        $("#remote_video").hide();
    }
}


/**
 * 내 local media stream을
 * 나와 상대방에게 양방향 전송
 */
function getLocalMediaStream(mediaStream) {
    localStream = mediaStream;
    localVideo.srcObject = mediaStream;
    localStream.getTracks().forEach(track => myPeerConnection.addTrack(track, localStream));
}



// handle get media error
function handleGetUserMediaError(error) {
    console.log('navigator.getUserMedia error: ', error);
    switch(error.name) {
        case "NotFoundError":
            alert("Unable to open your call because no camera and/or microphone were found.");
            break;
        case "SecurityError":
        case "PermissionDeniedError":
            // Do nothing; this is the same as the user canceling the call.
            break;
        default:
            alert("Error opening your camera and/or microphone: " + error.message);
            break;
    }

    stop();
}

/**
 * 최적의 ICE candidate(주소)를 서버를 통해 상대방에게 보냄
 */
function handleICECandidateEvent(event) {
    if (event.candidate) {
        sendToServer({
            from: localUserName,
            data: localRoom,
            type: 'ice',
            candidate: event.candidate
        });
        console.log('ICE Candidate Event: ICE candidate sent');
    }
}

function handleTrackEvent(event) {
    console.log('Track Event: set stream to remote video element');
    remoteVideo.srcObject = event.streams[0];
}

// WebRTC called handler to begin ICE negotiation
// WebRTC 의 ICE 통신 순서
// 1. WebRTC offer 생성
// 2. local media description 생성
// 3. 미디어 형식, 해상도 등에 대한 내용을 서버에 전달
function handleNegotiationNeededEvent() {
    myPeerConnection.createOffer().then(function(offer) {
        return myPeerConnection.setLocalDescription(offer);
    })
        .then(function() {
            sendToServer({
                from: localUserName,
                data:localRoom,
                type: 'offer',
                sdp: myPeerConnection.localDescription
            });
            console.log('Negotiation Needed Event: SDP offer sent');
        })
        .catch(function(reason) {
            // an error occurred, so handle the failure to connect
            handleErrorMessage('failure to connect error: ', reason);
        });
}


// 두번째 참여자 막 입장했을 때
function handleOfferMessage(message) {
    console.log('Accepting Offer Message');
    console.log(message);
    let desc = new RTCSessionDescription(message.sdp);

    if (desc != null && message.sdp != null) {
        console.log('RTC Signalling state: ' + myPeerConnection.signalingState);
        
        /**
		 * 이 아래 함수 실행 전에 signal ICE 보냄
		 * 각 peer가 ICE 후보중 최적의 경로를 협상
		 * 
		 * SDP(Session Description Protocol)란?
		 * WebRTC에서 스트리밍 미디어의 해상도나 형식, 코덱 등의 멀티미디어 컨텐츠의 초기 인수를 설명하기 위한 프로토콜
		 * 비디오의 해상도, 오디오 전송 또는 수신 여부 등을 송수신 할 수 있음
		 * 
		 * 어떤 peer가 media stream 교환하자고 제안하면 상대방으로부터 수신 응답을 기다리고
		 * 응답을 받으면 최적의 ICE를 선정한 뒤 가장 지연 시간이 적고 안정적인 경로를 찾는다
		 * 이 과정이 끝나면 peer간 P2P 연결이 완전하게 설정되고 활성화됨.
		 * 각 peer에 의해 local media stream의 end point가 생성되고
		 * webscket을 사용하여 양방향으로 전송한다.
		 */
        myPeerConnection.setRemoteDescription(desc).then(function () {
        	
        	// 상대방(두번째 peer)의 local media stream 설정
            console.log("Set up local media stream");
            return navigator.mediaDevices.getUserMedia(mediaConstraints);
        })
            .then(function (stream) {
				
	        	// 상대방에게 그 사람 본인의 local media stream 띄우기
                console.log("-- Local video stream obtained");
                localStream = stream;
                try {
                    localVideo.srcObject = localStream;
                } catch (error) {
                    localVideo.src = window.URL.createObjectURL(stream);
                }
                
				// 우리 서로 media stream 교환하자~! 하고 요청
                console.log("-- Adding stream to the RTCPeerConnection");
                localStream.getTracks().forEach(track => myPeerConnection.addTrack(track, localStream));
            })
            .then(function () {
				
				// 상대방 peer가 그래 좋아!라고 응답
                console.log("-- Creating answer");
                return myPeerConnection.createAnswer();
            })
            .then(function (answer) {
				
				// SDP(media와 관련된 초기 정보) 세팅
                console.log("-- Setting local description after creating answer");
                return myPeerConnection.setLocalDescription(answer);
            })
            .then(function () {
				
				// 상대방 peer에게 내 media streem 전달 (양방향 통신 가능)
                console.log("Sending answer packet back to other peer");
                sendToServer({
                    from: localUserName,
                    data: localRoom,
                    type: 'answer',
                    sdp: myPeerConnection.localDescription
                });

            })
            // .catch(handleGetUserMediaError);
            .catch(handleErrorMessage)
    }
}

function handleAnswerMessage(message) {
    console.log("The peer has accepted request");

    // Configure the remote description, which is the SDP payload
    // in our "video-answer" message.
    // myPeerConnection.setRemoteDescription(new RTCSessionDescription(message.sdp)).catch(handleErrorMessage);
    myPeerConnection.setRemoteDescription(message.sdp).catch(handleErrorMessage);
}


/**
 * 두번째 참여자 입장하면 OFFER -> ICE 순으로 signal 보냄
 * ICE signal에서 이어지는 함수
 * 
 * ICE란?
 * 
 * ICE는 Client가 통신 가능한 모든 주소를 식별하는 것.
 * 클라이언트가 STUN 메시지를 TURN 서버로 요청하고 응답받으면서 3가지 주소 확인 가능
 * 1. Relayed Address : TURN 서버가 패킷 릴레이를 위해 할당하는 주소
 * 2. Server Reflexive Address : NAT 가 매핑한 클라이언트의 공인망(Public IP, Port)
 * 3. Local Address : 클라이언트의 사설주소(Private IP, Port)
 * 
 * 여기서 Candidate란?
 * 
 * IP와 포트의 조합으로 표시된 주소. 이제 이 확보된 것을 통해 연결을 한다.
 * Direct Connection : Host 같의 직접적인 미디어 송수신
 * Server Reflexive Connection : Server Reflexive Candidate를 이용한 미디어 송수신
 * TURN Relay Connection : Relay Candidate를 이용한 미디어 송수신
 * 이렇게 확보된 3개의 주소들의 우선순위를 정하여 SDP내에 포함시켜 전송한다.
 * Connection 체크 후 Connection이 완료되면 RTP 및 RTCP 패킷을 전송하여 통화가 가능하게 된다.
 */
function handleNewICECandidateMessage(message) {
    let candidate = new RTCIceCandidate(message.candidate);
    
    // 웹사이트 관리자모드 콘솔창에 세가지 주소들이 전부 기록된다.
    // 연결을 끊지 않는 한
    // 통신할 수 있는 주소를 계속해서 찾아냄 (최적의 주소로 통신하려고)
    console.log("Adding received ICE candidate: " + JSON.stringify(candidate));
    myPeerConnection.addIceCandidate(candidate).catch(handleErrorMessage);
}

/**
 * 화면 공유
 */
const screenHandler = new ScreenHandler();
let shareView = null;

/**
 * ScreenHandler
 * @constructor
 * 화면 공유기능 관리 함수
 */
function ScreenHandler() {
    // let localStream = null;

	// 웹콘솔창에 가장 먼저 찍히는 로그
    log('Loaded ScreenHandler', arguments);

    // REF https://developer.mozilla.org/en-US/docs/Web/API/MediaTrackConstraints#Properties_of_shared_screen_tracks
    // 나 자신의 비디오 설정
    const constraints = {
        audio: true,
        video: {
            width: 1980, // 최대 너비
            height: 1080, // 최대 높이
            frameRate: 40, // 최대 프레임
        },
    };

    /**
     * 스크린캡쳐 API를 브라우저 호환성 맞게 리턴.
     * navigator.mediaDevices.getDisplayMedia 호출 (크롬 72 이상 지원)
     * navigator.getDisplayMedia 호출 (크롬 70 ~ 71 실험실기능 활성화 or Edge)
     */
    function getCrossBrowserScreenCapture() {
        if (navigator.getDisplayMedia) {
            return navigator.getDisplayMedia(constraints);
        } else if (navigator.mediaDevices.getDisplayMedia) {
            return navigator.mediaDevices.getDisplayMedia(constraints);
        }
    }

    /**
     * 스크린캡쳐 API를 호출.
     * @returns shareView
     */
    async function start() {

        try {
            shareView = await getCrossBrowserScreenCapture();
        } catch (err) {
            log('Error getDisplayMedia', err);
        }

        return shareView;
    }

    /**
     * 스트림의 트렉을 stop()시켜 스트림이 전송을 중지.
     */
    function end() {

        shareView.getTracks().forEach((track) => {
            // log("화면 공유 중지")
            track.stop();
        });

        // 전송 중단 시 share-video 부분 hide
        // $("#share-video").hide();
    }

    /**
     * extends
     */
    this.start = start;
    this.end = end;

}

/**
 * 로컬 스트림 핸들링
 * @param stream
 */
function onLocalStream(stream) {
    log('onLocalStream', stream);

    const $video = document.querySelector('#share-video');
    $video.srcObject = stream;
}

/**
 * screenHandler를 통해 스크린 API를 호출합니다 - 시작
 * 원격 화면을 화면 공유 화면으로 교체
 */
async function startScreenShare() {
	
	// 스크린 API 호출 & 시작
    await screenHandler.start();
    
     // 1. myPeerConnection 에 연결된 다른 sender 쪽으로 - 즉 다른 Peer 쪽으로 -
    // 2. shareView 의 Track 에서 0번째 인덱스에 들어있는 값 - 즉 videoStream 로 - 교체한다.
    await myPeerConnection.getSenders().forEach((sender)=>{ // 연결된 sender 로 보내기위한 반복문

    // 3. track 를 shareView 트랙으로 교체
    sender.replaceTrack(shareView.getTracks()[0])
    })
	

    /**
     * 화면 공유 중지 눌렀을 때 이벤트
     * share-video 가 사라지는 것 뿐만 아니라 추가로 video-off 에서 video-on 으로 변경되도록 만들어야함
     */
    shareView.getVideoTracks()[0].addEventListener('ended', () =>{
        // log('screensharing has ended')

        // 4. 화면 공유 중지 시 Track 를 localstream 의 videoStram 로 교체함
        myPeerConnection.getSenders().forEach((sender) =>{
            sender.replaceTrack(localStream.getTracks()[1]);
        })

        // $("#share-video").hide();

    });

}

/*
*  video off 버튼을 통해 스크린 API 종료
* */
async function stopScreenShare(){
    // screen share 종료
    await screenHandler.end();
    // myPeerConnection
    await myPeerConnection.getSenders().forEach((sender) =>{
        
    // 4. 화면 공유 중지 시 Track 를 localstream 의 videoStram 로 교체함
    sender.replaceTrack(localStream.getTracks()[1]);
    })
}

