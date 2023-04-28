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


// 드롭존 부분
    Dropzone.autoDiscover=false;

    $(function(){

        const myDropzone = new Dropzone('div.dropzone', {
            url: "/Lemo/admin/cs/event/modify",
            method:'post',
            headers: {
                // 요청 보낼때 헤더 설정
                'X-CSRF-TOKEN' : $('meta[name="_csrf"]').attr('content') // jwt
            },

            thumbnailHeight: 90,                                                        // Upload icon size
            thumbnailWidth: 90,
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
            var submitButton = document.querySelector("#btnModify");
            // 최초 dropzone 설정시 init을 통해 호출
            console.log('최초 실행');
            let myDropzone = this; // closure 변수 (화살표 함수 쓰지않게 주의)

            if(viewThumbs != null) {
                const thumbs = viewThumbs.split('/');

                for(thumb in thumbs) {

                    let imgSrc = '/Lemo/img/cs/'+cs_no+'/'+thumbs[thumb];
                    let dataURL;
                    let fileName = thumbs[thumb];

                    var mockFile = {
                        name : fileName,
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
                console.log("imgDelete : "+usaveFormData);
            });


            let myViewZone = $('div.dropzone');
            let title = document.querySelector('input[name="cs_title"]');
            let content = document.querySelector('textarea[name="cs_content"]');
            let eventBanner = document.querySelector('input[name="eventbannerImg[]"]');
            let MainBanner = document.querySelector('input[name="eventMainImg[]"]');


            let fileInput = $('#ex_file');
            let mainInput = $('#main_file');




        // 서버에 제출 submit 버튼 이벤트 등록
        document.querySelector('#btnModify').addEventListener('click', function (e) {

            e.preventDefault();

            let cs_title = $('input[name="cs_title"]').val();
            let cs_content = $('textarea[name="cs_content"]').val();
            let cs_eventStart = $('input[name="cs_eventStart"]').val();
            let cs_eventEnd = $('input[name="cs_eventEnd"]').val();
            let cs_eventBanner = fileInput[0].files[0];
            let cs_eventMainBanner =  mainInput[0].files[0];


            console.log("업로드1", myDropzone.files);

            if(title.value.trim() === ''){
                Swal.fire({
                    title : '제목을 입력해주세요',
                    icon : 'warning',
                    confirmButtonText : '확인'
                })
                title.focus();
                return;
            }

            if(content.value.trim() === ''){
                Swal.fire({
                    title : '내용을 입력해주세요',
                    icon : 'warning',
                    confirmButtonText : '확인'
                })
                content.focus();
                return;
            }

            // 이벤트 뷰
            if (myViewZone.get(0).dropzone.files == null || myViewZone.get(0).dropzone.files.length == 0) {
                sweetalert("사진을 최소 1장 이상 등록해 주십시오.", "warning");
                return;
            }


            // 거부된 파일이 있다면
            if (myDropzone.getRejectedFiles().length > 0) {
                let files = myDropzone.getRejectedFiles();
                sweetalert("거부된 파일이 있습니다.", "warning");
                return;
            }

            usaveFormData.append("cs_title", cs_title);
            usaveFormData.append("cs_content", cs_content);
            usaveFormData.append("cs_eventStart", cs_eventStart);
            usaveFormData.append("cs_eventEnd", cs_eventEnd);
            usaveFormData.append("cs_no", cs_no);
            if(MainBanner.files.length === 0){
                usaveFormData.append('main'+mainImgType, mainOriFile); // 기존 main 파일
            }else{
                usaveFormData.append('main'+mainImgType, chkMainOriFile);  // 수정 main 파일
            }
            if(eventBanner.files.length === 0){
                usaveFormData.append('eventBanner'+bannerImgType, bannerOriFile);  // 기존 banner 파일
            }else{
                usaveFormData.append('eventBanner'+bannerImgType, chkBannerOriFile);  // 수정 banner 파일
            }



            $.ajax({
                url: '/Lemo/admin/cs/event/modify',
                method: 'POST',
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token);
                },
                data: usaveFormData,
                contentType: false,
                processData: false,
                enctype : 'multipart/form-data',
                success: function(data) {

                    if(data == 'usaveEventFail') {
                        location.href = '/Lemo/admin/cs/event/list';
                    }else if(data == 'usaveEventSuccess') {
                        location.href= "/Lemo/admin/cs/event/view?cs_no="+cs_no+"&page="+page;
                    }
                }
            });

        });
        },
    });
});
