$(document).ready(function() {


    ajaxAPI("admin/updateMemo", jsonData, "POST").then((response)=> {
        if(response.result > 0){
             alert('메모 작성이 완료되었습니다.');
            $(this).closest('li').find('#d_Memo').hide();
        }
    }).catch((errorMsg)=>{
        console.log(errorMsg);
    });


});