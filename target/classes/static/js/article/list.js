// list.jsp

// 전체 체크박스를 누르면 모든 체크박스가 체크되기
// checked 속성은 선택 여부에 따라 true/false로 구분됨
$('.checkbox-all-article-id').change(function() {
	const allCheck = $(this);
	const allChecked = allCheck.prop('checked');
	$('.checkbox-article-id').prop('checked', allChecked);
// disabled 박스는 당연히 체크안됨
	$('.checkbox-article-id:is(:disabled)').prop('checked', false);
})


// 아래쪽 체크박스를 다 누르면 맨 위 체크박스도 선택되게 하기
$('.checkbox-article-id').change(function() {
	const checkboxArticleIdCount = $('.checkbox-article-id').length;
	const checkboxArticleIdCheckedCount = $('.checkbox-article-id:checked').length;
	const checkboxDisabledCount = $('.checkbox-article-id:is(:disabled)').length;
	const allChecked = (checkboxArticleIdCount - checkboxDisabledCount) == checkboxArticleIdCheckedCount;
	$('.checkbox-all-article-id').prop('checked', allChecked);
})


// 체크한 게시글들 삭제할 수 있도록 하기
$('.btn-delete-selected-articles').click(function() {
	
	// 몇번 몇번이 체크됐는지 그 값을 배열화 하기
	const values = $('.checkbox-article-id:checked').map((index, el) => el.value).toArray();
	
	if (values.length == 0) {
		alert('선택된 게시글이 없습니다');
		return;
	} 
	
	if (confirm('선택한 글을 삭제하시겠습니까?') == false) {						
		return;
	}
	
	// 그 번호들을 콤마로 구분해서 한줄로 만들기
	$('input[name=ids]').val(values.join(','));
	
	// 폼 실행하기
	$('form[name=do-delete-articles-form]').submit();
})


// 공부인증 게시판은 하루에 글 하나로 제한하는 기능
function articleNumLimit(boardId,loginedMemberId) {
	
	const now = new Date();
	let year = now.getFullYear();
    let month = now.getMonth() + 1;
    let date = now.getDate();
    
    if (month < 10) {
    	  month = '0' + month;
    } if (date < 10) {
	    date = '0' + date;
    }
    
    let today = year + "-" + month + "-" + date;
    
    // 오늘날짜 가져가서 DB에서 내가 오늘 쓴 글 있나 찾아볼것임
    $.get('getArticleNumLimit', {
		today : today,
		boardId : boardId,
		loginedMemberId : loginedMemberId
	}, function(data) {
		console.log(data);
		
		if(data.success) {
			// 글 쓸 수 있으면 write.jsp로 페이지 이동
			location.replace('write?boardId='+boardId);
		} else {
			// 이미 글 썼으면 오류 메시지 보여주고 제자리
			alert(data.msg);
		}
	}, 'json');
	
}