// 할인율 적용 계산
function insertSalePercent(){
    let OriPrice = $(this).parent().prev().children().val();

    let percent = $(this).val();
    
    console.log('OriPrice: '+OriPrice);
    console.log('percent: '+percent);

    let SalePrice = OriPrice - (OriPrice * (percent/100));
    console.log('SalePrice: '+SalePrice);

    $(this).parent().next().text(SalePrice);
}

/*
$(document).ready( function() {
    $("#insertSalePercent").change(function () { 
        
        
        let OriPrice = $(this).parent().prev().children().val();

        let percent = $(this).val();
        
        console.log('OriPrice: '+OriPrice);
        console.log('percent: '+percent);

        let SalePrice = OriPrice - (OriPrice * (percent/100));
        console.log('SalePrice: '+SalePrice);

        $(this).parent().next().text(SalePrice);
    });
});
*/