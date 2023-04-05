let lat;
let lng;
let autocompleteInput = document.getElementById('search-box');
let option    = {fields: ["address_components", "formatted_address", "geometry", "icon", "name"]};

/* 검색 */
function initMap(){
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

    });
}

$(function(){
    // Enterkey로 검색 실행
    $(document).on('keyup', '#search-box', function(e){
        if(e.keyCode == '13'){
            $('.btnSearch.place').trigger('click');
        }
    });

    // 검색어를 입력하지 않았을때 검색 실행 막기
    $(document).on('click', '.btnSearch.searchplace', function(){
        let keyword = $('#search-box').val();
        if(keyword.trim()==''){
            sweetalert("검색어를 입력하세요", "warning");
            return false;
        }
    });
});