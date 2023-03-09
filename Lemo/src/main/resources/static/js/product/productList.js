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



});