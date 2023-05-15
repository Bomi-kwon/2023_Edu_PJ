<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="${board.name } 게시판" />

<%@ include file="../common/head.jsp" %>

<section class="mt-8 mx-auto text-xl">
	<div class="container mx-auto px-3">
			<form action="doHomeworkWrite" >
				<div class="table-box-type-1 overflow-x-auto">
				<div>과제 등록</div>
						<table border="1" class="mx-auto able w-full bg-gray-100">
						<colgroup>
							<col width="100"/>
							<col width="700"/>
						</colgroup>
						<tr>
							<th>제목</th>
							<td><input class="input input-bordered input-success w-full" type="text" name="title" placeholder="제목을 입력해주세요." required/></td>
						</tr>
						<tr>
							<th>내용</th>
							<td>
								<textarea class="textarea textarea-success w-full" name="body" placeholder="내용을 입력해주세요."></textarea>
							</td>
						</tr>
					</table>
				</div>
				<div class="flex justify-end">
					<button class="btn btn-success mr-2">작성</button>
					<button class="btn btn-success" type="button" onclick="history.back();">뒤로</button>
				</div>
			</form>
			<div class="btns flex justify-start">
			</div>
		</div>
</section>
	
	
	
	
<%@ include file="../common/foot.jsp" %>