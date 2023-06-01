<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="게시판" />

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
					<th>내용</th>
					<td colspan="6">
						<div>${article.getForPrintBody() }</div>
					</td>
				</tr>
				<tr>
					<c:if test="${article.boardId != 4}">
						<th>이미지 등록</th>
						<td colspan="5"><img src="/project/home/file/${file.id }"/></td>
					</c:if>
					<c:if test="${article.boardId == 4}">
						<th>동영상</th>
						<td colspan="5">
							<div>
								<iframe width="1120" height="630" src="https://www.youtube.com/embed/${article.youTubeLink.substring(17) }?mute=1&autoplay=1" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" allowfullscreen></iframe>
							</div>
						</td>
					</c:if>
				</tr>
				<tr>
					<td colspan="6">
						<div>2명 확인</div>
					</td>
				</tr>
			</table>
		</div>
		<div class="flex justify-end mt-2">
			<c:if test="${homeworks.isEmpty() }">
				<div>
			        <a class="homeworkChk btn btn-success mr-2" 
			        onclick="getStudentsByClass(${article.id},${article.classId});">과제검사</a>
			    </div>
		    </c:if>
		    <c:if test="${homeworks.isEmpty() == false}">
				<div>
			        <a class="getHws btn btn-success btn-outline mr-2">과제확인</a>
			    </div>
		    </c:if>
			<a href="list" class="btn btn-success mr-2" >목록</a>
			<c:if test="${rq.getLoginedMemberId() == article.memberId }">
				<a href="modify?id=${article.id }&memberId=${article.memberId}" class="btn btn-success mr-2" >수정</a>
				<a href="doDelete?id=${article.id }&boardId=${article.boardId}&memberId=${article.memberId}" class="btn btn-success" 
				onclick="if(confirm('정말 삭제하시겠습니까?')==false) return false;">삭제</a>
			</c:if>
		</div>
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