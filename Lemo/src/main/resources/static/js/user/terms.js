 $(function(){
let isChecked = [false, false, false];

// 필수 약관동의 체크 확인 함수
const reqChk = function (){
    $('.required').each(function(i){
        isChecked[i] = $(this).is(':checked');
    });

    if(!isChecked.includes(false)){
        $("#terms_agree_btn").attr("disabled", false);
        $("#terms_agree_btn").addClass("on")
    } else {
        $("#terms_agree_btn").attr("disabled", true);
        $("#terms_agree_btn").removeClass("on")
    }
}

$('#checkAll').change(function(){
     if($(this).is(":checked"))
        $("input[name='termsType_no']").prop("checked", true);

     else
        $("input[name='termsType_no']").prop("checked", false);
     $('required').trigger('change');
    reqChk();
});

$('.required').change(function (){
    reqChk();
});

$('#terms_agree_btn').click(function(){
     $(".required").attr('required', true);
});

})