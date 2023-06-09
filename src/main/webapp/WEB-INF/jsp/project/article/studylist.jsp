<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="${board.name } 게시판" />

<%@ include file="../common/head.jsp" %>


<section class="mt-8 mx-auto text-xl">
	<div class="container mx-auto px-3">
	  <div class="table-box-type-1">
	  <table class="table w-full">
	    <thead>
	      <tr>
	        <th><input type="checkbox" class="checkbox checkbox-all-article-id" /></th>
	        <th>제목</th>
	        <th>수신대상자</th>
        	<th>등록일</th>
	        <th>작성자</th>
	        <c:if test="${board.id == 2 }">
		        <th>제출기한</th>
		        <th>과제검사여부</th>
	        </c:if>
	      </tr>
	    </thead>
	    
	    <tbody>
		<c:forEach var="article" items="${articles }">
	      <tr class="hover">
	        <c:choose>
	        	<c:when test="${rq.getLoginedMemberId() == article.memberId }">
			        <th><input type="checkbox" class="checkbox checkbox-article-id" value="${article.id }"/></th>
	        	</c:when>
	        	<c:otherwise>
			        <th><input type="checkbox" class="checkbox checkbox-article-id" value="${article.id }" disabled/></th>
	        	</c:otherwise>
	        </c:choose>
	        
	        <td>
	          <div class="flex items-center space-x-3">
	            <div>
	              <div class="font-bold"><a href="detail?id=${article.id }">${article.title }</a></div>
	            </div>
	          </div>
	        </td>
	        <td>${article.groupName }</td>
        	<td>${article.regDate.substring(0,10) }</td>
	        <td>${article.writerName }</td>
	        <c:if test="${board.id == 2 }">
		        <td>${article.deadLine.substring(0,10) }</td>
		        <td>${article.hwChk }</td>
	        </c:if>
	      </tr>
		</c:forEach>
	    </tbody> 
	  </table>
	  </div>
	  <c:if test="${rq.getLoginedMember().getAuthLevel() == 1 }">
		<div class="flex justify-end">
			<button class="btn btn-success mr-2 btn-delete-selected-articles">글 삭제</button>
			<a href="write?boardId=${board.id }" class="btn btn-success" >글 쓰기</a>
			
			<form action="doDeleteArticles?boardId=${board.id }" method="POST" name="do-delete-articles-form">
				<input type="hidden" name="ids" value="" />
			</form>
			
		</div>
	</c:if>
	</div>
</section>

	
<%@ include file="../common/foot.jsp" %>