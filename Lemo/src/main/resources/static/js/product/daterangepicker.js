/* 날짜 */
let tommorrow = now.getDate() + 1;
let endDate = nowYear + '-' + nowMonth + '-' + tommorrow;
$(function(){

    checkInParam = urlParams.get("checkIn");
    checkOutParam = urlParams.get("checkOut");

    if( checkInParam != null &&  checkOutParam != null) {
        date = checkInParam;
        endDate = checkOutParam;
    }

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
            "firstDay": 1,
        },
        "startDate": date,
        "endDate": endDate,
        "drops": "down",
        "opens": "center"
    }, function (start, end, label) {
        checkIn = start.format('YYYY-MM-DD');
        checkOut = end.format('YYYY-MM-DD');
    });
});