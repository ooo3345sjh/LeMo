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

    $('#companySelect').on('change', function() {
        var companyId = $(this).val();

        // 소유 숙소 셀렉트 옵션 추가
        let jsonData = {"user_id": "0hotelthem1@gmail.com", "company_id": companyId};
        ajaxAPI("business/review/findAccOwnedForReview", jsonData, "GET").then((response) => {

            // Clear the selectAccOwned dropdown and add the options
            const selectAccOwned = document.querySelector(".selectAccOwned");
            selectAccOwned.innerHTML = "<option value=''>회사명</option>";
            for (let i = 0; i < response.length; i++) {
                let option = document.createElement("option");
                option.name = "searchAccForReview";
                option.value = response[i].acc_id;
                option.text = response[i].acc_name;
                selectAccOwned.add(option);
            }

        // Clear the selectReview dropdown and add the options
        const selectReview = document.querySelector(".selectReview");
        selectReview.innerHTML = "<option value=''>선택</option>";
        for (let i = 0; i < response.length; i++) {
            let option = document.createElement("option");
            option.value = response[i].acc_id;
            option.text = response[i].acc_name;
            selectReview.add(option);
        }

        // Change event handler for selectAccOwned dropdown
        selectAccOwned.onchange = function() {
            const selectedAccId = this.value;
            // Find the option in selectReview dropdown that matches the selectedAccId
            const options = selectReview.options;
            for (let i = 0; i < options.length; i++) {
            if (options[i].value === selectedAccId) {
                options[i].selected = true;
                break;
            }
          }
        };
        }).catch((errorMsg) => {
            console.log(errorMsg);
        });
    });


});