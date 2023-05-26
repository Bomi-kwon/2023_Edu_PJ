<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="성적 게시물 수정" />

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
				$('#group').append(`<option value="`+classId+`">`+groupName+`</option>`);
			}
		}, 'json');
	}
	
</script>


<section class="mt-8 mx-auto text-xl">
	<div class="container mx-auto px-3">
			<form action="scoremodify" >
				<input type="hidden" name="memberId" value="${article.memberId }"/>
				<input type="hidden" name="id" value="${article.id }"/>
				<div class="table-box-type-1 overflow-x-auto">
				<div>성적 입력</div>
						<table border="1" class="mx-auto able w-full bg-gray-100">
						<colgroup>
							<col width="100"/>
							<col width="700"/>
						</colgroup>
						<tr>
							<th>시험 명</th>
							<td><input class="input input-bordered input-success w-full" type="text" name="title" 
							placeholder="제목을 입력해주세요." value="${article.title }"/></td>
						</tr>
						<tr>
							<th>대상 반</th>
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
						<tr>
							<th>시험일자</th>
							<td><input class="input input-bordered input-success w-60" type="date" name="regDate" 
							min="2023-01-01" max="2024-12-31" value="${article.regDate }"/></td>
						</tr>
					</table>
				</div>
				<div class="flex justify-end">
					<button class="btn btn-success mr-2">수정</button>
					<button class="btn btn-success" type="button" onclick="history.back();">뒤로</button>
				</div>
			</form>
		</div>
</section>

			


	
	
	
	
<%@ include file="../common/foot.jsp" %>