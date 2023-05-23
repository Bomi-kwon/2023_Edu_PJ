<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="반 리스트" />

<%@ include file="../common/head.jsp" %>

<section class="mt-8 mx-auto text-xl">
	<div class="container mx-auto px-3">
	  <table class="table w-full">
	    <thead>
	      <tr>
	        <th>대상 학년</th>
	      	<th>반 이름</th>
	        <th>수강 요일</th>
	        <th>학생 수</th>
	      </tr>
	    </thead>
	    
		<c:forEach var="group" items="${groups }">
	    <tbody>
	      <tr>
	        <td>${group.grade }</td>
	      	<td>${group.groupName }</td>
	        <td>${group.groupDay }</td>
	        <td>8명</td>
	      </tr>
	    </tbody>
		</c:forEach>
	  </table>
	</div>
</section>
	
	
	
	
<%@ include file="../common/foot.jsp" %>