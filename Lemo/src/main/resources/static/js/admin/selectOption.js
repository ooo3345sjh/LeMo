// 관리자 쿠폰 - 등록 select box
function optionChange(){
    let acc_all = ['전체적용'];
    let acc_type = ['모텔','호텔·리조트','펜션','게스트하우스','캠핑·글램핑','해외여행'];
    let acc_province = ['서울','경역','인천','강원','제주','부산','경남','대구','경북','울산','대전','충남','충북','광주','전남','전북'];

    let cate1 = $('.category1').val();

    let change;

    switch(cate1){
        case '0':
            change = acc_all;
            break;
        case '1':
            change = acc_type;
            break;
        case '2':
            change = acc_province;
            break;
    }

    $('.category2').empty();
    let option;
    option = $("<option value='' disabled selected>-</option>");
    $('.category2').append(option);

    for (let i=0; i < change.length; i++){
        option = $("<option value="+[i]+">"+change[i]+"</option>");
        $('.category2').append(option);
    }
}


