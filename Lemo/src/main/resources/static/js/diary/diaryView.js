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
    $(document).on('click', '.comment', function(e){
        e.preventDefault();

        let com_no  = $(this).attr('data-no');
        let com_pno = $(this).attr('data-pno');

        let content = '<div class="write_reReply re'+com_pno+'">';
           content += '<form action="#" class="comWrite">';
           content += '<textarea></textarea>';
           content += '<button data-pno='+com_no+' data-no='+com_pno+'>댓글 작성</button>';
           content += '</form>';
           content += '</div>';

        let status = $(this).hasClass('open');

        if(status){
            $(this).parent().parent().parent().next().remove();
            $(this).removeClass('open');
            $(this).text('댓글 쓰기');
        }else {
            $(this).parent().parent().parent().after(content);
            $(this).addClass('open');
            $(this).text('댓글 쓰기 닫기');
        }
    })

    /* 댓글쓰기 */
    $(document).on('submit', '.comWrite', function(e){
        e.preventDefault();
        let comment = $(this).children('textarea').val();
        let com_no  = $(this).children('button').attr('data-no');
        let com_pno = $(this).children('button').attr('data-pno');

        console.log(com_no);
        console.log(com_pno);
        let re_reply = $('.com'+com_no).length;

        let jsonData = { "com_pno":com_pno, "com_comment":comment, "arti_no":arti_no, "com_no":com_pno };

        ajaxAPI("diary/comment", jsonData, "POST").then((response)=>{

            let content = '<li class="re_reply com'+com_pno+'" style="display:block;">';
               content += '<div class="repdiv1">';
               content += '<img src="/Lemo/images/admin/user_default.png" alt="프사">';
               content += '<div>';
               content += '<strong>'+response["nick"]+'</strong><span>'+date+'</span>';
               content += '</div>';
               content += '</div>';
               content += '<div class="repdiv2">';
               content += '<p class="reply_id">@'+response["com_nick"]+'</p>'
               content += '<textarea readonly>'+comment+'</textarea>';
               content += '</div>';
               content += '<div class="repdiv3">';
               content += '<div><a href="#" class="comment" data-no="'+com_no+'" data-pno="'+response["com_pno"]+'">댓글 쓰기</a></div>';
               content += '<div>';
               content += '<a href="#" data-no="'+com_no+'" data-pno="'+response["com_pno"]+'">수정</a>';
               content += '<a href="#" data-no="'+com_no+'" data-pno="'+response["com_pno"]+'">삭제</a>';
               content += '</div>';
               content += '</div>';
               content += '</li>';

            if(re_reply == 0){
                $(this).parent().after(content);
                $(this).parent().prev().find('.comment').removeClass('open');
                $(this).parent().prev().find('.comment').text('댓글 쓰기');
                $(this).parent().next('.emptyCom').remove();
                $(this).parent().remove();
                //$(this).parent().next('.emptyCom').remove();
            }else {
                $('.com'+com_no+':eq('+ (re_reply - 1) +')').after(content);
                $(this).parent().prev().find('.comment').removeClass('open');
                $(this).parent().prev().find('.comment').text('댓글 쓰기');
                $(this).parent().next('.emptyCom'+com_pno).remove();
                $(this).parent().remove();
            }

        });
    });

    /* 댓글 모두보기 팝업 */
    $('.showall').click(function(e){
        e.preventDefault();

        let com_pno = $(this).attr('data-no');

        let status = $(this).hasClass('open');

        let content = '<li class="re_reply com'+com_pno+' emptyCom'+com_pno+' style="display:block; font-size:12px; padding-bottom:15px;">등록된 답글이 없습니다.</li>';

        if($('.com'+com_pno).length == 0) {
            if($('.re'+com_pno).length == 1) {
                $(this).parent().parent().parent().next().after(content)
            }else if($('.re'+com_pno).length == 0){
                $(this).parent().parent().parent().after(content);
            }
        }

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

    $(document).on('click', '.comDelete', function(e){
        e.preventDefault();
        let com_no  = $(this).attr('data-no');
        let com_pno = $(this).attr('data-pno');

        let jsonData = { "com_no":com_no };

        ajaxAPI("diary/comment", jsonData, "DELETE").then((response)=>{
            if(response == 1) {
                alert('삭제 되었습니다.');

                $(this).parent().parent().parent().remove();
            }else {
                alert('다시 시도해주세요!');
            }
        });

    });

    $(document).on('click', '.comModify', function(e){
        e.preventDefault();

        let textarea = $(this).parent().parent().prev().children();
        textarea.removeAttr('readonly');
    });
});