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
	        <th>
	          <label>
	            <input type="checkbox" class="checkbox" />
	          </label>
	        </th>
	        <th>제목</th>
	        <th>수신대상자</th>
        	<th>등록일</th>
	        <th>작성자</th>
	        <c:if test="${board.id == 2 }">
		        <th>제출기한</th>
		        <th>과제검사</th>
	        </c:if>
	      </tr>
	    </thead>
	    
		<c:forEach var="article" items="${articles }">
	    <tbody>
	      <!-- row 1 -->
	      <tr>
	        <th>
	          <label>
	            <input type="checkbox" class="checkbox" />
	          </label>
	        </th>
	        <td>
	          <div class="flex items-center space-x-3">
	            <!-- 
	            <div class="avatar">
	              <div class="mask mask-squircle w-12 h-12">
	                <img src="/tailwind-css-component-profile-2@56w.png" alt="Avatar Tailwind CSS Component" />
	              </div>
	            </div>
	             -->
	            <div>
	              <div class="font-bold"><a href="homeworkdetail?id=${article.id }">${article.title }</a></div>
	            </div>
	          </div>
	        </td>
	        <td>${article.classId }</td>
        	<td>${article.regDate.substring(0,10) }</td>
	        <td>${article.memberId }</td>
	        <c:if test="${board.id == 2 }">
		        <td>${article.deadLine.substring(0,10) }</td>
		        <td>과제검사</td>
	        </c:if>
	      </tr>
	    </tbody>
		</c:forEach>
	  </table>
	  </div>
	  <c:if test="${rq.getLoginedMember().getAuthLevel() == 1 }">
	<div class="flex justify-end">
		<a href="write?boardId=${board.id }" class="btn btn-success" >글 쓰기</a>
	</div>
	</c:if>
	</div>
</section>
	
	
	
	
<%@ include file="../common/foot.jsp" %>