$(document).ready(function(){

    /* 배너 */
    $( '.slider' ).bxSlider( {
        auto: true,         // 자동 실행 여부
        pause: 4000,        // 멈춤 속도
        minSlides: 1,       // 최소 노출 개수
        maxSlides: 5,       // 최대 노출 개수
        slideWidth: 1100,   // 슬라이드 너비
        pager: false,       // 현재 위치 페이징 표시 여부 설정
        controls: false,     // 이전 다음 버튼 노출 여부
        touchEnabled : (navigator.maxTouchPoints > 0)
    } );

    $( document ).ready( function() {
        $( '.main_top_table.best' ).slick( {
            slidesToShow: 4,
            slidesToScroll: 1,
            autoplay: Number(bestCnt) > 4,
            autoplaySpeed: 2000,
            variableWidth:Number(bestCnt) < 4
        } );
        $( '.main_top_table.re' ).slick( {
            slidesToShow: 4,
            slidesToScroll: 1,
            autoplay: Number(revisitCnt) > 4,
            autoplaySpeed: 2000,
            variableWidth:Number(revisitCnt) < 4
        } );
    } );


});