/** 쿠폰 팝업 해제 */
function popdown_coupon(){
    $('#popup_cp').removeClass('on');
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

    console.log(totPrice);
    $('.in_price').text(totPrice.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')+'원');
    $('.product_amount > b').text(totPrice.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')+'원');


}

// 데이터 검증에 사용하는 정규표현식
const regHp = /^(01[016789]{1})([0-9]{3,4})([0-9]{4})$/;
const regName = /^[가-힣a-zA-Z]{2,}$/; // 한글, 영어 2글자 이상
const regPoint = /^[0-9]+$/; // 포인트

// 데이터 검증결과 상태변수
let isHpOk = false; // 휴대폰
let isNameOk = false; // 이름
let isAgreeOk = false; // 필수 동의항목

$(function(){
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


    /* 핸드폰 번호 유효성 검사 */
    $(document).on('keyup', $('input[name=phone]'),function(){
        let hp = $('input[name=hp]').val();
        hp = hp.replace(/[^0-9]/g, "");
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
        let hp = $('input[name=hp]').val();

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


    });

});

