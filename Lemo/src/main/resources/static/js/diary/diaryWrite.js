/** 카카오 맵 */

            // #1 마커를 클릭하면 장소명을 표출할 인포윈도우 입니다
            var infowindow = new kakao.maps.InfoWindow({zIndex:1});
            var container = document.getElementById('listMap'); //지도를 담을 영역의 DOM 레퍼런스
            var options = { //지도를 생성할 때 필요한 기본 옵션
                center: new kakao.maps.LatLng(33.450701, 126.570667), //지도의 중심좌표.
                level: 7 //지도의 레벨(확대, 축소 정도)
            };

            var map = new kakao.maps.Map(container, options); //지도 생성 및 객체 리턴

            // #2 지도에 표시된 마커 객체를 가지고 있을 배열입니다
            var markers = [];
            var searchMarkers = [];

            // #3 장소 검색 객체를 생성합니다
            var ps = new kakao.maps.services.Places();

            // #4 키워드 검색 완료 시 호출되는 콜백함수 입니다
            let lat;
            let lng;
            let autocompleteInput = document.getElementById('searchAddr');
            let searchBtn = document.getElementById('searchBtn');
            let option    = {fields: ["address_components", "formatted_address", "geometry", "icon", "name"]};
            const autocomplete = new google.maps.places.Autocomplete(autocompleteInput, option);
            autocomplete.setComponentRestrictions({country: ["kr"]});

            $(function(){
                autocomplete.addListener('place_changed', function(){
                    /** 재검색 시 이전 검색 마커 초기화 */
                    for(var i=0; i<searchMarkers.length; i++){
                        searchMarkers[i].setMap(null);
                    }

                    const place = autocomplete.getPlace();

                    if (!place.geometry || !place.geometry.location) {
                                                                   /** #5  */
                        ps.keywordSearch($('#searchAddr').val(), placesSearchCB);
                        return;
                    }

                    lat = place.geometry.location.lat();
                    lng = place.geometry.location.lng();

                    /** 검색 시 해당하는 좌표로 이동 */
                    var bounds = new kakao.maps.LatLngBounds();
                    bounds.extend(new kakao.maps.LatLng(lat, lng));
                    map.setBounds(bounds);

                    /** 마커 이미지 변경 */
                    var imageSrc = '/Lemo/images/diary/pin.png', // 마커이미지의 주소입니다
                    imageSize = new kakao.maps.Size(40, 40), // 마커이미지의 크기입니다
                    imageOption = {offset: new kakao.maps.Point(20, 40)}; // 마커이미지의 옵션입니다. 마커의 좌표와 일치시킬 이미지 안에서의 좌표를 설정합니다.

                    var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption);

                    // 마커가 표시될 위치입니다
                    var markerPosition  = new kakao.maps.LatLng(lat, lng);

                    var marker = new kakao.maps.Marker({
                        map: map,
                        image: markerImage,
                        position: new kakao.maps.LatLng(lat, lng)
                    });

                    // 마커에 mouseover이벤트를 등록합니다
                    kakao.maps.event.addListener(marker, 'mouseover', function() {
                        // 마커를 클릭하면 장소명이 인포윈도우에 표출됩니다
                        infowindow.setContent('<div style="padding:5px;font-size:12px;">' + place.name + '</div>');
                        infowindow.open(map, marker);
                    });

                    // 마커에 mouseout이벤트를 등록합니다
                    kakao.maps.event.addListener(marker, 'mouseout', function() {
                        infowindow.close();
                    });

                    searchMarkers.push(marker);

                });
            })

            // #5 키워드 검색 완료 시 호출되는 콜백함수 입니다
            function placesSearchCB (data, status, pagination) {
                if (status === kakao.maps.services.Status.OK) {

                    // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
                    // LatLngBounds 객체에 좌표를 추가합니다
                    var bounds = new kakao.maps.LatLngBounds();

                    for (var i=0; i<data.length; i++) {
                        /** #6 */
                        displayMarker(data[i]);
                        bounds.extend(new kakao.maps.LatLng(data[i].y, data[i].x));
                    }

                    // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
                    map.setBounds(bounds);
                }
            }

            // #6 지도에 마커를 표시하는 함수입니다
            function displayMarker(place) {

                var imageSrc = '/Lemo/images/diary/pin.png', // 마커이미지의 주소입니다
                imageSize = new kakao.maps.Size(40, 40), // 마커이미지의 크기입니다
                imageOption = {offset: new kakao.maps.Point(20, 40)}; // 마커이미지의 옵션입니다. 마커의 좌표와 일치시킬 이미지 안에서의 좌표를 설정합니다.

                var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption);

                // 마커를 생성하고 지도에 표시합니다
                var marker = new kakao.maps.Marker({
                    map: map,
                    image: markerImage,
                    position: new kakao.maps.LatLng(place.y, place.x)
                });

                searchMarkers.push(marker);

                // 마커에 mouseover이벤트를 등록합니다
                kakao.maps.event.addListener(marker, 'mouseover', function() {
                    // 마커를 클릭하면 장소명이 인포윈도우에 표출됩니다
                    infowindow.setContent('<div style="padding:5px;font-size:12px;">' + place.place_name + '</div>');
                    infowindow.open(map, marker);
                });

                // 마커에 mouseout이벤트를 등록합니다
                kakao.maps.event.addListener(marker, 'mouseout', function() {
                    infowindow.close();
                });
            }

            // #7 지도를 클릭했을때 클릭한 위치에 마커를 추가하도록 지도에 클릭이벤트를 등록합니다
            kakao.maps.event.addListener(map, 'click', function(mouseEvent) {
                // 클릭한 위치에 마커를 표시합니다
                addMarker(mouseEvent.latLng);
            });

            // #8 마커를 생성하고 지도위에 표시하는 함수입니다
            function addMarker(position) {

                let place = prompt('장소를 입력하세요.');
                if(!place){
                    alert('장소를 입력하지 않으셨습니다.!');
                    return false;
                }
                // 마커를 생성합니다
                var marker = new kakao.maps.Marker({
                    position: position
                });

                // 마커가 지도 위에 표시되도록 설정합니다
                marker.setMap(map);

                // 생성된 마커를 배열에 추가합니다
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

                let addContent  = '<div class="place place_' + place+ '">';
                    addContent += '<div class="spot">';
                    addContent += '<img src="/img/diaryImg/marker4.png" width="36">';
                    addContent += '<span>'+ place +'</span>';
                    addContent += '</div>';
                    addContent += '<img src="/img/csImg/arr_down.png" class="arrow">';
                    addContent += '<div class="toggle">';
                    addContent += '<div class="image">';
                    addContent += '<input type="file" class="ex_file" accept="image/png, image/jpeg" required style="display: none;">';
                    addContent += '<img class="inputImage" src="/HTML/img/diaryImg/imgUpload.png">';
                    addContent += '</div>';
                    addContent += '<textarea class="content" placeholder="내용을 입력해주세요."></textarea>';
                    addContent += '</div>';
                    addContent += '</div>';

                $('.list-area').append(addContent);

                // 마커에 click이벤트를 등록합니다
                kakao.maps.event.addListener(marker, 'click', function() {
                    let conf = confirm('지도에서 지우시면 작성 하시던 일기도 지워집니다. 지우시겠습니까?');

                    if(conf){
                        marker.setMap(null);
                        infowindow.close();
                        $('.place_'+place).remove();
                    }
                });
            }

            let count = 0;

            // // #9 임시 마커 선택 Jquery
            // $(document).on('click', '#listMap > div:eq(0) > div > div:eq(5) > div', function(){
            //     // 임시 기능. marker를 생성할때 div의 특정영역에 생성되고 순차적으로 쌓임 또한 markers의 배열에 입력됨
            //     // 따라서 생성된 div가 몇번째 요소인지 알 수 있다면
            //     // markers 배열에 입력된 해당하는 marker의 정보를 가져 올 수 있음
            //     count = $(this).index();

            //     alert(markers[count]);
            //     console.log(markers[count]);

            //     // 마커 지우기
            //     markers[count].setMap(null);

            //     // 지움과 동시에 markers 배열에서도 빼줘야함
            //     markers.splice(count, 1);
            //     console.log(markers);
            //     infowindow.close();
            // });

            map.setZoomable(false);
            function setZoomable(zoomable) {
                // 마우스 휠로 지도 확대,축소 가능여부를 설정합니다
                map.setZoomable(zoomable);
            }