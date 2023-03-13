// 관리자 쿠폰 - 등록 select box
function optionChange(){
    let all = ['전체적용'];
    let type = ['모텔','호텔·리조트','펜션','게스트하우스','캠핑·글램핑','해외여행'];
    let province = ['서울','경역','인천','강원','제주','부산','경남','대구','경북','울산','대전','충남','충북','광주','전남','전북'];

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
    option = $("<option value='' disabled selected>적용항목선택</option>");
    $('.category2').append(option);


    if(cate1 == 'ALL'){
        for (let i = 0; i < all.length; i++) {
            const option = $("<option>").attr("value", all[i]).text(all[i]);
            $('.category2').append(option);
        }
    }else if(cate1 == 'acc_type'){
        for (let i = 0; i < type.length; i++) {
            const option = $("<option>").attr("value", type[i]).text(type[i]);
            $('.category2').append(option);
        }
    }else if (cate1 == 'acc_province'){
        for (let i = 0; i < province.length; i++) {
            const option = $("<option>").attr("value", province[i]).text(province[i]);
            $('.category2').append(option);
        }
    }

}


