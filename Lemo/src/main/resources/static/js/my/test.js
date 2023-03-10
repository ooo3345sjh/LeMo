// F4 : 검색된 마크에 입혀지는 공통 Event 함수
function commonMarkerEvent(marker, place, lat, lng){
    if(markers.length >= 5){
        alert('더 이상 등록하실 수 없습니다.');
    }else {
        marker.setMap(null);
        infowindow.close();

        /** 마커 이미지 변경 */
        var markerImage = setMarkerImg();

        marker = new kakao.maps.Marker({
            position: new kakao.maps.LatLng(lat, lng),
            image: markerImage,
            map: map
        });

        let addContent = addDiary(place);
        $('.list-area').append(addContent);
        markers.push(marker);

        // 마커에 mouseover 이벤트를 등록합니다
        kakao.maps.event.addListener(marker, 'mouseover', function() {
            // 마커를 클릭하면 장소명이 인포윈도우에 표출됩니다
            infowindow.setContent('<div style="padding:5px;font-size:12px;">' + place + '</div>');
            infowindow.open(map, marker);
        });

        // 마커에 mouseout 이벤트를 등록합니다
        kakao.maps.event.addListener(marker, 'mouseout', function() {
            infowindow.close();
        });

        // 마커에 click이벤트를 등록합니다
        kakao.maps.event.addListener(marker, 'click', function() {
            // #F8
            removeAlert(marker, place);
        });
    }
}