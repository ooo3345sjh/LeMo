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

// URL로부터 parameter 가져오기
const urlParams = new URLSearchParams(window.location.search);
const res_no = urlParams.get('res_no');

Dropzone.autoDiscover=false;

// formData 생성
let usaveFormData = new FormData();

// rating 변경
let rating  = document.querySelector('input[name="revi_rate"]');

// rating 변경
// modify 화면 불러올 시 value값에 해당하는 input에 cheked 걸어버리기 때문에
// 변경 시 checked를 풀어줘야함
$('input[name="revi_rate"]').click(function(){
    $('input[name="revi_rate"]').attr("checked", false);
    $(this).attr("checked", true);
    rating.value = $(this).val();
});

$(function(){
    const myDropzone = new Dropzone('div.dropzone', {

        url: "/Lemo/my/review/modify",
        method:'post',
        headers: {
            // 요청 보낼때 헤더 설정
            'X-CSRF-TOKEN' : $('meta[name="_csrf"]').attr('content')
        },

        autoProcessQueue: false,
        maxFiles: 5, // 업로드 파일수
        parallelUploads: 5, // 동시파일업로드 수(이걸 지정한 수 만큼 여러파일을 한번에 넘긴다.)
        uploadMultiple: true, // 다중업로드 기능
        addRemoveLinks: true, // 업로드 후 파일 삭제버튼 표시 여부
        dictRemoveFile: '삭제', // 삭제버튼 표시 텍스트
        acceptedFiles: '.jpeg,.jpg,.png,.gif,.JPEG,.JPG,.PNG,.GIF', // 이미지 파일 포맷만 허용
        dictInvalidFileType: "이 파일 형식은 업로드할 수 없습니다.",                         // Set the invalid file type message
        dictFileTooBig: "이 파일이 너무 큽니다. ({{filesize}} MB). 최대 파일 크기는 {{maxFilesize}} MB 입니다.",            // Set the file too big message
        dictResponseError: "서버에서 {{statusCode}} 코드를 받았습니다.",                                               // Set the server response error message
        dictCancelUpload: "업로드를 취소하시겠습니까?",                                                                // Set the cancel upload message
        dictCancelUploadConfirmation: "정말로 이 파일의 업로드를 취소하시겠습니까?",                                       // Set the cancel upload confirmation message

        init: function () {
            // 최초 dropzone 설정시 init을 통해 호출
            let myDropzone = this; // closure 변수 (화살표 함수 쓰지않게 주의)

            // thumbs는 revi_thumbs를 불러와서 배열로 split한 것임
            for(thumb in thumbs) {
                let imgSrc = '/Lemo/img/review/'+acc_id+'/'+thumbs[thumb];
                let dataURL;
                let fileName = thumbs[thumb];

                var mockFile = {
                    name: thumbs[thumb],
                    size: 1000,
                    type: 'image/jpeg',
                    url: fileName,
                    accepted: true
                };

                // imSrc로 base64를 생성하고 base64로 파일을 생성한다음 formdata에 append
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
                // dropzone을 이용해 추가한 이미지는 조절이 되어 잘 들어가지만
                // displayExistingFile이걸 이용한 이미지는 조절이 안됨..
            {
                $('[data-dz-thumbnail]').css('height', '90');
                $('[data-dz-thumbnail]').css('width', '90');
                $('[data-dz-thumbnail]').css('object-fit', 'cover');
            };

            // 이미지 추가 시 작동하는 기능
            // 이 기능은 기존 이미지가 추가 될때는 작동하지 않음
            // 오로지 새로운 이미지를 추가할때만 발생함
            // file.name으로 formdata에 입력해준다.
            myDropzone.on("addedfile", function(file) {
                usaveFormData.append(file.name, file);
            });

            // 이미지 삭제시 작동하는 기능
            // 이건 기존 이미지 또는 새로운 이미지 모두 작동하는 기능임
            // 따라서 formdata에 append시킨 name으로 삭제를 함
            myDropzone.on("removedfile", function(file) {
                usaveFormData.delete(file.name);
            });

            // 서버에 제출 submit 버튼 이벤트 등록
            document.querySelector('#btn_dropzone').addEventListener('click', function (e) {
                let title   = document.querySelector('input[name="revi_title"]');
                let content = document.querySelector('textarea[name="revi_content"]');

                // 나머지 input value append
                usaveFormData.append("revi_title", title.value);
                usaveFormData.append("revi_content", content.value);
                usaveFormData.append("revi_rate", rating.value);
                usaveFormData.append("acc_id", acc_id);
                usaveFormData.append("res_no", res_no);

                // queue 기능을 사용하지 않음
                //myDropzone.processQueue();

                $.ajax({
                    url: '/Lemo/my/review/usave',
                    method: 'POST',
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader(header, token);
                    },
                    data: usaveFormData,
                    contentType: false,
                    processData: false,
                    enctype : 'multipart/form-data',
                    success: function(data) {
                        if(data == 'usaveImageFail') {
                            location.href = '/Lemo/my/review/list';
                        }else if(data == 'usaveImageSuccess') {
                            location.href= "/Lemo/my/review/view?res_no="+res_no;
                        }
                    }
                });

            });

            myDropzone.on("complete", function(file) {
                location.href= "/Lemo/my/review/view?res_no="+res_no;
            });

        },
    });

    $('#btn_upload').submit(function(e){ e.preventDefault() });

});
