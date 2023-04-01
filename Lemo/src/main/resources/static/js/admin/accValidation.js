/*
* 날짜:2023/03/22
* 내용: 판매자 숙소 등록 유효성 검사
* 이름: 이원정
*/


$(function(){

    let myDropzone = $('div.dropzone');

    let btnSubmit = document.getElementById('btnSubmit');
        let acc_name = $('input[name=acc_name]');
        let acc_addrDetail = $('input[name=province_no]');
        let acc_checkIn = $('input[name=acc_checkIn]');
        let acc_checkOut = $('input[name=acc_checkOut]');
        let sc_no = $('input[name="sc_no"]');
        let acc_info = $('textarea[name=acc_info]');
        let acc_comment = $('input[name=acc_comment]');
        let rp_peakSeason_weekday = $('input[name=rp_peakSeason_weekday]');
        let rp_peakSeason_weekend = $('input[name=rp_peakSeason_weekend]');
        let rp_offSeason_weekday = $('input[name=rp_offSeason_weekday]');
        let rp_offSeason_weekend = $('input[name=rp_offSeason_weekend]');


        btnSubmit.addEventListener('click', function(e){


            // 숙소명
            if(!acc_name.val()) {
                sweetalert("숙소명을 입력해 주십시오.", "warning");
                return false;
            }

             // 주소
            if(!acc_addrDetail.val()) {
                sweetalert("주소 입력 후 '주소 입력' 버튼을 클릭해 주십시오.", "warning");
                return false;
            }

             // 체크인, 체크아웃
            if(!acc_checkIn.val() || !acc_checkOut.val()) {
                sweetalert("체크인 및 체크아웃 시간을 선택해 주십시오.", "warning");
                return false;
            }

           // 숙소 이미지 -> dragNdropMulti.js
           /*if (myDropzone.files == null && myDropzone.files.length == 0) {
               alert("사진을 최소 1장 이상 등록해 주십시오.");
               return false;
           }*/
            /*if (!myDropzone.get(0).files.length) {
                alert("사진을 최소 1장 이상 등록해 주십시오.");
                return false;
            }*/
            if (myDropzone.get(0).dropzone.files == null || myDropzone.get(0).dropzone.files.length == 0) {
                sweetalert("사진을 최소 1장 이상 등록해 주십시오.", "warning");
                return false;
            }

           // 숙소 정보
           if(acc_info.val() == '') {
               sweetalert("숙소 정보를 입력해 주십시오.", "warning");
               return false;
           }

           // 사장님 한마디
           if(!acc_comment.val()) {
               sweetalert("사장님 한마디를 입력해 주십시오.", "warning");
               return false;
           }

            // 할인율 적용
           if (!rp_peakSeason_weekday.val() || !rp_peakSeason_weekend.val() || !rp_offSeason_weekday.val() || !rp_offSeason_weekend.val()) {
                sweetalert("할인율을 입력해 주십시오.", "warning");
                return false;
           }

/*

            Swal.fire({
               title: '숙소가 등록되었습니다.',
               icon: 'success',

               showCancelButton: false, // cancel버튼 보이기. 기본은 원래 없음
               confirmButtonColor: '#bbd444', // confrim 버튼 색깔 지정
               confirmButtonText: '확인', // confirm 버튼 텍스트 지정

            }).then(result => {
               // 만약 Promise리턴을 받으면,
               if (result.isConfirmed) { // 만약 모달창에서 confirm 버튼을 눌렀다면
                  location.href = "list";
               }
            });

*/

        });


});



