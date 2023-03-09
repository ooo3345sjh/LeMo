package kr.co.Lemo.service;

import kr.co.Lemo.dao.CsDAO;
import kr.co.Lemo.domain.CsVO;
import kr.co.Lemo.utils.PageHandler;
import kr.co.Lemo.utils.SearchCondition;
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
    public List<CsVO> selectEventArticles(SearchCondition sc, Model model){

        log.info("here1");
        int totalCnt = dao.countEventArticles(sc.getCs_cate());
        log.info("here2");
        log.info("total : " + totalCnt);
        int totalPage = (int) Math.ceil(totalCnt / (double)sc.getPageSize());

        log.info("here3");
        if(sc.getPage() > totalPage) sc.setPage(totalPage);
        PageHandler pageHandler = new PageHandler(totalCnt, sc);

        log.info("here4");

        List<CsVO> eventArticles = dao.selectEventArticles(sc);

        log.info("here5");

        model.addAttribute("eventArticles", eventArticles);
        model.addAttribute("ph", pageHandler);

        log.info("here6");

        return eventArticles;
    }

    public CsVO selectEventArticle(@RequestParam("cs_no") int cs_no) {
        return dao.selectEventArticle(cs_no);
    }



    /** insert **/
    public int insertEvent(CsVO vo){
        return dao.insertEvent(vo);
    }




    /** update **/





    /** delete **/



}
