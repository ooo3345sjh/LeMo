$(function(){
    /** filter 확장 */
    $('#filter > a').mouseover(function(){
        $('#popupFilter').addClass('on');

        /** Range Slider */
        $(".js-range-slider").ionRangeSlider({
            type: "double",
            min: 0,
            max: 1000000,
            from: 300000,
            to: 600000,
            postfix : '원',
            onFinish : function(data){
                console.log(data.from);
                console.log(data.to);
            }
        });
    });

    $('#popupFilter').mouseleave(function(){
        $(this).removeClass('on');
    });

    // 검색 키워드를 지도 중심으로 설정
    setCenter(clat, clng);

});

    /** 카카오 맵 */
    var container = document.getElementById('listMap'); //지도를 담을 영역의 DOM 레퍼런스
    var options = { //지도를 생성할 때 필요한 기본 옵션
        center: new kakao.maps.LatLng(33.450701, 126.570667), //지도의 중심좌표.
        level: 6 //지도의 레벨(확대, 축소 정도)
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
        var markerPosition = new kakao.maps.LantLng(lat,lng);
        var marker = new kakao.maps.Marker({
            position: markerPosition
        })
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