// 드래그앤드롭 파일 업로드

Dropzone.autoDiscover=false;
$(document).ready(function(){

    const myDropzone = new Dropzone('div.dropzone', {
        url: "/Lemo/business/info/rsave",       //업로드할 url (ex)컨트롤러)
        method: 'post',
        headers: {
          // 요청 보낼때 헤더 설정
           'X-CSRF-TOKEN' : $('meta[name="_csrf"]').attr('content') // jwt
        },
        autoProcessQueue: false,                                                    // 자동업로드 여부 (true일 경우, 바로 업로드 되어지며, false일 경우, 서버에는 올라가지 않은 상태임 processQueue() 호출시 올라간다.)
        clickable: true,                                                            // 클릭가능여부
        //autoQueue: false,                                                           // 드래그 드랍 후 바로 서버로 전송
        thumbnailHeight: 90,                                                        // Upload icon size
        thumbnailWidth: 90,                                                         // Upload icon size
        maxFiles: 20,                                                               // 업로드 파일수
        maxFilesize: 100,                                                           // 최대업로드용량 : 100MB
        parallelUploads: 20,                                                        // 동시파일업로드 수(이걸 지정한 수 만큼 여러파일을 한번에 컨트롤러에 넘긴다.)
        addRemoveLinks: true,                                                       // 삭제버튼 표시 여부
        dictRemoveFile: '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;',               // 삭제버튼 표시 텍스트
        uploadMultiple: true,                                                       // 다중업로드 기능
        paramName: 'files',                                                         // 전송받는 파일 파라미터명
        acceptedFiles: '.jpeg,.jpg,.png,.gif,.JPEG,.JPG,.PNG,.GIF',                 // 이미지 파일 포맷만 허용
        dictDefaultMessage: "사진을 첨부하시려면 클릭하거나 드로우 앤 드롭해주세요",
        dictInvalidFileType: "이 파일 형식은 업로드할 수 없습니다.",                         // Set the invalid file type message
        dictFileTooBig: "이 파일이 너무 큽니다. ({{filesize}} MB). 최대 파일 크기는 {{maxFilesize}} MB 입니다.",            // Set the file too big message
        dictResponseError: "서버에서 {{statusCode}} 코드를 받았습니다.",                                               // Set the server response error message
        dictCancelUpload: "업로드를 취소하시겠습니까?",                                                                // Set the cancel upload message
        dictCancelUploadConfirmation: "정말로 이 파일의 업로드를 취소하시겠습니까?",                                       // Set the cancel upload confirmation message
        createImageThumbnails: true, //파일 업로드 썸네일 생성

        init: function(){
            var submitButton = document.querySelector("#btnSubmit");
            var myDropzone = this;

            submitButton.addEventListener("click", function (e) {

                //console.log("업로드1", myDropzone.files);
                console.log("업로드1", myDropzone.files[0]);

                e.preventDefault();

                for(var i=0; i<myDropzone.files.length; i++){
                    console.log("File"+i);
                    console.log(myDropzone.files[i]);
                }

                // 거부된 파일이 있다면
                if (myDropzone.getRejectedFiles().length > 0) {
                    let files = myDropzone.getRejectedFiles();
                    console.log('거부된 파일이 있습니다.', files);
                    return;
                }

                myDropzone.processQueue();

            });

            /*

            var count = 0;

            myDropzone.on("addedfile", function(file) {
                count++;
            });
            */
            let btnSubmit = document.getElementById('btnSubmit');

            /*btnSubmit.addEventListener('click', function(e){
               // 숙소 이미지 -> dragNdropMulti.js
                if (myDropzone.files != null && myDropzone.files.length > 0) {
                    return true;
                }
                else {
                  alert("사진을 최소 1장 이상 등록해 주십시오.");
                  return false;
                }
            });*/




            myDropzone.on("sending", function(file, xhr, formData){
                formData.append("acc_name", $('input[name="acc_name"]').val());
                formData.append("accType_no", $('select[name="accType_no"]').val());
                formData.append("province_no", $('input[name="province_no"]').val());
                formData.append("acc_city", $('input[name="acc_city"]').val());
                formData.append("acc_zip", $('input[name="acc_zip"]').val());
                formData.append("acc_addr", $('input[name="acc_addr"]').val());
                formData.append("acc_addrDetail", $('input[name="acc_addrDetail"]').val());
                formData.append("acc_longtitude", $('input[name="acc_longtitude"]').val());
                formData.append("acc_lattitude", $('input[name="acc_lattitude"]').val());
                formData.append("acc_mainInfo", $('input[name="acc_mainInfo"]').val());
                formData.append("acc_info", $('textarea[name="acc_info"]').val());
                formData.append("acc_comment", $('input[name="acc_comment"]').val());
                formData.append("acc_season", $('select[name="acc_season"]').val());
                formData.append("rp_peakSeason_weekday", $('input[name="rp_peakSeason_weekday"]').val());
                formData.append("rp_peakSeason_weekend", $('input[name="rp_peakSeason_weekend"]').val());
                formData.append("rp_offSeason_weekday", $('input[name="rp_offSeason_weekday"]').val());
                formData.append("rp_offSeason_weekend", $('input[name="rp_offSeason_weekend"]').val());
                formData.append("sc_no", submitRef.join(',') );
                formData.append("acc_checkIn", $('input[name="acc_checkIn"]').val());
                formData.append("acc_checkOut", $('input[name="acc_checkOut"]').val());
            });




            myDropzone.on("success", function(file, response) {
              alert("업로드 완료!");
            });

        },

    });


});