<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${pageTitle }</title>
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
	<span>안뇽하세용</span>
	
	<div>
        <video id="localVideo" autoplay width="480px"></video>
        <video id="remoteVideo" width="480px" autoplay></video>
    </div>

	<script src="https://cdnjs.cloudflare.com/ajax/libs/socket.io/4.6.2/socket.io.js"></script>
    <script src="/socket.io/socket.io.js"></script>
   	<script src="https://cdn.jsdelivr.net/rtc/latest/rtc.min.js"></script>
   	<script src="https://webrtc.github.io/adapter/adapter-latest.js"></script>
    <script src="./rtc.js"></script>

<script>
<!-- index.js -->
const http = require('http');
const os = require('os');
const socketIO = require('socket.io');
const nodeStatic = require('node-static');

let fileServer = new(nodeStatic.Server)();
let app = http.createServer((req,res)=>{
    fileServer.serve(req,res);
}).listen(8080);

let io = socketIO.listen(app);
io.sockets.on('connection',socket=>{
    function log() {
        let array = ['Message from server:'];
        array.push.apply(array,arguments);
        socket.emit('log',array);
    }

    socket.on('message',message=>{
        log('Client said : ' ,message);
        socket.broadcast.emit('message',message);
    });

    socket.on('create or join',room=>{
        let clientsInRoom = io.sockets.adapter.rooms[room];
        let numClients = clientsInRoom ? Object.keys(clientsInRoom.sockets).length : 0;
        log('Room ' + room + ' now has ' + numClients + ' client(s)');
        
        if(numClients === 0){
            console.log('create room!');
            socket.join(room);
            log('Client ID ' + socket.id + ' created room ' + room);
            socket.emit('created',room,socket.id);
        }
        else if(numClients===1){
            console.log('join room!');
            log('Client Id' + socket.id + 'joined room' + room);
            io.sockets.in(room).emit('join',room);
            socket.join(room);
            socket.emit('joined',room,socket.id);
            io.sockets.in(room).emit('ready');
        }else{
            socket.emit('full',room);
        }
    });


});
</script>


<script>
<!-- rtc.js -->


<!-- rtc.js 내 영상 정보 가져오기 -->
<!-- mediaDevice 객체의 getUserMedia Method를 통해서 사용자의 미디어 데이터를 스트림으로 받아올 수 있다. -->
<!-- localStream과 localVideo에 출력할 영상을 본인 캠으로 지정한다. -->
let localVideo = document.getElementById("localVideo");
let remoteVideo = document.getElementById("remoteVideo");
let localStream;
let remoteStream;
let isInitiator = false;
let isChannelReady = false;
let isStarted = false;
let pc;

<!-- 소켓통신 -->
let pcConfig = {
    'iceServers': [{
        'urls': 'stun:stun.l.google.com:19302'
      }]
}

let room = 'foo';

let socket = io.connect();

  if(room !==''){
    socket.emit('create or join',room);
    console.log('Attempted to create or join Room',room);
  }

socket.on('created', (room,id)=>{
  console.log('Created room' + room+'socket ID : '+id);
  isInitiator= true;
})

socket.on('full', room=>{
  console.log('Room '+room+'is full');
});

socket.on('join',room=>{
  console.log('Another peer made a request to join room' + room);
  console.log('This peer is the initiator of room' + room + '!');
  isChannelReady = true;
})

socket.on('joined',room=>{
  console.log('joined : '+ room );
  isChannelReady= true;
})
socket.on('log', array=>{
  console.log.apply(console,array);
});

socket.on('message', (message)=>{
  console.log('Client received message :',message);
  if(message === 'got user media'){
    maybeStart();
  }else if(message.type === 'offer'){
    if(!isInitiator && !isStarted){
      maybeStart();
    }
    pc.setRemoteDescription(new RTCSessionDescription(message));
    doAnswer();
  }else if(message.type ==='answer' && isStarted){
    pc.setRemoteDescription(new RTCSessionDescription(message));
  }else if(message.type ==='candidate' &&isStarted){
    const candidate = new RTCIceCandidate({
      sdpMLineIndex : message.label,
      candidate:message.candidate
    });

    pc.addIceCandidate(candidate);
  }
})


