<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="게시물 수정" />

<%@ include file="../common/head.jsp" %>

<section class="mt-8 mx-auto text-xl">
	<div class="container mx-auto px-3">
		<form action="doModify">
			<input type="hidden" name="id" value="${article.id }"/>
			<input type="hidden" name="memberId" value="${article.memberId }"/>
			<input type="hidden" name="boardId" value="${article.boardId }"/>
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
						<td colspan="5"><input class="input input-bordered input-success w-full" type="text" name="title" value="${article.title }" placeholder="제목을 입력해주세요."/></td>
					</tr>
					<tr>
						<th>우리반</th>
						<td colspan="5">수상월수금반</td>
					</tr>
					<tr>
						<th>작성자</th>
						<td>${article.memberId }</td>
						<th>작성일</th>
						<td>${article.updateDate.substring(0,10) }</td>
						<th>제출일</th>
						<td>${article.deadLine.substring(0,10) }</td>
					</tr>
					<tr>
						<td colspan="6">
							<div><textarea class="textarea textarea-success w-full" name="body" placeholder="내용을 입력해주세요.">${article.getForPrintBody() }</textarea></div>
						</td>
					</tr>
					<tr>
						<th>이미지</th>
						<td colspan="5">
						<c:if test="${file != null}">
							<div>현재 이미지</div>
							<div><img src="/project/home/file/${file.id }"/></div>
							<input type="hidden" name="fileId" value="${file.id }"/>
						</c:if>
							<div>
								<span>수정할 이미지 : </span>
								<div class=""><input type="file" name="file"/></div>
							</div>
						</td>
					</tr>
					
					<c:if test="${article.boardId == 4}">
					<tr>
						<th>동영상</th>
						<td colspan="5">
						<c:if test="${article.youTubeLink != null}">
							<div>현재 동영상</div>
							<div>
								<iframe width="1120" height="630" src="https://www.youtube.com/embed/${article.youTubeLink.substring(17) }?mute=1" title="YouTube video player" frameborder="0" allow="accelerometer; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" allowfullscreen></iframe>
							</div>
						</c:if>
							<div>
								<span>수정할 동영상 링크: </span>
								<div class=""><input class="input input-bordered input-success w-full" type="text" name="youTubeLink"/></div>
							</div>
						</td>
					</tr>
					</c:if>
					
					<tr>
						<td colspan="6">
							<div>2명 확인</div>
						</td>
					</tr>
				</table>
			</div>
			<div class="flex justify-end">
				<a class="btn btn-success mr-2" onclick="history.back();">뒤로</a>
				<button class="btn btn-success mr-2" >수정</button>
				<a href="doDelete?id=${article.id }&boardId=${article.boardId}&memberId=${article.memberId}" class="btn btn-success mr-2" 
				onclick="if(confirm('정말 삭제하시겠습니까?')==false) return false;">삭제</a>
			</div>
		</form>
	</div>
</section>
	
	
	
	
<%@ include file="../common/foot.jsp" %>