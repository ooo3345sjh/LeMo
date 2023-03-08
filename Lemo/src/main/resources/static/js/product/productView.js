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
    $('.sellerComment > button').click(function(){

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
    $('.product_coupon').click(function(){
        $('#popup_coupon').addClass('on');
    });
    
    /** 쿠폰 팝업 해제 */

    /** 확장 이미지 닫기 버튼 */
    $('.expansionImg > button').click(function(){
        $(this).parent().removeClass('on');
        let count = $(this).parent().parent().parent().index();
        
        mySliders[count].destroySlider();
        mySliders.splice(count, 1);
        console.log(count);
        console.log(mySliders);
    });

    /** 객실안내/예약 팝업 */
    $('#showRoom').click(function(){
        remove_tab_class();
        $(this).addClass('on');
        $('#detail_room').attr('style', "display:block;");
    });

    /** 탭 - 숙소정보 */
    $('#showInfo').click(function(){
        remove_tab_class();
        $(this).addClass('on');
        $('.detail_info').attr('style', "display:block;");
    });

    /** 탭 - 리뷰 */
    $('#showReview').click(function(){
        remove_tab_class();
        $(this).addClass('on');
        $('.detail_review').attr('style', "display:block;");

        /** 리뷰 사진 슬라이더 */
        $('.gallerySlider').bxSlider({
            slideWidth: 500,
            pager: false
        });
    });

    /** 탭 - 여행일기 */
    $('#showDiary').click(function(){
        remove_tab_class();
        $(this).addClass('on');
        $('.detail_diary').attr('style', 'display:block');
    });

    /** 탭 - 문의하기 */
    $('#showQna').click(function(){
        remove_tab_class();
        $(this).addClass('on');
        $('.prodQna').attr('style', "display:block;");
    });

    /** 숙소정보 */
    /** 숙소정보 - 기본정보 */
    $('.detail_info_default').click(function(){
        
        let info_default = $(this).next().attr('class')

        if( info_default == 'info_default' ){
            $('.info_default').addClass('on');
        }else if( info_default == 'info_default on' ){
            $('.info_default').removeClass('on');
        }
    });

    /** 숙소정보 - 편의시설 및 서비스 */
    $('.detail_info_service').click(function(){
        let info_service = $(this).next().attr('class')
        
        if( info_service == 'info_service' ){
            $('.info_service').addClass('on');
        }else if( info_service == 'info_service on' ){
            $('.info_service').removeClass('on');
        }
    });

    /** 숙소정보 - 판매자 정보 */
    $('.detail_info_seller').click(function(){
        let info_seller = $(this).next().attr('class')
        
        if( info_seller == 'info_seller' ){
            $('.info_seller').addClass('on');
        }else if( info_seller == 'info_seller on' ){
            $('.info_seller').removeClass('on');
        }
    });
});