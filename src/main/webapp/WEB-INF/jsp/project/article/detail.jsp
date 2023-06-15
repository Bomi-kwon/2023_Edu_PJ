<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="상세보기" />

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
					<td colspan="5">${group.groupName }</td>
				</tr>
				<tr>
					<th>작성자</th>
					<td>${group.teacherName }</td>
					<th>작성일</th>
					<td>${article.updateDate }</td>
					<th>제출일</th>
					<td>${article.deadLine }</td>
				</tr>
				<tr>
					<th>내용</th>
					<td colspan="6">
						<div>${article.getForPrintBody() }</div>
					</td>
				</tr>
				<tr>
					<c:if test="${article.boardId != 4 && file != null}">
						<th>이미지 등록</th>
						<td colspan="5"><img src="/project/home/file/${file.id }"/></td>
					</c:if>
					<c:if test="${article.boardId == 4 && article.youTubeLink != null}">
						<th>동영상</th>
						<td colspan="5">
							<div>
								<iframe width="1120" height="630" src="https://www.youtube.com/embed/${article.youTubeLink.substring(17) }?mute=1&autoplay=1" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" allowfullscreen></iframe>
							</div>
						</td>
					</c:if>
				</tr>
				<c:if test="${visitors != null }">
				<tr>
					<th>조회</th>
					<td colspan="5">
						<div class="avatar-group -space-x-6">
							<c:forEach var="visitor" items="${visitors }">
								<section class="flex flex-col justify-center">
									<div class="avatar bg-white">
									    <div class="w-20 rounded-full ring ring-primary ring-offset-base-100 ring-offset-2">
									      <img src="/project/home/file/${visitor.fileId }" />
									    </div>
									</div>
									<div class="text-xs text-center">${visitor.name }</div>
								</section>
							</c:forEach>
						</div>
					</td>
				</tr>
				</c:if>
			</table>
		</div>
		<div class="flex justify-end mt-2">
			<c:if test="${article.boardId == 2 && rq.getLoginedMember().getAuthLevel() == 1}">
				<c:choose>
					<c:when test="${homeworks.isEmpty() }">
						<div>
					        <a class="homeworkChk btn btn-success mr-2" 
					        onclick="getStudentsByClass(${article.id},${article.classId});">과제검사</a>
					    </div>
					</c:when>
					<c:otherwise>
						<div>
				        	<a class="getHws btn btn-success btn-outline mr-2">과제확인</a>
				    	</div>
					</c:otherwise>
				</c:choose>
			</c:if>
			
			<a href="list?boardId=${article.boardId }" class="btn btn-success mr-2" >목록</a>
			<c:if test="${rq.getLoginedMemberId() == article.memberId }">
				<a href="modify?id=${article.id }&memberId=${article.memberId}" class="btn btn-success mr-2" >수정</a>
				<a href="doDelete?id=${article.id }&boardId=${article.boardId}&memberId=${article.memberId}" class="btn btn-success" 
				onclick="if(confirm('정말 삭제하시겠습니까?')==false) return false;">삭제</a>
			</c:if>
		</div>
	</div>
</section>
	

