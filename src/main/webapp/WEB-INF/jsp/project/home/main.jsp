<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="Home" />

<%@ include file="../common/head.jsp" %>

	<section class="mt-8 mx-auto text-xl">
		<div class="container mx-auto px-3">
	
			<div><a href="../article/homeworklist">숙제 리스트</a></div>
			<div><a href="../member/memberlist">회원 리스트</a></div>
			
			
			<c:if test="${rq.getLoginedMember() == null }">
			    <div><a href="../member/memberlogin">로그인</a></div>
			    <div><a href="../member/memberjoin">회원가입</a></div>
			</c:if>
			
			<c:if test="${rq.getLoginedMember() != null }">
			    <div><a href="../member/doMemberLogout">로그아웃</a></div>
			</c:if>
			
			<div>안뇽하세용</div>
			<div>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Facilis modi nemo aperiam voluptatum reprehenderit cum reiciendis laboriosam! Tempore nobis facilis distinctio voluptas hic nam veniam sint quibusdam doloremque repellendus autem!</div>
			<div>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Facilis modi nemo aperiam voluptatum reprehenderit cum reiciendis laboriosam! Tempore nobis facilis distinctio voluptas hic nam veniam sint quibusdam doloremque repellendus autem!</div>
			<div>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Facilis modi nemo aperiam voluptatum reprehenderit cum reiciendis laboriosam! Tempore nobis facilis distinctio voluptas hic nam veniam sint quibusdam doloremque repellendus autem!</div>
			<div>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Facilis modi nemo aperiam voluptatum reprehenderit cum reiciendis laboriosam! Tempore nobis facilis distinctio voluptas hic nam veniam sint quibusdam doloremque repellendus autem!</div>
			<div>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Facilis modi nemo aperiam voluptatum reprehenderit cum reiciendis laboriosam! Tempore nobis facilis distinctio voluptas hic nam veniam sint quibusdam doloremque repellendus autem!</div>
			<div>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Facilis modi nemo aperiam voluptatum reprehenderit cum reiciendis laboriosam! Tempore nobis facilis distinctio voluptas hic nam veniam sint quibusdam doloremque repellendus autem!</div>
		
		</div>
	</section>

	
</body>
</html>