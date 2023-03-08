$(function(){
    /** 카카오맵 - 스크롤 따라 이동 */
    let currentPosition = parseInt($('#listMap').css('top'));
    let mapHeight = $('#listMap').height();
    let listHeight = $('.list-area').height();
    let boxOffsetTop = $('.list-area').offset().top;
    $(window).scroll(function() {
        let scrollTop = $(window).scrollTop();
        let point;
        let endPoint = listHeight - mapHeight;
        //console.log(position + currentPosition);

        if( scrollTop < boxOffsetTop ){
            point = 0;
        }else if( scrollTop > endPoint ) {
            point = endPoint-30;
        }else {
            point = (scrollTop - boxOffsetTop)+100;
        }
        $('#listMap').stop().animate({top: point}, 700);
    });
});