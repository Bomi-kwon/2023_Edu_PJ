<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="수강신청" />

<%@ include file="../common/head.jsp" %>

<!-- 결제창 연동 라이브러리 추가 -->
<script type="text/javascript" src="https://cdn.iamport.kr/js/iamport.payment-1.2.0.js"></script>

<script>
	var IMP = window.IMP; 
	IMP.init("imp71714386"); 
  
    var today = new Date();   
    var hours = today.getHours(); // 시
    var minutes = today.getMinutes();  // 분
    var seconds = today.getSeconds();  // 초
    var milliseconds = today.getMilliseconds();
    var makeMerchantUid = hours +  minutes + seconds + milliseconds;
    var paymentComplete;
	
	function requestPay() {
	    IMP.request_pay({
	      pg: "kcp.INIBillTst",
	      pay_method: "card",
	      merchant_uid: "IMP" + makeMerchantUid,   // 주문번호
	      name: "인터넷강의 수강신청",
	      amount: 500,                         // 숫자 타입
	      buyer_email: "rnjsqhal51@naver.com",
	      buyer_name: "권보미",
	      buyer_tel: "010-9748-0218",
	      buyer_addr: "대전광역시 서구 둔산동",
	      buyer_postcode: "35205"
	    }, function (rsp) { // callback
	      //rsp.imp_uid 값으로 결제 단건조회 API를 호출하여 결제결과를 판단합니다.
	    	  console.log(rsp);
	      if(rsp.success) {
	    	  alert('결제가 완료되었습니다.');
	    	  paymentComplete = true;
	      } else {
	    	  alert('결제에 실패하였습니다. 에러 내용 : ' + rsp.error_msg);
	    	  paymentComplete = false;
	      }
	      
	      $.get('doRegisterAfterPayment', {
				classId : ${group.id},
				paymentComplete : paymentComplete
			}, function(data) {
				alert(data);
				location.replace('/');
			}, 'text');
	      
	    });
	  }
	
		
	
		
	
	
</script>

<section class="mt-8 mx-auto text-xl">
	<div class="container mx-auto px-3">
		<div class="table-box-type-1 mb-5">
			<span>신청과목</span>
			<table class="mx-auto table w-full">
				<colgroup>
					<col width="300"/>
					<col width="200"/>
					<col width="400"/>
				</colgroup>
				<tr>
					<td colspan="3">${group.groupName } - ${group.groupDay }</td>
				</tr>
				<tr>
					<td rowspan="5">NO IMAGE</td>
				</tr>
				<tr>
					<th>수강료</th>
					<td>200,000원</td>
				</tr>
				<tr>
					<th>강좌제공 기간</th>
					<td>100일</td>
				</tr>
				<tr>
					<th>강사</th>
					<td>${group.teacherName }</td>
				</tr>
				<tr>
					<th>교재</th>
					<td>${group.textbook }</td>
				</tr>
			</table>
		</div>
		
		<c:if test="${coupon != null }">
			<div class="table-box-type-1 mb-5">
				<span>쿠폰내역</span>
				<table class="mx-auto table w-full">
					 <thead>
					      <tr>
					      	<th>NO</th>
					        <th>사용가능 보유쿠폰</th>
					        <th>할인액</th>
					        <th>발급일</th>
					        <th>마감일</th>
					        <th>선택</th>
					      </tr>
				    </thead>
				    
				    <tbody>
					      <tr>
					      	<td>${coupon.id }</td>
					        <td>학원지급쿠폰</td>
					        <td>200,000원</td>
					        <td>${coupon.regDate.substring(0,10) }</td>
					        <td>${coupon.deadLine.substring(0,10) }</td>
					        <td><label><input type="checkbox" class="checkbox couponChk" /></label></td>
				     	 </tr>
			    	</tbody>
				</table>
			</div>
		</c:if>
		
		<div class="table-box-type-1 mb-5">
			<span>결제금액</span>
			<table style="table-layout:fixed" class="mx-auto table w-full">
				 <colgroup>
				 	<col width="100"/>
				 	<col width="100"/>
				 	<col width="100"/>
				 </colgroup>
				
				 <thead>
				      <tr>
				      	<th>수강료</th>
				        <th>쿠폰</th>
				        <th>총 결제금액</th>
				      </tr>
			    </thead>
			    
			    <tbody>
				      <tr>
				      	<td>200,000원</td>
				        <td class="couponprice">0원</td>
				        <td class="totalprice">200,000원</td>
			     	 </tr>
		    	</tbody>
			</table>
		</div>
		
		
		<div class="flex justify-end">
			<a class="btn btn-success mr-2 payBtn" onclick="requestPay()">결제하기</a>
			<a style="display:none;" class="btn btn-success mr-2 registerBtn" href="doRegister?classId=${group.id }">수강신청하기</a>
			<a href="list" class="btn btn-success" >목록</a>
		</div>
	</div>
