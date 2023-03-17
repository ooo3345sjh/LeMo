package kr.co.Lemo.service;

import kr.co.Lemo.dao.CsDAO;
import kr.co.Lemo.domain.CsVO;
import kr.co.Lemo.domain.search.Cs_SearchVO;
import kr.co.Lemo.utils.PageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
        log.info("total : " + totalCnt);
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

    public List<CsVO> findAllQnaArticles(Cs_SearchVO sc, Model model) {
       List<CsVO> qnaArticles = dao.selectQnaArticles(sc);
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
    public int rsaveEventArticle(CsVO vo){
        return dao.insertEventArticle(vo);
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

    /** delete **/
    //@since 2023/03/15
    public int removeAdminArticle(@RequestParam("cs_no") int cs_no){
        return dao.deleteFaqWrite(cs_no);
    }


}
