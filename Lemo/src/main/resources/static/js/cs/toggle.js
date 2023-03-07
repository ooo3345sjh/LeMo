/* 글 보기 */
$('.show_list > li').on('click', function(e){
    e.preventDefault();
    $(this).find('.toggle').slideToggle();
    $(this).find('.list_n').toggleClass('open');
});