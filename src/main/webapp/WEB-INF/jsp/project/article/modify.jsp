<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="게시물 수정" />

<%@ include file="../common/head.jsp" %>

<script>
	function setSelectBox(grade) {
		$('#group').html(`<option class="text-lg" value="">전체</option>`);
		
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

<!-- 숙제, 알림장, 공부인증 게시판 글 수정 -->
<c:if test="${article.boardId != 3}">

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
</c:if>



<!-- 성적 게시판 글 수정 -->
<c:if test="${article.boardId == 3}">
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
										<option value="elementary" ${group.grade == 'elementary' ? 'selected' : ''}>초등</option>
										<option value="middle" ${group.grade == 'middle' ? 'selected' : ''}>중등</option>
										<option value="high" ${group.grade == 'high' ? 'selected' : ''}>고등</option>
									</select>
								</div>
								<div>
									<select name="classId" id="group" class="select select-success w-full max-w-xs">
										<option value="">전체</option>
										<c:forEach var="groupbygrade" items="${groupList }">
											<option value="${groupbygrade.id }" ${group.id == groupbygrade.id ? 'selected' : ''}>
											${groupbygrade.groupName }</option>
										</c:forEach>
									</select>
								</div>
								</div>
							</td>
						</tr>
						<tr>
							<th>시험일자</th>
							<td>
								<input class="input input-bordered input-success w-60" type="date" name="regDate" 
								min="2023-01-01" max="2024-12-31" value="${article.regDate.substring(0,10) }"/>
							</td>
						</tr>
						<tr>
							<th>교사의견</th>
							<td>
								<textarea class="textarea textarea-success w-full" name="body" placeholder="내용을 입력해주세요." required>${article.body }</textarea>
							</td>
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
</c:if>
	
	
	
	
<%@ include file="../common/foot.jsp" %>