<!-- sendMessage() -->
<!-- 시그널링 서버로 소켓정보를 전송하는 메소드이다. -->
<!-- 시그널링 서버 : 다른 Peer로의 데이터를 전송하는 method -->
function sendMessage(message){
  console.log('Client sending message: ',message);
  socket.emit('message',message);
}

navigator.mediaDevices
	.getUserMedia({
	  video: true,
	  audio: false,
	})
	.then(gotStream)
	.catch((error) => console.error(error));

function gotStream(stream) {
console.log("Adding local stream");
localStream = stream;
localVideo.srcObject = stream;
sendMessage("got user media");
if (isInitiator) {
  maybeStart();
}
}

<!-- RTC Peer 연결하기 -->
function createPeerConnection() {
  try {
    pc = new RTCPeerConnection(null);
    pc.onicecandidate = handleIceCandidate;
    pc.onaddstream = handleRemoteStreamAdded;
    console.log("Created RTCPeerConnection");
  } catch (e) {
    alert("connot create RTCPeerConnection object");
    return;
  }
}

<!-- iceCandidate할 대상이 생기면 실행하는 메소드 -->
<!-- 상대 Peer가 내 Stream에 연결할 수 있도록 해주기 -->
function handleIceCandidate(event) {
  console.log("iceCandidateEvent", event);
  if (event.candidate) {
    sendMessage({
      type: "candidate",
      label: event.candidate.sdpMLineIndex,
      id: event.candidate.sdpMid,
      candidate: event.candidate.candidate,
    });
  } else {
    console.log("end of candidates");
  }
}

function handleCreateOfferError(event) {
  console.log("createOffer() error: ", event);
}

<!-- 연결된 Peer는 이 함수를 통해 remoteVideo 뷰에 띄워주기 -->
function handleRemoteStreamAdded(event) {
  console.log("remote stream added");
  remoteStream = event.stream;
  remoteVideo.srcObject = remoteStream;
}

<!-- 자신의 RTCPeerConnection 초기화하고 -->
<!-- 상대방의 RTCPeerConnection과 연결하는 함수 -->
function maybeStart() {
  console.log(">>MaybeStart() : ", isStarted, localStream, isChannelReady);
  if (!isStarted && typeof localStream !== "undefined" && isChannelReady) {
    console.log(">>>>> creating peer connection");
    createPeerConnection();
    pc.addStream(localStream);
    isStarted = true;
    console.log("isInitiator : ", isInitiator);
    if (isInitiator) {
      doCall();
    }
  }else{
    console.error('maybeStart not Started!');
  }
}

<!-- 실제로 연결되면 이 함수로 데이터를 주고받는다 -->
function doCall() {
  console.log("Sending offer to peer");
  pc.createOffer(setLocalAndSendMessage, handleCreateOfferError);
}

function doAnswer() {
  console.log("Sending answer to peer");
  pc.createAnswer().then(
    setLocalAndSendMessage,
    onCreateSessionDescriptionError
  );
}

function setLocalAndSendMessage(sessionDescription) {
  pc.setLocalDescription(sessionDescription);
  sendMessage(sessionDescription);
}

function onCreateSessionDescriptionError(error) {
  console.error("Falied to create session Description", error);
}

</script>

<!-- 카카오 샘플, 어차피 key 없어서 못함 
<main>
  <section>
    <h2>Sponge Edu</h2>
    <h1>Video Conference</h1>

    <div id="button-group">
      <button id="connect">Connect</button>
      <button id="disconnect">Disconnect</button>
    </div>

    <div id="status">Disconnected</div>

    <div id="video-list">
      <ul id="local-container"></ul>
      <h3>Others</h3>
      <ul id="remote-container"></ul>
    </div>

    <div id="log-list">
      <h3>Log</h3>
      <ul id="log">
        <li>Ready to connect</li>
      </ul>
    </div>
  </section>
