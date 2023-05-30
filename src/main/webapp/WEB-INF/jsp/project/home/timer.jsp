<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="../common/head.jsp" %>


<body>
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
    <div class="timertest">시간 이벤트 테스트</div>
</body>

  <script>
    const dpTime = function () {
      const now = new Date();
      let year = now.getFullYear();
      let month = now.getMonth();
      let date = now.getDate();
      
      let hours = now.getHours();
      let minutes = now.getMinutes();
      let seconds = now.getSeconds();
      
      const startTime = new Date('2023-05-30 14:30:00');
      const endTime = new Date('2023-05-30 15:05:00');
      
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

	  console.clear();
      console.log('시작 시간 : ' + startTime);
      console.log('끝 시간 : ' + endTime);
      console.log('현재 : ' + now);
      
      if(now > startTime) {
    	  if(now < endTime ) {
    		  $('.timertest').css('background-color','blue');
    	  }
    	  else {
    		  $('.timertest').css('background-color','yellow');
    	  }
      }
    }
    setInterval(dpTime, 1000)  // 1초마다 함수 실행되도록 설정
  </script>
  
  
