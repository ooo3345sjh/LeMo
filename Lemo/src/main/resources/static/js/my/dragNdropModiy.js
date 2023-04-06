const toDataURL = url => fetch(url)
  .then(response => response.blob())
  .then(blob => new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.onloadend = () => resolve(reader.result)
    reader.onerror = reject
    reader.readAsDataURL(blob)
  }))

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

let testFormData = new FormData();

$(function(){
    const myDropzone = new Dropzone('div.dropzone', {

        url: "/Lemo/my/review/modify",
        method:'post',
        headers: {
            // 요청 보낼때 헤더 설정
            'X-CSRF-TOKEN' : $('meta[name="_csrf"]').attr('content') // jwt
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

                //myDropzone.options.addedfile.call(myDropzone, mockFile);
                //myDropzone.options.thumbnail.call(myDropzone, mockFile, imgSrc);
                toDataURL(imgSrc).then(dataUrl => {
                    var fileTest = base64toFile(dataUrl, fileName);
                    testFormData.append(fileName, fileTest);
                });

                myDropzone.displayExistingFile(mockFile, imgSrc);
                myDropzone.files.push(mockFile);

            }

            {
                $('[data-dz-thumbnail]').css('height', '120');
                $('[data-dz-thumbnail]').css('width', '120');
                $('[data-dz-thumbnail]').css('object-fit', 'cover');
            };

            myDropzone.on("thumbnail", function(file, dataURL) {
                // 안쓰이지만 추후에 제거
            });

            myDropzone.on("addedfile", function(file) {
                console.log("addedfile");
                console.log(file);
                testFormData.append(file.name, file);
            });

            myDropzone.on("removedfile", function(file) {
              alert('checkRemove');
              console.log(file);
              testFormData.delete(file.name);
            });

            // 서버에 제출 submit 버튼 이벤트 등록
            document.querySelector('#btn_dropzone').addEventListener('click', function (e) {
                console.log(myDropzone.files);

                let title   = document.querySelector('input[name="revi_title"]');
                let content = document.querySelector('textarea[name="revi_content"]');

                console.log(rating.value);

                testFormData.append("revi_title", title.value);
                testFormData.append("revi_content", content.value);
                testFormData.append("revi_rate", rating.value);
                testFormData.append("acc_id", acc_id);
                testFormData.append("res_no", res_no);

                //myDropzone.processQueue();
//                $.ajax({
//                    url: '/Lemo/my/review/usave',
//                    method: 'POST',
//                    beforeSend: function (xhr) {
//                        xhr.setRequestHeader(header, token);
//                    },
//                    data: testFormData,
//                    contentType: false,
//                    processData: false,
//                    enctype : 'multipart/form-data',
//                    success: function(data) {
//
//                    }
//                });

            });

            myDropzone.on("complete", function(file) {
                console.log('test');
                //location.href= "/Lemo/my/review/view?res_no="+res_no;
            });

        },
    });

    $('#btn_upload').submit(function(e){ e.preventDefault() });

});
