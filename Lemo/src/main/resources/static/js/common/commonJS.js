/** 공통 */
/** 현재날짜 */
let now        = new Date();
let nowYear    = now.getFullYear();
let nowMonth   = now.getMonth() + 1;
let nowDate    = now.getDate();
let date       = nowYear + '-' + nowMonth + '-' + nowDate;

/** 스크롤 탑 */
function scrollToTop(){
    $('html, body').animate({ scrollTop : 0 }, 400);
    return false;
}
/** 스크롤 바텀 */
function scrollToBottom(){
    $('html, body').animate({ scrollTop : 2963 }, 600);
    return false;
}

$(function(){
    /** 헤더 메뉴 */
    $('#dropdown').mouseover(function(){
        $('#dropdown_content').addClass('on');	
    });

    $('#dropdown_menu').mouseleave(function(){
        $('#dropdown_content').removeClass('on');
    });

    $('#dropdown2').mouseover(function(){
        $('#dropdown_content2').addClass('on');	
    });
    $('#dropdown_menu2').mouseleave(function(){
        $('#dropdown_content2').removeClass('on');
    }); 
});