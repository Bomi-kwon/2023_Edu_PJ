<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="../common/head.jsp" %>


<body>


<section class="mt-8 mx-auto text-xl">
	<div class="container mx-auto px-3">
	    <div class="clock-container">
		    <div class="clock">
		    	<div class="hour"></div>
		    	<div class="minute"></div>
		    	<div class="second"></div>
		    </div>
		    <div class="timer text-5xl">
	        	<div id="time" class=""></div>
	    	</div>
	    </div>
	    <div class="timertest text-center">
	    	<div class="mb-5">시간 이벤트 테스트</div>
	    	<div class="table-box-type-1">
	    	<table class="w-full">
	    		<thead>
			      <tr>
			        <th>교시</th>
			        <th>시험영역</th>
			        <th colspan="2">시간</th>
			      </tr>
			    </thead>
			    <tbody>
			    	<tr class="korean">
				        <th>1</th>
				        <th>국어</th>
				        <th>08:40 - 10:00</th>
				        <th>80분</th>
			      	</tr>
			      	<tr class="math">
				        <th>2</th>
				        <th>수학</th>
				        <th>10:30 - 12:10</th>
				        <th>100분</th>
			      	</tr>
			      	<tr class="english">
				        <th>3</th>
				        <th>영어</th>
				        <th>13:10 - 14:20</th>
				        <th>70분</th>
			      	</tr>
			      	<tr class="history">
				        <th rowspan="3">4</th>
				        <th>한국사</th>
				        <th>14:50 - 15:20</th>
				        <th>30분</th>
			      	</tr>
			      	<tr class="chemistry">
				        <th>탐구1</th>
				        <th>15:35 - 16:05</th>
				        <th>30분</th>
			      	</tr>
			      	<tr class="biology">
				        <th>탐구2</th>
				        <th>16:07 - 16:37</th>
				        <th>30분</th>
			      	</tr>
			      	<tr class="chinese">
				        <th>5</th>
				        <th>제2외국어/한문</th>
				        <th>17:05 - 17:45</th>
				        <th>40분</th>
			      	</tr>
			    </tbody>
	    	</table>
	    	<div class="">
	    		<span>영어듣기평가 파일 : </span>
	    		<audio id="myAudio" src="https://blog.kakaocdn.net/dn/GfTMk/btskfczI6z8/s9g8hbJUItpfUo87N30Opk/00_%EC%95%88%EB%82%B4%EB%A9%98%ED%8A%B8.mp3?attach=1&knm=tfile.mp3" controls></audio>
	    	</div>
	    	<div>
	    		<span>재생할 날짜와 시간</span>
	    		<input id="nowDate" type="date" min="2023-06-01" max="2023-07-31"/>
	    		<input id="nowTime" type="time" />
	    	</div>
	    	<button id="play-button">입력</button>
	    	</div>
	    	</div>
    </div>
</section>
    
</body>

  <script>
    const dpTime = function () {
      const now = new Date();
      let year = now.getFullYear();
      let month = now.getMonth() + 1;
      let date = now.getDate();
      
      let hours = now.getHours();
      let minutes = now.getMinutes();
      let seconds = now.getSeconds();
      
      if (month < 10) {
    	  month = '0' + month;
      }
      if (date < 10) {
    	  date = '0' + date;
      }
      if (hours < 10) {
    	  hours = '0' + hours;
      }
      if (minutes < 10) {
    	  minutes = '0' + minutes;
      }
      if (seconds < 10) {
    	  seconds = '0' + seconds;
      }
      $('#time').html(year + "년 " + month + "월 " + date + "일" +'<br />'+ hours + "시 " + minutes + "분 " + seconds + "초");
      
      let degHour = hours * (360/12) + minutes * (360/12/60);
      let degMinute = minutes * (360/60);
      let degSecond = seconds * (360/60);
      $('.hour').css('transform','rotate('+degHour+'deg)');
      $('.minute').css('transform','rotate('+degMinute+'deg)');
      $('.second').css('transform','rotate('+degSecond+'deg)');

      
      let nowTime = hours + "" + minutes + "" + seconds;
     
      <!-- 
	  console.clear();
      console.log('현재 : ' + now);
      console.log('시간만(밀리초): ' + now.getTime());
      console.log('시간만: ' + nowTime);
      -->
      
      if(nowTime >= 084000 && nowTime <= 100000) {
    	  $('.korean').css('color','red');
    	  $('.korean').css('background-color','yellow');
      }
      else if(nowTime >= 103000 && nowTime <= 121000) {
    	  $('.math').css('color','red');
    	  $('.math').css('background-color','yellow');
      }
      else if(nowTime >= 131000 && nowTime <= 142000) {
    	  $('.english').css('color','red');
    	  $('.english').css('background-color','yellow');
      }
      else if(nowTime >= 145000 && nowTime <= 152000) {
    	  $('.history').css('color','red');
    	  $('.history').css('background-color','yellow');
      }
      else if(nowTime >= 153500 && nowTime <= 160500) {
    	  $('.chemistry').css('color','red');
    	  $('.chemistry').css('background-color','yellow');
      }
      else if(nowTime >= 160700 && nowTime <= 163700) {
    	  $('.biology').css('color','red');
    	  $('.biology').css('background-color','yellow');
      }
      else if(nowTime >= 170500 && nowTime <= 174500) {
    	  $('.chinese').css('color','red');
    	  $('.chinese').css('background-color','yellow');
      }
      
      
    }
    
    setInterval(dpTime, 1000)  // 1초마다 함수 실행되도록 설정
    
    
    
    function playAudioAtTime(audioPath, playTime) {
    	  var audio = new Audio(audioPath);
    	  var currentTime = new Date().getTime();
    	  var delay = playTime - currentTime;

    	  if (delay > 0) {
    	    setTimeout(function() {
    	      audio.play();
    	    }, delay);
    	  } else {
    	    // 이미 지정한 시간이 지난 경우 즉시 재생
    	    audio.play();
    	  }
    	}
    
    $('#play-button').click(function() {
    	
	    console.log($('#nowDate').value);
	    console.log($('#nowTime').value);
	    var playTime = new Date(2023, 5, 16, 18, 39, 0).getTime();
	    var audioPath = "https://blog.kakaocdn.net/dn/GfTMk/btskfczI6z8/s9g8hbJUItpfUo87N30Opk/00_%EC%95%88%EB%82%B4%EB%A9%98%ED%8A%B8.mp3?attach=1&knm=tfile.mp3";
	    
	    playAudioAtTime(audioPath, playTime);
    })

    
  </script>
  
  
