/** 카카오 맵 */
var container = document.getElementById('listMap'); //지도를 담을 영역의 DOM 레퍼런스
var options = { //지도를 생성할 때 필요한 기본 옵션
    center: new kakao.maps.LatLng(36.3504119, 127.3845475), //지도의 중심좌표.
    disableDoubleClickZoom: true,
    level: 13 //지도의 레벨(확대, 축소 정도)
};

var map = new kakao.maps.Map(container, options); //지도 생성 및 객체 리턴
map.setZoomable(false);

var bounds = new kakao.maps.LatLngBounds();

var clusterer = new kakao.maps.MarkerClusterer({
    map: map, // 마커들을 클러스터로 관리하고 표시할 지도 객체
    averageCenter: true, // 클러스터에 포함된 마커들의 평균 위치를 클러스터 마커 위치로 설정
    minLevel: 13 // 클러스터 할 최소 지도 레벨
});

function setZoomable(zoomable) {
    // 마우스 휠로 지도 확대,축소 가능여부를 설정합니다
    map.setZoomable(zoomable);
}

function resizeMap() {
    var container = document.getElementById('listMap');
    container.style.marginLeft = '100px';
    container.style.width = '600px';
}

// 마커 이미지 변경하는 공통 함수
// 함수가 전체적인 기능을 지원하려면
// imageSrc, imageSize, imageOption의 속성을 변수로 가지고
// 호출된 변수에 따라 이미지 속성이 변경되어야하는게 맞다고 생각함
// 하지만 지금 단계에서는 중복으로 들어가는걸 최소화 하기위해 아무런 변수지정없이 함수를 선언함
function setMarkerImg() {
    var imageSrc = '/Lemo/images/index/lemo_logo-removebg-preview.png', // 마커이미지의 주소입니다
    imageSize = new kakao.maps.Size(40, 60), // 마커이미지의 크기입니다
    imageOption = {offset: new kakao.maps.Point(21, 59)}; // 마커이미지의 옵션입니다. 마커의 좌표와 일치시킬 이미지 안에서의 좌표를 설정합니다.

    var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption);

    return markerImage;
}

$(function(){
    // positionMap은 (key, value) => (arti_no, 여행일기) 형식인 Object
    // 여행일기 지도찾기 버튼 클릭 시 세부여행일기들을 핵심적으로 표시하기 위함
    // onclick(arti_no)으로 arti_no에 해당하는 여행일기 세부여행일기들의 marker 배열을 가져올 수 잇음
    // 그렇게되면 해당 이미지를 바꾸고 다시 셋팅, 등등 많은 것을 할 수 있을 것 같아서 이렇게 구현함
    for(let key in positionMap) {
        var positionMarker = [];

        positionMap[key].map(function(position, i) {

            var marker = new kakao.maps.Marker({
                position : new kakao.maps.LatLng(position.spot_lattitude, position.spot_longtitude)
            });

            let content = '<div class="spot_wrap">';
               content += '<div class="spot_title">';
               content += position.spot_title
               content += '<div class="spot_close" onclick="closeOverlay()" title="닫기"></div>'
               content += '</div>';
               content += '<div class="spot_content">';
               content += position.arti_title
               content += '</div>';
               content += '</div>';

            var overlay = new kakao.maps.CustomOverlay({
                content: content,
                position: marker.getPosition()
            });

            kakao.maps.event.addListener(marker, 'click', function() {
                overlay.setMap(map);
            });

            $(document).on('click', '.spot_close', function(){
                overlay.setMap(null);
            });

            // 맵에 key값으로 분류되는 value로 넣기 위한 배열
            positionMarker.push(marker);

            // clusterer을 표기하기위한 배열
            markers.push(marker);
        });

        // key=arti_no, value(세부여행일기 marker의 배열)
        markerObject.set(key, positionMarker);

        // 초기화
        positionMarker = [];
    }

    clusterer.addMarkers(markers);
});