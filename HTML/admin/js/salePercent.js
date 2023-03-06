// 할인율 적용 계산
/*
function insertSalePercent(){
    let OriPrice = $(this).parent().prev().children().val();

    let percent = $(this).val();
    
    console.log('OriPrice: '+OriPrice);
    console.log('percent: '+percent);

    let SalePrice = OriPrice - (OriPrice * (percent/100));
    console.log('SalePrice: '+SalePrice);

    $(this).parent().next().text(SalePrice);
}
*/


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

// 성수기 주중 
$(document).ready(function(){
    $('#insertSalePercent').change(function(){
        let OriPrice = $('#insertPrice').val();
        let percent = $(this).val();

        let SalePrice = OriPrice - (OriPrice * (percent/100));
        $('#priceResult').text(SalePrice);
    });
});

// 성수기 주말
$(document).ready(function(){
    $('#insertSalePercent2').change(function(){
        let OriPrice = $('#insertPrice2').val();
        let percent = $(this).val();

        let SalePrice = OriPrice - (OriPrice * (percent/100));
        $('#priceResult2').text(SalePrice);
    });
});

// 비성수기 주중 
$(document).ready(function(){
    $('#insertSalePercent3').change(function(){
        let OriPrice = $('#insertPrice3').val();
        let percent = $(this).val();

        let SalePrice = OriPrice - (OriPrice * (percent/100));
        $('#priceResult3').text(SalePrice);
    });
});

// 비성수기 주말
$(document).ready(function(){
    $('#insertSalePercent4').change(function(){
        let OriPrice = $('#insertPrice4').val();
        let percent = $(this).val();

        let SalePrice = OriPrice - (OriPrice * (percent/100));
        $('#priceResult4').text(SalePrice);
    });
});
