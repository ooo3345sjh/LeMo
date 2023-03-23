/** 객실 이용 안내 팝업 */
function popup_room_guide(room_num){
    $('.popup_room_guide'+room_num).addClass('on')
}

/** 객실 이용 안내 팝업 해제 */
function popdown_room_guide(room_num){
    $('.popup_room_guide'+room_num).removeClass('on');
}

/** 쿠폰 팝업 해제 */
function popdown_coupon(){
    $('#popup_coupon').removeClass('on');
}

/** 탭 통합 class 제거 */
function remove_tab_class(){
    $('#showRoom').removeClass('on');
    $('#detail_room').attr('style', "display:none;");

    $('#showInfo').removeClass('on');
    $('.detail_info').attr('style', "display:none;");

    $('#showReview').removeClass('on');
    $('.detail_review').attr('style', "display:none;");

    $('#showDiary').removeClass('on');
    $('.detail_diary').attr('style', "display:none");

    $('#showQna').removeClass('on');
    $('.prodQna').attr('style', "display:none;");
}

/* 탭의 데이터 로드 */
function loadData(cate, acc_id) {

    let url = "/Lemo/product/detail"+cate+"?acc_id=" + acc_id;

    $('#detail_'+cate).load(url, function() {
        console.log('load complete!');
    });

}

/* 페이지 이동 함수 */
function movePage(event,obj){
    event.preventDefault();
    let href = $(obj).attr('href');
    alert(href);

    $('#detail_qna').load(href, function() {
        console.log('load complete!');
    });
}

/* 문의글 답변 가져오기 */
function getQNA(event, obj) {

    event.preventDefault();
    let qna_no = $(obj).parent().parent().attr('id');

    ajaxAPI("product/getQna?qna_no=" +qna_no, null, "GET").then((response) => {
        if(response.result > 0) {
            alert("성공");
        }
        else {
            console.log("실패..");
        }
    }).catch((errorMsg) => {
        console.log(errorMsg)
    });
}


// 현재 주소의 파라미터
const urlParams = new URLSearchParams(window.location.search);

