<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="쿠폰발행" />

<%@ include file="../common/head.jsp" %>

<script>
	
	let deadLineVal;
	
	function getStudentsByNameKeyWord() {
		
		$('#studentlisthavingkeyword').html(``);
		
		deadLineVal = $('#deadLine').val();
		let keyWordVal = $('#keyWord').val();
		
		$.get('getStudentsByNameKeyWord', {
			keyWord : keyWordVal
		}, function(data) {
			console.log(keyWordVal);
			console.log(data);
			console.log(deadLineVal);
			
			for(var i = 0; i < data.data1.length; i++) {
				let id = data.data1[i].id;
				let name = data.data1[i].name;
				let loginID = data.data1[i].loginID;
				let cellphoneNum = data.data1[i].cellphoneNum;
				
				$('#studentlisthavingkeyword').append(`
						<div class="mt-2">
							<span>`+name+`</span>
							<span>(`+loginID+`), </span>
							<span>`+cellphoneNum+`</span>
							<button class="btn btn-xs" onclick="selectstudent(`+id+`);">선택</button>
						</div>
						`);
			}
		}, 'json');
		
	}
	
	function selectstudent(id) {
		
		console.log(deadLineVal);
		
		$('#doGiveCoupon').html(`
				<input type="hidden" name="deadLine" value="`+deadLineVal+`"/>
				<input type="hidden" name="studentId" value="`+id+`"/>
				`);
		
		$('#doGiveCoupon').submit();
		
	}
</script>


<section class="mt-8 mx-auto text-xl">
	<div class="container mx-auto px-3">
		<div>
			<div>
				<span>유효기간 : </span>
				<input class="input input-bordered input-success w-60" type="date" name="deadLine" id="deadLine" 
				min="2023-01-01" max="2024-12-31" required/> &nbsp;&nbsp;&nbsp;
				<span>회원명 : </span>
				<input class="input input-bordered input-success w-60" type="text" name="name" id="keyWord"
				placeholder="검색어를 입력해주세요." required/>
				<button class="btn" onclick="getStudentsByNameKeyWord();">검색</button>
			</div>
			
			<!-- 검색한 학생 보여주는 곳 -->
			<div id="studentlisthavingkeyword"></div>
			
			
			<form action="doGiveCoupon" id="doGiveCoupon"></form>
			
		</div>
		
	</div>
</section>
	
	
	
	
<%@ include file="../common/foot.jsp" %>