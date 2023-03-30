let fileInputList = [];
let count = 1;
$(function(){
    /* 글 토글 */
    $(document).on('click', '.spot', function(e){
        e.stopPropagation();

        $(this).parent().find('.toggle').slideToggle(600);

        let currentPosition = parseInt($('#listMap').css('top'));

        /** 토글 + 화살표 변경 */
        const arrow = $(this).parent().find('.arrow');
        if(arrow.attr('class') == 'arrow'){
            arrow.addClass('on');
        }else if(arrow.attr('class') == 'arrow on'){
            arrow.removeClass('on');
        }
    });

    /* 내용 input */
    $(document).on('click', '.place > .toggle > .contentArea', function(e){
        e.preventDefault();
        e.stopPropagation();
    });

    /** 이미지 미리보기 **/
    const fileDOM = $('.ex_file');
    const image = $('.image');

    $(document).on('click', '.ex_file', function(e){
        e.stopPropagation();

        // 서정현 TEST
        fileInputList.push($(this));
        let inputFile = $(this).parent('.image');
        $(this).change(function(e){
            let preview = $('.inputImage', inputFile);
            const reader = new FileReader();
            var oFile = $(this)[0].files;

            if(oFile.length < 1){
                preview.attr('src', '/Lemo/images/diary/imgUpload.png');
            }else {
                reader.readAsDataURL(e.target.files[0]);
                reader.onload = function(event){
                    preview.attr('src', event.target.result);
                };
            }
        });
    })

    /* 사진 클릭시 파일 업로드 화면 띄우기 */
    $(document).on('click', '.image', function(e){
        e.stopPropagation();
        $(this).find('input[type=file]').trigger("click");
    })
});