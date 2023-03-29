/** 쿠폰 팝업 해제 */
function popdown_coupon(){
    $('#popup_coupon').removeClass('on');
}

$(function(){
    /** 쿠폰 팝업 */
    $('.discount_button').click(function(){
        $('#popup_coupon').addClass('on');
    });

});

