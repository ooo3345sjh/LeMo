// Validation Check
let companyOk = false;
let openDateOk = false;
let bizRegNumOk = false;
let ceoOk = false;
let telOk = false;
let bizRegCheck = false;
let zipOk = false;
let addrOk = false;

const reCompany = /^[a-zA-Z가-힣0-9() ][a-zA-Zㄱ-힣0-9() ]*$/;
const reCeo = /^[가-힣][가-힣]*$/;
const regBizRegNum = /^[0-9]+$/;
const regTel = /^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})([0-9]{3,4})([0-9]{4})$/;


// Input
const companyInput = $('input[name=bis_company]');
const ceoInput = $('input[name=bis_ceo]');
const openDateInput = $('input[name=bis_openDate]');
const bizRegNumInput = $('input[name=bis_bizRegNum]');
const telInput = $('input[name=bis_tel]');
const zipInput = $('#zip');
const addrInput = $('#addr');
const addrDetailInput = $('#addrDetail');
inputList.push(companyInput, ceoInput, openDateInput, bizRegNumInput, telInput, zipInput, addrInput);

// button
const bizRegNumBtn = $('.btn_common.bizRegNum');

// auth - 인증완료된 데이터 변수에 저장
let company_auth;
let ceo_auth;
let openDate_auth;
let bizRegNum_auth;

function bizRegBtnActive(){
    const bizRegNumOkList = [companyOk, ceoOk, openDateOk, bizRegNumOk];
    if(!bizRegCheck) {
        if (!bizRegNumOkList.includes(false)) {
            $('.btn_common.bizRegNum').addClass('active');
        } else {
            $('.btn_common.bizRegNum').removeClass('active');
        }
    }
}

function reAddrCheck() {
    let zip = zipInput.val().trim().replace(/[^0-9]/g, "");
    zipInput.val(zip);
    if (zip.length === 5) {
        zipOk = true;
        $('#addr_msg').text('');
    } else {
        zipOk = false;
        $('#addr_msg').text('우편번호 및 기본주소를 올바르게 입력해주세요.');
    }

    let addr = addrInput.val().trim();
    if (addr.length !== 0) {
        addrOk = true;
        $('#addr_msg').text('');
    } else {
        addrOk = false;
        $('#addr_msg').text('우편번호 및 기본주소를 올바르게 입력해주세요.');
    }
}

