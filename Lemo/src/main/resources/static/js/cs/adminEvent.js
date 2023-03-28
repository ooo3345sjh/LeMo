$(document).ready(function(){

            $('.btnAct').on('click', function(){

                let status = $(this).hasClass('on');
                if(!status){
                    $(this).addClass('on');
                    $('.btnDeAct').removeClass('on');
                }
            });

            $('.btnDeAct').on('click', function(){

                let status = $(this).hasClass('on');
                if(!status){
                    $(this).addClass('on');
                    $('.btnAct').removeClass('on');
                }
            });

             // event 활성화
            $('.btnAct').click(function(e){
                e.preventDefault();

                Swal.fire({
                    title: '해당 글을 활성화하시겠습니까?',
                    icon: 'question',

                    showCancelButton: true, // cancel버튼 보이기. 기본은 원래 없음
                    confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
                    cancelButtonColor: '#d33', // cancel 버튼 색깔 지정
                    confirmButtonText: '승인', // confirm 버튼 텍스트 지정
                    cancelButtonText: '취소', // cancel 버튼 텍스트 지정

                    reverseButtons: true,
                }).then(result => {
                    if(result.isConfirmed){
                        let cs_no = $(this).attr('data-no');

                        console.log('cs_no ajax : ' + cs_no);

                        const jsonData = {"cs_no": cs_no};

                        ajaxAPI("admin/cs/event/eventOn", jsonData, "post").then((response) => {
                            if(response.result > 0) {
                                Swal.fire('해당 글이 활성화되었습니다', 'success');
                                location.replace("/Lemo/admin/cs/event/list");
                            }else {
                                alert('error');
                            }
                        }).catch((errMsg) => {
                            console.log(errorMsg);
                        });
                    }

                });
            });

             // event 비활성화
            $('.btnDeAct').click(function(e){
                e.preventDefault();

                let isChangeOK = confirm('해당 글을 비활성화시키겠습니까?');

                if(isChangeOK){
                    let cs_no = $(this).attr('data-no');

                    console.log('cs_no ajax : ' + cs_no);

                    const jsonData = {"cs_no": cs_no};

                    ajaxAPI("admin/cs/event/eventEnd", jsonData, "post").then((response) => {
                        if(response.result > 0) {
                            alert("해당 글이 비활성화되었습니다.");
                            location.replace("/Lemo/admin/cs/event/list");
                        }else {
                            alert('error');
                        }
                    }).catch((errMsg) => {
                        console.log(errorMsg);
                    });
                }

            });


           // event 삭제
            $('.btnEventDelete').click(function(e){
                e.preventDefault();

                let isDeleteOK = confirm('해당 글을 삭제하시겠습니까?');

                if(isDeleteOK){
                    let cs_no = $(this).attr('data-no');

                    console.log('cs_no ajax : ' + cs_no);

                    const jsonData = {"cs_no": cs_no};

                    ajaxAPI("admin/cs/event/articleRemove", jsonData, "delete").then((response) => {
                        if(response.result > 0) {
                            alert("해당 글이 삭제되었습니다.");
                            location.replace("/Lemo/admin/cs/event/list");
                        }else {
                            alert('error');
                        }
                    }).catch((errMsg) => {
                        console.log(errorMsg);
                    });
                }
            });
        });