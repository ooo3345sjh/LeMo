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
    public CsVO selectCsArticle(@Param("cs_no") int cs_no);
    public List<CsVO> selectQnaArticles(SearchCondition sc);
    // @since 2023/03/11
    public List<CsVO> selectFaqArticles(SearchCondition sc);
    public int countFaqArticles(@Param("cs_cate") String cs_cate, @Param("cs_type") String cs_type);
    // @since 2023/03/12
    public CsVO selectEventPrev(@Param("cs_no") int cs_no);
    public CsVO selectEventNext(@Param("cs_no") int cs_no);

    /**
     * @since 2023/03/12
     * @apiNote Admin/cs
     * */
    public List<CsVO> selectAdminQnaArticles(SearchCondition sc);
    public CsVO selectAdminCsArticle(@Param("cs_cate") String cs_cate, @Param("cs_no") int cs_no);
    public void selectFaqArticles();
    public void selectTermsArticles();


    /** insert **/
    // @since 2023/03/09
    public int insertEvent(CsVO vo);
    public int insertNoticeArticle(CsVO vo);
    //@since 2023/03/10
    public int insertArticleQna(CsVO vo);
    //@since 2023/03/15
    public int insertFaqArticle(CsVO vo);
    /** update **/
    //@since 2023/03/14
    public int updateQnaArticle(@Param("cs_reply") String cs_reply, @Param("cs_no") int cs_no);
    //@since 2023/03/14
    public int updateAdminNotice(CsVO vo);
    //@since 2023/03/16
    public int updateFaqArticle(CsVO vo);
    /** delete **/
    //@since 2023/03/15
    public int deleteFaqWrite(@Param("cs_no") int cs_no);
}
