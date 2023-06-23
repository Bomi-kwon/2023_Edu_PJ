<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="../common/head.jsp" %>


	<section class="">
		<div class="container mx-auto px-3 h-full ">
			<span class="mb-3">수만휘 BEST 수기 글 모음</span>
			<br />
			<br />
			
			
			
		<div class="table-box-type-1">
		  <table class="table w-full">
		    <thead>
		      <tr>
		        <th>날짜</th>
		        <th>제목</th>
	        	<th>바로가기</th>
		      </tr>
		    </thead>
		    
		    <tbody>
			<c:forEach var="infoArticle" items="${infoArticleList }">
		      <tr class="hover">
		        <td>${infoArticle.date.substring(0,10) }</td>
	        	<td>${infoArticle.title }</td>
		        <td>
		        	<div class="badge badge-accent badge-outline">
		        		<a href="${infoArticle.url }" target="_blank">
		        			해당 글로 이동
		        		</a>
		        	</div>
		        </td>
		      </tr>
			</c:forEach>
		    </tbody> 
		  </table>
	  </div>
			
			
			<c:if test="${infoArticleList.isEmpty() }">
				<div>inforArticleList가 비어있습니다.</div>
			</c:if>
			
		</div>
	</section>

<%@ include file="../common/foot.jsp" %>