<!-- 댓글란 -->
<section class="mt-8 mx-auto text-xl">
	<div class="container mx-auto px-3">
		<h2>댓글</h2>
			<c:forEach var="reply" items="${replies }" varStatus="status">
				<div class="flex border-top-line bg-white bg-opacity-80 mb-1 px-8 container">
					<div class="w-16 object-fill flex items-center mr-4">
				        <img src="/project/home/file/${reply.fileId }" alt="" />
					</div>
					
					<!-- 그냥 댓글 보여줄때 -->
					<div id="${status.count }" class="py-2 text-base pl-2 pt-5 flex-grow">
						<div class="flex justify-between items-end">
							<div class="font-semibold flex items-center"><span>${reply.writerName }</span></div>
							
							<!-- 댓글 수정/삭제 버튼 -->
							<c:if test="${rq.getLoginedMemberId() == reply.replymemberId }">
								<div class="dropdown">
								    <button class="btn btn-square btn-xs btn-ghost">
								      <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" class="inline-block w-5 h-5 stroke-current"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 12h.01M12 12h.01M19 12h.01M6 12a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0z"></path></svg>
								    </button>
								    <ul tabindex="0" class="menu menu-compact dropdown-content mt-3 p-2 shadow bg-base-100 rounded-box w-20">
								        <li><a onclick="replyModify_getForm(${reply.id },${status.count });">수정</a></li>
								        <li><a href="../reply/doDeleteReply?id=${reply.id }&replymemberId=${reply.replymemberId}" onclick="if(confirm('정말 삭제하시겠습니까?')==false) return false;">삭제</a></li>
								    </ul>
								</div>
							</c:if>
						</div>
						<div class="detailbody my-1 text-sm break-all whitespace-normal"><span>${reply.getForPrintReplybody() }</span></div>
						<div class="text-xs text-gray-400 mb-2"><span>${reply.updateDate }</span></div>
					</div>
				</div>
			</c:forEach>
			
			
			<!-- 댓글 작성 폼 -->
			<c:if test="${rq.getLoginedMemberId() != 0 }">
				<form action="../reply/doWriteReply" onsubmit="replyWrite_submitForm(this); return false;">
					<input type="hidden" name="relTypeCode" value="article"/>
					<input type="hidden" name="relId" value="${article.id }"/>
					<div class="my-4 border border-gray-400 rounded-lg text-base p-4 bg-white bg-opacity-80">
						<div class="mb-2"><span>${rq.getLoginedMember().getName() }</span></div>
						<textarea class="textarea textarea-bordered w-full" name="replybody" placeholder="댓글을 입력해주세요."></textarea>
						<div class="flex justify-end"><button class="btn btn-outline btn-sm">댓글 작성</button></div>
					</div>
				</form>
			</c:if>
			
			<!-- 로그인 안했을때의 댓글 작성 창 -->
			<c:if test="${rq.getLoginedMemberId() == 0 }">
				<div class="mt-4 border border-gray-400 rounded-lg text-lg p-4 bg-white bg-opacity-80">
					<textarea class="textarea textarea-bordered w-full" name="replybody" placeholder="로그인 후 댓글을 입력해주세요."></textarea>
					<div class="flex justify-end"><a class="btn btn-outline btn-sm" href="../member/login">로그인 하러가기</a></div>
				</div>
			</c:if>
	
	</div>
	</section>
	
	
	
<script>
function getStudentsByClass(relId,classId) {
	
	$.get('getStudentsByClass', {
		classId : classId
	}, function(data) {
		
		console.log(data);
		if(data.data1 != null) {
			
			$('#hwChktable').html(`<thead>
				      <tr>
			        <th>이름</th>
			        <th>숙제 완성도</th>
			        <th>교사의견</th>
			      </tr>
			    </thead>`);
			
			for(var i = 0; i < data.data1.length; i++) {
				let name = data.data1[i].name;
				let memberId = data.data1[i].id;
				
				$('#hwChktable').append(`<tbody>
						<input type="hidden" name="homeworklist[`+i+`].memberId" value="`+memberId+`"/>
						<input type="hidden" name="homeworklist[`+i+`].classId" value="`+classId+`"/>
						<input type="hidden" name="homeworklist[`+i+`].relId" value="`+relId+`"/>
					      <tr>
				        <td>`+name+`</td>
				        <td><input class="input input-bordered input-success w-20" type="text"
					        name="homeworklist[`+i+`].hwPerfection" required/> %</td>
			        	<td>
				        	<select name="homeworklist[`+i+`].hwMsg" class="select select-success w-full max-w-xs">
								<option value="숙제가 완벽해요">숙제가 완벽해요</option>
								<option value="숙제를 전혀 안 했어요">숙제를 전혀 안 했어요</option>
								<option value="숙제를 안 가져왔어요">숙제를 안 가져왔어요</option>
								<option value="숙제를 베꼈어요">숙제를 베꼈어요</option>
								<option value="숙제를 찍었어요">숙제를 찍었어요</option>
							</select>
			        	</td>
				      </tr>
				    </tbody> `);
			}
		}
	}, 'json');
	
}

function hwmodify() {
	$('.getHwsmodal-bg, .getHwsmodal').hide();
	$('.hwChkmodal-bg, .hwChkmodal-form, .hwChkmodal').show();
	
	$('.hwChkmodal-form').attr('action','doHwModify');
	
	$('#hwChktable').html(`<thead>
		      <tr>
	        <th>이름</th>
	        <th>숙제 완성도</th>
	        <th>교사의견</th>
	      </tr>
	    </thead>`);
	
	$('#hwChktable').append(`<tbody>
			<c:forEach var="homework" items="${homeworks }" varStatus="status">
			  <tr>
			  <input type="hidden" name="homeworklist[${status.index}].id" value="${homework.id }"/>
			    <td>${homework.name }</td>
		        <td><input class="input input-bordered input-success w-20" type="text"
			        name="homeworklist[${status.index}].hwPerfection" value="${homework.hwPerfection }" required/> % </td>
	        	<td>
		        	<select name="homeworklist[${status.index}].hwMsg" class="select select-success w-full max-w-xs">
		        		<option value="숙제가 완벽해요" ${homework.hwMsg == '숙제가 완벽해요' ? 'selected' : ''}>숙제가 완벽해요</option>
						<option value="숙제를 전혀 안 했어요" ${homework.hwMsg == '숙제를 전혀 안 했어요' ? 'selected' : ''}>숙제를 전혀 안 했어요</option>
						<option value="숙제를 안 가져왔어요" ${homework.hwMsg == '숙제를 안 가져왔어요' ? 'selected' : ''}>숙제를 안 가져왔어요</option>
						<option value="숙제를 베꼈어요" ${homework.hwMsg == '숙제를 베꼈어요' ? 'selected' : ''}>숙제를 베꼈어요</option>
						<option value="숙제를 찍었어요" ${homework.hwMsg == '숙제를 찍었어요' ? 'selected' : ''}>숙제를 찍었어요</option>
					</select>
			    </td>
		      </tr>
	 	</c:forEach>
	 	</tbody>`);
	
	$('.hwChkmodal-btn').html('수정하기');
}


