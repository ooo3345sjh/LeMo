package kr.co.Lemo.test;

import kr.co.Lemo.domain.CsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.HashMap;
/**
 *  2023/03/17
 *  황원진
 *  dropzone Test Controller
 * */


@Slf4j
@Controller
public class DropZoneTest {

    @Autowired
    private FileService service;

    @GetMapping("test/dropzone")
    public String test(){
        return "test/dropzone";
    }


    @ResponseBody
    @PostMapping("test/dropzone")
    public String upload(CsVO vo, MultipartHttpServletRequest request, @RequestParam HashMap<String, Object> parameter){

        service.insertTestArticle(vo,request, parameter);

        service.uploadFile(request, parameter);

        log.info("dropzone!!!!");

        return "success";
    }
}
