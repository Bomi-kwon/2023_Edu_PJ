<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="비밀번호 확인" />

<%@ include file="../common/head.jsp" %>

<script>
	function check(form) {
		
		if(form.loginPW.value.trim().length == 0) {
			alert('비밀번호를 입력해주세요.');
			form.loginPW.focus();
			return;
		}
		
		form.submit();
	}
</script>

<section class="mt-8 mx-auto text-xl">
	<div class="container mx-auto px-3">
			<form action="membermodify" onsubmit="check(this); return false;">
				<input type="hidden" name="id" value="${member.id }"/>
				<div class="table-box-type-1 overflow-x-auto">
				<div>비밀번호 확인</div>
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
							<th>로그인 비밀번호</th>
							<td><input class="input input-bordered input-success w-full" type="text" name="loginPW" 
							placeholder="로그인 비밀번호를 입력해주세요."/></td>
						</tr>
					</table>
				</div>
				<div class="flex justify-end">
					<button class="btn btn-success mr-2" >확인</button>
					<button class="btn btn-success" type="button" onclick="history.back();">뒤로</button>
				</div>
			</form>
			<div class="btns flex justify-start">
			</div>
		</div>
</section>
	

<%@ include file="../common/foot.jsp" %>