<!-- 댓글 미입력 방지 -->
function replyWrite_submitForm(form) {
	
	if (form.replybody.value.trim().length < 2) {
		alert('댓글을 2글자 이상 입력하세요');
		form.replybody.focus();
		return;
	}
	
	form.submit();
	
}

originalForm = null;
originalId = null;

function replyModify_getForm(replyId,i) {
	
	if(originalForm != null) {
		replyModify_cancle(originalId);
	}
	
	$.get('../reply/getReplyContent', {
		id : replyId
	}, function(data) {
		
		
		let replyContent = $('#' + i);
		
		originalId = i;
		originalForm = replyContent.html();
		
		
		let addHtml = `
			<form action="../reply/doModifyReply" onsubmit="replyWrite_submitForm(this); return false;">
				<input type="hidden" name="id" value="\${data.data1.id}"/>
					<input type="hidden" name="replymemberId" value="\${data.data1.replymemberId }"/>
				<div class="py-1 text-base pl-2 pt-5 flex-grow">
					<div class="mb-2"><span>\${data.data1.writerName}</span></div>
					<textarea class="textarea textarea-bordered w-full" name="replybody" placeholder="댓글을 입력해주세요.">\${data.data1.replybody}</textarea>
					<div class="flex justify-end">
						<a onclick="replyModify_cancle(\${i});" class="btn btn-outline btn-sm">취소</a>
						<button class="btn btn-outline btn-sm">수정</button>
					</div>
				</div>
		</form>
		`;
	
		replyContent.empty().html("");
		replyContent.append(addHtml);
	}, 'json');
	
}

function replyModify_cancle(i) {
	let replyContent = $('#' + i);
	replyContent.html(originalForm);
	
	originalForm = null;
	originalId = null;
}
	
</script>
	
	
 <!-- 과제검사 모달창 -->
<div class="hwChkmodal-bg"></div>
<form action="doHwCheck" class="hwChkmodal-form">
	<div class="hwChkmodal">
		<h1>과제검사</h1>
		<a class="close-btn"><i class="fa-regular fa-circle-xmark"></i></a>
		
		<table class="table w-full" id="hwChktable">
  	 	 </table>
  	 	 <div class="flex justify-end "><button class="hwChkmodal-btn btn btn-success">검사완료</button></div>
		
	</div>
</form>

 <!-- 과제확인 모달창 -->
<div class="getHwsmodal-bg"></div>
<div class="getHwsmodal">
	<h1>과제확인</h1>
	<a class="getHws-close-btn"><i class="fa-regular fa-circle-xmark"></i></a>
	
	<table class="table w-full" id="getHwstable">
		<thead>
	      <tr>
	        <th>이름</th>
	        <th>숙제 완성도</th>
	        <th>교사의견</th>
	      </tr>
	    </thead>
 	 		<tbody>
		 	 	<c:forEach var="homework" items="${homeworks }" varStatus="status">
					  <tr>
				        <td>${homework.name }</td>
				        <td>${homework.hwPerfection } %</td>
			        	<td>${homework.hwMsg }</td>
				      </tr>
		 	 	</c:forEach>
		   </tbody>
 	 </table>
 	 <c:if test="${rq.getLoginedMemberId() == article.memberId }">
	 	 <div class="flex justify-end ">
	 	 	<a class="btn btn-success mr-2" onclick="hwmodify();">수정</a>
	 	 	<a href="doHwDelete?relId=${article.id }&memberId=${article.memberId}" 
	 	 	class="btn btn-success" onclick="if(confirm('정말 삭제하시겠습니까?')==false) return false;">삭제</a>
	 	 </div>
	</c:if>
</div>
	
	
	
<%@ include file="../common/foot.jsp" %>