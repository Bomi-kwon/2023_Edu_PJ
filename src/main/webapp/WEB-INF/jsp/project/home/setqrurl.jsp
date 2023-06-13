<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="../common/head.jsp" %>


	<section class="mt-8 mx-auto text-xl">
		<div class="container mx-auto px-3 h-full ">

		<form action="doMakeQR">
		<div class="mb-2">출석체크 QR코드 생성기</div>
			<div class="flex">
				<span class="flex items-center mr-2">날짜 :   </span>
				<input class="input input-bordered input-success w-60" type="date" name="todayDate" 
				min="2023-06-01" max="2023-12-31"/>
			</div>
			<div class="flex">
				<span class="flex items-center mr-2">반 이름 :   </span>
				<div>
					<select name="grade" class="select select-success w-full max-w-xs" onchange="setSelectBox(this)">
						<option value="">학년</option>
						<option value="elementary">초등</option>
						<option value="middle">중등</option>
						<option value="high">고등</option>
					</select>
				</div>
				<div>
					<select name="classId" id="group" class="select select-success w-full max-w-xs">
						<option value="">전체</option>
					</select>
				</div>
			</div>
			<button class="btn">만들기</button>
		</form>

		</div>
	</section>


<script>
	<!-- 학년 선택하면 반 소분류 나오게하기 -->
	function setSelectBox(grade) {
		$('#group').html(`<option value="">전체</option>`);
		
		let gradeVal = grade.value;
		
		$.get('../article/setSelectBox', {
			grade : gradeVal
		}, function(data) {
			console.log(data);
			for(var i = 0; i < data.data1.length; i++) {
				let groupName = data.data1[i].groupName;
				let classId = data.data1[i].id;
				console.log(groupName);
				$('#group').append(`<option value="`+classId+`">`+groupName+`</option>`);
			}
		}, 'json');
	}
</script>

<%@ include file="../common/foot.jsp" %>