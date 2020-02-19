(function ($) {

    // Init Wow
    wow = new WOW({
        animateClass: 'animated',
        offset: 100
    });
    wow.init();

    // Navigation scrolls
    $('.navbar-nav li a').bind('click', function (event) {
        $('.navbar-nav li').removeClass('active');
        $(this).closest('li').addClass('active');
        var $anchor = $(this);
        var nav = $($anchor.attr('href'));
        if (nav.length) {
            $('html, body').stop().animate({
                scrollTop: $($anchor.attr('href')).offset().top
            }, 1500, 'easeInOutExpo');

            event.preventDefault();
        }
    });

    // About section scroll
    $(".overlay-detail a").on('click', function (event) {
        event.preventDefault();
        var hash = this.hash;
        $('html, body').animate({
            scrollTop: $(hash).offset().top
        }, 900, function () {
            window.location.hash = hash;
        });
    });

    //jQuery to collapse the navbar on scroll
    $(window).scroll(function () {
        if ($(".navbar-default").offset().top > 50) {
            $(".navbar-fixed-top").addClass("top-nav-collapse");
        } else {
            $(".navbar-fixed-top").removeClass("top-nav-collapse");
        }
    });

    // Testimonials Slider
    $('.bxslider').bxSlider({
        adaptiveHeight: true,
        mode: 'fade'
    });

    $('.form_group.floating input').keyup(function (event) {
        if ($(this).val() != '') {
            $(this).parent('.form_group').addClass('filled');
        } else {
            $(this).parent('.form_group').removeClass('filled');
        }
    });
    $('.form_group.floating input').focusin(function (event) {
        $(this).parent('.form_group').addClass('focused');
    });

    $('.form_group.floating input').focusout(function (event) {
        $(this).parent('.form_group').removeClass('focused');
    });

})(jQuery);