$(function(){
    /** Jquery UI Date Picker */
    $('#daterange').daterangepicker({
        "locale": {
            "format": "YYYY-MM-DD",
            "separator": " ~ ",
            "applyLabel": "확인",
            "cancelLabel": "취소",
            "fromLabel": "From",
            "toLabel": "To",
            "customRangeLabel": "Custom",
            "weekLabel": "W",
            "daysOfWeek": ["월", "화", "수", "목", "금", "토", "일"],
            "monthNames": ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"],
            "firstDay": 1
        },
        "startDate": date,
        "endDate": date,
        "drops": "down",
        "opens": "center"
    }, function (start, end, label) {
        alert(" 체크인 :  " + start.format('YYYY-MM-DD') + " 체크아웃 : " + end.format('YYYY-MM-DD'));
    });

    /** Range Slider */
    $(".js-range-slider").ionRangeSlider({
        type: "double",
        min: 0,
        max: 1000000,
        from: 300000,
        to: 600000,
        postfix : '원',
        onFinish : function(data){
            console.log(data.from);
            console.log(data.to);
        }
    });

    /** filter 확장 */
    $('#filter > a').mouseover(function(){
        $('#popupFilter').addClass('on');
    });

    $('#popupFilter').mouseleave(function(){
        $(this).removeClass('on');
    });

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