<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="../common/head.jsp" %>


	<section class="">
		<div class="container mx-auto px-3 h-full ">

		<span>QR코드로 연결</span>
		<form action="doMakeQR">
			<input class="input input-bordered input-success" type="text" name="url" 
			placeholder="URL을 입력해주세요."/>
			<button class="btn">만들기</button>
		</form>

		</div>
	</section>

<%@ include file="../common/foot.jsp" %>