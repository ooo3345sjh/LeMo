package kr.co.Lemo.test;

import kr.co.Lemo.domain.CsVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 *  2023/03/17
 *  황원진
 *  dropzone Test service
 * */


@Log4j2
@Service
public class FileService {

    @Autowired
    private testDAO dao;



    /* PDF 등록 yunsd@20190115 */
    public HashMap<String, Object> uploadFile(MultipartHttpServletRequest request, HashMap<String, Object> parameter) {
        HashMap<String,Object> resultMap = new HashMap<String, Object>();

        // Getting uploaded files from the request object
        Map<String, MultipartFile> fileMap = request.getFileMap();

        // 리스트 저장용


        //파일 정보 저장용도
        ArrayList<HashMap<String,Object>> ArrFilesInfo = new ArrayList<HashMap<String,Object>>();
        HashMap<String,Object> sendParam = new HashMap<String,Object>();

        // 사진 저장
        String path = new File("C:/Users/hwangwonjin/Desktop/workspace/LeMo/Lemo/img/cs").getAbsolutePath();


        // Iterate through the map
        for (MultipartFile multipartFile : fileMap.values()) {

            //'저장용 맵'
            HashMap<String,Object> param = new HashMap<String,Object>();

            /*파일 기본 정보 추출*/
            HashMap<String, Object> fileInfo = getUploadedFileInfo(multipartFile);

            //파일 이름 추출 (확장자 제거)
            String fName = (String)fileInfo.get("fileName");
            String ext = fName.substring(fName.indexOf("."));
            String newName = UUID.randomUUID().toString() + ext;
            log.info("newName : " + newName);

            // 파일 저장
            try {
                multipartFile.transferTo(new File(path, newName));
            } catch (IllegalStateException e) {
                log.error(e.getMessage());
            } catch (IOException e) {
                log.error(e.getMessage());
            }



            param.put("New_NAME",newName);

            ArrFilesInfo.add(param);
        }
        //mybatis foreach를 위해 array를 param에 담는다.

        //String newName = String.valueOf(ArrFilesInfo);

        log.info("arrFileInfo" + ArrFilesInfo);


        resultMap.put("RESULT", ArrFilesInfo);

        return resultMap;
    }

    /*fileMap에서 파일 정보 추출*/
    private HashMap<String, Object> getUploadedFileInfo(MultipartFile multipartFile){

        HashMap<String, Object> fileInfo = new HashMap<String, Object>();
        fileInfo.put("fileName",multipartFile.getOriginalFilename());
        fileInfo.put("fileSize",multipartFile.getSize());
        fileInfo.put("fileContentType",multipartFile.getContentType());

        log.info("fileSize" + multipartFile.getSize());

        return fileInfo;
    }


    public int insertTestArticle(String newName, CsVO vo, MultipartHttpServletRequest request, HashMap<String, Object> parameter){

        HashMap<String, Object> imgName = uploadFile(request, parameter);

        log.info("imgName5 : " + imgName);

        vo.setCs_eventbannerImg(newName);

        log.info("cs_eventbanner : " + vo.getCs_eventbannerImg());

        return dao.insertTestArticle(vo);
    }
}