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

    let url = "/Lemo/product/detail-"+cate+"?acc_id=" + acc_id;

    $('#detail_'+cate).load(url, function() {
        console.log('load complete!');

        if(cate=='review'){
            /** 리뷰 사진 슬라이더 */
            $('.gallerySlider').bxSlider({
                slideWidth: 500,
                pager: false
            });
        }else if(cate=='diary'){
            $('#acc_name_tag').text(acc_name);
        }
    });

}

/* 페이지 이동 함수 */
function movePage(event,obj,cate){
    event.preventDefault();
    let href = $(obj).attr('href');

    $('#detail_'+cate).load(href, function() {
        console.log('load complete!');

        if(cate=='review'){
            /** 리뷰 사진 슬라이더 */
            $('.gallerySlider').bxSlider({
                slideWidth: 500,
                pager: false
            });
        }

    });
}

/* 숙소 예약 */
function reserv(obj){

    if(uid == ""){
       sweetalert("로그인을 하셔야 합니다.", "warning");
       return;
    }

    let CI = startDate;
    let CO = endDate;

    let room_id = $(obj).parent().attr('data-room_id');

    jsonData = {
        "checkIn" : CI,
        "checkOut" : CO,
        "room_id" : room_id
    }
    // 세션에 정보를 저장후 reservation 페이지로 이동
    ajaxAPI("product/reservation", jsonData, "POST").then((response) => {
        location.href="/Lemo/product/reservation";
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
        }
    }); 

    /** 쿠폰 팝업 */
    $(document).on('click', '.product_coupon', function(){

        jsonData = {
            "user_id" : uid,
            "acc_id" : acc_id,
            "province_no" : province_no,
            "accType_no" : accType_no
        }

        ajaxAPI("product/coupon", jsonData, "POST").then((response) => {

            if(response.length > 0 ) {

                let htmlAll = "";
                let htmlAcc = "";

                for(let i = 0; i < response.length; i++) {

                    if(response[i].cp_group != null){ // 전체 발급용 쿠폰
                        htmlAll += '<li>' + response[i].cp_subject;
                        if(response[i].cp_limitedIssuance - response[i].cp_IssuedCnt > 0){ // 발급 수량이 남아있는 경우
                            if(response[i].mcp_id > 0){
                                htmlAll +='<button class="getCoupon" data-cpid="'+response[i].cp_id+'">발급완료</button></li>';
                            }else {
                                htmlAll +='<button class="getCoupon on" data-cpid="'+response[i].cp_id+'">쿠폰발급</button></li>';
                            }
                        }else {
                            htmlAll +='<button class="getCoupon" data-cpid="'+response[i].cp_id+'">발급마감</button></li>';
                        }

                    }else { // 개별 숙소에서 발급하는 쿠폰
                        htmlAcc += '<li>' + response[i].cp_subject;
                        if(response[i].cp_limitedIssuance - response[i].cp_IssuedCnt > 0){
                            if(response[i].mcp_id > 0){
                               htmlAcc +='<button class="getCoupon" data-cpid="'+response[i].cp_id+'">발급완료</button></li>';
                            }else {
                               htmlAcc +='<button class="getCoupon on" data-cpid="'+response[i].cp_id+'">쿠폰발급</button></li>';
                            }
                        }else {
                            htmlAcc +='<button class="getCoupon" data-cpid="'+response[i].cp_id+'">발급마감</button></li>';
                        }
                    }
                }

                if(htmlAll != ""){ // 전체 쿠폰이 존재하면 초기화후 append
                    $('.dot_coupon_txt.all').text("");
                    $('.dot_coupon_txt.all').append(htmlAll);
                }

                if(htmlAcc != ""){ // 숙소 쿠폰이 존재하면 초기화후 append
                    $('.dot_coupon_txt.acc').text("");
                    $('.dot_coupon_txt.acc').append(htmlAcc);
                }
            }

        }).catch((errorMsg) => {
            console.log(errorMsg)
        });

        $('#popup_coupon').addClass('on');
    });
    
    /** 쿠폰 팝업 해제 */

    /* 쿠폰 발급 */
    $(document).on('click', '.getCoupon.on', function(){

        let cp_id = $(this).attr('data-cpid');

        if(uid == ""){
            sweetalert("먼저 로그인을 하셔야 합니다.", "error")
            return false;
        }

        let jsonData = {
            "cp_id" : cp_id,
            "user_id" : uid
        }

        ajaxAPI("product/coupon/receive", jsonData, "POST").then((response) => {
            if(response.result == 1 ) {
                sweetalert("쿠폰 수량이 마감되었습니다.", "warning");
            }else if(response.result == 2) {
                sweetalert("이미 발급받은 쿠폰입니다.", "warning");
            }else if(response.result == 3){
                sweetalert("쿠폰이 발급되었습니다.", "success");
                $(this).removeClass('on');
                $(this).text("발급완료");
            }else {
                sweetalert("쿠폰 발급에 실패하였습니다.\n잠시 후 다시 시도해주세요.", "error")
            }

        }).catch((errorMsg) => {
            console.log(errorMsg)
        });


    });

    /** 확장 이미지 닫기 버튼 */
    $(document).on('click', '.expansionImg > button', function(){
        $(this).parent().removeClass('on');
        let count = $(this).parent().parent().parent().index();
        
        mySliders[count].destroySlider();
        mySliders.splice(count, 1);

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
    /* daterangepicker 구분용 변수 */
    page = 'view';



   /* 찜하기 */
    $(document).on('click', '#pick', function(){

        if(uid == "") {
            sweetalert("로그인 후 찜하기가 가능합니다.", "error");
            return false;
        }

        let status = $(this).hasClass('on');

        jsonData = {
            "user_id" : uid,
            "acc_id" : acc_id,
            "status" : status
        }


        ajaxAPI("product/pick", jsonData, "POST").then((response) => {
            if(response.result > 0 ) {
                if(!status) { // 찜하기
                    $(this).addClass('on');
                }else { // 찜해제
                    $(this).removeClass('on');
                }
            }
        }).catch((errorMsg) => {
            console.log(errorMsg)
        });

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

    $(document).on('click',  '#w_qna', function(e){

        e.preventDefault();

        if(uid == "") {
            sweetalert("로그인을 하셔야\n 문의글 작성이 가능합니다.", "error")
            return;
        }
        //dialog.showModal();
        $('.qna-popup').addClass('on');
    });

    $(document).on('click', '.bt_cancel', function(){
        $('#qa_title').val("");
        $('#ta_content').val("");
        $('.qna-popup').removeClass('on');
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
            sweetalert("문의 제목을 작성해주세요.", "warning");
            return;
        }

        if(content.trim().length == 0) {
            sweetalert("문의 내용을 작성해주세요.", "warning");
            return;
        }

        let jsonData = {
            "acc_id" : acc_id,
            "user_id" : uid,
            "qna_title" : title,
            "qna_content" : content,
            "qna_secret" : secret
        }

        ajaxAPI("product/qna", jsonData, "POST").then((response) => {
            if(response.result > 0) {
                // 글 등록 성공시 문의작성폼 초기화
                $('#qa_title').val("");
                $('#ta_content').val("");
                $('.qna-popup').removeClass('on');
                // qna 탭 새로 고침
                let cate = 'qna';
                loadData(cate, acc_id);
            }
            else {
                sweetalert("실패", "error");
            }
        }).catch((errorMsg) => {
            console.log(errorMsg)
        });
   })

   /* 문의 답변 보기 */
    $(document).on('click', '.qt_tit > a', function(e){
        e.preventDefault();
        let qna_no = $(this).parent().parent().attr('id').slice(1);
        let qnacontent = $('#a' + qna_no);

        if($(this).attr('data-locked') == 1) {
            sweetalert('비밀글은 작성자만\n 조회할 수 있습니다.', "warning"); return;
        }

        let status = $(qnacontent).hasClass('on');
        if(status) {
            qnacontent.removeClass('on');
        }else {
            qnacontent.addClass('on');
        }
    });

    /* 문의글 검색 */
    $(document).on('click', '.btn_sch', function() {

        let keyword = $('#inputSearch').val();

        // 검색어가 없을 경우
        if(keyword.trim().length == 0) {
            sweetalert('검색어를 입력해 주세요.', "warning");
            return;
        }

        let href = "/Lemo/product/detail-qna?searchWord="+keyword+"&acc_id="+acc_id;

        $('#detail_qna').load(href, function() {
            console.log('load complete!');
        });
    });

    /* 전체 문의글 보기 */
    $(document).on('click', '.tl', function(e){
        e.preventDefault();
        loadData('qna', acc_id)
    });


    let isFetchingData = false; // Ajax 요청이 진행 중인지 여부를 저장하는 변수

    /* 여행일기 스크롤시 게시글 가져오기 */
    $(document).on('scroll', function(){

        let scrollT = $(document).scrollTop(); // 페이지 스크롤 top 높이
        let scrollH = $(document).height(); // 전체 페이지의 높이
        let contentH = $('#detail_diary').height(); // 여행일기 content 높이

        if(scrollT > contentH){
            let status = $('#showDiary').hasClass('on');
            if(status) { // 여행일기 탭이 열려 있을 경우
                let currentPage = parseInt($('.diary_list li:last-child').attr('data-page'));
                let endPage = parseInt($('.diary_list li:last-child').attr('data-endpage'));
                let page = currentPage + 1;

                // 마지막 페이지이면 데이터 불러오는거 중지
                if(page > endPage) {
                    return false;
                }

                // Ajax 요청이 진행 중인 경우, 다음 요청을 보내지 않음
                if (isFetchingData) {
                    return;
                }

                isFetchingData = true;
                $.ajax({
                    url: '/Lemo/product/detail-diary?acc_id='+acc_id+"&page="+page,
                    success: function(response) {
                        // 응답 결과를 jQuery객체로 변환후 html 추출후 append
                        let html = $('<div>').html(response).find('.diary_list').html();
                        $('.diary_list').append(html);
                        isFetchingData = false; // ajax 요청 종료
                    },
                    error: function(){
                        isFetchingData = false; // ajax 요청 종료
                    }
                });
            }
        }
    });
});

