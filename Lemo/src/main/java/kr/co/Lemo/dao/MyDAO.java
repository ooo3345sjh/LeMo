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
    public ProductAccommodationVO selectProvinceAddr(int res_no);
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
    public ReviewVO selectReview(@Param("res_no") int res_no, @Param("user_id") String user_id);
    public ReviewVO selectReviewAccommodation(@Param("res_no") int res_no, @Param("user_id") String user_id);
    public int insertReview(Map<String, Object> param);

    // @since 2023/03/29
    public ReservationVO selectReservation(@Param("res_no") int res_no, @Param("user_id") String user_id);
    public int deleteReservation(int res_no);
    public int selectCheckReview(int res_no);
    public int selectCheckDiary(int res_no);

    // @since 2023/03/30
    public int updateReservationState(int res_no);

    // @since 2023/03/31
    public ProductAccommodationVO selectDiaryXY(int res_no);
    public String selectReservationImpUid(int res_no);

    // @since 2023/04/05
    public List<ProductQnaVO> selectDiaryQna(SearchCondition sc);
    public int selectDiaryQnaCnt(SearchCondition sc);

    // @since 2023/04/06
    public String selectCheckReviewId(int res_no);
    public int updateReview(Map<String, Object> param);
    public int deleteReview(int res_no);

}