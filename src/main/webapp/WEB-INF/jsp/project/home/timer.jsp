<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="Home" />

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
</body>

  <script>
    const dpTime = function () {
      const now = new Date();
      let hours = now.getHours();
      let minutes = now.getMinutes();
      let seconds = now.getSeconds();
      
      let degHour = hours * (360/12) + minutes * (360/12/60);
      let degMinute = minutes * (360/60);
      let degSecond = seconds * (360/60);
      
      let ampm = '';
      if (hours > 12) {
        hours -= 12
        ampm = '오후'
      } else {
        ampm = '오전'
      }
      
      if (hours < 10) {
        hours = '0' + hours
      }
      if (minutes < 10) {
        minutes = '0' + minutes
      }
      if (seconds < 10) {
        seconds = '0' + seconds
      }
      
      $('#time').html(ampm + " " + hours + ":" + minutes + ":" + seconds);
      
      $('.hour').css('transform','rotate('+degHour+'deg)');
      $('.minute').css('transform','rotate('+degMinute+'deg)');
      $('.second').css('transform','rotate('+degSecond+'deg)');
      
    }
    setInterval(dpTime, 1000)  // 1초마다 함수 실행되도록 설정
  </script>
  
  
</body>