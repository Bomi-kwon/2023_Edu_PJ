<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="게시물 작성" />

<%@ include file="../common/head.jsp" %>

<script>
	function setSelectBox(grade) {
		$('#group').html(`<option value="">전체</option>`);
		
		let gradeVal = grade.value;
		
		$.get('setSelectBox', {
			grade : gradeVal
		}, function(data) {
			console.log(data);
			for(var i = 0; i < data.data1.length; i++) {
				let groupName = data.data1[i].groupName;
				let classId = data.data1[i].id;
				console.log(groupName);
				$('#group').append(`<option value="`+classId+`">`+groupName+`</option>`);
			}
		}, 'json');
	}
</script>




<section class="mt-8 mx-auto text-xl">
	<div class="container mx-auto px-3">
			<form action="doWrite" method="POST" enctype="multipart/form-data" >
				<input type="hidden" name="boardId" value="${boardId }"/>
				<div class="table-box-type-1 overflow-x-auto">
				<div>게시물 등록</div>
						<table border="1" class="mx-auto able w-full bg-gray-100">
						<colgroup>
							<col width="100"/>
							<col width="700"/>
						</colgroup>
						<tr>
							<th>제목</th>
							<td><input class="input input-bordered input-success w-full" type="text" name="title" placeholder="제목을 입력해주세요."/></td>
						</tr>
						<tr>
							<th>반 이름</th>
							<td>
								<div class="flex">
								<div>
									<select name="grade" class="select select-success w-full max-w-xs" onchange="setSelectBox(this)">
										<option value="">학년</option>
										<option value="elementary">초등</option>
										<option value="middle">중등</option>
										<option value="high">고등</option>
									</select>
								</div>
								<div>
									<select name="classId" id="group" class="select select-success w-full max-w-xs">
										<option value="">전체</option>
									</select>
								</div>
								</div>
							</td>
						</tr>
						<c:if test="${boardId == 2 }">
							<tr>
								<th>마감기한</th>
								<td><input class="input input-bordered input-success w-60" type="date" name="deadLine" min="2023-01-01" max="2024-12-31"/></td>
							</tr>
						</c:if>
						<tr>
							<th>내용</th>
							<td>
								<textarea class="textarea textarea-success w-full" name="body" placeholder="내용을 입력해주세요."></textarea>
							</td>
						</tr>
						<tr>
							<c:if test="${boardId != 4}">
								<th>이미지 등록</th>
								<td><input type="file" name="file"/></td>
							</c:if>
							<c:if test="${boardId == 4}">
								<th>첨부파일</th>
								<td><input type="file" name="file"/></td>
							</c:if>
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