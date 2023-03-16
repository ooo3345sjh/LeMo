let overlays = new Map();

/** 카카오 맵 */
var container = document.getElementById('listMap'); //지도를 담을 영역의 DOM 레퍼런스
var options = { //지도를 생성할 때 필요한 기본 옵션
    center: new kakao.maps.LatLng(33.450701, 126.570667), //지도의 중심좌표.
    disableDoubleClick : true,
    level: 5 //지도의 레벨(확대, 축소 정도)
};

var map = new kakao.maps.Map(container, options); //지도 생성 및 객체 리턴
map.setZoomable(false);
function setZoomable(zoomable) {
    // 마우스 휠로 지도 확대,축소 가능여부를 설정합니다
    map.setZoomable(zoomable);
}

var bounds = new kakao.maps.LatLngBounds();

function closeOverlay(spot_no) {
    let overlay = overlays.get(spot_no);
    overlay.setMap(null);
}

var imageSrc = '/Lemo/images/index/lemo_logo-removebg-preview.png', // 마커이미지의 주소입니다
imageSize = new kakao.maps.Size(40, 60), // 마커이미지의 크기입니다
imageOption = {offset: new kakao.maps.Point(21, 59)}; // 마커이미지의 옵션입니다. 마커의 좌표와 일치시킬 이미지 안에서의 좌표를 설정합니다.

var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption);

positionMap.forEach(function(position, i){

    var marker = new kakao.maps.Marker({
        image : markerImage,
        position : new kakao.maps.LatLng(position.spot_lattitude, position.spot_longtitude)
    });

    marker.setMap(map);

    bounds.extend(marker.getPosition());

    let content = '<div class="spot_wrap">';
       content += '<div class="spot_title">';
       content += position.spot_title
       content += '<div class="spot_close" title="닫기" onclick="closeOverlay('+position.spot_no+')"></div>'
       content += '</div>';
       content += '<div class="spot_content">';
       content += position.arti_title
       content += '</div>';
       content += '</div>';

    var overlay = new kakao.maps.CustomOverlay({
        content: content,
        position: marker.getPosition()
    });

    overlays.set(position.spot_no, overlay);

    kakao.maps.event.addListener(marker, 'click', function() {
        overlays.forEach(function(over){ over.setMap(null); });

        overlay.setMap(map);
    });

});

map.setBounds(bounds);

/* 댓글 모두 보기 */
$(function(){

    /** 카카오맵 - 스크롤 따라 이동 */
    $(window).scroll(function() {

        let currentPosition = parseInt($('#listMap').css('top'));
        let mapHeight = $('#listMap').height();
        let listHeight = $('.list-area').height();
        let boxOffsetTop = $('.list-area').offset().top;

        let scrollTop = $(window).scrollTop();
        let point;
        let endPoint = listHeight - mapHeight;


        if( scrollTop < boxOffsetTop ){
            point = 0;
        }else if( scrollTop > endPoint ) {
            point = endPoint-30;
        }else {
            point = (scrollTop - boxOffsetTop)+100;
        }

        if(point < 0) {point = 0}

        $('#listMap').stop().animate({top: point}, 700);
    });


    /* 글 토글 */
    $('.place').click(function(e){
        e.preventDefault();
        $(this).find('.toggle').slideToggle();
        $(this).find('.toggle').prev().toggleClass('on');
    });

    /* 답 댓글쓰기 팝업 */
    $('.comment').click(function(e){
        e.preventDefault();

        let com_pno = $(this).attr('data-no');

        let length = $('.re'+com_pno).length;

        let content = '<div class="write_reply re'+com_pno+'">';
           content += '<form action="#" class="comWrite">';
           content += '<textarea></textarea>';
           content += '<button data-no='+com_pno+'>댓글 작성</button>';
           content += '</form>';
           content += '</div>';

        if(length == 0) {
            $(this).parent().parent().parent().after(content);
            $(this).text('댓글 쓰기 닫기');
        }else {
            $('.re'+com_pno).remove();
            $(this).text('댓글 쓰기');
        }
    });

    /* 댓글쓰기 */
    $(document).on('submit', '.comWrite', function(e){
        e.preventDefault();
        let comment = $(this).children('textarea').val();
        let com_no  = $(this).children('button').attr('data-no');

        let jsonData = { "com_pno":com_no, "com_comment":comment, "arti_no":arti_no };

        ajaxAPI("diary/rsaveComment", jsonData, "POST").then((response)=>{
            if(response == 1) {
                alert('작성 되었습니다.');

                let content = '<li>';
                   content += '<div class="repdiv1">';
                   content += '<img th:src="@{/images/admin/user_default.png}" alt="프사">';
                   content += '<div>';
                   content += '<strong>[[${comment.nick}]]</strong><span>[[${comment.com_rdate.substring(0,10)}]]</span>';
                   content += '</div>';
                   content += '</div>';
                   content += '<div class="repdiv2">';
                   content += '<textarea readonly>[[${comment.com_comment}]]</textarea>';
                   content += '</div>';
                   content += '<div class="repdiv3">';
                   content += '<div>';
                   content += '<a href="#">수정</a>';
                   content += '<a href="#">삭제</a>';
                   content += '</div>';
                   content += '</div>';
                   content += '</li>';

            }
        });
    });

    /* 댓글 모두보기 팝업 */
    $('.showall').click(function(e){
        e.preventDefault();

        let com_pno = $(this).attr('data-no');

        let status = $(this).hasClass('open');

        if(status) {
            $(this).text('댓글 모두보기');
            $(this).removeClass('open');
            $(this).parent().parent().find('.re_reply').removeClass('open');
            $('.com'+com_pno).attr('style', "display:none;");
        }else {
            $(this).text('댓글 숨기기');
            $(this).addClass('open');
            $(this).parent().parent().find('.re_reply').addClass('open'); // 답댓글 보기
            $('.com'+com_pno).attr('style', "display:block;");
        }
    });
});