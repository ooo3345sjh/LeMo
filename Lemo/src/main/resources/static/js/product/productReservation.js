/** 쿠폰 팝업 해제 */
function popdown_coupon(){
    $('#popup_cp').removeClass('on');
}

/** 약관 팝업 해제*/
function popdown_terms(){
    $('.popup_terms').removeClass('on');
}

function displayPrice(){

    let totPrice = price;
    let disprice = 0;
    let inputpoint = $('input[name=point]').val();
    let el = $('.coupon-el.on').find('.coupon-amount');

    if(el.length > 0){
        if(!$('.coupon-el.on').hasClass('none')){
            disprice = el.attr("data-price");
        }
    }

    totPrice -= inputpoint;
    totPrice -= disprice;
    totalPrice = totPrice;


    if(totPrice < 100){ // 전체 금액이 100원 이하 일 때 -> 결제 테스트 api 100원 이상 결제 되어야 함.
        sweetalert("주문금액은\n 100원 이상이어야합니다.", "warning");
        $('input[name=point]').val("");
        $('.coupon-el.on').removeClass('on');
        totPrice = price;
        $('.in_price').text(totPrice.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')+'원');
        $('.product_amount > b').text(totPrice.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')+'원');
        return false;
    }

    $('.in_price').text(totPrice.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')+'원');
    $('.product_amount > b').text(totPrice.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')+'원');


}

/* 주문 번호 만들기 */
function createMerchantUid(){
    let Year    = now.getFullYear();
    let Month   = ('0' + (now.getMonth() + 1)).slice(-2);
    let Date    = ('0' + now.getDate()).slice(-2);
    let Hours   = ('0' + now.getHours()).slice(-2);
    let Minutes = ('0' + now.getMinutes()).slice(-2);
    let Seconds = ('0' + now.getSeconds()).slice(-2);
    let Milliseconds = ('00' + now.getMilliseconds()).slice(-3);

    var merchantUid = Year + Month + Date + Hours + Minutes + Seconds + Milliseconds;

    return merchantUid;
}

// 카드 결제

function paymentCard(data) {

    var IMP = window.IMP;

    IMP.init(imp_code);

	IMP.request_pay({ // param
        pg : data.pg,
        pay_method : 'card',
        merchant_uid: data.merchant_uid,
        name : room_name,
        amount : data.totalPrice,
        buyer_name : data.name,
        buyer_email : user_email,
        buyer_tel : user_hp,
        display: {
            card_quota: [3]  // 할부개월 3개월까지 활성화
        }
  	},
	function (rsp) { // callback
		if (rsp.success) {
            console.log(rsp);
            data.imp_uid = rsp.imp_uid;

            completePayment(data);

		} else {
          // 결제 실패 시 로직
          console.log(rsp);
		}
	});

}

// 결제 완료
function completePayment(data){

    ajaxAPI("product/payment-complete", data, "POST").then((response) => {
        location.href="/Lemo/product/result"

    }).catch((errorMsg) => {
        sweetalert("결제를 실패하였습니다.", "error");
        console.log(errorMsg)
    });

}

// 데이터 검증에 사용하는 정규표현식
const regHp = /^(01[016789]{1})([0-9]{3,4})([0-9]{4})$/;
/*const regHp = /^01(?:0|1|[6-9])-(?:\d{4})-\d{4}$/*/
const regName = /^[가-힣a-zA-Z]{2,}$/; // 한글, 영어 2글자 이상
const regPoint = /^[0-9]+$/; // 포인트

// 데이터 검증결과 상태변수
let isHpOk = false; // 휴대폰
let isNameOk = false; // 이름
let isAgreeOk = false; // 필수 동의항목

// 최종 주문금액
let totalPrice = 0;
// 적용한 쿠폰 id
let cp_id = "";

$(function(){

    displayPrice();

    /** 쿠폰 팝업 */
    $('.discount_button.coupon').click(function(){
        $('#popup_cp').addClass('on');
    });

    $('.wrap-icon').click(function(){
        popdown_coupon();
    });

    $(document).on('click', '.coupon-el', function(){
        $('.coupon-el').removeClass('on');
        $(this).addClass('on');
        cp_id = $(this).attr("id");

        let disprice = 0;

        if(!$(this).hasClass('none')){
            disprice = $(this).find('.coupon-amount').attr("data-price");
        }
        $('.btn-coupon').text(disprice.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')+'원 적용하기');

    });

    $(document).on('click', '.btn-coupon', function(){
        displayPrice();
        popdown_coupon();
    });

    /* 핸드폰 번호 지우기 */
    $(document).on('click', '.deleteHp', function(){
        $('input[name=hp]').val("");
    });

    /* 핸드폰 번호 유효성 검사 */
    $(document).on('keyup', $('input[name=phone]'),function(){
        let hp = $('input[name=hp]').val();
        /*hp = hp.replace(/[^0-9]/g, "");*/
        hp = hp.replace(/[^0-9]/g, '') // 숫자를 제외한 모든 문자 제거
               .replace(/^(\d{2,3})(\d{3,4})(\d{4})$/, `$1-$2-$3`);
        $('input[name=hp]').val(hp);
    })

    /* 전체 동의 체크 */
    const checkAll = document.querySelector('input[name=checkAll]');
    const checkboxes = document.querySelectorAll('input[name=checkOne]');

    // 체크박스 전체선택, 해제
    checkAll.addEventListener('click', ()=> {

        for(let i=0; i < checkboxes.length; i++){
            checkboxes[i].checked = checkAll.checked;
        }

    });

    // 개별 체크박스를 누르면 전체 선택 해제
    for(let i=0; i < checkboxes.length; i++){

        checkboxes[i].addEventListener('click', ()=>{
            if(!this.checked){
            checkAll.checked = false;
            }
        });
    }

    // 포인트
    $(document).on('keyup', 'input[name=point]', function(){

        let point = $(this).val();
        if(point == ""){
            $('.discount_button.point').text("포인트 사용 " + userPoint.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')+"P");
        }else {
            $('.discount_button.point').text("포인트 사용 취소");
        }

        if(!point.match(regPoint) && point != ""){
            sweetalert("숫자만 입력가능합니다.", "warning");
            $(this).val("");
            displayPrice();
            return false;
        }

        displayPrice();

        if(point > userPoint){ // 보유포인트보다 입력포인트가 많을 경우
            sweetalert(userPoint + '점 포인트 이하까지 사용가능합니다.', "warning");
            $(this).val("");
            displayPrice();

            return false;
        }

    });

    // 포인트 사용 취소 버튼 클릭
    $(document).on('click', '.discount_button.point', function(){
        $('input[name=point]').val("");
        $(this).text("포인트 사용 " + userPoint.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')+"P");
        displayPrice();
    });


    // 결제하기 버튼 클릭
    $(document).on('click', '.btn_pay', function(){

        let name = $('input[name=name]').val();
        let hp = $('input[name=hp]').val().replace(/[^0-9]/g, "");
        let point = $('.discount_input').val();
        let payment = $('#payment_select').val();
        let pg = '';

        if(name.trim() == ""){
            sweetalert("예약자 이름을 입력하세요.", "warning");
            return false;
        }

        if(!name.match(regName)){
            sweetalert("올바른 이름이 아닙니다.", "warning");
            return false;
        }

        if(hp == ""){
            sweetalert("휴대폰 번호를 입력하세요.", "warning");
            return false;
        }

        if(!hp.match(regHp)){
            sweetalert("올바른 휴대폰 번호 양식이 아닙니다.", "warning");
            return false;
        }

        for(let i=0; i < checkboxes.length; i++){
            if(!checkboxes[i].checked){
                sweetalert("필수 동의항목에 동의를 하셔야 합니다.");
                return false;
            }
        }

        /*
        switch(payment) {
            case 1:
                pg = 'html5_inicis.INIpayTest';
                break;
            case 2:
                pg = 'tosspay';
                break;
            case 3:
                pg = 'payco.AUTOPAY';
                break;
            case 4:
                pg = 'kakaopay.TC0ONETIME';
                break;
        }*/

        if(payment == 1){
            pg = 'html5_inicis.INIpayTest';
        }else if(payment == 2) {
            pg = 'tosspay';
        }else if(payment == 3){
            pg = 'payco.PARTNERTEST';
        }else if(payment == 4){
            pg = 'kakaopay.TC0ONETIME';
        }

        console.log("payment : " + payment);
        console.log("pg : " + pg);

        jsonData = {
            "point" : point,
            "cp_id" : cp_id,
            "name" : name,
            "hp" : hp,
            "merchant_uid" : createMerchantUid(),
            "payment" : payment,
            "totalPrice" : totalPrice,
            "pg" : pg
        }

        console.log(jsonData);

        //completePayment(jsonData);

        paymentCard(jsonData);

    });

    /* 약관 팝업 */
    $(document).on('click', '.tl', function(){

        let no = $(this).attr('data-no');

        jsonData = {
            "termsType_no" : no
        }

        ajaxAPI("product/terms", jsonData, "POST").then((response) => {

            console.log(response);
            let html = "";

            html += '<div><div class="fix_title">'
            html += response.termsType_type_ko;
            html += '<button type="button" onclick="popdown_terms()">닫기</button></div>'
            html += '<div class="iscroll_cp" style="touch-action: none;">'
            html += '<div class="content" style="transform: translate(0px, 0px) translateZ(0px);">'
            html += response.terms_content+'</div></div></div>'

            console.log(html);

            $('.popup_terms').text("");
            $('.popup_terms').append(html);

        }).catch((errorMsg) => {
            console.log(errorMsg)
        });

        $('.popup_terms').addClass('on');
    });

});

