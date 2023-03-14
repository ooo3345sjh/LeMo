$(function(){
    /** filter 확장 */
    $('#filter > a').mouseover(function(){
        $('#popupFilter').addClass('on');

        /** Range Slider */
        $(".js-range-slider").ionRangeSlider({
            type: "double",
            min: 0,
            max: 1000000,
            from: 0,
            to: 600000,
            postfix : '원',
            onFinish : function(data){
                minPrice = data.from;
                maxPrice = data.to;
            }
        });
    });

    $('#popupFilter').mouseleave(function(){
        $(this).removeClass('on');
    });

    // 검색 키워드를 지도 중심으로 설정
    setCenter(clat, clng);

    // 마커 표시
    accs.forEach(function(acc, i){
        displayMarker(map, accs[i].acc_lattitude, accs[i].acc_longtitude);
    });

    // 검색

    // 편의시설 선택 갯수 제한
    let maxcount = 5;

    // 체크박스를 클릭할 때마다 실행될 함수
    $('input[name="chk"]').on('click', function() {
      // 현재 체크된 체크박스의 개수
      let count = $('input[name="chk"]:checked').length;

      // 체크된 체크박스의 개수가 제한 개수를 초과하면
      if (count > maxcount) {
        // 체크를 해제하고 이전 상태로 되돌림
        $(this).prop('checked', false);
        alert("편의시설은 최대 " + maxcount + "개까지 선택할 수 있습니다.");
      }
    });

    // 검색 필터
    $('input[name=search]').on('click', function(){

        // 편의 시설
//        let services = [];
//        $('input[name=chk]:checked').each(function(){
//            services.push($(this).val());
//        });
        //setUrlParams('services', services.join(','));

        // 숙소 유형
        let accTypes = [];
        $('input[name=chkAccType]:checked').each(function(){
            accTypes.push($(this).val());
        });
        setUrlParams('accTypes', accTypes.join(','));


//        urlParams.set('minPrice', minPrice);
//        urlParams.set('maxPrice', maxPrice);
//
//        urlParams.set('accTypes', accTypes.join(','));
//        urlParams.set('services', services.join(','));

        // 가격 조건
        setUrlParams('minPrice', minPrice);
        setUrlParams('maxPrice', maxPrice);

        goSearch();

    });

    // 정렬 선택
    $('select[name=sort]').on('change', function(){
        let sort = $(this).val();

        // url 파라미터 설정 후 페이지 이동
        setUrlParams('sort', sort);
        goSearch();
    });


    // 검색 조건
    $('.result').on('click', function(e){

        e.preventDefault();

        let headcount = $('input[name=numPeople]').val();
        if(headcount > 0 ){setUrlParams('headcount', headcount)};

        //return ;
        setUrlParams('checkIn', checkIn);
        setUrlParams('checkOut', checkOut);
        goSearch();

    });

});

    const urlParams = new URLSearchParams(window.location.search);

    // 주소 파라미터값을 재설정
    function setUrlParams(param, paramValue){

        // 파라미터에 값이 없으면 파라미터 url에서 삭제
        if(paramValue =="") {
            urlParams.delete(param);
            return false;
        }

        // url 파라미터를 설정
        urlParams.set(param, paramValue);
    }

    // 새 url주소로 이동
    function goSearch(){
        const newUrl = `${window.location.pathname}?${urlParams.toString()}`;
        window.location.replace(decodeURIComponent(newUrl));
    }

    // 가격 조건
    let minPrice = 0;
    let maxPrice = 600000;

    // 체크인/체크아웃
    let checkIn = '';
    let checkOut = '';

    /** 카카오 맵 */
    var container = document.getElementById('listMap'); //지도를 담을 영역의 DOM 레퍼런스
    var options = { //지도를 생성할 때 필요한 기본 옵션
        center: new kakao.maps.LatLng(33.450701, 126.570667), //지도의 중심좌표.
        level: 7 //지도의 레벨(확대, 축소 정도)
    };

    var map = new kakao.maps.Map(container, options); //지도 생성 및 객체 리턴
    map.setZoomable(false);


    function setZoomable(zoomable) {
        // 마우스 휠로 지도 확대,축소 가능여부를 설정합니다
        map.setZoomable(zoomable);
    }

    // 지도의 중심좌표 설정
    function setCenter(lat, lng){
        var moveLatLon = new kakao.maps.LatLng(lat, lng);

        // 지도 중심 이동
        map.setCenter(moveLatLon);
    }


    // 마커 표시하기
    function displayMarker(map, lat, lng){
        // 마커가 표시될 위치입니다
        var markerPosition  = new kakao.maps.LatLng(lat, lng);

        // 마커를 생성합니다
        var marker = new kakao.maps.Marker({
            position: markerPosition
        });
        // 마커가 지도 위에 표시되도록 설정합니다
        marker.setMap(map);
    }

    // 지도의 현재 영역을 얻어옵니다
    var bounds = map.getBounds();

    // 영역의 남서쪽 좌표를 얻어옵니다
    var swLatLng = bounds.getSouthWest();

    // 영역의 북동쪽 좌표를 얻어옵니다
    var neLatLng = bounds.getNorthEast();

    // 영역정보를 문자열로 얻어옵니다. ((남,서), (북,동)) 형식입니다
    var boundsStr = bounds.toString();

//    console.log('남서쪽 ' + swLatLng.getLat());
//    console.log('남서쪽 ' + swLatLng.getLng());
//    console.log('북동쪽 ' + neLatLng.getLat());
//    console.log('북동쪽 ' + neLatLng.getLng());


