
$(document).ready(function(){

    /* 배너 */
    $( '.slider' ).bxSlider( {
        auto: true,         // 자동 실행 여부
        pause: 4000,        // 멈춤 속도
        minSlides: 1,       // 최소 노출 개수
        maxSlides: 5,       // 최대 노출 개수
        slideWidth: 1100,   // 슬라이드 너비
        pager: false,       // 현재 위치 페이징 표시 여부 설정
        controls: false     // 이전 다음 버튼 노출 여부
    } );

    $( document ).ready( function() {
        $( '.main_top_table' ).slick( {
            slidesToShow: 4,
            slidesToScroll: 1,
            autoplay: true,
            autoplaySpeed: 2000,
        } );
    } );


    let lat;
    let lng;
    let autocompleteInput = document.getElementById('search-box');
    let option    = {fields: ["address_components", "formatted_address", "geometry", "icon", "name"]};

    /* 검색 */
    $(function(){
        const autocomplete = new google.maps.places.Autocomplete(autocompleteInput, option);
        autocomplete.setComponentRestrictions({country: ["kr"]});

        autocomplete.addListener('place_changed', function(){

            const place = autocomplete.getPlace();

            // 구글 자동완성 검색 결과가 없을 시 return
            if (!place.geometry || !place.geometry.location) {
                return;
            }

            // 구글 자동완성 검색 결과가 있을 때 밑으로 실행
            lat = place.geometry.location.lat();
            lng = place.geometry.location.lng();

            // product list로 이동
            location.href = '/Lemo/product/list?lat=' + lat + '&lng='+ lng;



            // AJAX
//            let jsonData = {"lat" : lat, "lng" : lng}
//            ajaxAPI("Lemo/product/list?lat=" + lat + "&lng=" + lng,  jsonData, "get").then((response) => {
//            	// 전송 성공시 코드 작성
//            	alert('성공!');
//            }).catch((errorMsg) => {
//            	// 전송 실패시 코드 작성
//            	alert('실패!');
//            });

        });
    })


});