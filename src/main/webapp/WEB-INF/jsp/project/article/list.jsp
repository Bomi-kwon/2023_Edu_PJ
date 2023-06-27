<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="${board.name } 게시판" />

<%@ include file="../common/head.jsp" %>

<style>
	.row::after {
	  content:"";
	  display: block;
	  clear:both;
	}
	
	.row > li:hover > .article-name {
	text-decoration: underline;
	}
	
	.cropping {
	max-width: 340px;
	max-height: 340px;
	overflow: hidden;
	}
	
	.cropping img {
	max-width: 400px;
	max-height: 400px;
	min-width: 340px;
	min-height: 340px;
	}
</style>


<!-- 숙제, 알림장, 수학강의 등 게시판 리스트 -->
<c:if test="${board.id != 5 && board.id != 3}">
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
			        <!-- 내가 쓴 글이면 그냥 체크박스 -->
			        <th><input type="checkbox" class="checkbox checkbox-article-id" value="${article.id }"/></th>
	        	</c:when>
	        	<c:otherwise>
			        <!-- 내가 쓴 글 아니면 선택 불가능한 체크박스 -->
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
			<a href="write?boardId=${board.id }" class="btn btn-success">글 쓰기</a>
			
			<form action="doDeleteArticles?boardId=${board.id }" method="POST" name="do-delete-articles-form">
				<input type="hidden" name="ids" value="" />
			</form>
		</div>
	  </c:if>
	</div>
</section>
</c:if>

<!-- 성적 게시판 리스트 -->
<c:if test="${board.id == 3 }">
<section class="mt-8 mx-auto text-xl">
	<div class="container mx-auto px-3">
	  <div class="table-box-type-1">
	  <table class="table w-full">
	    <thead>
	      <tr>
	        <th><input type="checkbox" class="checkbox checkbox-all-article-id" /></th>
	        <th>시험명</th>
	        <th>시험대상반</th>
	        <th>시험일자</th>
	        <th>작성자</th>
	        <th>응시현황</th>
	        <th>평가방식</th>
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
	              <div class="font-bold"><a href="scoredetail?relId=${article.id }">${article.title }</a></div>
	            </div>
	          </div>
	        </td>
	        <td>${article.groupName }</td>
	        <td>${article.regDate }</td>
	        <td>${article.writerName }</td>
	        <td>2/4</td>
	        <td>만점기준:100점</td>
	      </tr>
		</c:forEach>
	    </tbody>
	  </table>
	  </div>
	  <c:if test="${rq.getLoginedMember().getAuthLevel() == 1 }">
	<div class="flex justify-end">
		<button class="btn btn-success mr-2 btn-delete-selected-articles">성적 삭제</button>
		<a href="write?boardId=3" class="btn btn-success" >성적 입력</a>
	
		<form action="doDeleteArticles?boardId=${board.id }" method="POST" name="do-delete-articles-form">
			<input type="hidden" name="ids" value="" />
		</form>
	
	</div>
	</c:if>
	</div>
</section>

</c:if>



<!-- 공부인증 게시판 리스트 -->
<!-- 다른 게시판들과 다르게 사진 위주로 나열해야함 -->
<!-- 다른 게시판들과 다르게 하루에 게시물 하나만 쓸 수 있음 -->
<c:if test="${board.id == 5 }">
	<section class="mt-8 mx-auto text-xl">
	<div class="container mx-auto px-3">
	    <ul class="row">
			<c:forEach var="article" items="${articles }">
				<li class="cell box-border float-left w-1/4 relative">
					<div class="cropping mb-3 w-full"><a href="detail?id=${article.id }">
						<img class="block" src="/project/home/file/${article.fileId }"/></a></div>
					<div class="article-name mb-1" ><a href="detail?id=${article.id }">${article.title }</a></div>
					<div class="font-light text-gray-500 text-sm">${article.writerName }</div>
					<div class="text-gray-400 text-xs mb-5">
						<span>${article.regDate.substring(0,10) }</span>
						<span>(조회 : ${article.hit })</span>
					</div>
				</li>
			</c:forEach>
	    </ul>
		
		<c:if test="${rq.getLoginedMember().getAuthLevel() == 2 }">
			<div class="flex justify-end">
				<a href="write?boardId=${board.id }" class="btn btn-success" 
				onclick="articleNumLimit(${board.id},${rq.getLoginedMemberId() }); return false;">글 쓰기</a>
			</div>
		</c:if>
	</div>
</section>
</c:if>


<script src="/js/article/list.js"></script>
	
<%@ include file="../common/foot.jsp" %>