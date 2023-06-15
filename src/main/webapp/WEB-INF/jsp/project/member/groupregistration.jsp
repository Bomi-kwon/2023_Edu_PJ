<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="수강신청" />

<%@ include file="../common/head.jsp" %>

<script>
	function getGroupsByTeacherID(groupTeacherId) {
		$('#grouptable').html(`<thead>
			      <tr>
		      	<th>사진</th>
		        <th>강좌명</th>
		        <th>수강료</th>
		        <th>교재</th>
		        <th>수강신청</th>
		      </tr>
		    </thead>`);
		
		let groupTeacherIdVal = groupTeacherId.value;
		
		$.get('getGroupsByTeacherID', {
			groupTeacherId : groupTeacherIdVal
		}, function(data) {
			console.log(data);
			for(var i = 0; i < data.data1.length; i++) {
				let groupId = data.data1[i].id;
				let groupName = data.data1[i].groupName;
				let textbook = data.data1[i].textbook;
				
				$('#grouptable').append(`
						<tbody>
					      <tr>
					      	<td>사진넣을곳</td>
					        <td>`+groupName+`</td>
					        <td>200,000원</td>
					        <td>`+textbook+`</td>
					        <td><a href="groupregisterdetail?id=`+groupId+`" class="btn btn-success">수강신청</a></td>
				     	 </tr>
				    	</tbody>`);
				
			}
		
		}, 'json');
	}
</script>


<section class="mt-8 mx-auto text-xl">
	<div class="container mx-auto px-3">
	
	<div class="mb-5">
		<label for="">선생님 성함 : </label>
		<select name="groupTeacherId" class="select select-success w-full max-w-xs" onchange="getGroupsByTeacherID(this);">
				<option value="0">전체</option>
			<c:forEach var="teacher" items="${teachers }">
				<option value="${teacher.id }">${teacher.name }</option>
			</c:forEach>
		</select>
	</div>
	
	  <table class="table w-full" id="grouptable">
	    <thead>
	      <tr>
	      	<th>사진</th>
	        <th>강좌명</th>
	        <th>수강료</th>
	        <th>교재</th>
	        <th>수강신청</th>
	      </tr>
	    </thead>
	    
	    <tbody>
	    <c:forEach var="group" items="${groups }">
	      <tr>
	      	<td>사진넣을곳</td>
	        <td>${group.groupName }</td>
	        <td>200,000원</td>
	        <td>${group.textbook }</td>
	        <td><a href="groupregisterdetail?id=${group.id }" class="btn btn-success">수강신청</a></td>
     	 </tr>
     	 </c:forEach>
    	</tbody>
	    
	  </table>
	</div>
</section>
	
	
	
	
<%@ include file="../common/foot.jsp" %>