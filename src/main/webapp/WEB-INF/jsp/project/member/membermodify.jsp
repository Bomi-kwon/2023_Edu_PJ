<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="회원정보 수정" />

<%@ include file="../common/head.jsp" %>

<script>
	function check(form) {
		
		if(form.name.value.trim().length == 0) {
			alert('이름을 입력해주세요.');
			form.name.focus();
			return;
		}
		if(form.cellphoneNum.value.trim().length == 0) {
			alert('전화번호를 입력해주세요.');
			form.cellphoneNum.focus();
			return;
		}
		if(form.email.value.trim().length == 0) {
			alert('이메일을 입력해주세요.');
			form.email.focus();
			return;
		}
		
		form.submit();
	}
</script>

<section class="mt-8 mx-auto text-xl">
	<div class="container mx-auto px-3">
			<form action="doMemberModify" onsubmit="check(this); return false;">
				<input type="hidden" name="id" value="${member.id }"/>
				<div class="table-box-type-1 overflow-x-auto">
				<div>회원정보 수정</div>
						<table border="1" class="mx-auto able w-full bg-gray-100">
						<colgroup>
							<col width="200"/>
							<col width="600"/>
						</colgroup>
						<tr>
							<th>로그인 아이디</th>
							<td>${member.loginID }</td>
						</tr>
						<tr>
							<th>이름</th>
							<td><input class="input input-bordered input-success w-full" type="text" name="name" 
							value="${member.name }" placeholder="이름을 입력해주세요." /></td>
						</tr>
						<tr>
							<th>전화번호</th>
							<td><input class="input input-bordered input-success w-full" type="text" name="cellphoneNum" 
							value="${member.cellphoneNum }" placeholder="전화번호를 입력해주세요." /></td>
						</tr>
						<tr>
							<th>이메일</th>
							<td><input class="input input-bordered input-success w-full" type="text" name="email" 
							value="${member.email }" placeholder="이메일을 입력해주세요." /></td>
						</tr>
					</table>
				</div>
				<div class="flex justify-end">
					<button class="btn btn-success mr-2">수정하기</button>
					<a class="btn btn-success mr-2" href="passwordmodify?id=${member.id }">비밀번호 수정</a>
					<button class="btn btn-success" type="button" onclick="history.back();">뒤로</button>
				</div>
			</form>
		</div>
</section>
	
	
	
	
<%@ include file="../common/foot.jsp" %>