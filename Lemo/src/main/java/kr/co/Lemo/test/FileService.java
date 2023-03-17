package kr.co.Lemo.test;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Service
public class FileService {

    /* PDF 등록 yunsd@20190115 */
    public HashMap<String, Object> uploadFile(MultipartHttpServletRequest request, HashMap<String, Object> parameter) {
        HashMap<String,Object> resultMap = new HashMap<String, Object>();

        // Getting uploaded files from the request object
        Map<String, MultipartFile> fileMap = request.getFileMap();

        //파일 정보 저장용도
        ArrayList<HashMap<String,Object>> ArrFilesInfo = new ArrayList<HashMap<String,Object>>();
        HashMap<String,Object> sendParam = new HashMap<String,Object>();

        // Iterate through the map
        for (MultipartFile multipartFile : fileMap.values()) {

            //'저장용 맵'
            HashMap<String,Object> param = new HashMap<String,Object>();

            /*파일 기본 정보 추출*/
            HashMap<String, Object> fileInfo = getUploadedFileInfo(multipartFile);

            //파일 이름 추출 (확장자 제거)
            String fName = (String)fileInfo.get("fileName");
            log.info("fName : " + fName);


            param.put("ORI_NAME",fName);

            ArrFilesInfo.add(param);

        }
        //mybatis foreach를 위해 array를 param에 담는다.

        resultMap.put("RESULT", "SUCCESS");

        return resultMap;
    }

    /*fileMap에서 파일 정보 추출*/
    private HashMap<String, Object> getUploadedFileInfo(MultipartFile multipartFile){

        HashMap<String, Object> fileInfo = new HashMap<String, Object>();
        fileInfo.put("fileName",multipartFile.getOriginalFilename());
        fileInfo.put("fileSize",multipartFile.getSize());
        fileInfo.put("fileContentType",multipartFile.getContentType());

        return fileInfo;
    }
}