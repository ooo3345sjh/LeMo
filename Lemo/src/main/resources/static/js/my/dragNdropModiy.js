const toDataURL = url => fetch(url)
  .then(response => response.blob())
  .then(blob => new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.onloadend = () => resolve(reader.result)
    reader.onerror = reject
    reader.readAsDataURL(blob)
  }))

function base64toBlob(b64Data, contentType, sliceSize) {
    contentType = contentType || '';
    sliceSize = sliceSize || 512;
    var byteCharacters = atob(b64Data);
    var byteArrays = [];
    for (var offset = 0; offset < byteCharacters.length; offset += sliceSize) {
        var slice = byteCharacters.slice(offset, offset + sliceSize);
        var byteNumbers = new Array(slice.length);
        for (var i = 0; i < slice.length; i++) {
            byteNumbers[i] = slice.charCodeAt(i);
        }
        var byteArray = new Uint8Array(byteNumbers);
        byteArrays.push(byteArray);
    }
    var blob = new Blob(byteArrays, {type: contentType});
    return blob;
}

function b64toBlob(b64Data, contentType = '', sliceSize = 512) {
   const image_data = atob(b64Data.split(',')[1]); // data:image/gif;base64 필요없으니 떼주고, base64 인코딩을 풀어준다

   const arraybuffer = new ArrayBuffer(image_data.length);
   const view = new Uint8Array(arraybuffer);

   for (let i = 0; i < image_data.length; i++) {
      view[i] = image_data.charCodeAt(i) & 0xff;
      // charCodeAt() 메서드는 주어진 인덱스에 대한 UTF-16 코드를 나타내는 0부터 65535 사이의 정수를 반환
      // 비트연산자 & 와 0xff(255) 값은 숫자를 양수로 표현하기 위한 설정
   }

   return new Blob([arraybuffer], { type: contentType });
}

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

                var mockFile = {
                    name: thumbs[thumb],
                    size: 1000,
                    type: 'image/jpeg',
                    url: imgSrc,
                    accepted: true
                };

                //myDropzone.options.addedfile.call(myDropzone, mockFile);
                //myDropzone.options.thumbnail.call(myDropzone, mockFile, imgSrc);


                toDataURL(imgSrc)
                  .then(dataUrl => {
                    console.log(dataUrl);
//                    fetch(dataUrl)
//                        .then((res) => res.blob())
//                        .then((myBlob) => {
//                            console.log(myBlob);
//                            myBlob.name = 'image.jpeg';
//                            myBlob.lastModified = new Date();
//
//                            const myFile = new File([myBlob], 'image.jpeg', {
//                                type: myBlob.type,
//                            });
//
//                            console.log(myFile);
//                        });

                    let b64toBlobs = b64toBlob(dataUrl,'image/png');
                    console.log(b64toBlobs)
                    myDropzone.addFile(b64toBlobs);

//                    let test123 = new File([b64toBlobs], "test")
//                    console.log(test123)
//                    console.log(dataUrl);
//                    let testestest = base64toFile(dataUrl, thumbs[thumb]);
//                    console.log(testestest);
                  })

            }

            {
                $('[data-dz-thumbnail]').css('height', '120');
                $('[data-dz-thumbnail]').css('width', '120');
                $('[data-dz-thumbnail]').css('object-fit', 'cover');
            };

            let title   = document.querySelector('input[name="revi_title"]');
            let content = document.querySelector('textarea[name="revi_content"]');
            let rating  = document.querySelector('input[name="revi_rate"]');

            myDropzone.on("sending", function(file, xhr, formData){
                formData.append("revi_title", title.value);
                formData.append("revi_content", content.value);
                formData.append("revi_rate", rating.value);
                formData.append("acc_id", acc_id);
                formData.append("res_no", res_no);
            });

            // 서버에 제출 submit 버튼 이벤트 등록
            document.querySelector('#btn_dropzone').addEventListener('click', function (e) {
                console.log('alert');
                console.log(myDropzone.files);
                //myDropzone.processQueue();
            });

            myDropzone.on("complete", function(file) {
                console.log('test');
                //location.href= "/Lemo/my/review/view?res_no="+res_no;
            });

        },
    });

    $('#btn_upload').submit(function(e){ e.preventDefault() });

});
