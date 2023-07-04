<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="../common/head.jsp" %>

<link rel="stylesheet" href="/css/home/timer.css" />



<section class="mt-8 mx-auto text-xl">
	<div class="container mx-auto px-3 relative">
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
	    <div class="timertest text-center">
	    	<div class="mb-3">시간 이벤트 테스트</div>
	    	<div class="table-box-type-1">
	    	<table class="w-full">
	    		<thead>
			      <tr>
			        <th>교시</th>
			        <th>시험영역</th>
			        <th colspan="2">시간</th>
			      </tr>
			    </thead>
			    <tbody>
			    	<tr class="korean">
				        <th>1</th>
				        <th>국어</th>
				        <th>08:40 - 10:00</th>
				        <th>80분</th>
			      	</tr>
			      	<tr class="math">
				        <th>2</th>
				        <th>수학</th>
				        <th>10:30 - 12:10</th>
				        <th>100분</th>
			      	</tr>
			      	<tr class="english">
				        <th>3</th>
				        <th>영어</th>
				        <th>13:10 - 14:20</th>
				        <th>70분</th>
			      	</tr>
			      	<tr class="history">
				        <th rowspan="3">4</th>
				        <th>한국사</th>
				        <th>14:50 - 15:20</th>
				        <th>30분</th>
			      	</tr>
			      	<tr class="chemistry">
				        <th>탐구1</th>
				        <th>15:35 - 16:05</th>
				        <th>30분</th>
			      	</tr>
			      	<tr class="biology">
				        <th>탐구2</th>
				        <th>16:07 - 16:37</th>
				        <th>30분</th>
			      	</tr>
			      	<tr class="chinese">
				        <th>5</th>
				        <th>제2외국어/한문</th>
				        <th>17:05 - 17:45</th>
				        <th>40분</th>
			      	</tr>
			    </tbody>
	    	</table>
	    	<div>
	    		<div class="my-2">재생할 파일과 시간</div>
	    		<div class="">
		    		<form id="fileForm" method="post" enctype="multipart/form-data">
		    			<span class="">파일 : </span>
					    <input class="input input-bordered input-success w-96" type="file" name="file">
					</form>
				</div>
	    		<div><span>날짜 : </span><input class="input input-bordered input-success w-96" id="reserveDate" type="date" min="2022-06-01" max="2023-07-31"/></div>
	    		<div><span>시간 : </span><input class="input input-bordered input-success w-96" id="reserveTime" type="time" /></div>
	    	</div>
	    	<button id="reserve-button" class="btn btn-active btn-ghost mt-1">예약</button>
	    	<button id="play-button" class="btn btn-active btn-ghost mt-1"><i class="fa-regular fa-circle-play"></i></button>
	    	<button id="stop-button" class="btn btn-active btn-ghost mt-1"><i class="fa-regular fa-circle-pause"></i></button>
	    	</div>
	    	</div>
    </div>
</section>

<script src="/js/home/timer.js"></script>
    

  
  
