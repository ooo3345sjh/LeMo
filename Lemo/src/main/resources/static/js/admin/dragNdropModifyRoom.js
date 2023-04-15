// imgsrc(경로)를 base64로 인코딩
const toDataURL = url => fetch(url)
  .then(response => response.blob())
  .then(blob => new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.onloadend = () => resolve(reader.result)
    reader.onerror = reject
    reader.readAsDataURL(blob)
  }))

// base64를 파일로 변환
function base64toFile(base_data, filename) {
    var arr = base_data.split(','),
        mime = arr[0].match(/:(.*?);/)[1],
        bstr = atob(arr[1]),
        n = bstr.length,
        u8arr = new Uint8Array(n);

    while(n--){
        u8arr[n] = bstr.charCodeAt(n);
    }

    return new File([u8arr], filename, {type:mime});
}

// formData 생성
let usaveFormData = new FormData();

// 드래그앤드롭 파일 업로드
Dropzone.autoDiscover=false;
$(document).ready(function(){

    const myDropzone = new Dropzone('div.dropzone', {
        url: "/Lemo/business/room/modify",       //업로드할 url (ex)컨트롤러)
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

            if(roomThumbs != null) {

                const thumbs = roomThumbs.split('/');

                for(thumb in thumbs) {

                    let imgSrc = '/Lemo/img/product/'+province_no+'/'+acc_id+'/'+thumbs[thumb];
                    let dataURL;
                    let fileName = thumbs[thumb];

                    var mockFile = {
                        name: fileName,
                        size: 100,
                        type: 'image/jpeg',
                        url: fileName,
                        accepted: true
                    }

                    // imgSrc로 bsse64를 생성하고 base64로 파일을 생성한 다음 formdata에 append
                    toDataURL(imgSrc).then(dataUrl => {
                        var oriFile = base64toFile(dataUrl, fileName);
                        usaveFormData.append(fileName, oriFile);
                    });

                    // 가지고있는 이미지를 dropzone에 표기
                    myDropzone.displayExistingFile(mockFile, imgSrc);

                    // files에 push를 해줘야 최대 업로드 수 등등 dropzone 기능 온전히 사용가능
                    myDropzone.files.push(mockFile);

                }

                // 이미지를 css로 90px로 변경
                {
                    $('[data-dz-thumbnail]').css('height', '90');
                    $('[data-dz-thumbnail]').css('width', '90');
                    $('[data-dz-thumbnail]').css('object-fit', 'cover');
                };

            }

            // 이미지 추가
            myDropzone.on("addedfile", function(file) {
                let fileLength = myDropzone.files.length;
                if(fileLength <= 5) {
                    usaveFormData.append(file.name, file);
                }
            });

            // 이미지 삭제
            myDropzone.on("removedfile", function(file) {
                usaveFormData.delete(file.name);
            });


            // 서버에 제출

            let btnSubmit = document.getElementById('btnSubmit');

            btnSubmit.addEventListener('click', function (e) {

                e.preventDefault();

                // 추가 input 데이터
                let room_name = $('input[name="room_name"]').val();
                let room_stock = $('input[name="room_stock"]').val();
                let room_price = $('input[name="room_price"]').val();
                let room_defNum = $('input[name="room_defNum"]').val();
                let room_maxNum = $('input[name="room_maxNum"]').val();
                let room_addPrice = $('input[name="room_addPrice"]').val();
                let room_info = $('textarea[name="room_info"]').val();
                let room_checkIn = $('input[name="room_checkIn"]').val();
                let room_checkOut = $('input[name="room_checkOut"]').val();

                if(!room_name) {
                    sweetalert("객실명을 입력해 주십시오.", "warning");
                    return false;
                }

                if(!room_stock) {
                    sweetalert("객실 수를 입력해 주십시오.", "warning");
                    return false;
                }

                if(!room_price) {
                    sweetalert("객실 가격을 입력해 주십시오.", "warning");
                    return false;
                }

                if(!room_defNum || !room_maxNum) {
                    sweetalert("객실 수용 인원을 입력해 주십시오.", "warning");
                    return false;
                }

                if(!room_addPrice) {
                    sweetalert("인원 추가 가격을 입력해 주십시오.", "warning");
                    return false;
                }

                if(!room_info) {
                    sweetalert("객실 이용안내를 입력해 주십시오.", "warning");
                    return false;
                }

                // 나머지 input value append
                usaveFormData.append("room_name", room_name);
                usaveFormData.append("room_stock", room_stock);
                usaveFormData.append("room_price", room_price);
                usaveFormData.append("room_defNum", room_defNum);
                usaveFormData.append("room_maxNum", room_maxNum);
                usaveFormData.append("room_addPrice", room_addPrice);
                usaveFormData.append("room_info", room_info);
                usaveFormData.append("room_checkIn", room_checkIn);
                usaveFormData.append("room_checkOut", room_checkOut);
                usaveFormData.append("province_no", province_no);
                usaveFormData.append("acc_id", acc_id);
                usaveFormData.append("room_thumbs", roomThumbs);
                usaveFormData.append("room_id", room_id);

                $.ajax({
                    url: '/Lemo/business/room/modify',
                    method: 'POST',
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader(header, token);
                    },
                    data: usaveFormData,
                    contentType: false,
                    processData: false,
                    enctype : 'multipart/form-data',
                    success: function(data) {

                        if(data == 0) {
                            sweetalert("객실 수정에 실패하였습니다.\n잠시 후 다시 시도해 주세요.")
                        }else if(data == 1) {
                            location.href= "/Lemo/business/room/view?room_id="+room_id;
                        }
                    }
                });

            });




        },

    });


});