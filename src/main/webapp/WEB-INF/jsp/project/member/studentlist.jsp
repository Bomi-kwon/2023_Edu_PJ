<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="회원 리스트" />

<%@ include file="../common/head.jsp" %>


<section class="mt-8 mx-auto text-xl">
	<div class="container mx-auto px-3">
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
	        <td>${student.email }</td>
	      </tr>
	    </tbody>
		</c:forEach>
	  </table>
	  </div>
	  
	  <divb class="flex justify-end">
	  	<a class="btn " href="excelDownload?classId=${classId }">출석부 파일 다운로드</a>
	  </div>
	  
	</div>
</section>
	
	
	
	
<%@ include file="../common/foot.jsp" %>