// 사진 업로드
    $(document).ready(function() {
        let wrapCount = 0;      // 사진 추가 버튼 누를 때마다 div 추가하기 위해 첫 wrapCount = 0
        const maxWraps = 20;    // 최대 추가 가능 이미지 개수

        // wrap div 추가하기
        function addWrap() {
            // wrap 템플릿 복사하기
            const wrapTemplate = document.querySelector('#wrap-template');
            const wrapClone = wrapTemplate.cloneNode(true);
            wrapClone.removeAttribute('id');
            wrapClone.style.display = 'inline-block';

            // wrapCount += 1, data-fileindex 업데이트
            wrapCount++;
            wrapClone.querySelector('.insertFile').setAttribute('data-fileindex', wrapCount);
            wrapClone.querySelector('.preview').setAttribute('data-previewindex', wrapCount);

            // 복사한 wrap 추가
            document.querySelector('#wrap-container').appendChild(wrapClone);

            // 이벤트 요소 추가
            const newInput = wrapClone.querySelector('.insertFile');
            newInput.addEventListener('change', (event) => {
                const selectedFile = event.target.files[0];
                const fileIndex = event.target.getAttribute('data-fileindex');
                console.log(selectedFile);
                console.log(fileIndex);

                // 업로드 사진 미리보기
                const reader = new FileReader();
                reader.onload = () => {
                  const previewIndex = document.querySelector(`[data-previewindex="${fileIndex}"]`);
                  previewIndex.style.backgroundImage = `url('${reader.result}')`;
                }
                reader.readAsDataURL(selectedFile);
            });

            // 삭제 버튼
            const newDeleteButton = wrapClone.querySelector('.preview_de');
            newDeleteButton.addEventListener('click', () => {

                // 해당 wrap 삭제
                wrapClone.remove();

                // 파일 삭제
                newInput.value = '';

                // 미리보기 삭제
                const previewIndex = wrapClone.querySelector('.preview').getAttribute('data-previewindex');
                document.querySelector(`[data-previewindex="${previewIndex}"]`).style.backgroundImage = '';
            });
        }

        // '추가하기' 버튼
        const addButton = document.querySelector('#add-wrap-button');
        addButton.addEventListener('click', () => {
            if (wrapCount < maxWraps) {
                addWrap();
            } else {
                alert('사진은 최대 20장까지 등록할 수 있습니다.');
            }
        });
    });

    // 업로드 사진 이름 랜덤 + 하나로
    $('.insertFile').on('change', function(){
        var files = $(this).get(0).files();
        var fileNames = [];     // 빈 배열 생성

        for (var i = 0; i < files.length; i++) {
            var oriName = files[i].name;
            var ext = oriName.substring(oriName.indexOf("."));
            var newName = UUID.randomUUID().toString() + ext;
            fileNames.push(newName);     // 배열에 파일 랜덤 이름 추가
        }
        $('#fileNames').val(fileNames.join('/'));  // hidden input에 /로 하나로 모으기

    });