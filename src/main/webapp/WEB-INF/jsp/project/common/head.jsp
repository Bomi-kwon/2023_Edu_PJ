<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${pageTitle }</title>
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


<div class="top-bar h-20 flex px-3 mx-auto container text-2xl">
  <div class="logo flex items-center">
    <a href="/"><img src="https://www.dshw.co.kr/DESIGN/nimg/common/logo_dshw_202111.png" alt=""></a>
  </div>
  <div class="empty flex-grow"></div>
  <div class="menu-box-1">
    <ul class="flex items-center h-full">
      <li><a href="#">브랜드소개</a>
        <ul>
          <li><a href="#">브랜드소개</a></li>
          <li><a href="#">퀀텀라이브러리</a></li>
        </ul>
      </li>
      <li><a href="#">All NEW 대성</a>
        <ul>
          <li><a href="#">All NEW 학습 콘텐츠<span>N</span></a></li>
          <li><a href="#">All NEW 강사진<span>N</span></a></li>
          <li><a href="#">All NEW 공간<span>N</span></a></li>
        </ul>
      </li>
      <li><a href="#">대입결과</a>
        <ul>
          <li><a href="#">대입결과</a></li>
          <li><a href="#">명예의 전당</a></li>
          <li><a href="#">2021 수능/대입결과</a></li>
        </ul>
      </li>
      <li><a href="#">설명회</a>
        <ul>
          <li><a href="#">설명회</a></li>
        </ul>
      </li>
      <li>
        <a href="#">모집프로그램</a>
        <ul>
          <li><a href="#">N수 정규 시즌<span>N</span></a></li>
          <li><a href="#">N수 시즌 Zero<span>N</span></a></li>
          <li><a href="#">재학생 윈터스쿨</a></li>
          <li><a href="#">N수 반수 시즌</a></li>
        </ul>
      </li>
      <li><a href="#" class="d_member"><span><em>디멤버</em> 재원생 전용</span></a></li>
    </ul>
  </div>
  <div class="empty flex-grow"></div>
  <div class="menu-box-2">
    <ul class="h-full flex items-center">
      <li><a href="#">출제진 모집</a></li>
      <li><a href="#">대성학원 전체보기</a></li>
    </ul>
  </div>
  <div class="menu-button flex self-center">
    <i></i>
    <i></i>
    <i></i>
  </div>
  <div class="menu-box-3">
    <ul class="h-full flex items-start justify-around">
      <li class="">
        <a href="#" class="">브랜드소개</a>
        <ul>
          <li><a href="#">브랜드소개</a></li>
          <li><a href="#">퀀텀라이브러리</a></li>
        </ul>
      </li>
      <li>
        <a href="#" class="">ALL NEW 대성</a>
        <ul>
          <li><a href="#">ALL NEW 학습 콘텐츠<span>N</span></a></li>
          <li><a href="#">ALL NEW 강사진<span>N</span></a></li>
          <li><a href="#">ALL NEW 공간<span>N</span></a></li>
        </ul>
      </li>
      <li>
        <a href="#">대입결과</a>
        <ul>
          <li><a href="#">대입결과</a></li>
          <li><a href="#">명예의 전당</a></li>
          <li><a href="#">2021 수능/대입결과</a></li>
        </ul>
      </li>
      <li>
        <a href="#">설명회</a>
        <ul>
          <li><a href="#">설명회</a></li>
        </ul>
      </li>
      <li>
        <a href="#">모집프로그램</a>
        <ul>
          <li><a href="#">N수 정규 시즌<span>N</span></a></li>
          <li><a href="#">N수 시즌 ZERO<span>N</span></a></li>
          <li><a href="#">재학생 윈터스쿨</a></li>
          <li><a href="#">N수 반수 시즌</a></li>
        </ul>
      </li>
    </ul>
  </div>
</div>





<div>
	<div class="h-20 flex container mx-auto text-2xl bg-red-50">
		<a href="/" class="px-3 flex items-center"><span><i class="fa-solid fa-house"></i></span></a>
		<a href="/" class="px-3 flex items-center"><span><i class="fa-solid fa-bars"></i></span></a>
		<div class="flex-grow"></div>
		<ul class="flex">
			<li class="hover:underline"><a class="h-full px-3 flex items-center" href="/project/article/homeworklist"><span>숙제</span></a></li>
			<li class="hover:underline"><a class="h-full px-3 flex items-center" href="/project/article/homeworklist"><span>성적</span></a></li>
			<li class="hover:underline"><a class="h-full px-3 flex items-center" href="/project/article/homeworklist"><span>알림장</span></a></li>
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
</div>

<section class="my-3 text-2xl">
		<div class="container mx-auto px-3">
			<h1>${pageTitle }</h1>
		</div>
	</section>