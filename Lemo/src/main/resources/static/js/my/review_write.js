// URL로부터 parameter 가져오기
const urlParams = new URLSearchParams(window.location.search);
const acc_id = urlParams.get('acc_id');
const res_no = urlParams.get('res_no');

Dropzone.autoDiscover=false;

const reviewFormData = new FormData();

$(function(){
    const myDropzone = new Dropzone('div.dropzone', {

        url: "/Lemo/my/review/write",
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

            let title   = document.querySelector('input[name="revi_title"]');
            let content = document.querySelector('textarea[name="revi_content"]');
            let rating  = document.querySelector('input[name="revi_rate"]');

            myDropzone.on("addedfile", function(file) {
                reviewFormData.append(file.name, file);
            });

            myDropzone.on("removedfile", function(file) {
                reviewFormData.delete(file.name);
            });

            // 서버에 제출 submit 버튼 이벤트 등록
            document.querySelector('#btn_dropzone').addEventListener('click', function (e) {
                reviewFormData.append("revi_title", title.value);
                reviewFormData.append("revi_content", content.value);
                reviewFormData.append("revi_rate", rating.value);
                reviewFormData.append("acc_id", acc_id);
                reviewFormData.append("res_no", res_no);

                //myDropzone.processQueue();

                $.ajax({
                    url: '/Lemo/my/review/write',
                    method: 'POST',
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader(header, token);
                    },
                    data: reviewFormData,
                    contentType: false,
                    processData: false,
                    enctype : 'multipart/form-data',
                    success: function(data) {
                        if(data.result == 'usavaWriteSuccess') {
                            Swal.fire({
                                title: '작성 되었습니다.',
                                icon: 'info',
                                confirmButtonColor: '#3085d6',
                                cancelButtonColor: '#d33',
                                confirmButtonText: '확인',
                                reverseButtons: false,
                            }).then(result => {
                                location.href = "/Lemo/my/review/view?res_no="+res_no
                            });
                        }else {
                            Swal.fire({
                                title: '새로고침 후 다시 시도해주세요.',
                                icon: 'info',
                                confirmButtonColor: '#3085d6',
                                cancelButtonColor: '#d33',
                                confirmButtonText: '확인',
                                reverseButtons: false,
                            }).then(result => {
                            });
                        }

                    }
                });
            });
        },
    });

    $('#btn_upload').submit(function(e){ e.preventDefault() });

});
