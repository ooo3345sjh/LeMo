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
        url: "/Lemo/business/info/usave",       //업로드할 url (ex)컨트롤러)
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

            if(accThumbs != null) {

                const thumbs = accThumbs.split('/');

                for(thumb in thumbs) {

                    let imgSrc = '/Lemo/img/product/'+provinceNo+'/'+acc_id+'/'+thumbs[thumb];
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
                let myDropzone = $('div.dropzone');

                let acc_name = $('input[name=acc_name]').val();
                let accType_no = $('select[name="accType_no"]').val();
                let acc_city = $('input[name="acc_city"]').val();
                let province_no = $('input[name="province_no"]').val();
                let acc_zip = $('input[name="acc_zip"]').val();
                let acc_addr = $('input[name="acc_addr"]').val();
                let acc_addrDetail = $('input[name=acc_addrDetail]').val();
                let acc_longtitude = $('input[name="acc_longtitude"]').val();
                let acc_lattitude = $('input[name="acc_lattitude"]').val();
                let acc_checkIn = $('input[name=acc_checkIn]').val();
                let acc_checkOut = $('input[name=acc_checkOut]').val();
                let sc_no = submitRef.join(',');
                let acc_mainInfo = $('input[name="acc_mainInfo"]').val()
                let acc_info = $('textarea[name=acc_info]').val();
                let acc_comment = $('input[name=acc_comment]').val();
                let acc_season = $('select[name="acc_season"]').val();
                let rp_peakSeason_weekday = $('input[name=rp_peakSeason_weekday]').val();
                let rp_peakSeason_weekend = $('input[name=rp_peakSeason_weekend]').val();
                let rp_offSeason_weekday = $('input[name=rp_offSeason_weekday]').val();
                let rp_offSeason_weekend = $('input[name=rp_offSeason_weekend]').val();


                // 유효성 검사
                // 숙소명
                if(!acc_name) {
                    sweetalert("숙소명을 입력해 주십시오.", "warning");
                    return false;
                }

                 // 주소
                if(!acc_addrDetail) {
                    sweetalert("주소 입력 후 '주소 입력' 버튼을 클릭해 주십시오.", "warning");
                    return false;
                }

                 // 체크인, 체크아웃
                if(!acc_checkIn || !acc_checkOut) {
                    sweetalert("체크인 및 체크아웃 시간을 선택해 주십시오.", "warning");
                    return false;
                }

                // 숙소 이미지
                if (myDropzone.get(0).dropzone.files == null || myDropzone.get(0).dropzone.files.length == 0) {
                    sweetalert("사진을 최소 1장 이상 등록해 주십시오.", "warning");
                    return false;
                }

                // 숙소 정보
                if(acc_info == '') {
                   sweetalert("숙소 정보를 입력해 주십시오.", "warning");
                   return false;
                }

                // 사장님 한마디
                if(!acc_comment) {
                   sweetalert("사장님 한마디를 입력해 주십시오.", "warning");
                   return false;
                }

                // 할인율 적용
                if (!rp_peakSeason_weekday|| !rp_peakSeason_weekend || !rp_offSeason_weekday || !rp_offSeason_weekend) {
                    sweetalert("할인율을 입력해 주십시오.", "warning");
                    return false;
                }

                // input 데이터 append
                usaveFormData.append("acc_id", acc_id);
                usaveFormData.append("acc_name", acc_name);
                usaveFormData.append("accType_no", accType_no);
                usaveFormData.append("province_no", province_no);
                usaveFormData.append("acc_city", acc_city);
                usaveFormData.append("acc_zip", acc_zip);
                usaveFormData.append("acc_addr", acc_addr);
                usaveFormData.append("acc_addrDetail", acc_addrDetail);
                usaveFormData.append("acc_longtitude", acc_longtitude);
                usaveFormData.append("acc_lattitude", acc_lattitude);
                usaveFormData.append("acc_mainInfo", acc_mainInfo);
                usaveFormData.append("acc_info", acc_info);

                usaveFormData.append("acc_comment", acc_comment);
                usaveFormData.append("acc_season", acc_season);
                usaveFormData.append("rp_peakSeason_weekday", rp_peakSeason_weekday);
                usaveFormData.append("rp_peakSeason_weekend", rp_peakSeason_weekend);
                usaveFormData.append("rp_offSeason_weekday", rp_offSeason_weekday);
                usaveFormData.append("rp_offSeason_weekend", rp_offSeason_weekend);
                usaveFormData.append("sc_no", submitRef.join(','));
                usaveFormData.append("acc_checkIn", acc_checkIn);
                usaveFormData.append("acc_checkOut", acc_checkOut);

                usaveFormData.append("provinceNo", provinceNo); // 기존 province_no
                usaveFormData.append("accThumbs", accThumbs); // 기존 숙소 사진



                $.ajax({
                    url: '/Lemo/business/info/usave',
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
                            sweetalert("숙소 수정에 실패하였습니다.\n잠시 후 다시 시도해 주세요.")
                        }else if(data == 1) {
                            location.href= "/Lemo/business/info/view?acc_id="+acc_id;
                        }
                    }
                });

            });

        },

    });

});
