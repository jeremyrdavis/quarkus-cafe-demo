$(document).ready(function(){
	



// Smooth Scroll to Anchors




$('a[href^="#"]:not(.carousel-control)').on('click',function (e) {
    e.preventDefault();

    var target = this.hash,
    $target = $(target);

    $('html, body').stop().animate({
        'scrollTop': $target.offset().top
    }, 900, 'swing', function () {
        window.location.hash = target;
    });
});




// Nav Shrink




$(window).scroll(function() {
  if ($(document).scrollTop() > 80) {
    $('nav').addClass('shrink');
  } else {
    $('nav').removeClass('shrink');
  }
});




// Back to Top




var $backToTop = $("#back-to-top");
$backToTop.hide();


$(window).on('scroll', function() {
  if ($(this).scrollTop() > 250) {
    $backToTop.fadeIn();
  } else {
    $backToTop.fadeOut();
  }
});

$backToTop.on('click', function(e) {
  $("html, body").animate({scrollTop: 0}, 900);
});




// Match Height




$(function() {
  $('.js-matchheight').matchHeight();
});




// Active Class




$(window).scroll(function () {

    var y = $(this).scrollTop();

     $('nav a').each(function (event) {
        if (y >= $($(this).attr('href')).offset().top - 75) {
            $('nav a').not(this).removeClass('active');
            $(this).addClass('active');
         }
    });

});




});









