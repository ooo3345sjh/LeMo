$(function(){

    // /** 카카오맵 - 스크롤 따라 이동 */
    // let currentPosition = parseInt($('#listMap').css('top'));
    // let mapHeight = $('#listMap').height();
    // let listHeight = $('.list-area').height();
    // let boxOffsetTop = $('.list-area').offset().top;
    //
    // $(window).scroll(function() {
    //     let scrollTop = $(window).scrollTop();
    //     let point;
    //     let endPoint = listHeight - mapHeight;
    //
    //
    //     if( scrollTop < boxOffsetTop ){
    //         point = 0;
    //     }else if( scrollTop > endPoint ) {
    //         point = endPoint-30;
    //     }else {
    //         point = (scrollTop - boxOffsetTop)+100;
    //     }
    //
    //     if(point < 0) {point = 0}
    //
    //     $('#listMap').stop().animate({top: point}, 700);
    // });


    /* 이미지 슬라이드 */
    $('.img_slider').slick({
        slide : 'div',
        infinite: true,
        slidesToShow: 1,
        slidesToScroll: 1,
        speed: 1000,
        arrows: true,
        dots:true
    });

    /* 이미지 위에 마우스 hover시 사진 넘기는 버튼 나오기 */
    $('.img_slider').hover(function(){
        let status = $(this).find('.slick-arrow').hasClass('on');
        if(!status){
            $(this).find('.slick-arrow').addClass('on');
        }
    });

    $('.img_slider').mouseleave(function(){
        $(this).find('.slick-arrow').removeClass('on');
    });

    $('.img_slider').click(function(e) {
        e.preventDefault();
        $(this).find('.slick-arrow').removeClass('on');
        $(this).parent().find('.diary_content').addClass('on');

    });

    $('.slick-arrow').click(function(event) {
        event.stopPropagation(); // 화살표를 누를땐 클릭 이벤트 전파 중단
    });


    $('.diary_content').click(function(){
        $(this).removeClass('on');
    });


    /* 찜하기 */
    $('.heart').click(function(){
        $(this).toggleClass('on');
    });
});