// cs_type 선택
        $(document).ready(function(){
              let cs_type = $('.stayName').find("select[name=cs_type]").val();
              // = document.getElementById('faqCs_type').value;
              console.log(cs_type);

              $('#cs_typeForm').submit(function(e){
                if( cs_type == '0'){
                        Swal.fire({
                             title : '유형을 선택해주세요',
                             icon : 'success',
                             confirmButtonText : '확인'
                         })
                        e.preventDefault();
                    }
              });

            $('select[name=cs_type]').change(function(){
                console.log("select 값 확인" +$(this).val());
                cs_type = $(this).val();
                $('#cs_typeForm').submit(function(e){
                    if( cs_type == '0'){
                            Swal.fire({
                                 title : '유형을 선택해주세요',
                                 icon : 'success',
                                 confirmButtonText : '확인'
                             })
                            e.preventDefault();
                        }
                });
            });
        });
       $(document).ready(function() {
            // 타이틀 포커스
            const cs_title = document.getElementById('cs_title');
            cs_title.focus();
       });

       // 수정완료클릭시 확인
        function modifyCheck(e) {
          e.preventDefault(); // form submit 막음
          console.log("1");
          Swal.fire({
            title: '정말 수정하시겠습니까?',
            icon: 'question',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '승인',
            cancelButtonText: '취소',
            reverseButtons: false,
          }).then((result) => {
            if (result.isConfirmed) {
              e.target.submit(); // form submit 실행
            }
          });
        }