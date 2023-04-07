 // var regExp_ctn = /^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})([0-9]{3,4})([0-9]{4})$/;
const regHp = /^(01[016789]{1})([0-9]{3,4})([0-9]{4})$/;
const regCode = /^[0-9]+$/;

let authHp;
let isSended = false;      // 인증번호 전송 유무
let isAuth = false;        // 인증번호 인증여부 유무
let smsAuthCode = -999999; // 인증번호
let smsAuthCodeCnt = 5;    // 인증번호 입력 제한 5회
let my_profile = $("#my_profile");

function appendAuthCodeTag(){
    let authCodeTag = "<section id='section3'>"
                    + "    <b>인증번호</b>"
                    + "    <div class='inp_type2 form-errors'>"
                    + "        <input type='text' name='authcode' maxlength='6'>"
                    + "        <span id='timer' class='timer'>03:00</span>"
                    + "        <button type='button' class='btn_common' id='confirmCode'>확인</button>"
                    + "        <label id='authcode_msg' class='validate_msg'></label>"
                    + "    </div>"
                    + "</section>"
    $('#section2 .inp_type2').after(authCodeTag)
}

function regHpConfirm(){
    let hp = $('input[name=hp]').val();
    hp = hp.replace(/[^0-9]/g, "");
    $('input[name=hp]').val(hp);
    if(hp.length == 11 || hp.length == 10){
        if(regHp.test(hp)){
            $('#hp_result').text('');
            if(!isSended)
                $('#sendCodeBtn').addClass('active')
        }
        else{
            if(!isSended)
                $('#sendCodeBtn').removeClass('active');
        }
    } else {
        if(!isSended)
            $('#sendCodeBtn').removeClass('active')
    }
}

// 인증코드 시간 제한
PlAYTIME = null;
function TIMER(){
    const Timer=document.getElementById('timer'); //스코어 기록창-분
    let time= 180000;
    let min=3;
    let sec=60;

    Timer.textContent=String(min).padStart(2, '0')+":"+'00';

    PlAYTIME=setInterval(function(){
        time=time-1000; //1초씩 줄어듦
        min=time/(60*1000); //초를 분으로 나눠준다.

        if(sec>0){ //sec=60 에서 1씩 빼서 출력해준다.
            sec=sec-1;

            //실수로 계산되기 때문에 소숫점 아래를 버리고 출력해준다.
            Timer.textContent=String(Math.floor(min)).padStart(2, '0')+':'+String(sec).padStart(2, '0');

        }
        if(sec===0){
            // 0에서 -1을 하면 -59가 출력된다.
            // 그래서 0이 되면 바로 sec을 60으로 돌려주고 value에는 0을 출력하도록 해준다.
            sec=60;
            Timer.textContent=String(Math.floor(min)).padStart(2, '0')+':'+'00'
        }
        if(time == 120000){
            $('#sendCodeBtn').addClass('active');
            isSended = false;
        }
        if(time == 0){
            clearInterval(PlAYTIME);
            $('#section3').remove();
            smsAuthCodeCnt = 5;
        }

    },1000); //1초마다
}

// 세션 저장
function saveCodeAuth(){
    const now = new Date();
    const nowTime = now.getTime() / 1000 + 60;

    sessionStorage.setItem("AuthTime", nowTime); // 저장
}

// 세션 값 현재 시간과 비교 후 불리언 반환
function getCodeAuth(){
    const now = new Date();
    const nowTime = now.getTime() / 1000;
    const authTime = Number(sessionStorage.getItem("AuthTime"));

    if(authTime != null){
        if(authTime < nowTime){
            sessionStorage.removeItem("AuthTime");
            return false;
        }
        else{
            return true;
        }
    }

    else {
        return false;
    }
}

