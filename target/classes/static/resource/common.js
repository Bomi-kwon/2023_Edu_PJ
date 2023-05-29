$('.menu-button').click(function(){
	$('html').toggleClass('active');
	if($('html').hasClass('active')) {
		$('.menu-button').html('<i class="fa-solid fa-xmark"></i>');
	}
	else {
		$('.menu-button').html('<i class="fa-solid fa-bars"></i>');
	}
});

$('.homeworkChk').click(function(){
	$('.hwChkmodal-bg, .hwChkmodal').show();
});
$('.close-btn').click(function(){
	$('.hwChkmodal-bg, .hwChkmodal').hide();
	$('#hwChktable').html("");
});
$('.hwChkmodal-bg').click(function(){
	$('.hwChkmodal-bg, .hwChkmodal').hide();
	$('#hwChktable').html("");
});