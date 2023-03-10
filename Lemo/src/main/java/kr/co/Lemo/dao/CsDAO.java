package kr.co.Lemo.dao;

import kr.co.Lemo.domain.CsVO;
import kr.co.Lemo.utils.SearchCondition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @since 2023/03/08
 * @author 황원진
 * @apiNote csDAO
 */

@Mapper
@Repository
public interface CsDAO {
    /** select **/
    public List<CsVO> selectCsArticles(SearchCondition sc);
    public int countEventArticles(@Param("cs_cate") String cs_cate);
    public CsVO selectEventArticle(@Param("cs_no") int cs_no);
    public void selectFaqArticles();
    public void selectQnaArticles();
    public void selectTermsArticles();


    /** insert **/
    public int insertEvent(CsVO vo);
    public int insertArticleNotice(CsVO vo);
    public int insertArticleQna(CsVO vo);
    /** updare **/


    /** delete **/
}
