$('.menu-button').click(function(){
	$('html').toggleClass('active');
	if($('html').hasClass('active')) {
		$('.menu-button').html('<i class="fa-solid fa-xmark"></i>');
	}
	else {
		$('.menu-button').html('<i class="fa-solid fa-bars"></i>');
	}
});

