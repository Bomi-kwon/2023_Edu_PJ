<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="../common/head.jsp" %>

<style>
body {
    background-color: black;
    color: white;
}
#circlein {
    width: 70px;
    height: 70px;
    border-radius: 50%;
    justify-content: center;
    box-shadow: 0px -2px 15px #E0FF94;
    cursor: pointer;
}
.obj {
    display: flex;
    justify-content: center;
    height: 100%;
}
.mic-icon {
    height: 30px;
    position: absolute;
    margin: 21px;
}
.speak-container {
	width : 80%;
	margin-left: auto;
	margin-right: auto;
}
.words {
	border:1px solid black;
	width : 100%;
	height : 500px;
    font-size: 30px;
}
.copy-btn {
	
}

</style>


<section class="">
	<div class="container mx-auto px-10 h-full">
		<nav class="speak-container">
			<div class="words "></div>
			<div class="obj">
			    <div class="button" id="circlein">
			        <svg class="mic-icon" version="1.1" xmlns="http://www.w3.org/2000/svg"
			            xmlns:xlink="http://www.w3.org/1999/xlink" x="0px" y="0px" viewBox="0 0 1000 1000"
			            enable-background="new 0 0 1000 1000" xml:space="preserve" style="fill:#1E2D70">
			            <g>
			                <path
			                    d="M500,683.8c84.6,0,153.1-68.6,153.1-153.1V163.1C653.1,78.6,584.6,10,500,10c-84.6,0-153.1,68.6-153.1,153.1v367.5C346.9,615.2,415.4,683.8,500,683.8z M714.4,438.8v91.9C714.4,649,618.4,745,500,745c-118.4,0-214.4-96-214.4-214.4v-91.9h-61.3v91.9c0,141.9,107.2,258.7,245,273.9v124.2H346.9V990h306.3v-61.3H530.6V804.5c137.8-15.2,245-132.1,245-273.9v-91.9H714.4z" />
			            </g>
			        </svg>
			    </div>
			</div>
			<div class="flex justify-center mt-5">
				<button class="copy-btn" onclick="copyToClipBoard()">Copy</button>
			</div>
		</nav>
	</div>
</section>
	
	
<script src="/js/home/voice.js"></script>

<%@ include file="../common/foot.jsp" %>