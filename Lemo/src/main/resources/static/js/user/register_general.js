// Validation Check
let emailOk = false;
let passOk = false;
let confirmPassOk = false;
let nickOk = false;

// regex
const rePass = /^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{8,16}$/;
const reNick = /^[a-zA-Z가-힣0-9][a-zA-Zㄱ-힣0-9]*$/;
const reEmail = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;

// Input
const emailInput = $('input[type=email]');
const nickInput =  $('input[name=nick]');
const passInput =  $('input[name=pass]');
const confirmPassInput =  $('input[name=pass2]');
const inputList = [emailInput, passInput, confirmPassInput, nickInput];

// auth - 인증완료된 데이터 변수에 저장
let user_id_auth;

function getNick(){
    ajaxAPI("user/nick/create", null, "GET").then((response) => {
        if(response.nick == "error") {
            console.log('GET nick fail...');
        }
        else {
            nickInput.val(response.nick);
            $('#nick_msg').text('');
            nickOk = true;
        }
    }).catch((errorMsg) => {
        console.log(errorMsg)
    });
}

$(function (){
    emailInput.on('focusout', function (){
        const email = $(this).val();
        if(!reEmail.test(email)){
            $('#email_msg').text('이메일 주소를 올바르게 입력해주세요.');
            emailOk = false;
        }
        else {
            $('#email_msg').text('');
            emailOk = true;
        }
    });

    emailInput.on('keyup focus', function (){
        if($('.btn_common.email').attr('disabled') == undefined){
            const email = $(this).val();
            if(!reEmail.test(email)){
                $('.btn_common.email').removeClass("active");
                emailOk = false;
            }
            else {
                $('.btn_common.email').addClass("active");
                emailOk = true;
            }
        }
    });

    $('.btn_common.email').click(function (){
        const email = $('input[type=email]').val();

        if(!reEmail.test(email)) {
            $('#email_msg').text('이메일 주소를 올바르게 입력해주세요.');
            emailOk = false;
            return;
        }
        else {
            $('#email_msg').text('');
            emailOk = true;
        }

        ajaxAPI("user/email/duplicate?email="+email, null, "GET").then((response) => {
            if(response.result == -99999) {
                alert('중복확인에 실패했습니다.\n다시 시도해주세요.');
            } else if(response.result > 0) {
                $('#email_msg').text('이미 존재하는 이메일입니다.');
                emailOk = false;
            } else {
                $('.btn_common.email').text("확인완료");
                $('.btn_common.email').removeClass("active");
                $('.btn_common.email').attr('disabled', true);
                $('input[type=email]').attr('readonly', true);
                emailOk = true;
                user_id_auth=email;
            }
        }).catch((errorMsg) => {
            console.log(errorMsg)
        });
    });

    passInput.on('focusout keyup', function (){
        const pass = $(this).val();
        console.log(pass);
        if(!rePass.test(pass)){
            $('#pass1_msg').text('8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.')
            passOk = false;
        } else {
            $('#pass1_msg').text('')
            passOk = true;
        }
    });

    confirmPassInput.on('focusout keyup', function (){
        const confirmPass = $(this).val();
        const pass =  $('input[name=pass]').val();

        if(!rePass.test(confirmPass)){
            $('#pass2_msg').text('8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.');
            confirmPassOk = false;
        }

        else if(confirmPass != pass){
            $('#pass2_msg').text('비밀번호가 일치하지 않습니다.');
            confirmPassOk = false;
        }

        else {
            $('#pass2_msg').text('');
            confirmPassOk = true;
        }
    });

    getNick();

    $('.btn_common.nick').click(function (){
        getNick();
    });

    nickInput.on('focusout keyup', function (){
        const nick = $(this).val();
        if(!reNick.test(nick)){
            $('#nick_msg').text('한글과 영문을 사용해주세요.');
            nickOk = false;
            return;
        } else {
            $('#nick_msg').text('');
            nickOk = true;
        }

        ajaxAPI("user/nick/duplicate?nick="+nick, null, "GET").then((response) => {
            if(response.result == -99999) {
                console.log('중복확인에 실패했습니다. 다시 시도해주세요.');
                nickOk = false;
            } else if(response.result > 0) {
                $('#nick_msg').text('이미 존재하는 닉네임입니다.');
                nickOk = false;
            } else {
                $('#nick_msg').text('');
                nickOk = true;
            }
        }).catch((errorMsg) => {
            console.log(errorMsg)
        });
    });
});