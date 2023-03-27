// 관리자 쿠폰 - 등록 select box
function optionChange(){
    let all = [null];
    let type = ['모텔','호텔','펜션','게스트하우스','캠핑·글램핑'];
    let province = ['강원도', '경기도', '경상남도', '경상북도', '광주광역시', '대구광역시', '대전광역시', '부산광역시', '서울특별시', '울산광역시', '인천광역시', '전라남도', '전라북도', '제주특별자치도', '충청남도', '충청북도', '세종특별자치시'];


    let cate1 = $('.category1').val();

    let change;

    switch(cate1){
        case 'ALL':
            change = all;
            break;
        case 'acc_type':
            change = type;
            break;
        case 'acc_province':
            change = province;
            break;
    }

    $('.category2').empty();
    let option;
    //option = $("<option value='' disabled selected>적용항목선택</option>");
    $('.category2').append(option);


    if(cate1 == 'ALL'){
        $('.category2').css('display','inline-block');
        const option = $("<option>").attr("value", null).text('');
        $('.category2').append(option);
    }else if(cate1 == 'acc_type'){
        $('.category2').css('display','inline-block');
        for (let i = 0; i < type.length; i++) {
            const option = $("<option>").attr("value", i+1).text(type[i]);
            $('.category2').append(option);
        }
    }else if (cate1 == 'acc_province'){
        $('.category2').css('display','inline-block');
        for (let i = 0; i < province.length; i++) {
            const option = $("<option>").attr("value", i+1).text(province[i]);
            $('.category2').append(option);
        }
    }

}



