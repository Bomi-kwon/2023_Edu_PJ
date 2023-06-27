<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="성적 수정" />

<%@ include file="../common/head.jsp" %>


<section class="mt-8 mx-auto text-xl">
	<div class="container mx-auto px-3">
			<div class="table-box-type-1 overflow-x-auto">
				<div>성적 입력</div>
						<table border="1" class="mx-auto able w-full bg-gray-100">
						<colgroup>
							<col width="100"/>
							<col width="700"/>
						</colgroup>
						<tr>
							<th>시험 명</th>
							<td>${article.title }</td>
						</tr>
						<tr>
							<th>대상 반</th>
							<td>${article.classId }</td>
						</tr>
						<tr>
							<th>시험일자</th>
							<td>${article.deadLine.substring(0,10) }</td>
						</tr>
						<tr>
							<th>교사의견</th>
							<td>${article.getForPrintBody() }</td>
						</tr>
					</table>
				</div>
				<div class="flex justify-end">
					<button class="btn btn-success" type="button" onclick="history.back();">뒤로</button>
				</div>
				
				<form action="doModifyScoreArticle">
				<input type="hidden" name="articleId" value="${article.id }"/>
				<div class="table-box-type-1 mt-5">
					<table class="table w-full" id="scoretable">
						<thead>
						      <tr>
						        <th>원생명</th>
						        <th>총점</th>
						        <th>응시여부</th>
					     	 </tr>
					    </thead>
					    
						<tbody>
					    <c:forEach var="score" items="${scores }" varStatus="status">
							<input type="hidden" name="scorelist[${status.index }].id" value="${score.id }"/>
							<input type="hidden" name="scorelist[${status.index }].memberId" value="${score.memberId }"/>
						      <tr id="${status.count }">
						        <td>${score.name }</td>
						        <td><input class="input input-bordered input-success w-full" type="text"
						        name="scorelist[${status.index }].score" value="${score.score }" required/></td>
						        <td>
						        	<select name="classId" class="select select-success w-full max-w-xs">
										<option value="">응시</option>
										<option value="">미응시</option>
									</select>
						        </td>
						      </tr>
						</c:forEach>
						</tbody>
				  </table>
				  <button class="btn btn-success mr-2" onsubmit="check(this); return false;">수정하기</button>
	  		</div>
			</form>
			
		</div>
</section>

			


	
	
	
	
<%@ include file="../common/foot.jsp" %>