<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
 <title>Chat Room</title>
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


    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0">
        <!-- Latest minified Bootstrap & JQuery-->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.6/umd/popper.min.js"
            integrity="sha384-wHAiFfRlMFy6i5SRaxvfOCifBUQy1xHdJ/yoi7FRNXMRBu5WHdZYu1hA6ZOblgut"
            crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/js/bootstrap.min.js"
            integrity="sha384-B0UglyR+jN6CkvvICOB2joaf5I4l3gm9GU6Hc1og6Ls7i6U/mkkaduKaBhlAXv9k"
            crossorigin="anonymous"></script>

    <link rel="stylesheet" href="/css/rtc/main.css"/> 
    
    <style>
    	 .btn.active {
            display: none;
        }

        .btn span:nth-of-type(1)  {
            display: none;
        }
        .btn span:last-child  {
            display: block;
        }

        .btn.active span:nth-of-type(1)  {
            display: block;
        }
        .btn.active span:last-child  {
            display: none;
        }
    </style>
    
   </head>
<body>

<section class="mt-8 mx-auto text-xl">
	<div class="container mx-auto px-3">
<!-- Begin page content -->
<main role="main" class="container-fluid">
    <h1>${room.roomName}</h1>
    <input type="hidden" id="id" name="id" value="${room.roomId}"/>
    <input type="hidden" id="uuid" name="uuid" value="${uuid}">
    <div><span>방 번호 : ${room.roomId }</span></div>
    <div><span>내 이름 : ${uuid}</span></div>
    <div class="col-lg-12 mb-3">
        <div class="col-lg-12 mb-3">
            <div class="d-flex justify-content-around mb-3">
                <div id="buttons" class="row">
                    <div class="btn-group mr-2" role="group">
                        <div class="mr-2" data-toggle="buttons">
                            <label class="btn btn-outline-success" id="video_off">
                                <input type="radio" name="options" style="display:none" autocomplete="off">Video On
                            </label>
                            <label class="btn btn-outline-warning active" id="video_on">
                                <input type="radio" name="options" style="display:none" autocomplete="off" checked>Video Off
                            </label>
                        </div>
                        <div class="mr-2" data-toggle="buttons">
                            <label class="btn btn-outline-success" id="audio_off">
                                <input type="radio" name="options" style="display:none" autocomplete="off">Audio On
                            </label>
                            <label class="btn btn-outline-warning active" id="audio_on">
                                <input type="radio" name="options" style="display:none" autocomplete="off" checked>Audio Off
                            </label>
                        </div>
                        <div class="mr-2" data-toggle="buttons">
                            <label class="btn btn-outline-success" id="view_on">
                                <input type="radio" name="options" style="display:none" autocomplete="off">view On
                            </label>
                            <label class="btn btn-outline-warning active" id="view_off">
                                <input type="radio" name="options" style="display:none" autocomplete="off">view Off
                            </label>
                        </div>
                    </div>

                    <a href="/">
                        <button type="button" class="btn btn-outline-danger" id="exit" name="exit">
                            Exit Room
                        </button>
                    </a>
                </div>
            </div>
        </div>

        <div class="row justify-content-around mb-3">
            <div class="col-lg-6 mb-3">
                <video id="local_video" autoplay playsinline></video>
            </div>
            <div class="col-lg-6 mb-3">
                <video id="remote_video" autoplay playsinline></video>
            </div>
            <div class="col-lg-6 mb-3">
                <video id="share-video" autoplay=""></video>
            </div>
        </div>
    </div>
</main>
</div>
</section>

<script src="/js/chat/rtcroom.js"></script>
</body>
</html>