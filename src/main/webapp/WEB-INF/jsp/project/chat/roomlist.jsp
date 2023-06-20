<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org" >
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
	<!-- 폰트어썸 불러오기 -->
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.1/css/all.min.css" />
   
    <script src="https://code.jquery.com/jquery-3.6.0.min.js" integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=" crossorigin="anonymous"></script>
    <!-- CSS only -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    
    
    <script>

        function createRoom(){
            var name = $("#roomName").val();

            if(name === ""){
                alert("채팅방 이름을 입력해주세요.")
                return false;
            }
            if ($("#" + name).length > 0){
                alert("이미 존재하는 방입니다")
                return false;
            } else {
                return true;
            }
        }
    </script>
   
    <style>
        a {
            text-decoration: none;
        }
    </style>
</head>
<body>

<section class="mt-8 mx-auto text-xl">
	<div class="container mx-auto px-3">
	
	
	
	
	
	
    <th:block th:fragment="content">
        <div class="container">
            <div>
                <ul>
                <c:forEach var="room" items="${list }">
                    <li class="list-group-item d-flex justify-content-between align-items-start">
                        <div class="ms-2 me-auto">
                            <div class="fw-bold">
                                <span class="hidden" th:id="${room.roomName}"></span>
                                <a href="/project/chat/room?roomId=${room.roomId}">채팅방 이름 : [${room.roomName}]</a>
                            </div>
                        </div>
                        <span class="badge bg-primary rounded-pill">채팅방 인원수 : [${room.userCount}]명</span>
                    </li>
                </c:forEach>
                </ul>
            </div>
        </div>
        <form action="/project/chat/createroom" method="post" onsubmit="return createRoom()">
            <input type="text" name="name" class="form-control" id="roomName">
            <button class="btn btn-secondary" id="create">개설하기</button>
        </form>
    </th:block>
    </div>
    </section>
    
</body>
</html>