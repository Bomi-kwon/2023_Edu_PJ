<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="점수 상세보기" />

<%@ include file="../common/head.jsp" %>

<section class="mt-8 mx-auto text-xl">
	<div class="container mx-auto px-3">
		<div class="table-box-type-1 mb-5">
			<table class="mx-auto table w-full">
				<colgroup>
					<col width="100"/>
					<col width="300"/>
					<col width="100"/>
					<col width="300"/>
				</colgroup>
				<tr>
					<th>시험명</th>
					<td colspan="3">${article.title }</td>
				</tr>
				<tr>
					<th>시험대상반</th>
					<td colspan="3">${article.groupName }</td>
				</tr>
				<tr>
					<th>작성자</th>
					<td>${article.writerName }</td>
					<th>시험일자</th>
					<td>${article.regDate.substring(0,10) }</td>
				</tr>
				<tr>
					<th>응시현황</th>
					<td>${scores.size() }명 응시/총 ${studentNum }명</td>
					<th>만점기준</th>
					<td>100점</td>
				</tr>
				<tr>
					<th>통계</th>
					<td colspan="3">
						<div>평균 점수 : ${averageOfScores }</div>
						<div>최고 점수 : ${bestScore.score }</div>
						<div>최저 점수 : ${worstScore.score }</div>
					</td>
				</tr>
				<tr>
					<th>교사의견</th>
					<td colspan="3">${article.getForPrintBody() }</td>
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
		
		<div class="table-box-type-1">
		<table class="table w-full">
	    <thead>
	      <tr>
	        <th>원생명</th>
	        <th>총점</th>
	        <th>석차</th>
	        <th>응시여부</th>
	      </tr>
	    </thead>
	    
		<c:forEach var="score" items="${scores }">
	    <tbody>
	      <!-- row 1 -->
	      <tr>
	        <td>${score.name }</td>
	        <td>${score.score }</td>
	        <td>${scores.size()}명 중 ${score.score_rank}등</td>
	        <td>응시</td>
	      </tr>
	    </tbody>
		</c:forEach>
	  </table>
	  </div>
		
		<div class="flex justify-end">
			<a href="scorelist" class="btn btn-success mr-2" >목록</a>
			<c:if test="${rq.getLoginedMemberId() == article.memberId }">
				<a href="scorearticlemodify?id=${article.id }" class="btn btn-success mr-2" >수정</a>
				<a href="doScoreDelete?id=${article.id }&memberId=${article.memberId}" class="btn btn-success mr-2" 
				onclick="if(confirm('정말 삭제하시겠습니까?')==false) return false;">삭제</a>
			</c:if>
		</div>
	</div>
</section>
	
	
	
	
<%@ include file="../common/foot.jsp" %>