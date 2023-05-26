<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="회원 리스트" />

<%@ include file="../common/head.jsp" %>

<script>
	function selectAuthLevel(authLevel) {
		let authLevelVal = authLevel.value;
		
		$.get('getMembersByAuthLevel', {
			authLevel : authLevelVal
		}, function(data) {
			console.log(data);
			for(var i = 0; i < data.data1.length; i++) {
				
			}
		
		}, 'json');
	}
</script>


<section class="mt-8 mx-auto text-xl">
	<div class="container mx-auto px-3">
	
	<div class="mb-5">
		<label for="">회원 구분 : </label>
		<select name="authLevel" class="select select-success w-full max-w-xs" onchange="selectAuthLevel(this);">
			<option value="0">전체</option>
			<option value="1">선생님</option>
			<option value="2">학생</option>
			<option value="3">학부모</option>
		</select>
	</div>
	
	  <table class="table w-full">
	    <thead>
	      <tr>
	      	<th>회원구분</th>
	        <th>가입날짜</th>
	        <th>로그인 아이디</th>
	        <th>이름</th>
	        <th>수강하는 반</th>
	        <th>휴대전화</th>
	        <th>이메일</th>
	      </tr>
	    </thead>
	    
		<c:forEach var="member" items="${members }">
	    <tbody>
	      <tr>
	      	<td>${member.authLevel }</td>
	        <td>${member.regDate }</td>
	        <td>${member.loginID }</td>
	        <td>${member.name }</td>
	        <td>${member.classId }</td>
	        <td>${member.cellphoneNum }</td>
	        <td>${member.email }</td>
	      </tr>
	    </tbody>
		</c:forEach>
	  </table>
	</div>
</section>
	
	
	
	
<%@ include file="../common/foot.jsp" %>