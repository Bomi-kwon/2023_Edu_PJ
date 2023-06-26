<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="../common/head.jsp" %>

	<section class="">
		<div class="container mx-auto mt-20 px-3 h-full">
			<nav class="kakaomap flex">
				<div id="map" style="width:500px;height:500px;" class="mr-40 ml-60"></div>
				
				<!-- 카카오 api 불러오기 -->
				<!-- 맵 생성후 https 주소도 추가해줌 -->
				<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=36ad0dce027e7f00d0cf41818b68dc45"></script>
				
				<script>
				var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
				    mapOption = { 
				        center: new kakao.maps.LatLng(36.352127, 127.389771), // 지도의 중심좌표
				        level: 3 // 지도의 확대 레벨
				    };
				
				var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다
				
				// 마커가 표시될 위치
				var markerPosition  = new kakao.maps.LatLng(36.352127, 127.389771); 
				
				// 마커를 생성합니다
				var marker = new kakao.maps.Marker({
				    position: markerPosition
				});
				
				// 마커가 지도 위에 표시되도록 설정합니다
				marker.setMap(map);
				
				</script>
				
				<nav class="">
					<div class="mt-20 mb-10">
						<span class="text-4xl">찾아오시는 길</span>
					</div>
					<div class="mb-10">
						<span class="block">자가용 이용시</span>
						<span class="text-2xl">대전 서구 문예로 61 대림빌딩</span>
					</div>
					<div class="mb-10">
						<span class="block">주변 지하철역</span>
						<span class="text-2xl">시청역 6번 출구에서 332m</span>
					</div>
					<div class="mb-10">
						<span class="block">주변 버스정류장</span>
						<span class="text-2xl mr-2">간선 316, 617, 703, 705</span>
						<span class="text-2xl">지선 918</span>
					</div>
				</nav>
			</nav>
		</div>
	</section>

<%@ include file="../common/foot.jsp" %>