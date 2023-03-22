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

             init: function () {
               // 최초 dropzone 설정시 init을 통해 호출
                 console.log('최초 실행');
                 let myDropzone = this; // closure 변수 (화살표 함수 쓰지않게 주의)



               // 서버에 제출 submit 버튼 이벤트 등록
               document.querySelector('#btnUpload').addEventListener('click', function (e) {


                   console.log("업로드1", myDropzone.files);
                      e.preventDefault();

                      for( data in myDropzone)
                              console.log("myDropZone : " + JSON.stringify(data));

                          // 거부된 파일이 있다면
                          if (myDropzone.getRejectedFiles().length > 0) {
                             let files = myDropzone.getRejectedFiles();
                             console.log('거부된 파일이 있습니다.', files);
                             return;
                          }



                        myDropzone.processQueue(); // autoProcessQueue: false로 해주었기 때문에, 메소드 api로 파일을 서버로 제출
                     });

                    let fileInput = $('#ex_file');
                   myDropzone.on("sending", function(file, xhr, formData){
                        formData.append("cs_title", $('input[name="cs_title"]').val());
                        formData.append("cs_content", $('textarea[name="cs_content"]').val());
                        formData.append("cs_eventStart", $('input[name="cs_eventStart"]').val());
                        formData.append("cs_eventEnd", $('input[name="cs_eventEnd"]').val());
                        formData.append("cs_eventBanner", fileInput[0].files[0]);




                     });
               },
          });
     });



/*  dropzone test 구현 성공

#fileDropzone2.options.dropzone = {
    // Configuration options go here

    url: "/admin/cs/event/write",
    autoProcessQueue: false,
    maxFiles: 5, // 업로드 파일수
    parallelUploads: 5, // 동시파일업로드 수(이걸 지정한 수 만큼 여러파일을 한번에 넘긴다.)
    uploadMultiple: true, // 다중업로드 기능
    addRemoveLinks: true, // 업로드 후 파일 삭제버튼 표시 여부
    dictRemoveFile: '삭제', // 삭제버튼 표시 텍스트
    acceptedFiles: '.jpeg,.jpg,.png,.gif,.JPEG,.JPG,.PNG,.GIF', // 이미지 파일 포맷만 허용

    init: function () {
      // 최초 dropzone 설정시 init을 통해 호출
      console.log('최초 실행');
      let myDropzone = this; // closure 변수 (화살표 함수 쓰지않게 주의)


      // 서버에 제출 submit 버튼 이벤트 등록
      document.querySelector('#btnUpload').addEventListener('click', function (e) {

         console.log("업로드1", myDropzone.files);
         e.preventDefault();


         for( data in myDropzone)
                 console.log("myDropZone : " + JSON.stringify(data));

          //console.log("myDropzoneFile : " + myDropzone.getFile());

         //let data = JSON.stringify(myDropzone);




         // 거부된 파일이 있다면
         if (myDropzone.getRejectedFiles().length > 0) {
            let files = myDropzone.getRejectedFiles();
            console.log('거부된 파일이 있습니다.', files);
            return;
         }

         myDropzone.processQueue(); // autoProcessQueue: false로 해주었기 때문에, 메소드 api로 파일을 서버로 제출
      });
  }
 }
*/