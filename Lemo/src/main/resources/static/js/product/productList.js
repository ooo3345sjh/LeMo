$(function(){
    /** filter 확장 */
    $('#filter > a').mouseover(function(){
        $('#popupFilter').addClass('on');

        /** Range Slider */
        $(".js-range-slider").ionRangeSlider({
            type: "double",
            min: 0,
            max: 1000000,
            from: minPrice,
            to: maxPrice,
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

    // 지도 레벨
    if(level > 0) {
        setLevel(level);
    }

    // 마커 표시
    accs.forEach(function(acc, i){
        displayMarker(map, accs[i]);
    });

    // 숙소 유형의 파라미터 값이 존재할때 체크되도록 설정
    let aTypesParam = urlParams.get("accTypes");
    if(aTypesParam != null) {
        let aTypes = aTypesParam.split(",");

        aTypes.forEach(function(aType){

            $('input[name=chkAccType]').each(function(){
                if(aType == $(this).val()) {
                    $(this).prop('checked', true);
                }
            });

        });
    }

    // 인원수 설정
    let headcountParam = urlParams.get("headcount");
    if(headcountParam != null) {
        $('input[name=numPeople]').val(headcountParam);
    }else {
        $('input[name=numPeople]').val(1);
    }

    /* 검색 */

    // 검색 필터
    $('input[name=search]').on('click', function(){

        // 숙소 유형
        let accTypes = [];
        $('input[name=chkAccType]:checked').each(function(){
            accTypes.push($(this).val());
        });
        setUrlParams('accTypes', accTypes.join(','));

        if(maxPrice == 0){
            alert('최대금액은 0원 보다 큰 금액이어야합니다.');
            return false;
        }

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

        // 체크인, 체크아웃
        if(checkIn == ''){checkIn = date};
        if(checkOut == ''){checkOut = endDate};

        let checkInDate = new Date(checkIn);
        let today = new Date(date);

        if(checkInDate < today) {
            alert('지나간 날짜는 선택이 불가능합니다.');
            return;
        }

        // 인원수
        let headcount = $('input[name=numPeople]').val();
        if(headcount > 0 ){setUrlParams('headcount', headcount)};

        //return ;
        setUrlParams('checkIn', checkIn);
        setUrlParams('checkOut', checkOut);
        goSearch();

    });

    // 반경조건으로 재검색
    $('button[name=research]').click(function(){

        // 지도의 현재 영역을 얻어옵니다
        var bounds = map.getBounds();

        // 영역의 남서쪽 좌표를 얻어옵니다
        var swLatLng = bounds.getSouthWest();

        // 영역의 북동쪽 좌표를 얻어옵니다
        var neLatLng = bounds.getNorthEast();

        // 지도의 중심좌표
        var center = map.getCenter();

        // 지도의 레벨
        var level = map.getLevel();
        setUrlParams('level', level);

        clat = center.getLat();
        clng = center.getLng();

        // 키워드 파라미터 삭제 후 위도 경도 재설정
        urlParams.delete('keyword');
        setUrlParams('lat', clat);
        setUrlParams('lng', clng);


        let b = swLatLng.getLng() + " " + swLatLng.getLat()
                + ", " + neLatLng.getLng() + " " + neLatLng.getLat();

        setUrlParams('b', b);
        goSearch();

    });

});

    // 체크인/체크아웃
    let checkIn = '';
    let checkOut = '';

    /* url 관련 */

    // 현재 주소의 파라미터
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



    /** 카카오 맵 */
    var infowindow = new kakao.maps.InfoWindow({zIndex:1});

    var container = document.getElementById('listMap'); //지도를 담을 영역의 DOM 레퍼런스
    var options = { //지도를 생성할 때 필요한 기본 옵션
        center: new kakao.maps.LatLng(33.450701, 126.570667), //지도의 중심좌표.
        level: 7 //지도의 레벨(확대, 축소 정도)
    };

    //지도 생성 및 객체 리턴
    var map = new kakao.maps.Map(container, options);
    map.setZoomable(false);

    // 마우스 휠로 지도 확대,축소 가능여부를 설정합니다
    function setZoomable(zoomable) {
        map.setZoomable(zoomable);
    }

    // 지도의 중심좌표 설정
    function setCenter(lat, lng){
        var moveLatLon = new kakao.maps.LatLng(lat, lng);

        // 지도 중심 이동
        map.setCenter(moveLatLon);
    }

    // 지도 레벨 설정
    function setLevel(level){
        map.setLevel(level);
    }

    // 마커 표시하기
    function displayMarker(map, acc){
        // 마커가 표시될 위치입니다
        var markerPosition  = new kakao.maps.LatLng(acc.acc_lattitude, acc.acc_longtitude);

        // 마커를 생성합니다
        var marker = new kakao.maps.Marker({
            position: markerPosition
        });

        // 마커가 지도 위에 표시되도록 설정합니다
        marker.setMap(map);
        //let status = 0;

        // 마커를 클릭하면 장소명이 인포윈도우에 표출됩니다
        kakao.maps.event.addListener(marker, 'click', function() {

            let addrDetail = '';
            let str = '';
            if(acc.empty_room_stock > 0) {
                str = '<div><b>'+acc.room_price.toLocaleString()+'</b>원/ 1박</div>';
            }else {
                str = '<div><b class="soldout">예약마감</b></div>';
            }

            if(acc.acc_addrDetail != 'None') {
                addrDetail = acc.acc_addrDetail;
            }

            let thumbs = acc.acc_thumbs.split("/");
            let content = '<div class="iw"><div><img src="/Lemo/img/product/'+acc.province_no+'/'+acc.acc_id+'/';
                content += thumbs[0]+'"></div><div><p><a href="/Lemo/product/view?acc_id='+acc.acc_id+'">';
                content += acc.acc_name+'</a></p><span>'+acc.acc_addr+addrDetail+'</span>';
                content += str;
                content += '<button onclick=closeiw()>x</button></div></div>'

            infowindow.setContent(content);
            infowindow.open(map, marker);

        });
    }

    // 인포 윈도우 닫기
    function closeiw(){
        infowindow.close();
    }

    // 지도의 현재 영역을 얻어옵니다
    var bounds = map.getBounds();

    // 영역의 남서쪽 좌표를 얻어옵니다
    var swLatLng = bounds.getSouthWest();

    // 영역의 북동쪽 좌표를 얻어옵니다
    var neLatLng = bounds.getNorthEast();

    // 영역정보를 문자열로 얻어옵니다. ((남,서), (북,동)) 형식입니다
    var boundsStr = bounds.toString();

    console.log('남서쪽 ' + swLatLng.getLat());
    console.log('남서쪽 ' + swLatLng.getLng());
    console.log('북동쪽 ' + neLatLng.getLat());
    console.log('북동쪽 ' + neLatLng.getLng());
    console.log(boundsStr);


