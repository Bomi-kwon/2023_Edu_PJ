// 모의고사 시계 (timer.jsp)

const dpTime = function () {
      const now = new Date();
      let year = now.getFullYear();
      let month = now.getMonth() + 1;		//.getMonth는 달을 0~11로 나타내서 꼭 +1 해주기
      let date = now.getDate();
      let hours = now.getHours();
      let minutes = now.getMinutes();
      let seconds = now.getSeconds();
      
      // 한자리 수는 앞에 0 붙여주기
      if (month < 10) {
    	  month = '0' + month;
      } if (date < 10) {
    	  date = '0' + date;
      } if (hours < 10) {
    	  hours = '0' + hours;
      } if (minutes < 10) {
    	  minutes = '0' + minutes;
      } if (seconds < 10) {
    	  seconds = '0' + seconds;
      }
      
      // 디지털 시계
      $('#time').html(year + "년 " + month + "월 " + date + "일" +'<br />'+ hours + "시 " + minutes + "분 " + seconds + "초");
      
      // 아날로그 시계
      let degHour = hours * (360/12) + minutes * (360/12/60);
      let degMinute = minutes * (360/60);
      let degSecond = seconds * (360/60);
      $('.hour').css('transform','rotate('+degHour+'deg)');
      $('.minute').css('transform','rotate('+degMinute+'deg)');
      $('.second').css('transform','rotate('+degSecond+'deg)');

      let nowTime = hours + "" + minutes + "" + seconds;
      // console.clear();
      // console.log('현재 : ' + now);
      // console.log('시간만(밀리초): ' + now.getTime());
      // console.log('시간만: ' + nowTime);
      
      // 현재 시간에 따라 시험보는 과목 표시
      if(nowTime >= 084000 && nowTime <= 100000) {
    	  $('.korean').css('color','red');
    	  $('.korean').css('background-color','yellow');
      } else if(nowTime >= 103000 && nowTime <= 121000) {
    	  $('.math').css('color','red');
    	  $('.math').css('background-color','yellow');
      } else if(nowTime >= 131000 && nowTime <= 142000) {
    	  $('.english').css('color','red');
    	  $('.english').css('background-color','yellow');
      } else if(nowTime >= 145000 && nowTime <= 152000) {
    	  $('.history').css('color','red');
    	  $('.history').css('background-color','yellow');
      } else if(nowTime >= 153500 && nowTime <= 160500) {
    	  $('.chemistry').css('color','red');
    	  $('.chemistry').css('background-color','yellow');
      } else if(nowTime >= 160700 && nowTime <= 163700) {
    	  $('.biology').css('color','red');
    	  $('.biology').css('background-color','yellow');
      } else if(nowTime >= 170500 && nowTime <= 174500) {
    	  $('.chinese').css('color','red');
    	  $('.chinese').css('background-color','yellow');
      }
      
      
    }
    
    setInterval(dpTime, 1000)		// 1초마다 함수 실행되도록 설정
    
    var audio;
    
    function playAudioAtTime(audioPath, playTime) {
    	  audio = new Audio(audioPath);
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
    
    // 재생 예약 버튼 눌렀을때 실행되는 함수
    $('#reserve-button').click(function() {
    	
	    let reserveDate = $('#reserveDate').val();
	    let reserveTime = $('#reserveTime').val();
	    console.log('입력한 날짜 : ' + reserveDate);
	    console.log('입력한 시간 : ' + reserveTime);
	    console.log(reserveDate + ' ' + reserveTime);
	    
	    if(!reserveDate || !reserveDate) {
	    	alert('예약할 날짜와 시간을 입력해주세요');
	    	return;
	    }
	    
	    // form 내의 데이터를 FormData() 객체로 변환시켜 통째로 가져온다
	    var formData = new FormData($('#fileForm')[0]);
	    
	    // 선택한 오디오 파일 업로드
	    $.ajax({
	    	type: "POST",
	    	enctype: 'multipart/form-data',	
	    	url: 'uploadAudioFile',
	    	data: formData,		
	    	processData: false,		// false 쓰면 formData를 string으로 변환하지 않음
	    	contentType: false,		// false 쓰면 content-type 헤더가 multipart/form-data로 전송되게 함
	    	cache: false        // 브라우저 캐시를 막아서 이전 값이 아닌 현재 값을 호출한다
	    }).done(function(data){
	    		var audioPath = "/project/home/file/" + data;
	    		console.log(audioPath);
	   			var playTime = new Date(reserveDate + ' ' + reserveTime).getTime();
	  			playAudioAtTime(audioPath, playTime);
    	        alert('듣기평가 파일 재생이' + reserveDate + ' ' + reserveTime + '으로 예약되었습니다.');
    	        $('#reserve-button').html('예약완료');
	    });
    })
    
    // 오디오 멈춤
    $('#stop-button').click(function() {
    	audio.pause();
    })
    
    // 오디오 재생
    $('#play-button').click(function() {
    	audio.play();
    })