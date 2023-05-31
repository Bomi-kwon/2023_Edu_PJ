<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="마이페이지" />

<%@ include file="../common/head.jsp" %>

<section class="mt-8 mx-auto text-xl">
	<div class="container mx-auto px-3">
		<div class="table-box-type-1">
			<table class="mx-auto table w-full">
				<colgroup>
					<col width="100"/>
					<col width="600"/>
				</colgroup>
				<tr>
					<th>회원구분</th>
					<td>${member.authLevel }</td>
				</tr>
				<tr>
					<th>가입날짜</th>
					<td>${member.regDate }</td>
				</tr>
				<tr>
					<th>로그인 아이디</th>
					<td>${member.loginID }</td>
				</tr>
				<tr>
					<th>이름</th>
					<td>${member.name }</td>
				</tr>
				<tr>
					<th>휴대전화</th>
					<td>${member.cellphoneNum }</td>
				</tr>
				<tr>
					<th>이메일</th>
					<td>${member.email }</td>
				</tr>
				<tr>
					<th>프로필 이미지</th>
					<td>
						<c:if test="${profileImg != null }">
							<div class="w-12 h-12"><img src="/project/home/file/${profileImg.id }"/></div>
						</c:if>
						<c:if test="${profileImg == null }">
							<div>이미지 없음</div>
						</c:if>
					</td>
				</tr>
			</table>
		</div>
		<div class="flex justify-end">
			<a class="btn btn-success mr-2" onclick="history.back();">뒤로</a>
			<a href="checkpassword?id=${member.id }" class="btn btn-success mr-2" >회원정보 수정</a>
			<a href="doMemberDrop?id=${member.id }" class="btn btn-success mr-2" 
			onclick="if(confirm('정말 탈퇴하시겠습니까?')==false) return false;">탈퇴</a>
		</div>
	</div>
</section>
	
	
	
	
<%@ include file="../common/foot.jsp" %>