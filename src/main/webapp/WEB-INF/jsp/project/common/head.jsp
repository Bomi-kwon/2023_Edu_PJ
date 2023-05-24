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
<!-- 토스트 UI 에디터 코어 -->
<script src="https://uicdn.toast.com/editor/latest/toastui-editor-all.min.js"></script>
<link rel="stylesheet" href="https://uicdn.toast.com/editor/latest/toastui-editor.min.css" />

<link rel="stylesheet" href="/resource/common.css" />
<script src="/resource/common.js" defer="defer"></script>
</head>
<body>

<section class="top-top-bar">
	<nav class="px-3 mx-auto container h-full">
	<div class="logo flex items-center">
    	<a href="/" class="px-3 flex items-center text-3xl"><span>스폰지에듀</span></a>
    </div>
  
	  <div class="menu-box-2">
	    <ul class="h-full flex items-center w-full">
		    <c:if test="${rq.getLoginedMember() == null }">
				<li class="hover:underline"><a class="h-full px-3 flex items-center" href="/project/member/memberlogin"><span>로그인</span></a></li>
				<li class="hover:underline"><a class="h-full px-3 flex items-center" href="/project/member/memberjoin"><span>회원가입</span></a></li>
			</c:if>
			<c:if test="${rq.getLoginedMember() != null }">
				<c:if test="${rq.getLoginedMember().getAuthLevel() == 1 }">
					<li class="hover:underline"><a class="h-full px-3 flex items-center" href="/project/member/memberlist"><span>회원명단</span></a></li>
				</c:if>
			</c:if>
			<c:if test="${rq.getLoginedMember() != null }">
				<li class="hover:underline"><a class="h-full px-3 flex items-center" href="/project/member/memberprofile"><span>마이페이지</span></a></li>
				<li class="hover:underline"><a class="h-full px-3 flex items-center" href="/project/member/doMemberLogout"><span>로그아웃</span></a></li>
			</c:if>
	    </ul>
	  </div>
	  </nav>
</section>

<section class="top-bar">
  <nav class="flex justify-around px-3 mx-auto container text-2xl h-full">
	  <div class="menu-box-1">
	    <ul class="flex justify-between items-center h-full">
	      <li><a href="#">수업관리</a>
	        <ul>
	          <li class="hover:underline"><a class="h-full px-3 flex items-center" href="#"><span>출결</span></a></li>
	          <li class="hover:underline"><a class="h-full px-3 flex items-center" href="/project/member/membergroup"><span>반 관리</span></a></li>
	          <li class="hover:underline"><a class="h-full px-3 flex items-center" href="/project/article/list?boardId=2"><span>숙제</span></a></li>
	          <li class="hover:underline"><a class="h-full px-3 flex items-center" href="/project/article/scorelist"><span>성적</span></a></li>
	        </ul>
	      </li>
	      <li><a href="#">커뮤니티</a>
	        <ul>
	          <li class="hover:underline"><a class="h-full px-3 flex items-center" href="/project/article/list?boardId=1"><span>알림장</span></a></li>
	          <li class="hover:underline"><a class="h-full px-3 flex items-center" href="#"><span>메세지 발송</span></a></li>
	        </ul>
	      </li>
	      <li><a href="#">인터넷강의</a>
	        <ul>
	          <li class="hover:underline"><a class="h-full px-3 flex items-center" href="#"><span>수강신청</span></a></li>
	          <li class="hover:underline"><a class="h-full px-3 flex items-center" href="#"><span>실시간 강의</span></a></li>
	          <li class="hover:underline"><a class="h-full px-3 flex items-center" href="#"><span>수학인강</span></a></li>
	        </ul>
	      </li>
	      <li><a href="#">자율학습</a>
	        <ul>
	          <li class="hover:underline"><a class="h-full px-3 flex items-center" href="/project/home/timer"><span>모의고사 시계</span></a></li>
	          <li class="hover:underline"><a class="h-full px-3 flex items-center" href="#"><span>공부 인증</span></a></li>
	        </ul>
	      </li>
	      <li>
		      <div class="menu-button h-full w-12 flex items-center text-4xl">
		      <i class="fa-solid fa-bars"></i>
		      </div>
	      </li>
	    </ul>
	  </div>
	 
	  
  </nav>
  
  <div class="menu-box-3">
    <ul class="h-full flex items-start justify-around">
      <li><a href="#">수업관리</a>
        <ul>
          <li class="hover:underline"><a class="h-full px-3 flex items-center" href="#"><span>출결</span></a></li>
          <li class="hover:underline"><a class="h-full px-3 flex items-center" href="/project/member/membergroup"><span>반 관리</span></a></li>
          <li class="hover:underline"><a class="h-full px-3 flex items-center" href="/project/article/list?boardId=2"><span>숙제</span></a></li>
          <li class="hover:underline"><a class="h-full px-3 flex items-center" href="#"><span>성적</span></a></li>
        </ul>
      </li>
      <li><a href="#">커뮤니티</a>
        <ul>
          <li class="hover:underline"><a class="h-full px-3 flex items-center" href="/project/article/list?boardId=1"><span>알림장</span></a></li>
          <li class="hover:underline"><a class="h-full px-3 flex items-center" href="#"><span>메세지 발송</span></a></li>
        </ul>
      </li>
      <li><a href="#">인터넷강의</a>
        <ul>
          <li class="hover:underline"><a class="h-full px-3 flex items-center" href="#"><span>수강신청</span></a></li>
          <li class="hover:underline"><a class="h-full px-3 flex items-center" href="#"><span>실시간 강의</span></a></li>
          <li class="hover:underline"><a class="h-full px-3 flex items-center" href="#"><span>수학인강</span></a></li>
        </ul>
      </li>
      <li><a href="#">자율학습</a>
        <ul>
          <li class="hover:underline"><a class="h-full px-3 flex items-center" href="/project/home/timer"><span>모의고사 시계</span></a></li>
          <li class="hover:underline"><a class="h-full px-3 flex items-center" href="#"><span>공부 인증</span></a></li>
        </ul>
      </li>
    </ul>
  </div>
</section>


<section class="my-3 text-2xl">
		<div class="container mx-auto px-3">
			<h1>${pageTitle }</h1>
		</div>
	</section>