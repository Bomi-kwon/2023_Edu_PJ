<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="회원 리스트" />

<%@ include file="../common/head.jsp" %>

<script>
	function selectAuthLevel(authLevel) {
		$('#memberlisttable').html(`<thead>
			      <tr>
		        <th>가입날짜</th>
		        <th>로그인 아이디</th>
		        <th>이름</th>
		        <th>수강하는 반</th>
		        <th>휴대전화</th>
		        <th>이메일</th>
		      </tr>
		    </thead>`);
		
		let authLevelVal = authLevel.value;
		
		$.get('getMembersByAuthLevel', {
			authLevel : authLevelVal
		}, function(data) {
			console.log(data);
			for(var i = 0; i < data.data1.length; i++) {
				let regDate = data.data1[i].regDate;
				let loginID = data.data1[i].loginID;
				let name = data.data1[i].name;
				let groupName = data.data1[i].groupName;
				let cellphoneNum = data.data1[i].cellphoneNum;
				let email = data.data1[i].email;
				
				$('#memberlisttable').append(`<tbody>
					      <tr>
				        <td>`+regDate+`</td>
				        <td>`+loginID+`</td>
				        <td>`+name+`</td>
				        <td>`+groupName+`</td>
				        <td>`+cellphoneNum+`</td>
				        <td>`+email+`</td>
				      </tr>
				    </tbody>`);
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
	
	  <table class="table w-full" id="memberlisttable">
	    <thead>
	      <tr>
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
	        <td>${member.regDate }</td>
	        <td>${member.loginID }</td>
	        <td>${member.name }</td>
	        <td>${member.groupName }</td>
	        <td>${member.cellphoneNum }</td>
	        <td>${member.email }</td>
	      </tr>
	    </tbody>
		</c:forEach>
	  </table>
	</div>
</section>
	
	
	
	
<%@ include file="../common/foot.jsp" %>