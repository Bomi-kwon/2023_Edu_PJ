<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="수강신청" />

<%@ include file="../common/head.jsp" %>

<section class="mt-8 mx-auto text-xl">
	<div class="container mx-auto px-3">
		<div class="table-box-type-1 mb-5">
			<span>신청과목</span>
			<table class="mx-auto table w-full">
				<colgroup>
					<col width="300"/>
					<col width="200"/>
					<col width="400"/>
				</colgroup>
				<tr>
					<td colspan="3">${group.groupName } - ${group.groupDay }</td>
				</tr>
				<tr>
					<td rowspan="5">NO IMAGE</td>
				</tr>
				<tr>
					<th>수강료</th>
					<td>200,000원</td>
				</tr>
				<tr>
					<th>강좌제공 기간</th>
					<td>100일</td>
				</tr>
				<tr>
					<th>강사</th>
					<td>${group.teacherName }</td>
				</tr>
				<tr>
					<th>교재</th>
					<td>${group.textbook }</td>
				</tr>
			</table>
		</div>
		
		<div class="table-box-type-1 mb-5">
			<span>쿠폰내역</span>
			<table class="mx-auto table w-full">
				 <thead>
				      <tr>
				      	<th>NO</th>
				        <th>사용가능 보유쿠폰</th>
				        <th>할인액</th>
				        <th>발급일</th>
				        <th>사용기한</th>
				        <th>선택</th>
				      </tr>
			    </thead>
			    
			    <tbody>
				      <tr>
				      	<td>1</td>
				        <td>학원지급쿠폰</td>
				        <td>200,000원</td>
				        <td>2023-05-28</td>
				        <td>30일</td>
				        <td><label><input type="checkbox" class="checkbox" /></label></td>
			     	 </tr>
		    	</tbody>
			</table>
		</div>
		
		<div class="table-box-type-1 mb-5">
			<span>결제금액</span>
			<table class="mx-auto table w-full">
				 <thead>
				      <tr>
				      	<th>수강료</th>
				        <th>쿠폰</th>
				        <th>교재비</th>
				        <th>배송비</th>
				        <th>총 결제금액</th>
				      </tr>
			    </thead>
			    
			    <tbody>
				      <tr>
				      	<td>200,000원</td>
				        <td>0원</td>
				        <td>0원</td>
				        <td>0원</td>
				        <td>200,000원</td>
			     	 </tr>
		    	</tbody>
			</table>
		</div>
		
		
		<div class="flex justify-end">
			<a href="list" class="btn btn-success mr-2" >목록</a>
		</div>
	</div>
</section>
	
	
	
	
<%@ include file="../common/foot.jsp" %>