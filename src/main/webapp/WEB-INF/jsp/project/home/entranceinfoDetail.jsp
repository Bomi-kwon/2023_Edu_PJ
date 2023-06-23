<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="../common/head.jsp" %>


	<section class="">
		<div class="container mx-auto px-3 h-full ">
			
			<div>안녕하세용</div>
			
			
			<div class="mb-3">수만휘 BEST 수기 글 상세보기</div>
			<br />
			<br />
			
		    
			<c:forEach var="body" items="${bodyList }">
				<div>${body }</div>
			</c:forEach>
			
			
			<c:if test="${bodyList.isEmpty() }">
				<div>bodyList가 비어있습니다.</div>
			</c:if>
			
			
			
		</div>
	</section>

<%@ include file="../common/foot.jsp" %>