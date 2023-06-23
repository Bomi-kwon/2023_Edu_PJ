<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="반 리스트" />

<%@ include file="../common/head.jsp" %>

<script type="text/javascript" src="js/jquery.battatech.excelexport.js"></script>

<script>
	function getGroupsByGrade(grade) {
		$('#grouplisttable').html(`<thead>
			      <tr>
		        <th>대상 학년</th>
		      	<th>반 이름</th>
		        <th>수강 요일</th>
		        <th>담당 선생님</th>
		        <th>교재</th>
		        <th>학생 수</th>
		      </tr>
		    </thead>`);
		
		let gradeVal = grade.value;
		
		$.get('getGroupsByGrade', {
			grade : gradeVal
		}, function(data) {
			console.log(data);
			for(var i = 0; i < data.data1.length; i++) {
				
				let id = data.data1[i].id;
				let grade = data.data1[i].grade;
				let groupName = data.data1[i].groupName;
				let groupDay = data.data1[i].groupDay;
				let teacherName = data.data1[i].teacherName;
				let textbook = data.data1[i].textbook;
				let studentNum = data.data1[i].studentNum;
				
				$('#grouplisttable').append(`<tbody>
					      <tr>
				        <td>`+grade+`</td>
				      	<td><div class="font-bold"><a href="getMemberByClassId?classId=`+id+`">`+groupName+`</a></div></td>
				        <td>`+groupDay+`</td>
				        <td>`+teacherName+` 선생님</td>
				        <td>`+textbook+`</td>
				        <td>`+studentNum+`명</td>
				      </tr>
				    </tbody>`);
			}
		
		}, 'json');
	}
	
</script>


<section class="mt-8 mx-auto text-xl">
	<div class="container mx-auto px-3">
	  <div class="mb-5">
		<label for="">대상 학년 : </label>
		<select name="grade" class="select select-success w-full max-w-xs text-lg" onchange="getGroupsByGrade(this);">
			<option class="text-lg" value="all">전체</option>
			<option class="text-lg" value="elementary">초등</option>
			<option class="text-lg" value="middle">중등</option>
			<option class="text-lg" value="high">고등</option>
		</select>
	</div>
	  
	  <div class="table-box-type-1">
	  <table class="table w-full" id="grouplisttable">
	    <thead>
	      <tr>
	        <th>대상 학년</th>
	      	<th>반 이름</th>
	        <th>수강 요일</th>
	        <th>담당 선생님</th>
	        <th>교재</th>
	        <th>학생 수</th>
	      </tr>
	    </thead>
	    
		<c:forEach var="group" items="${groups }">
	    <tbody>
	      <tr>
	        <td>${group.grade }</td>
	      	<td><div class="font-bold"><a href="getMemberByClassId?classId=${group.id }">${group.groupName }</a></div></td>
	        <td>${group.groupDay }</td>
	        <td>${group.teacherName } 선생님</td>
	        <td>${group.textbook }</td>
	        <td>${group.studentNum }명</td>
	      </tr>
	    </tbody>
		</c:forEach>
	  </table>
	  </div>
	  
	  <c:if test="${rq.getLoginedMember().getAuthLevel() == 1 }">
		  <div class="flex justify-end mt-2">
		  	<a href="makeGroup" class="btn btn-success">반 생성</a>
		  </div>
	  </c:if>
	  
	</div>
</section>
	
	
	
	
<%@ include file="../common/foot.jsp" %>