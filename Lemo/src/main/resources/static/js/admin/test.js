$(document).ready(function() {


    ajaxAPI("admin/updateMemo", jsonData, "POST").then((response)=> {
        if(response.result > 0){
             alert('메모 작성이 완료되었습니다.');
            $(this).closest('li').find('#d_Memo').hide();
        }
    }).catch((errorMsg)=>{
        console.log(errorMsg);
    });


    function getA(id) {

        $('#id').click(function() {
            $.getJSON('url?pid=' + id, function(data) {
                $('output').each(function(index, dom) { $(dom).text(data.pid); })
            }
        })
    }

});