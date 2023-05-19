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
					<td colspan="3">${article.classId }</td>
				</tr>
				<tr>
					<th>작성자</th>
					<td>${article.memberId }</td>
					<th>시험일자</th>
					<td>${article.regDate }</td>
				</tr>
				<tr>
					<th>응시현황</th>
					<td>2명응시/2명미응시</td>
					<th>만점기준</th>
					<td>100점</td>
				</tr>
				<tr>
					<th>평균</th>
					<td colspan="3">평균 : 84, 최고 : 90, 최저 : 72</td>
				</tr>
			</table>
		</div>
		
		<div class="table-box-type-1">
		<table class="table w-full">
	    <thead>
	      <tr>
	        <th>
	          <label>
	            <input type="checkbox" class="checkbox" />
	          </label>
	        </th>
	        <th>원생명</th>
	        <th>총점</th>
	        <th>석차</th>
	        <th>응시여부</th>
	        <th>교사의견</th>
	      </tr>
	    </thead>
	    
		<c:forEach var="score" items="${scores }">
	    <tbody>
	      <!-- row 1 -->
	      <tr>
	        <th>
	          <label>
	            <input type="checkbox" class="checkbox" />
	          </label>
	        </th>
	        <td>${score.memberId }</td>
	        <td>${score.score }</td>
	        <td>1/2</td>
	        <td>응시</td>
	        <td>-</td>
	      </tr>
	    </tbody>
		</c:forEach>
	  </table>
	  </div>
		
		<div class="flex justify-end">
			<a href="scorelist" class="btn btn-success mr-2" >목록</a>
			<a href="scoremodify?id=${article.id }" class="btn btn-success mr-2" >수정</a>
			<a href="doScoreDelete?id=${article.id }" class="btn btn-success mr-2" 
			onclick="if(confirm('정말 삭제하시겠습니까?')==false) return false;">삭제</a>
		</div>
	</div>
</section>
	
	
	
	
<%@ include file="../common/foot.jsp" %>