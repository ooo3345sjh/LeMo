//  파일 업로드 및 미리보기 (썸네일) 자바스크립트 코드 

let sel_files = [];

$(document).ready(function(){
    $('#input_imgs').on('change', handleImgFileSelect);
});

function fileUploadAction(){
    console.log('fileUploadAction');
    $("#input_imgs").trigger('click');
}

function handleImgFileSelect(e){
    sel_files = [];
    $(".imgs_wrap").empty();

    let files = e.target.files;
    let filesArr = Array.prototype.slice.call(files);

    let index = 0;
    filesArr.forEach(function(f){
        if(!f.type.match("image.*")){
            alert("이미지 확장자만 가능합니다.");
            return;
        }

        sel_files.push(f);

        let reader = new FileReader();
        reader.onload = function(e){
            let html = "<a href=\"javascript:void(0);\" onclick=\"deleteImageAction("+index+")\" id=\"img_id_"+index+"\"><img src=\"" + e.target.result + "\" data-file='"+f.name+"' class='selProductFile' title='Click to remove'></a>";
            $(".imgs_wrap").append(html);
            index++;
        }
        reader.readAsDataURL(f);
    });
}

// 객실 사진 파일 업로드 및 미리보기 
let sel_files2 = [];

$(document).ready(function(){
    $('#input_imgs2').on('change', handleImgFileSelect2);
});

function fileUploadAction2(){
    console.log('fileUploadAction2');
    $("#input_imgs2").trigger('click');
}

function handleImgFileSelect2(e){
    sel_files2 = [];
    $(".imgs_wrap2").empty();

    let files = e.target.files;
    let filesArr = Array.prototype.slice.call(files);

    let index = 0;
    filesArr.forEach(function(f){
        if(!f.type.match("image.*")){
            alert("이미지 확장자만 가능합니다.");
            return;
        }

        sel_files.push(f);

        let reader = new FileReader();
        reader.onload = function(e){
            let html = "<a href=\"javascript:void(0);\" onclick=\"deleteImageAction("+index+")\" id=\"img_id_"+index+"\"><img src=\"" + e.target.result + "\" data-file='"+f.name+"' class='selProductFile' title='Click to remove'></a>";
            $(".imgs_wrap2").append(html);
            index++;
        }
        reader.readAsDataURL(f);
    });
}