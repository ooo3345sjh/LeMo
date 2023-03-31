    Dropzone.autoDiscover=false;

    $(function(){
        const myDropzone = new Dropzone('div.dropzone', {

            url: "/Lemo/admin/cs/event/write",
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
            console.log('최초 실행');

            let myDropzone = this; // closure 변수 (화살표 함수 쓰지않게 주의)
            let title = document.querySelector('input[name="cs_title"]');
            let content = document.querySelector('textarea[name="cs_content"]');
            let eventBanner = document.querySelector('input[name="eventbannerImg[]"]');
            let MainBanner = document.querySelector('input[name="eventMainImg[]"]');


            let fileInput = $('#ex_file');
            let mainInput = $('#main_file');
            console.log('file 존재여부 : ' + fileInput[0].files[0]);
            myDropzone.on("sending", function(file, xhr, formData){
                formData.append("cs_title", $('input[name="cs_title"]').val());
                formData.append("cs_content", $('textarea[name="cs_content"]').val());
                formData.append("cs_eventStart", $('input[name="cs_eventStart"]').val());
                formData.append("cs_eventEnd", $('input[name="cs_eventEnd"]').val());
                formData.append("cs_eventBanner", fileInput[0].files[0]);
                formData.append("cs_eventMainBanner", mainInput[0].files[0]);
            });



        // 서버에 제출 submit 버튼 이벤트 등록
        document.querySelector('#btnUpload').addEventListener('click', function (e) {

            console.log("업로드1", myDropzone.files);

            if(title.value.trim() === ''){
                alert('이벤트 이름을 입력하세요');
                title.focus();
                return;
            }

            if(content.value.trim() === ''){
                alert('이벤트 내용을 입력하세요');
                content.focus();
                return;
            }

            if(eventBanner.files.length === 0){
                alert('베너 이미지를 선택하세요');
                eventBanner.focus();
                return;
            }

            if(MainBanner.files.length === 0){
                alert('메인 베너 이미지를 선택하세요');
                MainBanner.focus();
                return;
            }

            // 거부된 파일이 있다면
            if (myDropzone.getRejectedFiles().length > 0) {
                let files = myDropzone.getRejectedFiles();
                console.log('거부된 파일이 있습니다.', files);
                return;
            }


            var img = document.getElementById("image");
            var file = document.getElementById("ex_file").files[0];
            var result;

            if (file) {
                var reader = new FileReader();
                reader.onload = function() {
                    var image = new Image();
                    image.src = reader.result;
                    image.onload = function() {
                        console.log("width1 : " + image.width);
                        console.log("height1 : " + image.height);
                        if (image.width < 1000 || image.height > 300 || image.height < 200) {
                            console.log("width2 : " + image.width);
                            console.log("height2 : " + image.height);
                            alert("베너이미지 크기(가로 1000px이상 세로 200px 이상 300px 이하)에 맞춰서 올려주세요.");
                            return;
                        }else{
                            img.src = reader.result;
                            myDropzone.processQueue();
                        }
                    };
                };
                    reader.readAsDataURL(file);
            } else {
                alert("파일을 선택하세요.");
            }
        });

            myDropzone.on('success', function (file, responseText) {

                location.href = "/Lemo/admin/cs/event/list";
            });
        },
    });
});
