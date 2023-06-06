<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="../common/head.jsp" %>


<body>

<section class="mt-8 mx-auto text-xl">
	<div class="container mx-auto px-3">
		<form method="post" action="sendmessage">
			<div>
				<div>
					<span>메시지 받는 사람 : </span>
					<input class="input input-bordered input-success" type="text" name="to" placeholder="번호를 입력해주세요." required/>
				</div>
				<div>
					<span>메시지 보낼 내용 : </span>
					<textarea class="textarea textarea-success w-full" name="content" placeholder="메시지를 입력해주세요." required></textarea>
				</div>
			</div>
			<div class="flex justify-end">
				<button class="btn btn-success" type="submit">메시지 보내기</button>
			</div>
		</form>
	</div>
</section>


</body>

  
  
