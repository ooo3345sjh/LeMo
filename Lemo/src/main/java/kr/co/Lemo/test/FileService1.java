package kr.co.Lemo.test;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Log4j2
@Service
public class FileService1 {

    /* PDF 등록 yunsd@20190115 */
    public void uploadFile(List<MultipartFile> fileList, HttpServletRequest req, HashMap<String, Object> param) {

        List<String> fileRenames = new ArrayList<>();

        log.info("fileRenames = " + fileRenames);


        // 사진 저장
        String path = new File("C:/Users/hwangwonjin/Desktop/workspace/LeMo/Lemo/img/cs").getAbsolutePath();


        // Iterate through the map
        for (MultipartFile multipartFile : fileList) {

            //파일 이름 추출 (확장자 제거)
            String fName = multipartFile.getOriginalFilename();
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
        }


        String newName = String.join("/", fileRenames);
        log.info("listNewName : " + newName);
    }
}