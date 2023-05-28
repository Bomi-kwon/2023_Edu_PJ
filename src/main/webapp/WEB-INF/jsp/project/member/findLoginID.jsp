<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="아이디 찾기" />

<%@ include file="../common/head.jsp" %>

<script>
	function check(form) {
		
		if(form.name.value.trim().length == 0) {
			alert('이름을 입력해주세요.');
			form.name.focus();
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
			<form action="doFindLoginID" onsubmit="check(this); return false;">
				<div class="table-box-type-1 overflow-x-auto">
				<div>아이디 찾기</div>
						<table border="1" class="mx-auto able w-full bg-gray-100">
						<colgroup>
							<col width="200"/>
							<col width="600"/>
						</colgroup>
						<tr>
							<th>이름</th>
							<td><input class="input input-bordered input-success w-full" type="text" name="name" 
							placeholder="이름을 입력해주세요."/></td>
						</tr>
						<tr>
							<th>이메일</th>
							<td><input class="input input-bordered input-success w-full" type="text" name="email" 
							placeholder="이메일을 입력해주세요."/></td>
						</tr>
					</table>
				</div>
				<div class="flex justify-end mt-2">
					<button class="btn btn-success mr-2">찾기</button>
					<a class="btn btn-success mr-2" href="findLoginPW">비밀번호 찾기</a>
					<button class="btn btn-success" type="button" onclick="history.back();">뒤로</button>
				</div>
			</form>
			<div class="btns flex justify-start">
			</div>
		</div>
</section>
	
	
<%@ include file="../common/foot.jsp" %>