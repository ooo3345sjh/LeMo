/** 공통 */
/** 현재날짜 */
let now        = new Date();
let nowYear    = now.getFullYear();
let nowMonth   = now.getMonth() + 1;
let nowDate    = now.getDate();
let date       = nowYear + '-' + nowMonth + '-' + nowDate;
/** 스크롤 탑 */
function scrollToTop(){
    $('html, body').animate({ scrollTop : 0 }, 400);
    return false;
}
/** 스크롤 바텀 */
function scrollToBottom(){
    $('html, body').animate({ scrollTop : 2963 }, 600);
    return false;
}

function daterangepicker(){
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
}

$(function(){
    /** 헤더 메뉴 */
    $('#dropdown').mouseover(function(){
        $('#dropdown_content').addClass('on');	
    });

    $('#dropdown_menu').mouseleave(function(){
        $('#dropdown_content').removeClass('on');
    });

    $('#dropdown2').mouseover(function(){
        $('#dropdown_content2').addClass('on');	
    });
    $('#dropdown_menu2').mouseleave(function(){
        $('#dropdown_content2').removeClass('on');
    }); 
});