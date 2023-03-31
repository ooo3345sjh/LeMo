// faq 삭제
        $('.btnFaqDelete').click(function(e){
            e.preventDefault();
                Swal.fire({
                    title: '해당 글을 활성화하시겠습니까?',
                    icon: 'question',

                    showCancelButton: true, // cancel버튼 보이기. 기본은 원래 없음
                    confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
                    cancelButtonColor: '#d33', // cancel 버튼 색깔 지정
                    confirmButtonText: '승인', // confirm 버튼 텍스트 지정
                    cancelButtonText: '취소', // cancel 버튼 텍스트 지정

                    reverseButtons: false,
                }).then(result => {
                    if(result.isConfirmed){
                        let cs_no = $(this).attr('data-no');

                        console.log('cs_no ajax : ' + cs_no);

                        const jsonData = {"cs_no": cs_no};

                        ajaxAPI("admin/cs/faq/articleRemove", jsonData, "delete").then((response) => {
                            if(response.result > 0) {
                                Swal.fire({
                                        title : '글이 삭제되었습니다.',
                                        icon : 'success',
                                        confirmButtonText : '확인'
                                    })
                                location.replace("/Lemo/admin/cs/faq/list");
                            }else {
                                alert('error');
                            }
                        }).catch((errMsg) => {
                            console.log(errorMsg);
                        });
                    }
                });
        });