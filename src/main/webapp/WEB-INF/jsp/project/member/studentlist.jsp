<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="회원 리스트" />

<%@ include file="../common/head.jsp" %>


<section class="mt-8 mx-auto text-xl">
	<div class="container mx-auto px-3">
	<c:if test="${students.isEmpty() == false }">
		<div class="table-box-type-1">
	  <table class="table w-full" id="memberlisttable">
	    <thead>
	      <tr>
	        <th>가입날짜</th>
	        <th>로그인 아이디</th>
	        <th>학생 이름</th>
	        <th>수강하는 반</th>
	        <th>휴대전화</th>
	        <th>부모님 휴대전화</th>
	      </tr>
	    </thead>
	    
		<c:forEach var="student" items="${students }">
	    <tbody>
	      <tr>
	        <td>${student.regDate }</td>
	        <td>${student.loginID }</td>
	        <td>${student.name }</td>
	        <td>${student.groupName }</td>
	        <td>${student.cellphoneNum }</td>
	        <td>${student.parentPhoneNum }</td>
	      </tr>
	    </tbody>
		</c:forEach>
	  </table>
	  </div>
	  
	  
	  <div class="flex justify-end mt-2">
	  	<a href="doDeleteGroup?classId=${classId }" class="btn btn-success mr-1" 
				onclick="if(confirm('정말 삭제하시겠습니까?')==false) return false;">반 삭제</a>
	  	<a class="btn" href="excelDownload?classId=${classId }">출석부 파일 다운로드</a>
	  </div>
	  </c:if>
	  
	  <c:if test="${students.isEmpty() }">
	  	<div>이 반에는 아직 수강생이 없습니다.</div>
	  	<div class="flex justify-end">
			<a href="doDeleteGroup?classId=${classId }" class="btn btn-success mr-1" 
				onclick="if(confirm('정말 삭제하시겠습니까?')==false) return false;">반 삭제</a>
			<button class="btn btn-success" type="button" onclick="history.back();">뒤로</button>
		</div>
	  </c:if>
	</div>
</section>
	
	
	
	
<%@ include file="../common/foot.jsp" %>