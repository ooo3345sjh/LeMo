/** 쿠폰 팝업 해제 */
function popdown_coupon(){
    $('#popup_coupon').removeClass('on');
}

// 휴대폰 번호 정규식
const regHp = /^(01[016789]{1})([0-9]{3,4})([0-9]{4})$/;

$(function(){
    /** 쿠폰 팝업 */
    $('.discount_button.coupon').click(function(){
        $('#popup_coupon').addClass('on');
    });

    $(document).on('keyup', $('input[name=phone]'),function(){
        let hp = $('input[name=hp]').val();
        hp = hp.replace(/[^0-9]/g, "");
        $('input[name=hp]').val(hp);

    })

});

