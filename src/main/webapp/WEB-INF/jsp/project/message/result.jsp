<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="../common/head.jsp" %>



<body>

<section class="mt-8 mx-auto text-xl">
	<div class="container mx-auto px-3">
		<h1>전송 결과</h1>
		<table class="table table-bordered">
		    <thead>
		      <tr>
		        <th>requestId</th>
		        <th>요청 시간</th>
		        <th>Status Code</th>
		        <th>Status Name</th>
		      </tr>
		    </thead>
		    <tbody>
		      <tr >
		        <td>${response.requestId}</td>
		        <td>${response.requestTime}</td>
		        <td>${response.statusCode}</td>
		        <td>${response.statusName}</td>
		      </tr>
		    </tbody>
	  </table>
	</div>
</section>


</body>

  
  
