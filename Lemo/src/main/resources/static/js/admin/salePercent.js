// 할인율 적용 계산

document.addEventListener('DOMContentLoaded', function() {
    const inputs = document.querySelectorAll('input[type="number"]');

    console.log('inputs length: ' + inputs.length);
    
    for (let i = 0; i <inputs.length; i++) {
        inputs[i].addEventListener('input',updateResult);
    }

    function updateResult() {
        const parent = this.parentNode.parentNode; 
        const value1 = parent.querySelector('#input1').value;
        const value2 = parent.querySelector('#input2').value; 
        const resultInput = parent.querySelector('#result');

        // const result = Number(value1) + Number(value2);
        const result = Number(value1) - (Number(value1) * (Number(value2)/100));
        resultInput.value = result;
    }
});

/*
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
*/