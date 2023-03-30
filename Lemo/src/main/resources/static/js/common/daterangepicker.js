let diary_start = date;
let diary_end = date;
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
        "minDate" : date,
        "startDate": date,
        "endDate": date,
        "selectBackward": false,
        "drops": "down",
        "opens": "center"
    }, function (start, end, label) {
        diary_start = start.format('YYYY-MM-DD');
        diary_end   = end.format('YYYY-MM-DD');
    });
});