$(function(){
    /** swiper Slider */
    // main slider 실행
    var swiper = new Swiper(".mySwiper", {
        spaceBetween: 10,
        slidesPerView: 4,
        freeMode: true,
        watchSlidesProgress: true,
        loop: true
    });
    var swiper2 = new Swiper(".mySwiper2", {
        spaceBetween: 10,
        loop: true,
        navigation: {
        nextEl: ".swiper-button-next",
        prevEl: ".swiper-button-prev",
        },
        thumbs: {
        swiper: swiper,
        }
    });
    
    /** 사장님 한마디 확장/축소 */
    $(document).on('click', '.sellerComment > button', function(){

        if( $(this).text() == '더보기' ){
            $('.sellerComment > div').removeClass('clamp');
            $(this).text('접기');
        }else if( $(this).text() == '접기' ){
            $('.sellerComment > div').addClass('clamp');
            $(this).text('더보기');
        }
    });
    
    let mySliders = [];
    /** 객실 이미지 클릭 시 확장 */
    $(document).on('click', '.expansion_Img', function(){

        let count = $(this).parent().parent().index();

        let name = $(this).next().next().next().attr('class');

        $(this).next().next().next().addClass('on');

        /** 다중 클릭 방지 */
        if(name == 'expansionImg'){
            /** bxSlider 삽입 */
            let mySlider = $(this).next().next().next().children('.view_slider').children().bxSlider({
                slideWidth: 1000,
                pager: false
            });
            mySliders.push(mySlider);
            mySlider.reloadSlider();
            console.log(count);
            console.log(mySliders);
        }
    }); 

    /** 쿠폰 팝업 */
    $(document).on('click', '.product_coupon', function(){
        $('#popup_coupon').addClass('on');
    });
    
    /** 쿠폰 팝업 해제 */

    /** 확장 이미지 닫기 버튼 */
    $(document).on('click', '.expansionImg > button', function(){
        $(this).parent().removeClass('on');
        let count = $(this).parent().parent().parent().index();
        
        mySliders[count].destroySlider();
        mySliders.splice(count, 1);
        console.log(count);
        console.log(mySliders);
    });

    /** 객실안내/예약 팝업 */
    $(document).on('click' ,'#showRoom',function(){
        remove_tab_class();
        $(this).addClass('on');
        $('#detail_room').attr('style', "display:block;");
    });

    /** 탭 - 숙소정보 */
    $(document).on('click', '#showInfo', function(){
        remove_tab_class();
        $(this).addClass('on');

        $('.detail_info').attr('style', "display:block;");

    });

    /** 탭 - 리뷰 */
    $(document).on('click','#showReview', function(){
        remove_tab_class();
        $(this).addClass('on');

        let cate = 'review';
        loadData(cate, acc_id);

        $('.detail_review').attr('style', "display:block;");

        /** 리뷰 사진 슬라이더 */
        $('.gallerySlider').bxSlider({
            slideWidth: 500,
            pager: false
        });
    });

    /** 탭 - 여행일기 */
    $(document).on('click','#showDiary', function(){
        remove_tab_class();
        $(this).addClass('on');

        let cate = 'diary';
        loadData(cate, acc_id);

        $('.detail_diary').attr('style', 'display:block');
    });

    /** 탭 - 문의하기 */
    $(document).on('click', '#showQna', function(){
        remove_tab_class();
        $(this).addClass('on');

        let cate = 'qna';
        loadData(cate, acc_id);

        $('.prodQna').attr('style', 'display:block');

    });

    /** 숙소정보 */
    /** 숙소정보 - 기본정보 */
    $(document).on('click', '.detail_info_default', function(){
        
        let info_default = $(this).next().attr('class')

        if( info_default == 'info_default' ){
            $('.info_default').addClass('on');
        }else if( info_default == 'info_default on' ){
            $('.info_default').removeClass('on');
        }
    });

    /** 숙소정보 - 편의시설 및 서비스 */
    $(document).on('click', '.detail_info_service', function(){
        let info_service = $(this).next().attr('class')
        
        if( info_service == 'info_service' ){
            $('.info_service').addClass('on');
        }else if( info_service == 'info_service on' ){
            $('.info_service').removeClass('on');
        }
    });

    /** 숙소정보 - 판매자 정보 */
    $(document).on('click','.detail_info_seller',function(){

        let info_seller = $(this).next().attr('class')

        if( info_seller == 'info_seller' ){
            $('.info_seller').addClass('on');
        }else if( info_seller == 'info_seller on' ){
            $('.info_seller').removeClass('on');
        }
    });

    page = 'view';




   /* 찜하기 */
    $(document).on('click', '#pick', function(){

        let status = $(this).hasClass('on');

        if(!status) {
            $(this).addClass('on');
        }else {
            $(this).removeClass('on');
        }

    });

    /* 문의하기 */
    $(document).on('click', '#check_secret', function(){

        let status = $(this).parent().hasClass('on');

        if(!status) {
            $(this).parent().addClass('on');
        }else {
            $(this).parent().removeClass('on');
        }

    });



    /* 문의 등록 dialog */
    const dialog = document.getElementById('qnadialog');

    $(document).on('click',  '#w_qna', function(){

        if(uid == "") {
            alert("로그인을 하셔야 문의글 작성이 가능합니다.")
            return;
        }
        dialog.showModal();
    });

    $(document).on('click', '.bt_cancel', function(){
        dialog.close();
    });


    /* 문의하기 등록 */

   $(document).on('click', '.bt_confirm', function(){

        let title = $('#qa_title').val();
        let content = $('#ta_content').val();

        let chk_secret = $('#check_secret').is(':checked');
        let secret = 0;

        if(chk_secret) {
            secret = 1;
        }

        if(title.trim().length == 0) {
            alert("문의 제목을 작성해주세요.");
            return;
        }

        if(content.trim().length == 0) {
            alert("문의 내용을 작성해주세요.");
            return;
        }

        let jsonData = {
            "acc_id" : acc_id,
            "user_id" : uid,
            "qna_title" : title,
            "qna_content" : content,
            "qna_secret" : secret
        }

        ajaxAPI("product/rsaveQna", jsonData, "POST").then((response) => {
            if(response.result > 0) {
                // 글 등록 성공시 문의작성폼 초기화
                $('#qa_title').val("");
                $('#ta_content').val("");
                dialog.close();
                // qna 탭 새로 고침
                let cate = 'qna';
                loadData(cate, acc_id);

            }
            else {
                alert("실패");
            }
        }).catch((errorMsg) => {
            console.log(errorMsg)
        });

   })

});

