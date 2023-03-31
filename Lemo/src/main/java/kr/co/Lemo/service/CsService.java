package kr.co.Lemo.service;

import kr.co.Lemo.dao.CsDAO;
import kr.co.Lemo.domain.CsVO;
import kr.co.Lemo.domain.UserVO;
import kr.co.Lemo.domain.search.Cs_SearchVO;
import kr.co.Lemo.utils.PageHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

/**
 * @since 2023/03/08
 * @author 황원진
 * @apiNote csService
 */

@Slf4j
@Service
public class CsService {

    @Autowired
    private CsDAO dao;


    /** select **/
    public List<CsVO> findAllCsArticles(Cs_SearchVO sc, Model model){

        log.info("cs_cate : " + sc.getCs_cate());
        int totalCnt = dao.countEventArticles(sc.getCs_cate());
        int totalPage = (int) Math.ceil(totalCnt / (double)sc.getPageSize());

        if(sc.getPage() > totalPage) sc.setPage(totalPage);
        PageHandler pageHandler = new PageHandler(totalCnt, sc);

        List<CsVO> eventArticles = dao.selectCsArticles(sc);

        model.addAttribute("csArticles", eventArticles);
        model.addAttribute("ph", pageHandler);
        model.addAttribute("totalCnt", totalCnt);
        model.addAttribute("sc", sc);

        return eventArticles;
    }

    public CsVO findCsArticle(@RequestParam("cs_no") int cs_no) {
        return dao.selectCsArticle(cs_no);
    }

    /**
     * 이벤트 상세보기 이전글, 다음글
     *  @since 2023/03/12
     */
    public CsVO findEventPrev(@RequestParam("cs_cate") String cs_cate, @RequestParam("cs_no") int cs_no) {return dao.selectEventPrev(cs_cate, cs_no);}
    public CsVO findEventNext(@RequestParam("cs_cate") String cs_cate, @RequestParam("cs_no") int cs_no) {return  dao.selectEventNext(cs_cate, cs_no);}

    public List<CsVO> findAllQnaArticles(
                                        CsVO vo,
                                        @AuthenticationPrincipal UserVO myUser,
                                        Model model) {
        vo.setUser_id(myUser.getUser_id());
       List<CsVO> qnaArticles = dao.selectQnaArticles(vo);
        log.info("qnaSize : " + qnaArticles.size());
       model.addAttribute("qnaArticles", qnaArticles);

       return qnaArticles;
    }

    // @since 23/03/11
    public List<CsVO> findAllFaqArticles(Cs_SearchVO sc, Model model){

        log.info("cs_faq : " + sc.getCs_cate());
        log.info("faqCs_type : " + sc.getCs_type());
        int totalCnt = dao.countFaqArticles(sc.getCs_cate(), sc.getCs_type());
        log.info("total : " + totalCnt);
        int totalPage = (int) Math.ceil(totalCnt / (double)sc.getPageSize());

        if(sc.getPage() > totalPage) sc.setPage(totalPage);
        PageHandler pageHandler = new PageHandler(totalCnt, sc);


        List<CsVO> faqArticles = dao.selectFaqArticles(sc);

        model.addAttribute("csArticles", faqArticles);
        model.addAttribute("ph", pageHandler);
        model.addAttribute("cs_type", sc.getCs_type());
        model.addAttribute("totalCnt", totalCnt);
        model.addAttribute("sc", sc);

        return faqArticles;
    }

    // @since 2023/03/12
    public List<CsVO> findAllAdminQnaArticles(Cs_SearchVO sc, Model model) {
        log.info("cs_cate : " + sc.getCs_cate());
        int totalCnt = dao.countEventArticles(sc.getCs_cate());
        log.info("total : " + totalCnt);
        int totalPage = (int) Math.ceil(totalCnt / (double)sc.getPageSize());

        if(sc.getPage() > totalPage) sc.setPage(totalPage);
        PageHandler pageHandler = new PageHandler(totalCnt, sc);

        List<CsVO> AdminQnaArticles = dao.selectAdminQnaArticles(sc);

        log.info("qnasize : " + AdminQnaArticles.size());

        model.addAttribute("AdminQnaArticles", AdminQnaArticles);
        model.addAttribute("ph", pageHandler);
        model.addAttribute("totalCnt", totalCnt);
        model.addAttribute("sc", sc);
        model.addAttribute("cs_type", sc.getCs_type());

        return AdminQnaArticles;
    }

    public CsVO findAdminCsArticle(@RequestParam("cs_cate") String cs_cate, @RequestParam("cs_no") int cs_no) {
        return dao.selectAdminCsArticle(cs_cate, cs_no);
    }

