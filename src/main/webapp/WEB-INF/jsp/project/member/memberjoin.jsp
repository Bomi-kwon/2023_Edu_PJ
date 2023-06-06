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

<body>


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

<section class="login-page">
	<div class="form">
			<form action="doMemberJoin" class="register-form" method="post" enctype="multipart/form-data">
				<div class="flex radiodiv">
					<input type="radio" name="authLevel" value="1"/><span>선생님</span>
					<input type="radio" name="authLevel" value="2" checked/><span>학생</span>
					<input type="radio" name="authLevel" value="3"/><span>학부모</span>
				</div>
				<input class="" type="text" name="loginID" placeholder="아이디를 입력해주세요." />
				<input class="" type="text" name="loginPW" placeholder="비밀번호를 입력해주세요." />
				<input class="" type="text" name="loginPWCheck" placeholder="비밀번호 확인을 입력해주세요." />
				<input class="" type="text" name="name" placeholder="이름을 입력해주세요." />
				<input class="" type="text" name="cellphoneNum" placeholder="전화번호를 입력해주세요." />
				<input class="" type="text" name="email" placeholder="이메일을 입력해주세요." />
				<input class="" type="file" name="file"/>
				<button class="">가입하기</button>
				<p class="message">이미 회원가입을 했나요? <a href="memberlogin">로그인하기</a></p>
			</form>
		</div>
</section>
	
	
	
	
</body>
</html>