<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="../common/head.jsp" %>


<body>

<section class="mt-8 mx-auto text-xl">
	<div class="container mx-auto px-3">
		<form action="">
			<div>
				<span>메세지 받는 사람 : </span>
				<input type="text" name="phoneNumber"/>
				<span>메세지 보낼 내용 : </span>
				<textarea class="textarea textarea-success w-full" name="message" placeholder="메시지를 입력해주세요."></textarea>
			</div>
		</form>
	</div>
</section>


</body>

  
  
