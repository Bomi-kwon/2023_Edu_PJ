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
</head>
<body>

<script>
$('')
	function appear_2() {
		alert('버튼 눌림');
		$('')
	}
</script>

<div class="h-20 flex container mx-auto text-xl">
	
	<ul class="w-full bg-red-100 flex">
		<li><a href="/"><i class="fa-solid fa-infinity"></i></a></li>
	  <li>
	    <a><i class="fa-solid fa-bars"></i></a>
	    <ul class="bg-base-100 hidden">
	      <li><a>과제</a></li>
	      <li><a>성적</a></li>
	      <li><a>알림장</a></li>
	    </ul>
	  </li>
	  
	  <li class="flex-grow"><a></a></li>
	   <li>
	    <a><i class="fa-regular fa-user"></i></a>
	    <ul class="bg-base-100 hidden">
	      <li><a>로그아웃</a></li>
	      <li><a>마이페이지</a></li>
	    </ul>
	  </li>
	</ul>
</div>

<section class="my-3 text-2xl">
		<div class="container mx-auto px-3">
			<h1>${pageTitle }</h1>
		</div>
	</section>