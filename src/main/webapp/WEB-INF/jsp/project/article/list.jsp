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
<c:if test="${board.id != 5 }">
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
			<a href="write?boardId=${board.id }" class="btn btn-success" onclick="articleNumLimit(${board.id},${rq.getLoginedMemberId() }); return false;">글 쓰기</a>
			
			<form action="doDeleteArticles?boardId=${board.id }" method="POST" name="do-delete-articles-form">
				<input type="hidden" name="ids" value="" />
			</form>
			
		</div>
	</c:if>
	</div>
</section>
</c:if>

<!-- 공부인증 게시판 리스트 -->
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
			<button class="btn btn-success mr-2 btn-delete-selected-articles">글 삭제</button>
			<a href="write?boardId=${board.id }" class="btn btn-success" onclick="articleNumLimit(${board.id},${rq.getLoginedMemberId() }); return false;">글 쓰기</a>
			<form action="doDeleteArticles?boardId=${board.id }" method="POST" name="do-delete-articles-form">
				<input type="hidden" name="ids" value="" />
			</form>
			
		</div>
		</c:if>
	</div>
</section>
</c:if>

<!-- 체크박스 스크립트는 html 아래에다 놔야 적용되넹?? -->
<script>
	<!-- 전체 체크박스 눌렀을때 -->
	<!-- checked 속성은 선택 여부에 따라 true/false로 구분됨 -->
	$('.checkbox-all-article-id').change(function() {
		const allCheck = $(this);
		const allChecked = allCheck.prop('checked');
		$('.checkbox-article-id').prop('checked', allChecked);
	<!-- 나중에 disabled 박스 생겼을때를 대비해 이 코드는 지우지 않겠음 -->
		$('.checkbox-article-id:is(:disabled)').prop('checked', false);
	})
	
	
	<!-- 아래쪽 체크박스를 다 누르면 전부다 선택되게 하기 -->
	$('.checkbox-article-id').change(function() {
		const checkboxArticleIdCount = $('.checkbox-article-id').length;
		const checkboxArticleIdCheckedCount = $('.checkbox-article-id:checked').length;
		const checkboxDisabledCount = $('.checkbox-article-id:is(:disabled)').length;
		const allChecked = (checkboxArticleIdCount - checkboxDisabledCount) == checkboxArticleIdCheckedCount;
		$('.checkbox-all-article-id').prop('checked', allChecked);
	})
	
	
	<!-- 체크한 게시글들 삭제할 수 있도록 하기 -->
	$('.btn-delete-selected-articles').click(function() {
		
		<!-- 몇번 몇번이 체크됐는지 그 값을 배열화 하기 -->
		const values = $('.checkbox-article-id:checked').map((index, el) => el.value).toArray();
		
		if (values.length == 0) {
			alert('선택된 게시글이 없습니다');
			return;
		}
		if (confirm('선택한 글을 삭제하시겠습니까?') == false) {						
			return;
		}
		
		<!-- 그 번호들을 콤마로 구분해서 한줄로 만들기 -->
		$('input[name=ids]').val(values.join(','));
		
		<!-- 폼 실행하기 -->
		$('form[name=do-delete-articles-form]').submit();
	})
	
	
	function articleNumLimit(boardId,loginedMemberId) {
		
		
		const now = new Date();
		let year = now.getFullYear();
	    let month = now.getMonth() + 1;
	    let date = now.getDate();
	    
	    if (month < 10) {
	    	  month = '0' + month;
	    }
        if (date < 10) {
    	    date = '0' + date;
        }
	    
	    let today = year + "-" + month + "-" + date;

	    $.get('getArticleNumLimit', {
			today : today,
			boardId : boardId,
			loginedMemberId : loginedMemberId
		}, function(data) {
			
			console.log(data);
			
			if(data.success) {
				location.replace('write?boardId='+boardId);
			} else {
				alert(data.msg);
			}
		}, 'json');
		
	}
</script>
	
<%@ include file="../common/foot.jsp" %>