</section>


 <!-- 쿠폰번호 입력 모달창 -->
<div class="CouponPassworkChkmodal-bg"></div>
<div class="CouponPassworkChkmodal">
	<h1>쿠폰번호 입력</h1>
	<a class="close-btn"><i class="fa-regular fa-circle-xmark"></i></a>
 	<div class="flex">
	 	<input class="input input-bordered input-success w-full" type="text" name="" id="CouponPassworkChktable"/>
	 	<button class="CouponPassworkChkmodal-btn btn btn-success">입력</button>
 	</div>
	
</div>

<style>
	/* 쿠폰번호 입력 모달창 커스텀 */
.CouponPassworkChkmodal-bg, .CouponPassworkChkmodal {
	display: none;
}
.CouponPassworkChkmodal-bg {
	position: absolute;
	top: 0;
	left: 0;
	bottom: 0;
	right: 0;
	background-color: rgba(0,0,0,0.5);
	z-index: 10;
}
.CouponPassworkChkmodal {
	position: absolute;
	height: 200px;
	width: 500px;
	background-color: white;
	top: 50%;
	left: 50%;
	transform: translateX(-50%) translateY(-50%);
	padding: 20px;
	z-index: 15;
	border: 2px solid black;
}
.CouponPassworkChkmodal > h1 {
	font-size: 1.5rem;
	margin-bottom: 30px;
}
.close-btn {
	position: absolute;
	top:20px;
	right: 20px;
	cursor: pointer;
	font-size: 2rem;
}
</style>
	
	
<script>
	$('.close-btn').click(function(){
		$('.CouponPassworkChkmodal-bg, .CouponPassworkChkmodal').hide();
		$('#CouponPassworkChktable').html("");
	});
	
	$('.CouponPassworkChkmodal-bg').click(function(){
		$('.CouponPassworkChkmodal-bg, .CouponPassworkChkmodal').hide();
		$('#CouponPassworkChktable').html("");
	});
	
	$('.couponChk').change(function() {
		
		if($('.couponChk').is(':checked')) {
			$('.CouponPassworkChkmodal-bg, .CouponPassworkChkmodal').show();
		} else {
			$('.couponprice').html('0원');
			$('.totalprice').html('200,000원');
			$('.payBtn').show();
			$('.registerBtn').hide();
		}
		
	})
	
	$('.CouponPassworkChkmodal-btn').click(function() {
		
		var couponPWVal = $('#CouponPassworkChktable').val();
		
		$.get('verifyPassword', {
			couponPassword : couponPWVal
		}, function(data) {
			console.log(data);
			
			if(data.success) {
				$('.CouponPassworkChkmodal-bg, .CouponPassworkChkmodal').hide();
				$('#CouponPassworkChktable').val("");
				$('.couponprice').html('200,000원');
				$('.totalprice').html('0원');
				$('.payBtn').hide();
				$('.registerBtn').show();
				alert(data.msg);
			}
			else {
				alert(data.msg);
				location.replace('groupregisterdetail?id=${group.id}');
			}
		}, 'json');
		
	})
</script>
	
	
<%@ include file="../common/foot.jsp" %>