</main>

<style>

main {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: auto;
}

section {
  width: 80%;
  margin: auto;
}

h1 {
  text-align: center;
  line-height: 2.5rem;
  font-size: 2.25rem;
}

h2 {
  text-align: center;
}

#button-group {
  margin-top: 2.5rem;
  margin-bottom: 2.5rem;
  text-align: center;
}

#connect {
  padding: 0.5rem;
  background-color: blue;
  color: white;
  border: 0;
  cursor: pointer;
}

#disconnect {
  padding: 0.5rem;
  background-color: red;
  color: white;
  border: 0;
  cursor: pointer;
}

#status {
  margin-top: 2rem;
  margin-bottom: 2rem;
  color: rgb(202 138 4 / var(--tw-text-opacity));
  text-align: center;
  line-height: 2rem;
  font-size: 0.8rem;
}

#video-list {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin-top: 2.5rem;
  margin-bottom: 2.5rem;
}

#local-container,
#remote-container {
  color: rgb(156 163 175 / var(--tw-text-opacity));
  text-align: center;
  list-style: none;
}

#local-container {
  margin-top: 1.25rem;
  margin-bottom: 1.25rem;
  padding: 0;
  line-height: 1.25rem;
  font-size: 0.875rem;
}

#remote-container {
  width: 80vw;
  margin-top: 0.5rem;
  margin-bottom: 0.5rem;
  padding: 0;
  line-height: 0.95rem;
  font-size: 0.65rem;
}

#remote-container > li {
  display: inline-block;
}

#local-video {
  width: 160px;
  margin-right: 0.5rem;
  transform: scaleY(var(--tw-scale-y));
}

#remote-video {
  width: 160px;
  margin-right: 0.5rem;
}

#log-list {
  text-align: center;
}

#log {
  padding: 0;
  color: rgb(156 163 175 / var(--tw-text-opacity));
  line-height: 1rem;
  font-size: 0.75rem;
  list-style: none;
}

.cursor-wait {
  cursor: wait;
}

.cursor-pointer {
  cursor: pointer;
}

</style>

<script>


// DATA
let localMedia = null;
let room = null;
let remoteParticipants = [];

const roomId = 'icl-video-call';
const connectButton = document.querySelector('#connect');
const disconnectButton = document.querySelector('#disconnect');
const status = document.querySelector('#status');
const log = document.querySelector('#log');

// METHODs
const createConference = async () => {
  room = ConnectLive.createRoom();

  if (!room) throw new Error('Fail to Create Conference');

  room.on('connected', async (evt) => {
    evt.remoteParticipants.forEach(async (participant) => {
      let videos = [];
      const unsubscribedVideos = participant.getUnsubscribedVideos();

      if (unsubscribedVideos.length) {
        const videoIds = unsubscribedVideos.map((video) => video.getVideoId());
        videos = await room.subscribe(videoIds);
      }

      remoteParticipants.push({ participant, videos });
      remoteParticipants.forEach((remoteParticipant) => {
        const isSameId = remoteParticipant.participant.id === participant.id;
        if (isSameId) addRemoteVideoNode(participant.videos);
      });
    });
  });

  room.on('remoteVideoPublished', async (evt) => {
    const videos = await room.subscribe([evt.remoteVideo.videoId]);

    if (videos.length) {
      const preJoinedParticipant = remoteParticipants.find(
        (item) => item.participant.id === evt.remoteParticipant.id
      );

      if (preJoinedParticipant) {
        preJoinedParticipant.videos =
          preJoinedParticipant.videos.concat(videos);
      }
    }

    addRemoteVideoNode(videos);
  });

  room.on('remoteVideoUnpublished', (evt) => {
    const participantToRemove = remoteParticipants.find(
      (item) => item.participant.id === evt.remoteParticipant.id
    );

    if (participantToRemove) {
      participantToRemove.videos = participantToRemove.videos.filter(
        (video) => video.videoId !== evt.remoteVideo.videoId
      );
    }

    removeRemoteVideoNode(evt.remoteParticipant.id);
    addLog(`${evt.remoteParticipant.id} Left`);
  });
};

