const regHp = /^(01[016789]{1})([0-9]{3,4})([0-9]{4})$/;
const regCode = /^[0-9]+$/;

let authHp;
let isSended = false;      // 인증번호 전송 유무
let smsAuthCode = -999999; // 인증번호
let smsAuthCodeCnt = 5;    // 인증번호 입력 제한 5회

function appendAuthCodeTag(){
    let authCodeTag = "<section id='section2'>"
                    + "    <b>인증번호</b>"
                    + "    <div class='inp_type2 form-errors'>"
                    + "        <input type='text' name='authcode' maxlength='6'>"
                    + "        <span id='timer' class='timer'>03:00</span>"
                    + "        <label id='authcode_msg' class='validate_msg'></label>"
                    + "        <button type='button' class='btn_common'>확인</button>"
                    + "    </div>"
                    + "</section>"
    $('.certification').append(authCodeTag)
}

function regHpConfirm(){
    let hp = $('input[name=hp]').val();
    hp = hp.replace(/[^0-9]/g, "");
    $('input[name=hp]').val(hp);
    if(hp.length == 11 || hp.length == 10){
        if(regHp.test(hp)){
            $('#hp_result').text('');
            if(!isSended)
                $('#section1 .btn_common').addClass('active')
        }
        else{
            if(!isSended)
                $('#section1 .btn_common').removeClass('active');
        }
    } else {
        if(!isSended)
            $('#section1 .btn_common').removeClass('active')
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
            $('#section1 .btn_common').addClass('active');
            isSended = false;
        }
        if(time == 0){
            clearInterval(PlAYTIME);
            $('#section2').remove();
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
        $('#section2').remove();
        regHpConfirm();
    });

    $(document).on('click', '#section1 .btn_common', function (){
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
                getSwal('인증코드 전송에 실패했습니다.\n잠시 후 다시 시도해주세요.', "info");
                isSended = false;
            } else {
                isSended = true;
                $(this).text('재전송');
                $(this).removeClass('active');

                smsAuthCode = response.result.code;
                authHp = hp;
                if(!document.getElementById("section2"))
                    appendAuthCodeTag();

                TIMER();
                saveCodeAuth();
            }
        }).catch((errorMsg) => {
            console.log(errorMsg)
        });

        $(document).on('keyup', '#section2 input[name="authcode"]', function(){
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

});