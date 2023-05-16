<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="회원가입" />

<%@ include file="../common/head.jsp" %>

<section class="mt-8 mx-auto text-xl">
	<div class="container mx-auto px-3">
			<form action="doMemberJoin" >
				<div class="table-box-type-1 overflow-x-auto">
				<div>회원 가입</div>
						<table border="1" class="mx-auto able w-full bg-gray-100">
						<colgroup>
							<col width="200"/>
							<col width="600"/>
						</colgroup>
						<tr>
							<th>로그인 아이디</th>
							<td><input class="input input-bordered input-success w-full" type="text" name="loginID" 
							placeholder="로그인 아이디를 입력해주세요." /></td>
						</tr>
						<tr>
							<th>로그인 비밀번호</th>
							<td><input class="input input-bordered input-success w-full" type="text" name="loginPW" 
							placeholder="로그인 비밀번호를 입력해주세요." /></td>
						</tr>
						<tr>
							<th>로그인 비밀번호 확인</th>
							<td><input class="input input-bordered input-success w-full" type="text" name="loginPWCheck" 
							placeholder="로그인 비밀번호 확인을 입력해주세요." /></td>
						</tr>
						<tr>
							<th>이름</th>
							<td><input class="input input-bordered input-success w-full" type="text" name="name" 
							placeholder="이름을 입력해주세요." /></td>
						</tr>
						<tr>
							<th>전화번호</th>
							<td><input class="input input-bordered input-success w-full" type="text" name="cellphoneNum" 
							placeholder="전화번호를 입력해주세요." /></td>
						</tr>
						<tr>
							<th>이메일</th>
							<td><input class="input input-bordered input-success w-full" type="text" name="email" 
							placeholder="이메일을 입력해주세요." /></td>
						</tr>
					</table>
				</div>
				<div class="flex justify-end">
					<button class="btn btn-success mr-2">가입하기</button>
					<button class="btn btn-success" type="button" onclick="history.back();">뒤로</button>
				</div>
			</form>
			<div class="btns flex justify-start">
			</div>
		</div>
</section>
	
	
	
	
<%@ include file="../common/foot.jsp" %>