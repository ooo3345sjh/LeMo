package kr.co.Lemo.service;

import kr.co.Lemo.dao.CsDAO;
import kr.co.Lemo.domain.CsVO;
import kr.co.Lemo.domain.TermVO;
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

        log.info("findAllCsArticles");
        int totalCnt = dao.countEventArticles(sc.getCs_cate());
        int totalPage = (int) Math.ceil(totalCnt / (double)sc.getPageSize());

        if(sc.getPage() > totalPage) sc.setPage(totalPage);
        PageHandler pageHandler = new PageHandler(totalCnt, sc);


        List<CsVO> csArticles = dao.selectCsArticles(sc);

        model.addAttribute("csArticles", csArticles);
        model.addAttribute("ph", pageHandler);
        model.addAttribute("totalCnt", totalCnt);
        model.addAttribute("sc", sc);

        return csArticles;
    }

    // @since 2023/04/10 관리자 - 이벤트 리스트
    public List<CsVO> findAllEventArticles(Cs_SearchVO sc, Model model){

        log.info("findAllEventArticles");
        int totalCnt = dao.countEventArticles(sc.getCs_cate());
        int totalPage = (int) Math.ceil(totalCnt / (double)sc.getPageSize());

        if(sc.getPage() > totalPage) sc.setPage(totalPage);
        PageHandler pageHandler = new PageHandler(totalCnt, sc);

        List<CsVO> adminEventArticles = dao.selectAdminEventArticles(sc);

        model.addAttribute("adminEventArticles", adminEventArticles);
        model.addAttribute("ph", pageHandler);
        model.addAttribute("totalCnt", totalCnt);
        model.addAttribute("sc", sc);

        return adminEventArticles;
    }


    public CsVO findCsArticle(@RequestParam("cs_no") int cs_no) {
        return dao.selectCsArticle(cs_no);
    }

    /**
     *  이용약관
     * @since  2023/04/04
     */
    public List<TermVO> findTerm(){
        return dao.selectTerm();
    }
    public List<TermVO> findLocation(){
        return dao.selectLocation();
    }
    public TermVO findPrivacy(){
        return dao.selectPrivacyRequire();
    }
    public TermVO findFourTeen(){return dao.selectFourTeen();}
    public TermVO findMarketing(){return dao.selectMarketing();}
    public void findTerms(Map map){
        map.put("term", findTerm());
        map.put("privacy", findPrivacy());
        map.put("location", findLocation());
        map.put("fourteen", findFourTeen());
        map.put("marketing", findMarketing());
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

       model.addAttribute("qnaArticles", qnaArticles);

       return qnaArticles;
    }

    // @since 23/03/11 자주묻는 질문 목록
    public List<CsVO> findAllFaqArticles(Cs_SearchVO sc, Model model){


        int totalCnt = dao.countFaqArticles(sc.getCs_cate(), sc.getCs_type());

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
    /**
     *  관리자
     * @since  2023/03/12
     */

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

    // @since 2023/04/09 이벤트 수정 GET
    public CsVO findAdminEventArticle(@RequestParam("cs_cate") String cs_cate, @RequestParam("cs_no") int cs_no) {
        CsVO csVO = dao.selectAdminCsArticle(cs_cate, cs_no);

        if(csVO != null) {
            String acc_thumb = csVO.getCs_eventViewImg();

            if(acc_thumb != null) {
                List<String> thumbs = Arrays.asList(acc_thumb.split("/"));
                csVO.setThumbs(thumbs);
            }
        }
        return csVO;
    }

    // @since 2023/04/10 관리자 약관 목록
    public List<TermVO> findAllAdminTerms(Cs_SearchVO sc, Model model){
        int totalCnt = dao.countAdminTerms(sc);
        int totalPage = (int) Math.ceil(totalCnt/ (double)sc.getPageSize());

        if(sc.getPage() > totalPage) sc.setPage(totalPage);
        PageHandler pageHandler = new PageHandler(totalCnt, sc);

        List<TermVO> termArticles = dao.selectAdminTerms(sc);

        model.addAttribute("terms", termArticles);
        model.addAttribute("ph", pageHandler);
        model.addAttribute("totalCnt", totalCnt);
        model.addAttribute("sc", sc);

        return termArticles;
    }

    // @since 2023/04/11 관리자 약관 유형 목록
    public List<TermVO> findTermsTypes(){
        return dao.selectAdminTermsType();
    }

    // @since 2023/04/12 관리자 약관 상세보기
    public TermVO findTermArticle(@RequestParam("terms_no") int terms_no){
        return dao.selectTermArticle(terms_no);
    }

    // @since 2023/04/19 관리자 메인 공지사항 목록
    public List<CsVO> findNoticeArticle(){
        return dao.selectAdminNoticeArticle();
    }

    // @since 2023/04/19 관리자 메인 1:1문의 목록
    public List<CsVO> findQnaArticles(){
        return dao.selectMainQnaArticles();
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


            String ext = fName.substring(fName.indexOf("."));


            String newName = UUID.randomUUID().toString() + ext;


            arrFilesInfo.add(newName);
            newArrFilesInfo.add(newName);
        }



        String bannerNewName = new String(arrFilesInfo.get(0).getBytes());
        String mainBannerNewName = new String(arrFilesInfo.get(1).getBytes());

        parameter.put("cs_eventbannerImg", bannerNewName);
        parameter.put("cs_eventMainBannerImg", mainBannerNewName);

        arrFilesInfo.remove(0);
        arrFilesInfo.remove(0);


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

    //@since 2023/04/09
    public int rsaveTermArticle(TermVO vo){
        return dao.insertTermArticle(vo);
    }

    /** update **/
    // @since 2023/04/18
    public String usaveEventArticle(Map<String, Object> param, Map<String, MultipartFile> fileMap){

        log.debug((String) param.get("cs_no"));
        int cs_no = Integer.parseInt(String.valueOf(param.get("cs_no")));
        param.put("cs_no", cs_no);



        for(MultipartFile mf: fileMap.values()) {
            log.debug("mf : " +mf.getOriginalFilename());
        }

        List<String> fileName = checkEventFile(param, fileMap);
        if(!fileName.isEmpty()) {
            String bannerNewName = new String(fileName.get(fileName.size() - 1).getBytes());
            String mainBannerNewName = new String(fileName.get(fileName.size() - 2).getBytes());

            log.debug("bannerImg : " + bannerNewName);
            log.debug("mainImg : " + mainBannerNewName);

            param.put("cs_eventbannerImg", bannerNewName);
            param.put("cs_eventMainBannerImg", mainBannerNewName);

            log.debug("fileName size : " + fileName.size());

            fileName.remove(fileName.size() - 1);
            fileName.remove(fileName.size() - 1);

            log.debug("fileName : " + fileName);

            String files = String.join("/", fileName);
            param.put("cs_eventViewImg", files);
        }else{
            param.put("cs_eventViewImg", null);
        }

        int result = dao.updateEventArticle(param);
        log.debug(String.valueOf(result));
        String success = "usaveEventFail";
        switch (result) {
            case 1 :
                success = "usaveEventSuccess";
                break;
            case 0 :
                success = "usaveEventFail";
                break;
        }
        log.debug(success);
        return success;
    }


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

    //@since 2023/03/29 메인베너 게시 ON/OFF
    public int usaveMainBanner(@RequestParam("cs_eventMainBannerState") int cs_eventMainBannerState, @RequestParam("cs_no") int cs_no){
        return dao.updateMainBanner(cs_eventMainBannerState, cs_no);
    }
    //@since 2023/03/30 이벤트베너 게시 ON/OFF
    public int usaveEventBanner(@RequestParam("cs_eventBannerState") int cs_eventBannerState, @RequestParam("cs_no") int cs_no){
        return dao.updateEventBanner(cs_eventBannerState, cs_no);
    }
    //@since 2023/04/15 약관 수정
    public int usaveTermArticle(TermVO vo){
        return dao.updateTerms(vo);
    }

    //@since 2023/04/17 관리자 이벤트 수정


    /** delete **/
    //@since 2023/03/15 관리자 - 단일 게시글 삭제
    public int removeAdminArticle(@RequestParam("cs_no") int cs_no) throws IOException {
       CsVO file =  dao.selectCsArticle(cs_no);
       log.info("deleteFile : " + file.getCs_eventViewImg());

       String delCs_no = Integer.toString(cs_no);

       if(file.getCs_eventViewImg() != null){

           String path = new File("C:/Users/hwangwonjin/Desktop/workspace/LeMo/Lemo/img/cs/"+delCs_no ).getAbsolutePath();


           File deleteFile = new File(path);
           if(deleteFile.exists()) {


               FileUtils.deleteDirectory(deleteFile);
           }
       }
        return dao.deleteFaqWrite(cs_no);
    }

    //@since 2023/03/27 관리자 - qna 선택삭제
    public int removeQnaList(@RequestParam("checkList[]") List checkList){
        log.info("serviceQnaRemove");
        return dao.deleteQnaList(checkList);
    }


    // @since 2023/04/11 관리자 - 약관 삭제
    public int removeTerm(@RequestParam("terms_no") int terms_no){
        return dao.deleteTerm(terms_no);
    }








    /** 이미지 등록 **/

    //@since 2023/03/30
    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    // 이벤트 작성 이미지 저장
    public int uploadFile(ArrayList<String> newArrFilesInfo, Map<String, MultipartFile> fileMap, String bannerNewName, MultipartFile cs_eventBanner, String cs_no) {
        log.info("updateFileStart...");

        // 사진 저장
        String path = new File(uploadPath+"cs/"+cs_no ).getAbsolutePath();


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


        return finalResult;
    }

    // 이벤트 수정 이미지 저장
    public List<String> checkEventFile(Map<String, Object> param, Map<String, MultipartFile> fileMap){

        String cs_no = String.valueOf(param.get("cs_no"));

        String dirPath = new File(uploadPath+"cs/"+cs_no).getAbsolutePath();

        log.info("checkEventFile...");

       File dir = new File(dirPath);



       File Files[] = dir.listFiles();

       List<String> oriEvent = new ArrayList<>();
       List<String> newEvent = new ArrayList<>();
       List<String> oriRemoveEvent = new ArrayList<>();
       List<String> newRemoveEvent = new ArrayList<>();

       for(File fname : Files){
           oriEvent.add(fname.getName());
           oriRemoveEvent.add(fname.getName());
       }

       log.debug(String.valueOf(fileMap.get("main.png")));

       for(MultipartFile mf : fileMap.values()){
           newEvent.add(mf.getOriginalFilename());
           newRemoveEvent.add(mf.getOriginalFilename());
       }

       // 저장되어질 eventImage
       newEvent.removeAll(oriRemoveEvent);
       log.debug("저장 : " + newEvent);

       // 삭제되어질 eventImage
        oriEvent.removeAll(newRemoveEvent);
        log.debug("삭제 : " + oriEvent);

        if(oriEvent.size() != 0){
            for(String deleteFile : oriEvent){
                File df = new File(dirPath + "/" + deleteFile);
                if(df.exists()) { df.delete();}
            }
        }

        List<String> changeSaveFile = new ArrayList<>();


        if(newEvent.size() != 0) {
            for(String saveFile : newEvent) {

                String ext = saveFile.substring(saveFile.indexOf("."));
                String newName = UUID.randomUUID() + ext;
                changeSaveFile.add(newName);

                MultipartFile mf = fileMap.get(saveFile);

                try {
                    mf.transferTo(new File(dirPath, newName));
                }catch (IllegalStateException e) {
                    log.error(e.getMessage());
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }

        // db에 저장될 리스트 이름
        oriRemoveEvent.removeAll(oriEvent);
        oriRemoveEvent.addAll(changeSaveFile);
        log.debug("DB : " +oriRemoveEvent);

        return oriRemoveEvent;
    }

}
