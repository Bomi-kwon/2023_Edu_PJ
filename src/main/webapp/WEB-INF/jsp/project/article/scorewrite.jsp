<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="성적 입력" />

<%@ include file="../common/head.jsp" %>

<script>
	function setSelectBox(grade) {
		$('#group').html(`<option value="">전체</option>`);
		
		let gradeVal = grade.value;
		
		$.get('setSelectBox', {
			grade : gradeVal
		}, function(data) {
			console.log(data);
			for(var i = 0; i < data.data1.length; i++) {
				let groupName = data.data1[i].groupName;
				let classId = data.data1[i].id;
				$('#group').append(`<option value="`+classId+`">`+groupName+`</option>`);
			}
		}, 'json');
	}
	
	function getMembersByClass() {
		let classId = $('#group option:selected').val();
		
		$('#scoretable').html(`<thead>
			      <tr>
		        <th>
		          <label>
		            <input type="checkbox" class="checkbox" />
		          </label>
		        </th>
		        <th>원생명</th>
		        <th>총점</th>
		        <th>응시여부</th>
		        <th>교사의견</th>
		      </tr>
		    </thead>`);
		console.log(classId);
		$.get('getStudentsByClass', {
			classId : classId
		}, function(data) {
			console.log(data);
			for(var i = 0; i < data.data1.length; i++) {
				let memberName = data.data1[i].name;
				let memberId = data.data1[i].id;
				$('#scoretable').append(`<tbody>
						<input type="hidden" name="memberId" value="`+memberId+`"/>
					      <tr>
					        <th>
					          <label>
					            <input type="checkbox" class="checkbox" />
					          </label>
					        </th>
					        <td>`+memberName+`</td>
					        <td><input class="input input-bordered input-success w-full" type="text" name="score"/></td>
					        <td>응시</td>
					        <td>-</td>
					      </tr>
					    </tbody>
					    <button class="btn btn-success mr-2">작성</button>
					    `);
			}
		}, 'json');
	}
</script>


<section class="mt-8 mx-auto text-xl">
	<div class="container mx-auto px-3">
			<form action="doScoreWrite" >
				<input type="hidden" name="boardId" value="3"/>
				<div class="table-box-type-1 overflow-x-auto">
				<div>성적 입력</div>
						<table border="1" class="mx-auto able w-full bg-gray-100">
						<colgroup>
							<col width="100"/>
							<col width="700"/>
						</colgroup>
						<tr>
							<th>시험 명</th>
							<td><input class="input input-bordered input-success w-full" type="text" name="title" placeholder="제목을 입력해주세요."/></td>
						</tr>
						<tr>
							<th>대상 반</th>
							<td>
								<div class="flex">
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
							</td>
						</tr>
						<tr>
							<th>시험일자</th>
							<td><input class="input input-bordered input-success w-60" type="date" name="regDate" min="2023-01-01" max="2024-12-31"/></td>
						</tr>
					</table>
				</div>
				<div class="flex justify-end">
					<button class="btn btn-success mr-2" onclick="getMembersByClass(); return false;">성적 입력</button>
					<button class="btn btn-success" type="button" onclick="history.back();">뒤로</button>
				</div>
			
				<div class="table-box-type-1 mt-5">
					<table class="table w-full" id="scoretable">
				    
				    
				  </table>
	  		</div>
			</form>
			
			
		</div>
</section>

			


	
	
	
	
<%@ include file="../common/foot.jsp" %>