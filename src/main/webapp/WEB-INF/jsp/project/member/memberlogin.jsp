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

</head>

<style>
	body {
	  height: 100%;
	  background: linear-gradient(90deg, rgba(141,194,111,1) 0%, rgba(118,184,82,1) 50%);
	  background-size: cover;
	  background-position: center;
	}
	html {
		height: 100%;
	}
</style>
<body>


<section class="login-page">
	<div class="form">
			<form action="doMemberLogin" class="login-form">
				<input class="" type="text" name="loginID" placeholder="아이디를 입력해주세요." required/>
				<input class="" type="password" name="loginPW" placeholder="비밀번호를 입력해주세요." required/>
				<button class="">로그인</button>
				
				<p class="message">아직 회원가입을 안 했나요? <a href="memberjoin">회원가입하기</a></p>
				<p class="message">아이디/비밀번호를 잊었나요? <a href="findLoginID">아이디/비밀번호 찾기</a></p>
			</form>
		</div>
</section>



	
	
	
	
</body>
</html>