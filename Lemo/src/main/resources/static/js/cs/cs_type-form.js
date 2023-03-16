$(document).ready(function(){
              let cs_type = $('.title').find("select[name=cs_type]").val();
              // = document.getElementById('faqCs_type').value;
              console.log(cs_type);

              $('#cs_typeForm').submit(function(e){

                    if( cs_type == '0'){
                            alert('유형을 선택해주세요');
                            e.preventDefault();
                        }
                    });

              $('select[name=cs_type]').change(function(){
                    console.log("select 값 확인" +$(this).val());
                    cs_type = $(this).val();
                    $('#cs_typeForm').submit(function(e){

                    if( cs_type == '0'){
                            alert('유형을 선택해주세요');
                            e.preventDefault();
                        }
                    });
              });
        });