    /** insert **/
    public int rsaveEventArticle(MultipartHttpServletRequest request, Map<String, Object> parameter, MultipartFile cs_eventBanner) {
        Map<String, MultipartFile> fileMap = request.getFileMap();

        ArrayList<String> arrFilesInfo = new ArrayList<>();
        ArrayList<String> newArrFilesInfo = new ArrayList<>();

        // evnetViewImg 이름 변경
        for (MultipartFile multipartFile : fileMap.values()) {
            //파일 이름 추출 (확장자 제거)
            String fName = multipartFile.getOriginalFilename();
            log.info("fName : " + fName);

            String ext = fName.substring(fName.indexOf("."));
            log.info("ext : " + ext);

            String newName = UUID.randomUUID().toString() + ext;
            log.info("newName : " + newName);

            arrFilesInfo.add(newName);
            newArrFilesInfo.add(newName);
        }

        log.info("filemap size : " + fileMap.size());
        log.info("arrFilesInfo size : " + arrFilesInfo.size());
        log.info("newArrFilesInfo size : " + newArrFilesInfo.size());

        String bannerNewName = new String(arrFilesInfo.get(0).getBytes());
        String mainBannerNewName = new String(arrFilesInfo.get(1).getBytes());

        parameter.put("cs_eventbannerImg", bannerNewName);
        parameter.put("cs_eventMainBannerImg", mainBannerNewName);

        arrFilesInfo.remove(0);
        arrFilesInfo.remove(0);
        log.info("arrFilesInfo size : " + arrFilesInfo.size());

        String newName = String.join("/", arrFilesInfo);

        parameter.put("cs_eventViewImg", newName);

        int result = dao.insertEventArticle(parameter);
        int finalResult = 0;
        if(result == 1 ) {
            String cs_no = String.valueOf(parameter.get("cs_no"));

            finalResult = uploadFile(newArrFilesInfo, fileMap, bannerNewName, cs_eventBanner, cs_no);
        }

        return finalResult;
    }



    public int rsaveNoticeArticle(CsVO vo) {
        return dao.insertNoticeArticle(vo);
    }

    public int rsaveArticleQna(CsVO vo){
        return dao.insertArticleQna(vo);
    }

    //@since 2023/03/15
    public int rsaveFaqArticle(CsVO vo) {
        return dao.insertFaqArticle(vo);
    }


    /** update **/
    //@since 2023/03/14
    public int usaveQnaArticle(@RequestParam("cs_reply") String cs_reply, @RequestParam("cs_no") int cs_no){
        return dao.updateQnaArticle(cs_reply, cs_no);
    }
    //@since 2023/03/14
    public int usaveAdminNotice(CsVO vo){
        return dao.updateAdminNotice(vo);
    }
    //@since 2023/03/16
    public int usaveFaqArticle(CsVO vo){
        return dao.updateFaqArticle(vo);
    }
    //@since 2023/03/17
    public int usaveOnEvent(@RequestParam("cs_no") int cs_no){
        return dao.updateOnEvent(cs_no);
    }
    //@since 2023/03/17
    public int usaveEndEvent(@RequestParam("cs_no") int cs_no){
        return dao.updateEndEvent(cs_no);
    }

    //@since 2023/03/29
    public int usaveMainBanner(@RequestParam("cs_eventMainBannerState") int cs_eventMainBannerState, @RequestParam("cs_no") int cs_no){
        return dao.updateMainBanner(cs_eventMainBannerState, cs_no);
    }
    //@since 2023/03/30
    public int usaveEventBanner(@RequestParam("cs_eventBannerState") int cs_eventBannerState, @RequestParam("cs_no") int cs_no){
        return dao.updateEventBanner(cs_eventBannerState, cs_no);
    }


    /** delete **/
    //@since 2023/03/15
    public int removeAdminArticle(@RequestParam("cs_no") int cs_no) throws IOException {
       CsVO file =  dao.selectCsArticle(cs_no);
       log.info("deleteFile : " + file.getCs_eventViewImg());

       String delCs_no = Integer.toString(cs_no);

       if(file.getCs_eventViewImg() != null){

           String path = new File("C:/Users/hwangwonjin/Desktop/workspace/LeMo/Lemo/img/cs/"+delCs_no ).getAbsolutePath();
           log.info("path : " + path);

           File deleteFile = new File(path);
           if(deleteFile.exists()) {

               log.info("deleteFile delete!!!");
               //deleteFile.delete();
               FileUtils.deleteDirectory(deleteFile);
           }
       }
        return dao.deleteFaqWrite(cs_no);
    }

    //@since 2023/03/27
    public int removeQnaList(@RequestParam(value = "checkList[]") List<String> checkList, @RequestParam("user_id") String user_id){
        log.info("serviceQnaRemove");
        return dao.deleteQnaList(checkList, user_id);
    }


    //@since 2023/03/30
    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    /** 이미지 등록 **/
    public int uploadFile(ArrayList<String> newArrFilesInfo, Map<String, MultipartFile> fileMap, String bannerNewName, MultipartFile cs_eventBanner, String cs_no) {
        log.info("newArrFilesInfo : "+newArrFilesInfo.size());
        log.info("fileMap : "+fileMap.size());
        // 사진 저장
        String path = new File(uploadPath+"cs/"+cs_no ).getAbsolutePath();

        log.info(path);
        // 폴더 존재 여부 검사
        File checkFolder = new File(path);
        if(!checkFolder.exists()){
            try {
                Files.createDirectories(checkFolder.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(checkFolder.isDirectory()){
            try{
                FileUtils.cleanDirectory(checkFolder);
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        // 이벤트 이미지 저장
        int count = 0;
        int finalResult = 0;
        // Iterate through the map
        for (MultipartFile mf : fileMap.values()) {

            // 파일 저장
            try {
                mf.transferTo(new File(path, newArrFilesInfo.get(count)));
                count ++;
                finalResult = 1;
            } catch (IllegalStateException e) {
                log.error(e.getMessage());
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }

        log.info("arrFileInfo" + newArrFilesInfo);


        return finalResult;
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
}
