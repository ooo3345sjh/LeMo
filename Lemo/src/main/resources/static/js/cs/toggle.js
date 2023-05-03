/* 글 보기 */
$('.show_list > li a').on('click', function(e){
    e.preventDefault();
    $(this).parent().find('.toggle').slideToggle();
    $(this).parent().find('.list_n').toggleClass('open');
});