package kr.co.Lemo.dao;

import kr.co.Lemo.domain.*;
import kr.co.Lemo.utils.SearchCondition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @since 2023/03/11
 * @author 박종협
 * @apiNote MyDAO
 */
@Repository
@Mapper
public interface MyDAO {

    // @since 2023/03/11
    public int insertDiaryArticle(ArticleDiaryVO diaryVO);
    public int insertDiarySpot(DiarySpotVO spotVO);

    // @since 2023/03/13
    public List<ArticleDiaryVO> selectDiaryArticle(String uid);
    public List<DiarySpotVO> selectDiarySpot(int arti_no);
    public List<DiarySpotVO> selectDiary(String user_id);

    // @since 2023/03/24
    public List<ProductAccommodationVO> selectPicks(SearchCondition sc);
    public List<ReservationVO> selectReservations(SearchCondition sc);

    // @since 2023/03/27
    public ProductAccommodationVO selectProvinceAddr(long res_no);
    public List<CouponVO> selectMemberCoupons(String user_id);
    public List<CouponVO> selectProductCoupons(String user_id);
    public int selectTotalReservations(SearchCondition sc);
    public List<PointVO> selectPoints(SearchCondition sc);
    public int selectTotalPoints(SearchCondition sc);
    public List<ReservationVO> selectReviews(SearchCondition sc);
    public int selectTotalPicks(SearchCondition sc);

    public int insertCoupon(CouponVO coupon);
    public int updateProductCoupon(CouponVO coupon);
    public CouponVO selectProductCouponCnt(CouponVO coupon);

    // @since 2023/03/28
    public int selectTotalReviews(SearchCondition sc);
    public ReviewVO selectReview(@Param("res_no") long res_no, @Param("user_id") String user_id);
    public ReviewVO selectReviewAccommodation(@Param("res_no") long res_no, @Param("user_id") String user_id);
    public int insertReview(Map<String, Object> param);

    // @since 2023/03/29
    public ReservationVO selectReservation(@Param("res_no") long res_no, @Param("user_id") String user_id);
    public int deleteReservation(long res_no);
    public int selectCheckReview(long res_no);
    public int selectCheckDiary(long res_no);


    /**
     * @since 2023/04/11
     * @author 서정현
     * @apiNote 회원 포인트 조회
     */
    int selectUserPoint (@Param("user_id")String user_id);
    /**
     * @since 2023/04/11
     * @author 서정현
     * @apiNote 회원 쿠폰 갯수 조회
     */
    int selectUserCoupon (@Param("user_id")String user_id);
    /**
     * @since 2023/04/11
     * @author 서정현
     * @apiNote 회원 탈퇴 업데이트
     */
    int updateWithdrawUser (@Param("user_id")String user_id);

    /**
     * @since 2023/04/11
     * @author 서정현
     * @apiNote 회원이 보유한 쿠폰 삭제
     */
    int deleteUserCoupon (@Param("user_id")String user_id);

    /***
     * @since 2023/04/11
     * @author 이해빈
     * @apNote 찜한 숙소 삭제
     */
    public int deletePick(Map map);

    /***
     * @since 2023/04/12
     * @author 이해빈
     * @apNote 숙박 문의 삭제
     */
    public int deleteQna(Map map);


    // @since 2023/03/30
    public int updateReservationState(long res_no);

    // @since 2023/03/31
    public ProductAccommodationVO selectDiaryXY(long res_no);
    public String selectReservationImpUid(long res_no);

    // @since 2023/04/05
    public List<ProductQnaVO> selectDiaryQna(SearchCondition sc);
    public int selectDiaryQnaCnt(SearchCondition sc);

    // @since 2023/04/06
    public String selectCheckReviewId(long res_no);
    public int updateReview(Map<String, Object> param);
    public int deleteReview(long res_no);

    // @since 2023/04/12
    public int updateDiaryArticle(ArticleDiaryVO diaryVO);

    public int updateDiarySpot(DiarySpotVO spotVO);

}