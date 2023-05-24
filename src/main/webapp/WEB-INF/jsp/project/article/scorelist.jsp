<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="점수 게시판" />

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
	        <th>시험명</th>
	        <th>시험대상반</th>
	        <th>시험일자</th>
	        <th>작성자</th>
	        <th>응시현황</th>
	        <th>평가방식</th>
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
	            <div>
	              <div class="font-bold"><a href="scoredetail?relId=${article.id }">${article.title }</a></div>
	            </div>
	          </div>
	        </td>
	        <td>${article.classId }</td>
	        <td>${article.regDate }</td>
	        <td>${article.memberId }</td>
	        <td>2/4</td>
	        <td>만점기준:100점</td>
	      </tr>
	    </tbody>
		</c:forEach>
	  </table>
	  </div>
	  <c:if test="${rq.getLoginedMember().getAuthLevel() == 1 }">
	<div class="flex justify-end">
		<a href="scorewrite" class="btn btn-success" >성적 입력</a>
	</div>
	</c:if>
	</div>
</section>
	
	
	
	
<%@ include file="../common/foot.jsp" %>