$(function() {
    companyInput.on('focusout keyup focus', function () {
        const company = $(this).val();

        if (!reCompany.test(company)) {
            $('#bis_company_msg').text("한글과 영문, '()'을 사용해주세요.");
            companyOk = false;
        } else {
            $('#bis_company_msg').text('');
            companyOk = true;
        }

    });

    ceoInput.on('focusout keyup focus', function () {
        const ceo = $(this).val();
        if (!reCeo.test(ceo)) {
            $('#bis_ceo_msg').text('한글만 입력해주세요.')
            ceoOk = false;
        } else {
            $('#bis_ceo_msg').text('')
            ceoOk = true;
        }
        bizRegBtnActive();
    });

    openDateInput.on('change focusout focus', function () {
        const openDate = $(this).val();
        console.log(openDate);
        if (!!openDate?.trim()) {
            $(this).addClass('on');
            $(this).removeClass('off');
            openDateOk = true;
            $('#bis_openDate_msg').text('');
        } else {
            $(this).addClass('off');
            $(this).removeClass('on');
            openDateOk = false;
            $('#bis_openDate_msg').text('개업일자를 입력해주세요.');
        }
        bizRegBtnActive();

    });

    bizRegNumInput.on('focusout keyup focus', function () {
        let bizRegNum = $(this).val();
        bizRegNum = bizRegNum.replace(/[^0-9]/g, "");
        $(this).val(bizRegNum);

        if (!regBizRegNum.test(bizRegNum) || bizRegNum.length != 10) {
            bizRegNumOk = false;
            $('#bis_bizRegNum_msg').text('사업자 등록번호 10자릿수를 입력해주세요.');
        } else if (regBizRegNum.test(bizRegNum) && bizRegNum.length == 10) {
            bizRegNumOk = true;
            $('#bis_bizRegNum_msg').text('');
        }
        bizRegBtnActive();

    });

    bizRegNumBtn.click(function () {
        const bizRegOkList = [companyOk, ceoOk, openDateOk, bizRegNumOk];
        const company = companyInput.val();
        const ceo = ceoInput.val();
        const bizRegNum = bizRegNumInput.val();
        const openDate = openDateInput.val();
        const openDateReplace = openDate.replaceAll("-", "");

        if (bizRegCheck) {
            alert('이미 인증이 완료되었습니다.\n다시 인증을 원하실 경우 새로고침 후에 시도해주세요.\n※단 지금까지 입력한 정보는 초기화 됩니다.');
            return;
        }
        if (bizRegOkList.includes(false)) {
            alert('사업자등록 인증을위해 사업자등록번호에 등록된\n회사명, 대표자이름, 개업일자, 사업자 등록번호 입력이 필요합니다.');
            if (!companyOk) {
                $('#bis_company_msg').text('회사 이름을 올바르게 입력해주세요.');
                companyInput.focus();
            }
            if (!ceoOk) {
                $('#bis_ceo_msg').text('대표자 이름을 올바르게 입력해주세요.');
                ceoInput.focus();
            }
            if (!openDateOk) {
                $('#bis_openDate_msg').text('개업일자를 입력해주세요.');
                openDateInput.focus();
            }
            if (!bizRegNumOk) {
                $('#bis_bizRegNum_msg').text('사업자 등록번호 10자릿수를 입력해주세요.');
                bizRegNumInput.focus();
            }
            return;
        }

        const jsonData = {
            "businesses": [
                {
                    // "b_no":"3118122413",
                    // "start_dt":"20050524",
                    // "p_nm":"채희관",
                    // "b_nm": "주식회사 에이치케이씨"
                    "b_no": bizRegNum,
                    "start_dt": openDateReplace,
                    "p_nm": ceo,
                    "b_nm": company
                }
            ]
        }

        $.ajax({
            url: "https://api.odcloud.kr/api/nts-businessman/v1/validate?serviceKey=" + url,  // serviceKey 값을 xxxxxx에 입력
            type: "POST",
            data: JSON.stringify(jsonData), // json 을 string으로 변환하여 전송
            dataType: "JSON",
            contentType: "application/json",
            accept: "application/json",
            success: function (result) {
                console.log(result);
                const valid_cnt = result.valid_cnt;

                if (valid_cnt !== undefined) {
                    const b_stt_cd = result.data[0].status.b_stt_cd; // 납세자상태(코드): 01: 계속사업자, 02: 휴업자, 03: 폐업자
                    if (b_stt_cd === "01") {
                        bizRegNumBtn.text("인증완료")
                        bizRegNumBtn.removeClass('active');
                        companyInput.attr('readonly', true);
                        ceoInput.attr('readonly', true);
                        openDateInput.attr('readonly', true);
                        bizRegNumInput.attr('readonly', true);

                        $('#bis_bizRegNum_msg').text('');
                        bizRegNumOk = true;
                        bizRegCheck = true;

                        // 인증완료된 값 저장
                        company_auth = company;
                        ceo_auth = ceo;
                        openDate_auth = openDate;
                        bizRegNum_auth = bizRegNum;
                        return;
                    }
                }

                $('#bis_bizRegNum_msg').text('등록된 사업자가 없습니다.');

            },
            error: function (result) {
                console.log(result.responseText); //responseText의 에러메세지 확인
            }
        });
    });

    telInput.on('focusout keyup', function () {
        let tel = $(this).val();
        tel = tel.replace(/[^0-9]/g, "");
        $(this).val(tel);

        if (!regTel.test(tel)) {
            $('#bis_tel_msg').text('전화번호를 올바르게 입력해주세요.');
            telOk = false;
        } else {
            $('#bis_tel_msg').text('');
            telOk = true;
        }
    });

    $('#zip, #addr, #addrDetail').on('change focusout focus', function () {
        reAddrCheck();
    });
})