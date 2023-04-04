package kr.co.Lemo.dao;

import kr.co.Lemo.domain.CsVO;
import kr.co.Lemo.domain.TermVO;
import kr.co.Lemo.utils.SearchCondition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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
    public List<CsVO> selectQnaArticles(CsVO vo);
    // @since 2023/03/11
    public List<CsVO> selectFaqArticles(SearchCondition sc);
    public int countFaqArticles(@Param("cs_cate") String cs_cate, @Param("cs_type") String cs_type);
    // @since 2023/03/12
    public CsVO selectEventPrev(@Param("cs_cate") String cs_cate, @Param("cs_no") int cs_no);
    public CsVO selectEventNext(@Param("cs_cate") String cs_cate, @Param("cs_no") int cs_no);
    // @since 2023/04/04
    public List<TermVO> selectTerm();
    public List<TermVO> selectLocation();
    public TermVO selectPrivacyRequire();
    public TermVO selectFourTeen();
    public TermVO selectMarketing();

    /**
     * @since 2023/03/12
     * @apiNote Admin/cs
     * */
    public List<CsVO> selectAdminQnaArticles(SearchCondition sc);
    public CsVO selectAdminCsArticle(@Param("cs_cate") String cs_cate, @Param("cs_no") int cs_no);



    /** insert **/
    // @since 2023/03/09
    public int insertEventArticle(Map<String, Object> parameter);
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
    //@since 2023/03/17
    public int updateOnEvent(@Param("cs_no") int cs_no);
    //@since 2023/03/17
    public int updateEndEvent(@Param("cs_no") int cs_no);
    //@since 2023/03/29
    public int updateMainBanner(@Param("cs_eventMainBannerState") int cs_eventMainBannerState, @Param("cs_no") int cs_no);
    //@since 2023/03/30
    public int updateEventBanner(@Param("cs_eventBannerState") int cs_eventBannerState, @Param("cs_no") int cs_no);


    /** delete **/
    //@since 2023/03/15
    public int deleteFaqWrite(@Param("cs_no") int cs_no);
    //@since 2023/03/27
    public int deleteQnaList(@Param("checkList") List<String> checkList);
}
