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
    // @since 2023/04/04 고객센터 - 약관
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
    //@since 2023/04/10 관리자 - 이벤트 목록
    public List<CsVO> selectAdminEventArticles(SearchCondition sc);
    //@since 2023/04/10 관리자 - 약관 목록
    public List<TermVO> selectAdminTerms(SearchCondition sc);
    //@since 2023/04/10 관리자 - 약관 총갯수
    public int countAdminTerms(SearchCondition sc);
    //@since 2023/04/11 관리자 - 약관 유형 목록
    public List<TermVO> selectAdminTermsType();
    //@since 2023/04/12 관리자 - 약관 상세보기
    public TermVO selectTermArticle(@Param("terms_no") int terms_no);
    //@since 2023/04/19 관리자 - 메인 공지사항 목록
    public List<CsVO> selectAdminNoticeArticle();
    //@since 2023/04/19 관리자 - 메인 1:1문의사항 목록
    public List<CsVO> selectMainQnaArticles();

    /** insert **/
    // @since 2023/03/09
    public int insertEventArticle(Map<String, Object> parameter);
    public int insertNoticeArticle(CsVO vo);
    //@since 2023/03/10
    public int insertArticleQna(CsVO vo);
    //@since 2023/03/15
    public int insertFaqArticle(CsVO vo);
    // @since 2023/04/09
    public int insertTermArticle(TermVO vo);

    /** update **/
    // @since 2023/04/07
    public int updateEventArticle(Map<String, Object> param);
    //@since 2023/03/14
    public int updateQnaArticle(@Param("cs_reply") String cs_reply, @Param("cs_no") int cs_no);
    //@since 2023/03/14
    public int updateAdminNotice(CsVO vo);
    //@since 2023/03/16
    public int updateFaqArticle(CsVO vo);
    //@since 2023/03/29
    public int updateMainBanner(@Param("cs_eventMainBannerState") int cs_eventMainBannerState, @Param("cs_no") int cs_no);
    //@since 2023/03/30
    public int updateEventBanner(@Param("cs_eventBannerState") int cs_eventBannerState, @Param("cs_no") int cs_no);
    //@since 2023/04/15 약관 수정
    public int updateTerms(TermVO vo);


    /** delete **/
    //@since 2023/03/15 관리자 - 단일 게시글 삭제
    public int deleteFaqWrite(@Param("cs_no") int cs_no);
    //@since 2023/03/27 관리자 - qna 선택 삭제
    public int deleteQnaList(@Param("checkList") List<String> checkList);
    //@since 2023/04/12 관리자 - 약관 삭제
    public int deleteTerm(@Param("terms_no") int terms_no);
}
