<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="${board.name } 게시판" />

<%@ include file="../common/head.jsp" %>

<section class="mt-8 mx-auto text-xl">
	<div class="container mx-auto px-3">
		<div class="table-box-type-1">
			<table class="mx-auto table w-full">
				<colgroup>
					<col width="100"/>
					<col width="300"/>
					<col width="100"/>
					<col width="300"/>
					<col width="100"/>
					<col width="300"/>
				</colgroup>
				<tr>
					<th>제목</th>
					<td colspan="5">${article.title }</td>
				</tr>
				<tr>
					<th>우리반</th>
					<td colspan="5">${article.classId }</td>
				</tr>
				<tr>
					<th>작성자</th>
					<td>${article.memberId }</td>
					<th>작성일</th>
					<td>${article.updateDate }</td>
					<th>제출일</th>
					<td>${article.deadLine }</td>
				</tr>
				<tr>
					<td colspan="6">
						<div>${article.getForPrintBody() }</div>
					</td>
				</tr>
				<tr>
					<td colspan="6">
						<div>2명 확인</div>
					</td>
				</tr>
			</table>
		</div>
		<div class="flex justify-end">
			<a href="list" class="btn btn-success mr-2" >목록</a>
			<c:if test="${rq.getLoginedMemberId() == article.memberId }">
				<a href="homeworkmodify?id=${article.id }&memberId=${article.memberId}" class="btn btn-success mr-2" >수정</a>
				<a href="doHomeworkDelete?id=${article.id }&boardId=${article.boardId}&memberId=${article.memberId}" class="btn btn-success mr-2" 
				onclick="if(confirm('정말 삭제하시겠습니까?')==false) return false;">삭제</a>
			</c:if>
		</div>
	</div>
</section>
	
	
	
	
<%@ include file="../common/foot.jsp" %>