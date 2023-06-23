<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="반 생성하기" />

<%@ include file="../common/head.jsp" %>



<section class="mt-8 mx-auto text-xl">
	<div class="container mx-auto px-3">
			<form action="doMakeGroup" >
				<div class="table-box-type-1 overflow-x-auto">
					<table border="1" class="mx-auto able w-full bg-gray-100">
						<colgroup>
							<col width="100"/>
							<col width="700"/>
						</colgroup>
						<tr>
							<th>학년</th>
							<td>
								<div class="flex">
									<div>
										<select name="grade" class="select select-success w-80" required>
											<option value="">학년</option>
											<option value="elementary">초등</option>
											<option value="middle">중등</option>
											<option value="high">고등</option>
										</select>
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<th>반 이름</th>
							<td>
								<input class="input input-bordered input-success w-full" type="text" name="groupName" placeholder="반 이름을 입력해주세요." required/>
							</td>
						</tr>
						<tr>
							<th>수업 요일</th>
							<td>
								<div class="flex">
									<div>
										<select name="groupDay" class="select select-success w-80" required>
											<option value="">요일</option>
											<option value="월수금">월수금</option>
											<option value="화목토">화목토</option>
											<option value="토일">토일</option>
										</select>
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<th>선생님</th>
							<td>
								<div class="flex">
									<div>
										<select name="groupTeacherId" class="select select-success w-80" required>
											<option value="">선생님</option>
											<c:forEach var="teacher" items="${teachers }">
												<option value="${teacher.id }">${teacher.name }</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<th>수업 교재</th>
							<td>
								<input class="input input-bordered input-success w-full" type="text" name="textbook" placeholder="책 이름을 입력해주세요." required/>
							</td>
						</tr>
					</table>
				</div>
				<div class="flex justify-end">
					<button class="btn btn-success mr-2">작성</button>
					<button class="btn btn-success" type="button" onclick="history.back();">뒤로</button>
				</div>
			</form>
			<div class="btns flex justify-start">
			</div>
		</div>
</section>
	
	
	
	
<%@ include file="../common/foot.jsp" %>