// DOM CONTROLS
const activateButton = () => {
  connectButton.disabled = false;
  connectButton.className = 'cursor-pointer';
};

const disableButton = () => {
  connectButton.disabled = true;
  connectButton.className = 'cursor-wait';
};

const changeStatus = (text) => {
  status.innerHTML = text;
};

const addLocalVideoNode = (localMedia, id) => {
  const localContainer = document.querySelector('#local-container');

  const videoItem = document.createElement('li');
  videoItem.id = 'local-video-item';

  const videoHeader = document.createElement('h3');
  videoHeader.innerHTML = `Me (${id})`;

  const localVideo = localMedia.video?.attach();
  localVideo.id = 'local-video';

  videoItem.appendChild(videoHeader);
  videoItem.appendChild(localVideo);
  localContainer.appendChild(videoItem);
};

const addRemoteVideoNode = (videos) => {
  const remoteContainer = document.querySelector('#remote-container');

  videos.forEach((video) => {
    const videoItem = document.createElement('li');
    videoItem.id = video.participantId;

    const videoHeader = document.createElement('h3');
    videoHeader.innerHTML = `Remote User (${video.participantId})`;
    addLog(`${video.participantId} Joined`);

    const remoteVideo = video.attach();
    remoteVideo.id = 'remote-video';

    videoItem.appendChild(videoHeader);
    videoItem.appendChild(remoteVideo);
    remoteContainer.appendChild(videoItem);
  });
};

const removeLocalVideoNode = () => {
  const videoItem = document.querySelector('#local-video-item');
  videoItem?.remove();
};

const removeRemoteVideoNode = (id) => {
  const remoteItem = document.querySelector(`#${id}`);
  remoteItem?.remove();
};

const removeRemoteVideoAll = () => {
  const remoteContainer = document.querySelector('#remote-container');
  remoteContainer.innerHTML = '';
};

const addLog = (text) => {
  const logNode = document.createElement('li');
  logNode.innerHTML = text;
  log.appendChild(logNode);
};

const resetLog = () => {
  log.innerHTML = '';
};

// EVENTs
const connectConference = async () => {
  try {
    resetLog();
    disableButton();
    changeStatus('Connecting...');

    // Provisioning
    await ConnectLive.signIn({
      serviceId: 'ICLEXMPLPUBL',
      serviceSecret: 'ICLEXMPLPUBL0KEY:YOUR0SRVC0SECRET',
    });
    addLog('User Signed In');

    // Create Local Media
    localMedia = await ConnectLive.createLocalMedia({
      audio: true,
      video: true,
    });
    addLog('Local Media Created');

    await createConference();
    addLog('Conference Created');

    if (!room || !localMedia) throw new Error('No Conference to Connect');

    await room.connect(roomId);
    room.publish([localMedia]);
    addLog('Video Connected');

    addLocalVideoNode(localMedia, room.localParticipant.id);
    addLog('Participant Showed');
    changeStatus('Connected');
  } catch (error) {
    console.error(error);
    activateButton();
    changeStatus('Failed to Connect');
    alert('Failed to Start Service');
  }
};

const disConnectConference = () => {
  try {
    if (!room || !localMedia) throw new Error('No Conference to Stop');
    changeStatus('Disconnecting...');

    room.disconnect();
    addLog('Conference Disconnected');

    localMedia.stop();
    localMedia = null;
    removeLocalVideoNode();
    addLog('Video Disconnected');

    remoteParticipants = [];
    removeRemoteVideoAll();
    addLog('Participants Cleared');

    ConnectLive.signOut();
    addLog('User Signed Out');

    activateButton();
    changeStatus('Disconnected');
  } catch (error) {
    console.error(error);
    changeStatus('Failed to Disconnect');
  }
};

connectButton.onclick = connectConference;
disconnectButton.onclick = disConnectConference;
</script>

 -->




</body>
</html>