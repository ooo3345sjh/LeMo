/* 날짜 */

let tomorrow = new Date();
tomorrow.setDate(tomorrow.getDate() + 1);

let tYear    = tomorrow.getFullYear();
let tMonth   = tomorrow.getMonth() + 1;
let tDate    = tomorrow.getDate();
let tdate       = tYear + '-' + tMonth + '-' + tDate;

let month = String(nowMonth).padStart(2, '0');
let day = String(nowDate).padStart(2, '0');
let today = nowYear + '-' + month + '-' + day

let startDate = today;
let endDate = tYear + '-' + String(tMonth).padStart(2, '0') + '-' + String(tDate).padStart(2, '0');


let page = '';
$(function(){

    checkInParam = urlParams.get("checkIn");
    checkOutParam = urlParams.get("checkOut");

    if( checkInParam != null &&  checkOutParam != null) {
        startDate = checkInParam;
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
        "startDate": startDate,
        "endDate": endDate,
        "minDate": today,
        "drops": "down",
        "opens": "center"
    }, function (start, end, label) {

        checkIn = start.format('YYYY-MM-DD');
        checkOut = end.format('YYYY-MM-DD');

        // 페이지가 view 일 경우
        if(page == 'view') {

            let newUrl = '/Lemo/product/view?acc_id='+acc_id+'&checkIn=' + checkIn + '&checkOut='+checkOut;

            startDate = checkIn;
            endDate = checkOut;
            // URL 파라미터 값 변경
            urlParams.set("checkIn", checkIn);
            urlParams.set("checkOut", checkOut);

            history.pushState({}, '', newUrl); // URL 변경

            $('#div-article').load(newUrl + ' #div-article', function() {
                console.log('load complete!');
            });

        }

    });
});