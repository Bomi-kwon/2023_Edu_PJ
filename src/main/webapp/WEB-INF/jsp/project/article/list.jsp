<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="${board.name } 게시판" />

<%@ include file="../common/head.jsp" %>

<section class="mt-8 mx-auto text-xl">
	<div class="container mx-auto px-3">
	  <table class="table w-full">
	    <thead>
	      <tr>
	        <th>
	          <label>
	            <input type="checkbox" class="checkbox" />
	          </label>
	        </th>
	        <th>과제제목</th>
	        <th>수신대상자</th>
	        <th>등록일</th>
	        <th>제출기한</th>
	        <th>작성자</th>
	        <th>과제검사</th>
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
	        <td>${article.regDate }</td>
	        <td>${article.deadLine }</td>
	        <td>${article.memberId }</td>
	        <td>과제검사</td>
	      </tr>
	    </tbody>
		</c:forEach>
	  </table>
	<div class="flex justify-end">
		<a href="homeworkwrite" class="btn btn-success" >과제 등록</a>
	</div>
	</div>
</section>
	
	
	
	
<%@ include file="../common/foot.jsp" %>