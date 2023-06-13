<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="../common/head.jsp" %>


	<section class="mt-8 mx-auto text-xl">
		<div class="container mx-auto px-3 h-full ">
			<form action="doAttendanceChk">
				<input type="hidden" name="todayDate" value="${todayDate }"/>
				<input type="hidden" name="classId" value="${group.id }"/>
				<div class="text-2xl mb-3">오늘도 열심히 공부해봅시다!!!</div>
				<div>오늘 날짜 : ${todayDate }</div>
				<div>수강하는 반 : ${group.groupName }</div>
				<div>수강 요일 : ${group.groupDay }</div>
				<div>선생님 성함 : ${group.teacherName } 선생님</div>
				<div>수업 교재 : ${group.textbook }</div>
				<div><input class="input input-bordered input-success w-60" 
				type="text" name="name" placeholder="이름을 입력해주세요."/></div>
				<button class="btn">출석체크하기</button>
			</form>
		</div>
	</section>



<%@ include file="../common/foot.jsp" %>