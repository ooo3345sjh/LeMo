package kr.co.Lemo.service;

import kr.co.Lemo.dao.CsDAO;
import kr.co.Lemo.domain.CsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @since 2023/03/08
 * @author 황원진
 * @apiNote csService
 */

@Service
public class CsService {

    @Autowired
    private CsDAO dao;


    /** select **/
    public List<CsVO> selectEventArticles(@RequestParam("cs_cate") String cs_cate){
        return dao.selectEventArticles(cs_cate);
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