$(function(){
    $(document).on('keyup', 'input[name=hp]', function(){
        $('#hpModReg').removeClass('active');
        $('#hpModReg').attr('disabled', true);
        isAuth = false;
        $('#section3').remove();
        regHpConfirm();
    });

    $(document).on('click', '#sendCodeBtn', function (){
        if(getCodeAuth()){
            $('#hp_result').text('1분 후에 다시 시도해주세요.');
            return;
        }

        if(isSended){
            $('#hp_result').text('1분 후에 다시 시도해주세요.');
            return;
        }

        else {
            $('#hp_result').text('');
            if(PlAYTIME != null)
                clearInterval(PlAYTIME);
        }

        regHpConfirm();
        let hp = $('input[name=hp]').val();
        if(hp == null || hp.trim().length == 0){
            $('#hp_result').text('휴대폰 번호를 입력해주세요.');
            return;
        }

        else if(!regHp.test(hp)){
            $('#hp_result').text('휴대폰 번호를 올바르게 입력해주세요.');
            return;
        }

        const jsonData = {
            "to":hp
        }
        ajaxAPI("user/sms/send", jsonData, "POST").then((response) => {
            if(response.result == "error") {
                Swal.fire({
                    title : '인증코드 전송에 실패했습니다.\n잠시 후 다시 시도해주세요.',
                    icon : 'error',
                    confirmButtonText : '확인'
                })
                isSended = false;
            } else {
                isSended = true;
                $(this).text('재전송');
                $(this).removeClass('active');

                smsAuthCode = response.result.code;
                authHp = hp;
                if(!document.getElementById("section3"))
                    appendAuthCodeTag();
                clearInterval(PlAYTIME);
                TIMER();
                saveCodeAuth();
            }
        }).catch((errorMsg) => {
            console.log(errorMsg)
        });

        $(document).on('keyup', '#section3 input[name="authcode"]', function(){
            code = $(this).val();
            button = $(this).parent().find('.btn_common');
            code = code.replace(/[^0-9]/g, "");
            $(this).val(code);

            if(code != null && code != '' && regCode.test(code) && code.length == 6){
                button.addClass('active');
            } else {
                button.removeClass('active');
            }
        });

    });

    $(document).on('click', '#section3 .btn_common', function(){
        codeInput = $(this).parent().find('input[name="authcode"]');
        authcode_msg = $(this).parent().find('#authcode_msg');
        code = codeInput.val();

        if(code == null || code == ''){
            authcode_msg.text('인증번호를 입력해주세요.');
            return;
        }

        if(!regCode.test(code)){
            authcode_msg.text('숫자만 입력해주세요.');
            code = code.replace(/[^0-9]/g, "");
            codeInput.val(code);
            return;
        }

        if(code.length != 6){
            authcode_msg.text('인증 번호 6자리를 입력해주세요.');
            return;
        }

        if(smsAuthCode != code){
            authcode_msg.text('인증 번호가 다릅니다. 재입력 해주세요.');
            smsAuthCodeCnt = smsAuthCodeCnt - 1;

            if(smsAuthCodeCnt <= 0){
                 Swal.fire({
                    title : "인증 번호 5회 입력 실패했습니다.\n재인증 해주세요.",
                    icon : 'warning',
                    confirmButtonText : '확인'
                })
                $('#section3').remove();
                smsAuthCodeCnt = 5;
            }

            return;
        }

        isAuth = true;
        $('#section3').remove();
        $('#hpModReg').addClass('active');
        $('#hpModReg').attr('disabled', false);
    });

    $(document).on('click', '#hpModReg', function(){
        if(!isAuth){
            Swal.fire({
                title : "인증 후에 시도해주세요.",
                icon : 'warning',
                confirmButtonText : '확인'
            })
            return;
        }

        Swal.fire({
            html: '<p style="font-size:18px; font-weight:bold;">휴대폰 번호를 정말 수정하시겠습니까?</p>',
            icon: 'question',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '수정',
            cancelButtonText: '취소'
        }).then((result) => {
            if(result.isConfirmed === true){
                const hp = $('input[name=hp]').val();
                const jsonData = {
                    "hp":hp
                }
                ajaxAPI("my/info/hp", jsonData, "PATCH").then((response) => {
                    if(response.result === 1){
                        $('#phoneNum').text(hp);
                    } else {
                        Swal.fire({
                            title : "휴대폰 번호 수정에 실패 했습니다.\n잠시 후에 다시 시도해주세요.",
                            icon : 'error',
                            confirmButtonText : '확인'
                        })
                    }
                    $('#hpModCan').trigger('click');

                }).catch((errorMsg) => {
                    console.log(errorMsg)
                    Swal.fire({
                        title : "휴대폰 번호 수정에 실패 했습니다.\n잠시 후에 다시 시도해주세요.",
                        icon : 'error',
                        confirmButtonText : '확인'
                    })
                    $('#hpModCan').trigger('click');
                });
            }
        });

    });

});

 const fileClone = $('#profile_file').clone();

 // button
 const profileModBtn = $(".profileBtn.mod");
 const profileDelBtn = $(".profileBtn.del");
 const modNickBtn = $('.btn_edit.nick');
 const changeNickBtn = $('.nickChangeBtn');
 const modHpBtn = $('.info_phone .btn_edit');

 // Validation Check
 let nickOk = false;

 // regex
 const reNick = /^[a-zA-Z가-힣0-9][a-zA-Zㄱ-힣0-9]*$/;


 // input
 const modNickInput = $('.mod_nick');

 // section
 const sectionNick = $('#section1');
 const sectionHp = $('#section2');
 sectionNick.remove();
 sectionHp.remove();

 // file
 let fileObject;
 function readURL(input) {
     const maxSize = 10 * 1024 * 1024;

     const fileName = input.value;
     const fileDot = fileName.lastIndexOf(".");
     const fileType = fileName.substring(fileDot+1, fileName.length).toLowerCase();

     console.log(fileType);

     if (input.files && input.files[0]) {
         if(fileType !== "jpg" && fileType !== "png" && fileType !== "jpeg"){
             Swal.fire({
                 title : "파일형식은 JPG, PNG, JPEG만\n 가능합니다.",
                 icon : 'warning',
                 confirmButtonText : '확인'
             })
             return;
         }

         if(input.files[0].size > maxSize){
             Swal.fire({
                 title : "첨부파일 사이즈는 10MB 이내로\n등록 가능합니다.",
                 icon : 'warning',
                 confirmButtonText : '확인'
             })
             return;
         }
         profileModBtn.addClass("active");
         profileModBtn.attr("disabled", false);

         const reader = new FileReader();
         reader.readAsDataURL(input.files[0]);
         fileObject = input.files[0];
         reader.onload = function(e) {
             document.getElementById('my_profile').src = e.target.result;
         };

     } else {
         document.getElementById('my_profile').src = my_profile.attr("src");
     }

     console.log(fileObject);
 }

 function getNick(){
     ajaxAPI("user/nick/create", null, "GET").then((response) => {
         if(response.nick == "error") {
             console.log('GET nick fail...');
         }
         else {
             modNickInput.val(response.nick);
             $('#nick_msg').text('');
             nickOk = true;
         }
         modNickInput.focus();
         changeNickBtn.focus();

     }).catch((errorMsg) => {
         console.log(errorMsg)
     });
 }

 $(function (){
     profileModBtn.on("click", function (){
         Swal.fire({
             html: '<p style="font-size:18px; font-weight:bold;">프로필 사진을 수정하시겠습니까?</p>',
             icon: 'question',
             showCancelButton: true,
             confirmButtonColor: '#3085d6',
             cancelButtonColor: '#d33',
             confirmButtonText: '수정',
             cancelButtonText: '취소'
         }).then((result) => {
             if(result.isConfirmed === true){
                 const formData = new FormData();
                 formData.append("profileFile", fileObject);
                 $.ajax({
                     type: "PATCH",
                     enctype: 'multipart/form-data',
                     url: contextPath + "my/info/profile",
                     data: formData,
                     dataType: "text",
                     processData: false,
                     contentType: false,
                     cache: false,
                     timeout: 600000,
                     beforeSend: function (xhr) {
                         xhr.setRequestHeader(header, token);
                     },
                     success: function (data) {
                         profileModBtn.removeClass("active");
                         profileModBtn.attr("disabled", true);
                         profileDelBtn.addClass("active");
                         profileDelBtn.attr("disabled", false);

                         console.log();

                         $('#profileImg img').attr('src', $('#my_profile').attr('src'));
                     },
                     error: function (e) {
                         console.log("ERROR : ", e);
                         Swal.fire({
                             title : "프로필 사진 수정에 실패 했습니다.\n잠시 후에 다시 시도해주세요.",
                             icon : 'error',
                             confirmButtonText : '확인'
                         })
                     }
                 });
             }
         })
     });

     profileDelBtn.on("click", function (){

         Swal.fire({
             html: '<p style="font-size:18px; font-weight:bold;">프로필 사진을 정말 삭제하시겠습니까?</p>',
             icon: 'question',
             showCancelButton: true,
             confirmButtonColor: '#3085d6',
             cancelButtonColor: '#d33',
             confirmButtonText: '삭제',
             cancelButtonText: '취소'
         }).then((result) => {
             if(result.isConfirmed === true){
                 const jsonData = {"profile":"delete"};
                 ajaxAPI("my/info/profile", null, "DELETE").then((response) => {
                     if(response.result === 1){
                         profileDelBtn.removeClass("active");
                         profileDelBtn.attr("disabled", true);

                         $('#profile_file').replaceWith(fileClone);
                         document.getElementById('my_profile').src = contextPath + "images/my/profile.png";
                         $('#profileImg img').attr('src', contextPath + "images/my/profile.png");
                     }

                 }).catch((errorMsg) => {
                     console.log(errorMsg)
                 });
             }
         });
     });



     $(document).on('click', '.btn_edit.nick', function (){
         modNickBtn.remove();
         $('.info_nick').append(sectionNick);
         modNickInput.val(principalNick);
         $('#nickModReg').removeClass("active");
         $('#nickModReg').attr('disabled', true);
         $('#nick_msg').text('');
         nickOk = false;
     });

     $(document).on('click', '.nickChangeBtn', function (){
         getNick();
     });

     $(document).on('click', '#nickModCan', function(){
         sectionNick.remove();
         $('.info_nick > div').append(modNickBtn);
     });

     $(document).on('input focus', '.mod_nick', function(){
         const nick = $(this).val();
         if(!reNick.test(nick) || nick === principalNick || nick.length > 8){
             if(!reNick.test(nick))
                 $('#nick_msg').text('한글과 영문을 사용해주세요.');

             else if(nick.length > 8)
                 $('#nick_msg').text('8자리 까지만 입력 가능 합니다.');

             $('#nickModReg').removeClass("active");
             $('#nickModReg').attr('disabled', true);
             nickOk = false;
             return;
         }

         ajaxAPI("user/nick/duplicate?nick="+nick, null, "GET").then((response) => {
             if(response.result > 0) {
                 $('#nickModReg').removeClass("active");
                 $('#nick_msg').text('이미 존재하는 닉네임입니다.');
                 $('#nickModReg').attr('disabled', true);
                 nickOk = false;
             } else {
                 $('#nick_msg').text('');
                 $('#nickModReg').addClass("active");
                 $('#nickModReg').attr('disabled', false);
                 nickOk = true;
             }
         }).catch((errorMsg) => {
             console.log(errorMsg)
         });
     });

     $(document).on('click', '#nickModReg', function (){
         $('.mod_nick').focus();
         const nick = $('.mod_nick').val();

         if(nick === principalNick){
             $('#nick_msg').text('현재 닉네임과 동일합니다.');
             return;
         }

         if(!nickOk){
             $('#nick_msg').text('닉네임을 올바르게 입력해주세요.');
             return;
         }


         Swal.fire({
             html: '<p style="font-size:18px; font-weight:bold;">닉네임을 정말 수정하시겠습니까?</p>',
             icon: 'question',
             showCancelButton: true,
             confirmButtonColor: '#3085d6',
             cancelButtonColor: '#d33',
             confirmButtonText: '수정',
             cancelButtonText: '취소'
         }).then((result) => {
             if(result.isConfirmed === true){
                 const jsonData = {
                     "nick":nick
                 }
                 ajaxAPI("my/info/nick", jsonData, "PATCH").then((response) => {
                     console.log('성공');
                     console.log(response);
                     if(response.result === 1) {
                         $(".nick_text").text(nick);
                         $("#profileNick").text(nick);
                     } else {
                         Swal.fire({
                             title : "닉네임 수정에 실패 했습니다.\n잠시 후에 다시 시도해주세요.",
                             icon : 'error',
                             confirmButtonText : '확인'
                         })
                     }
                     $("#nickModCan").trigger("click");
                 }).catch((errorMsg) => {
                     console.log(errorMsg)
                     Swal.fire({
                         title : "닉네임 수정에 실패 했습니다.\n잠시 후에 다시 시도해주세요.",
                         icon : 'error',
                         confirmButtonText : '확인'
                     })
                     $("#nickModCan").trigger("click");
                 });
             }
         });
     });

     $(document).on('click', '.info_phone .btn_edit', function (){
         $(this).remove();
         $('.info_phone').append(sectionHp);
     });

     $(document).on('click', '#hpModCan', function (){
         $('#section3').remove();
         $('#hp_result').text('');
         $('input[name=hp]').val('');
         $('#hpModReg').removeClass('active');
         $('#hpModReg').attr('disabled', true);
         sectionHp.remove();
         isAuth = false;
         $('.info_phone > div').append(modHpBtn);
     });

     $(document).on('click', '.on-off-toggle__input', function (){
         const isChecked = $(this).is(':checked');
         let isNoticeEnabled;

         if(isChecked)
             isNoticeEnabled = 1;
         else
             isNoticeEnabled = 0;

         const jsonData = {
             "isNoticeEnabled":isNoticeEnabled
         }
         ajaxAPI("my/info/notification", jsonData, "PATCH").then((response) => {
             let title;

             if(response.result === 1){
                 if(isNoticeEnabled == 1){
                     title = "마케팅 수신에 동의 하셨습니다."
                 } else {
                     title = "마케팅 수신을 해제 하셨습니다."
                 }
                 Swal.fire({
                     title : title,
                     icon : 'success',
                     confirmButtonText : '확인'
                 })
             } else {
                 if(isNoticeEnabled == 1){
                     title = "마케팅 수신 동의에 실패 했습니다.\n잠시 후에 다시 시도해주세요."
                     $(this).prop('checked',false);
                 } else {
                     title = "마케팅 수신 해제에 실패 했습니다.\n잠시 후에 다시 시도해주세요."
                     $(this).prop('checked',true);
                 }
                 Swal.fire({
                     title : title,
                     icon : 'error',
                     confirmButtonText : '확인'
                 })
             }

         }).catch((errorMsg) => {
             console.log(errorMsg)
             if(isNoticeEnabled == 1){
                 title = "마케팅 수신 동의에 실패 했습니다.\n잠시 후에 다시 시도해주세요."
                 $(this).prop('checked',false);
             } else {
                 title = "마케팅 수신 해제에 실패 했습니다.\n잠시 후에 다시 시도해주세요."
                 $(this).prop('checked',true);
             }
             Swal.fire({
                 title : title,
                 icon : 'error',
                 confirmButtonText : '확인'
             })
         });

     });
 });