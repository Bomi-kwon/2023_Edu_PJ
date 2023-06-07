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
		<a href="scorearticlewrite" class="btn btn-success" >성적 입력</a>
	
		<form action="doDeleteScoresArticles" method="POST" name="do-delete-articles-form">
			<input type="hidden" name="ids" value="" />
		</form>
	
	</div>
	</c:if>
	</div>
</section>
	

<script>
	$('.checkbox-all-article-id').change(function() {
		const allCheck = $(this);
		const allChecked = allCheck.prop('checked');
		$('.checkbox-article-id').prop('checked', allChecked);
		$('.checkbox-article-id:is(:disabled)').prop('checked', false);
	})
	
	
	$('.checkbox-article-id').change(function() {
		const checkboxArticleIdCount = $('.checkbox-article-id').length;
		const checkboxArticleIdCheckedCount = $('.checkbox-article-id:checked').length;
		const checkboxDisabledCount = $('.checkbox-article-id:is(:disabled)').length;
		const allChecked = (checkboxArticleIdCount - checkboxDisabledCount) == checkboxArticleIdCheckedCount;
		$('.checkbox-all-article-id').prop('checked', allChecked);
	})
	
	
	$('.btn-delete-selected-articles').click(function() {
		
		const values = $('.checkbox-article-id:checked').map((index, el) => el.value).toArray();
		
		if (values.length == 0) {
			alert('선택된 게시글이 없습니다');
			return;
		}
		if (confirm('선택한 글을 삭제하시겠습니까?') == false) {						
			return;
		}
		
		$('input[name=ids]').val(values.join(','));
		
		$('form[name=do-delete-articles-form]').submit();
	})
</script>
	
<%@ include file="../common/foot.jsp" %>