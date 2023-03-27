
        // qna 선택 삭제
        $('.btnQnaDelete').click(function(e){
            e.preventDefault();

            var checkList = [];

            $('input[name=cs_no]:checked').each(function(){checkList.push($(this).val()); });

            if(checkList == ''){
                alert('선택된 상품이 없습니다');

                return;
            }

            console.log("checkList : " + checkList);

            const jsonData = {"checkList": checkList};

            console.log("jsonData : " + jsonData);

            ajaxAPI("admin/cs/qna/listRemove", jsonData, "delete").then((response) => {
                if(response.result > 0) {
                    alert("해당 글이 삭제되었습니다.");
                    location.replace("/Lemo/admin/cs/qna/list");
                }else {
                    alert('error');
                }
            }).catch((errMsg) => {
                console.log(errorMsg);
            });

        });
