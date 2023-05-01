
        // qna 선택 삭제
        $('.btnQnaDelete').click(function(e){
            e.preventDefault();

            var checkList = [];

            $('input[name=cs_no]:checked').each(function(){checkList.push($(this).val()); });

            if(checkList == ''){
                Swal.fire({
                    title : '선택된 글이 없습니다',
                    icon : 'error',
                    confirmButtonText : '확인'
                }).then((result) => {
                    if (result.isConfirmed) {
                      location.replace("");
                    }
                  });
                return;
            }
            Swal.fire({
                title: '정말 삭제하시겠습니까?',
                icon: 'question',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: '확인',
                cancelButtonText: '취소',
                reverseButtons: false,
            }).then((result) => {
                if (result.isConfirmed) {
                    console.log("checkList : " + checkList);

                    const jsonData = {"checkList": checkList};

                    console.log("jsonData : " + jsonData);


                    ajaxAPI("admin/cs/qna", jsonData, "Delete").then((response) => {
                        if(response.result > 0) {
                            Swal.fire({
                                title : '해당 글이 삭제되었습니다',
                                icon : 'success',
                                confirmButtonText : '확인'
                            }).then((result) => {
                                if (result.isConfirmed) {
                                  location.replace("");
                                }
                              });
                        }else {
                            alert('error');
                        }
                    }).catch((errMsg) => {
                        console.log(errorMsg);
                    });
                }
            });

        });
