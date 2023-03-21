// 드래그앤드롭 파일 업로드 

Dropzone.autoDiscover=false;

$(document).ready(function(){
    const myDropzone = new Dropzone('div#fileDropzone2', {
        url: "/admin/cs/event/write",       //업로드할 url (ex)컨트롤러)
        method: 'post',
        headers: {
          // 요청 보낼때 헤더 설정
           'X-CSRF-TOKEN' : $('meta[name="_csrf"]').attr('content') // jwt
        },


        autoProcessQueue: false,    // 자동업로드 여부 (true일 경우, 바로 업로드 되어지며, false일 경우, 서버에는 올라가지 않은 상태임 processQueue() 호출시 올라간다.)
        clickable: true,            // 클릭가능여부
        autoQueue: false,           // 드래그 드랍 후 바로 서버로 전송
        thumbnailHeight: 90,        // Upload icon size
        thumbnailWidth: 90,         // Upload icon size
        maxFiles: 5,                // 업로드 파일수
        maxFilesize: 10,            // 최대업로드용량 : 10MB
        params: {
            "title" : "테스트입니다.",
            "content" : "test,test,test,test,test",
            "date" : date
        },
        parallelUploads: 5,        // 동시파일업로드 수(이걸 지정한 수 만큼 여러파일을 한번에 컨트롤러에 넘긴다.)
        addRemoveLinks: true,       // 삭제버튼 표시 여부
        dictRemoveFile: '삭제',      // 삭제버튼 표시 텍스트
        uploadMultiple: true,       // 다중업로드 기능
        paramName: 'image',           // 서버에서 사용할 formdata 이름 설정 (defualt: file)
        acceptedFiles: '.jpeg,.jpg,.png,.gif,.JPEG,.JPG,.PNG,.GIF', // 이미지 파일 포맷만 허용
        createImageThumbnails: true, //파일 업로드 썸네일 생성

        init: function(){
            var submitButton = document.querySelector("#btnUpload");
            var myDropzone = this; 

            submitButton.addEventListener("click", function (e) {
        
            console.log("업로드1", myDropzone.files);
            e.preventDefault();

            // 거부된 파일이 있다면
            if (myDropzone.getRejectedFiles().length > 0) {
                let files = myDropzone.getRejectedFiles();
                console.log('거부된 파일이 있습니다.', files);
                return;
            }

            myDropzone.processQueue();

            });

            myDropzone.on("success", function(file, response) {
              alert("업로드 완료!");
            });

        },

    });


    const myDropzone2 = new Dropzone('div.dropzone', {
        url: "/admin/cs/event/write",
        method: 'post',
        headers: {
          // 요청 보낼때 헤더 설정
           'X-CSRF-TOKEN' : $('meta[name="_csrf"]').attr('content') // jwt
        },

        autoProcessQueue: false,    // 자동업로드 여부 (true일 경우, 바로 업로드 되어지며, false일 경우, 서버에는 올라가지 않은 상태임 processQueue() 호출시 올라간다.)
        clickable: true,            // 클릭가능여부
        thumbnailHeight: 90,        // Upload icon size
        thumbnailWidth: 90,         // Upload icon size
        maxFiles: 5,                // 업로드 파일수
        maxFilesize: 10,            // 최대업로드용량 : 10MB
        parallelUploads: 99,        // 동시파일업로드 수(이걸 지정한 수 만큼 여러파일을 한번에 컨트롤러에 넘긴다.)
        addRemoveLinks: true,       // 삭제버튼 표시 여부
        dictRemoveFile: '삭제',     // 삭제버튼 표시 텍스트
        uploadMultiple: true,       // 다중업로드 기능
        acceptedFiles: '.jpeg,.jpg,.png,.gif,.JPEG,.JPG,.PNG,.GIF', // 이미지 파일 포맷만 허용
        createImageThumbnails: true, //파일 업로드 썸네일 생성

        init: function(){
            var submitButton = document.querySelector("#btnUpload");
            var myDropzone2 = this;

            submitButton.addEventListener("click", function () {

            console.log("업로드2", myDropzone2.files);

            // 거부된 파일이 있다면
            if (myDropzone2.getRejectedFiles().length > 0) {
                let files = myDropzone2.getRejectedFiles();
                console.log('거부된 파일이 있습니다.', files);
                return;
            }

            myDropzone2.processQueue();

            });
           myDropzone2.on("success", function(file, response) {
                  alert("업로드 완료!");
                });
        },

    });

        
});