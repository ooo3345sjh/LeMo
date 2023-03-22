Dropzone.autoDiscover=false;

     $(function(){

//             console.log("cs_ title : " + $('input[name="cs_title"]').val());
//             console.log(" cs_content : "  + $('textarea[name="cs_content"]').val());

          // 1-1  밑에서 콘솧 찍었을때는 정상적으로 데이터 호출되는 쿼리
//         function getCsValues(){
//            const cs_title = document.getElementById('cs_title').value;
//            const cs_content = document.getElementById('summernote').value;
//            return {cs_title, cs_content};
//         }

        // cs_title 값 정상적으로 하나만 들어가는 쿼리
//        let cs_title;
//        let summernote;
//        let content;
//        let eventStart;
//        let eventEnd;
//
//        document.getElementById('cs_title').addEventListener('input', function() {
//            cs_title = this.value;  // 입력값을 변수에 할당
//            console.log(cs_title);  // 변수값 출력
//        });
//
//        $('#summernote').summernote({
//            height: 450,
//           focus: true,
//           lang : 'ko-KR',
//           spellCheck: false,
//           disableDragAndDrop: true,
//           toolbar: [
//               // [groupName, [list of button]]
//               ['style', ['bold', 'italic', 'underline', 'clear']],
//               ['font', ['strikethrough']],
//               ['fontsize', ['fontsize']],
//               ['color', ['color']],
//               ['para', ['ul', 'ol', 'paragraph']],
//               ['insert', ['table','hr']],
//            callbacks: {
//                onInit: function() {
//                    // 에디터 초기화 이후 실행되는 콜백 함수
//                    summernote = $('#summernote').summernote();
//                },
//                onKeyup: function(e) {
//                    // 에디터 내용이 변경될 때 실행되는 콜백 함수
//                    content = $('#summernote').summernote('code');
//                    console.log('content : ' + content);
//                }
//            }
//        });

//        document.getElementById('endDate').addEventListener('onchange', function() {
//            eventStart = this.value;  // 입력값을 변수에 할당
//            console.log(cs_title);  // 변수값 출력
//        });
//
//        document.getElementById('currentDate').addEventListener('onchange', function() {
//            eventEnd = this.value;  // 입력값을 변수에 할당
//            console.log(cs_title);  // 변수값 출력
//        });

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
//                    params: function() {
//                        return {
//                            'cs_title':cs_title,
//                            'cs_content':content
//                            'cs_eventStart':eventStart,
//                            'cs_eventEnd':eventEnd
//                        };
//                    },
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
                            // 1-1 쿼리
//                            const { cs_title, cs_content } = getCsValues();
//                            myDropzone.options.params.cs_title = cs_title;
//                            myDropzone.options.params.cs_content = cs_content;
//                            console.log("cs_title : " + cs_title);
//                            console.log("cs_content : " + cs_content);
                       });

                       myDropzone.on("sending", function(file, xhr, formData){
                            formData.append("cs_title", $('input[name="cs_title"]').val());
                            formData.append("cs_content", $('textarea[name="cs_content"]').val());
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