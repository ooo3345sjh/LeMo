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
    $('.spot').click(function(e){
        e.preventDefault();
        let articleTextarea = $(this).parent().find('.toggle').children('textarea');
        $(this).parent().find('.toggle').slideToggle(600);
        articleTextarea.css('height', ( 15 + articleTextarea.prop('scrollHeight') ) + 'px');
        $(this).parent().find('.toggle').prev().toggleClass('on');
    });

    /* 답 댓글쓰기 팝업 */
    $(document).on('click', '.comment', function(e){
        e.preventDefault();

        let com_no  = $(this).parent().parent().parent().attr('data-no');
        let com_pno = $(this).parent().parent().parent().attr('data-pno');

        let emptyCom = $('.emptyCom'+com_no).length;

        if( emptyCom == 1 ) {
            $('.emptyCom'+com_no).remove();
            $(this).parent().find('.showall').removeClass('open');
            $(this).parent().find('.showall').text('댓글 모두보기');
        }

        let content = '<div class="write_reReply re'+com_pno+'">';
           content += '<form action="#" class="comWrite">';
           content += '<textarea class="comTextarea" onkeydown="resizeHeight(this)" onkeyup="resizeHeight(this)"></textarea>';
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
    $(document).on('submit', '.comOriWrite', function(e){
        e.preventDefault();
        let comment = $(this).children('textarea').val();

        let jsonData = { "arti_no":arti_no , "com_comment":comment }

        ajaxAPI("diary/oriComment", jsonData, "POST").then((response)=>{
            if(response["result"] == '1'){
                Swal.fire(`작성되었습니다.!`)

                let content = '<li data-no="'+response["com_no"]+'" data-pno="'+response["com_no"]+'" class="oriCom'+response["com_no"]+'">';
                   content += '<div class="repdiv1">';
                   if(response["photo"] == null ) {
                    content += '<img src="/Lemo/images/my/profile.png" alt="프사">';
                   }else {
                    content += '<img src="/Lemo/img/profile/'+response["photo"]+'" alt="프사">';
                   }
                   content += '<div>';
                   content += '<strong>'+response["nick"]+'</strong><span style="margin-left: 10px;">방금 전</span>';
                   content += '</div>';
                   content += '</div>';
                   content += '<div class="repdiv2">';
                   content += '<textarea readonly class="comTextarea" onkeydown="resizeHeight(this)" onkeyup="resizeHeight(this)">'+comment+'</textarea>';
                   content += '</div>';
                   content += '<div class="repdiv3">';
                   content += '<div>';
                   content += '<a href="#" class="showall">댓글 모두보기</a>';
                   content += '<a href="#" class="comment">댓글 쓰기</a>'
                   content += '</div>';
                   content += '<div>';
                   content += '<a href="#" class="comModify">수정</a> ';
                   content += '<a href="#" class="comDelete">삭제</a>';
                   content += '</div>';
                   content += '</div>';
                   content += '</li>';

                if($('.commentEmpty').length == 1) { $('.commentEmpty').remove(); }

                total += 1
                $('#tit').children('b').text(total);
                $(this).parent().parent().find('#rep').children().append(content);
                let com_pno_textarea = $('.oriCom'+response["com_no"]+' .comTextarea');
                com_pno_textarea.each(function(index, height){ resizeHeight(height); });

                $(this).children('textarea').val('');
                $(this).children('textarea').css('height', '76px');

            }
        });
    })

    /* 대댓글쓰기 */
    $(document).on('submit', '.comWrite', function(e){
        e.preventDefault();
        let comment = $(this).children('textarea').val();
        let com_no  = $(this).children('button').attr('data-no');
        let com_pno = $(this).children('button').attr('data-pno');

        let re_reply = $('.com'+com_no).length;

        let jsonData = { "com_pno":com_pno, "com_comment":comment, "arti_no":arti_no, "com_no":com_no };

        ajaxAPI("diary/comment", jsonData, "POST").then((response)=>{

            let content = '<li class="re_reply com'+com_pno+'" style="display:block;" data-no="'+com_no+'" data-pno="'+response["com_pno"]+'">';
               content += '<div class="repdiv1">';
               if(response["photo"] == null ) {
                content += '<img src="/Lemo/images/my/profile.png" alt="프사">';
               }else {
                content += '<img src="/Lemo/img/profile/'+response["photo"]+'" alt="프사">';
               }
               content += '<div>';
               content += '<strong>'+response["nick"]+'</strong><span style="margin-left: 10px;">방금 전</span>';
               content += '</div>';
               content += '</div>';
               content += '<div class="repdiv2">';
               content += '<p class="reply_id">@'+response["com_nick"]+'</p>'
               content += '<textarea readonly class="comTextarea" onkeydown="resizeHeight(this)" onkeyup="resizeHeight(this)">'+comment+'</textarea>';
               content += '</div>';
               content += '<div class="repdiv3">';
               content += '<div><a href="#" class="comment">댓글 쓰기</a></div>';
               content += '<div>';
               content += '<a href="#" class="comModify">수정</a> ';
               content += '<a href="#" class="comDelete">삭제</a>';
               content += '</div>';
               content += '</div>';
               content += '</li>';

            let commentWrite = $(this).parent().prev().find('.comment');
            let commentHide  = $(this).parent().prev().find('.showall');

            $('.com'+com_no).attr('style', "display:block;");
            $(this).parent().prev().after(content);
            let com_pno_textarea = $('.com'+com_no+' .comTextarea');
            com_pno_textarea.each(function(index, height){ resizeHeight(height); });
            $(this).parent().parent().find('.emptyCom'+com_pno).remove();
            commentWrite.removeClass('open');
            commentWrite.text('댓글 쓰기');
            commentHide.text('댓글 숨기기');
            commentHide.addClass('open');
            $(this).parent().remove();

            total += 1
            $('#tit').children('b').text(total);
        });
    });

    /* 댓글 모두보기 팝업 */
    $(document).on('click', '.showall', function(e){
        e.preventDefault();

        let com_pno = $(this).parent().parent().parent().attr('data-no');

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
            $('.com'+com_pno).attr('style', "display:none;");
        }else {
            $(this).text('댓글 숨기기');
            $(this).addClass('open');
            $('.com'+com_pno).attr('style', "display:block;");

            let com_pno_textarea = $('.com'+com_pno+' .comTextarea');

            com_pno_textarea.each(function(index, height){ resizeHeight(height); });

        }
    });

    /* 댓글 삭제 */
    $(document).on('click', '.comDelete', function(e){
        e.preventDefault();
        let com_no  = $(this).parent().parent().parent().attr('data-no');
        let com_pno = $(this).parent().parent().parent().attr('data-pno');

        let jsonData;
        if( $(this).parent().parent().parent().hasClass('oriCom'+com_no) ) {
            jsonData = { "com_no":com_pno, "com_state":0 };
            ajaxAPI("diary/oriComment", jsonData, "PATCH").then((response)=>{
                if(response == 1) {
                    Swal.fire(`삭제 되었습니다.!`)

                    let content = "<div class='removeCom'>";
                       content +=   "<p>삭제된 댓글입니다.</p>";
                       content +=   "<div>";
                       content +=       "<a href='#' class='showall open'>댓글 숨기기</a>";
                       content +=   "</div>";
                       content += "</div>";

                    $(this).parent().parent().parent().empty().append(content);

                }else {
                    Swal.fire(`다시 시도해주세요.!`)
                }
            });
        }else {
            jsonData = { "com_no":com_pno };
            ajaxAPI("diary/comment", jsonData, "DELETE").then((response)=>{
                if(response == 1) {
                    Swal.fire(`삭제 되었습니다.!`)
                    total -= 1
                    $('#tit').children('b').text(total);
                    $(this).parent().parent().parent().remove();
                }else {
                    Swal.fire(`다시 시도해주세요.!`)
                }
            });
        }
    });

    let oriContent;
    /* 댓글 수정 */
    $(document).on('click', '.comModify', function(e){
        e.preventDefault();

        let textarea = $(this).parent().parent().prev().children('textarea');
        oriContent = textarea.val();
        textarea.removeAttr('readonly');
        textarea.css('background', '#f9f8e0');
        textarea.css('padding', '5px');
        textarea.css('height', ( 15 + textarea.attr('scrollHeight') ) + 'px')
        $(this).text("취소");
        $(this).attr("class","comCancel");
        $(this).next().text("수정");
        $(this).next().attr("class","comSubmit");
    });

    /* 댓글 수정 취소 */
    $(document).on('click', '.comCancel', function(e){
        e.preventDefault();
        let textarea = $(this).parent().parent().prev().children('textarea');
        textarea.attr('readonly', true);
        textarea.css("background", "");
        textarea.css("padding", "");
        textarea.css("height", ( 15 + textarea.attr('scrollHeight') ) + 'px')
        $(this).text("수정");
        $(this).attr("class","comModify");
        $(this).next().text("삭제");
        $(this).next().attr("class","comDelete");
        textarea.val(oriContent);
    });

    /* 댓글 수정 */
    $(document).on('click', '.comSubmit', function(e){
        e.preventDefault();

        let textarea = $(this).parent().parent().prev().children('textarea');
        let com_pno     = $(this).parent().parent().parent().attr('data-pno');
        let com_comment = textarea.val();
        let modified = $(this).parent().parent().parent().find('.repdiv1');

        let jsonData = { "com_no":com_pno, "com_comment":com_comment };

        ajaxAPI("diary/comment", jsonData, "PATCH").then((response)=>{
            if(response == 1) {
                Swal.fire(`수정 되었습니다.`)

                textarea.attr('readonly', true);
                textarea.css('height', ( 15 + textarea.attr('scrollHeight') ) + 'px')
                textarea.css("background", "");
                textarea.css("padding", "");
                $(this).text("삭제");
                $(this).attr("class","comDelete");
                $(this).prev().text("수정");
                $(this).prev().attr("class","comModify");
                textarea.val(com_comment);

                if (modified.find('.comModified').length == 0 ) {
                    let content = "<span class='comModified'>&nbsp;수정됨&nbsp;</span>";
                    modified.children('div').append(content);
                }
            }else {
                Swal.fire(`다시 시도해주세요.`)
            }
        });
    });

    const urlParams = new URLSearchParams(window.location.search);
    const arti_no = urlParams.get('arti_no');
    /* 여행일기 삭제 */
    $('.diaryDelete').click(function(){
        let jsonData = { "arti_no":arti_no }

        ajaxAPI("diary/article", jsonData, "DELETE").then((response)=>{
            if(response == 1) {
                Swal.fire({
                    title: '삭제 되었습니다.',
                    icon: 'info',
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: '확인',
                    reverseButtons: false,
                }).then(result => {
                    location.href='/Lemo/my/diary/list';
                });
            }else {
                Swal.fire( '다시 시도해주세요.' )
            }
        });
    });
});