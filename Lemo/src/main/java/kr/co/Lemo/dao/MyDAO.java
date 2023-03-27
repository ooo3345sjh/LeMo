package kr.co.Lemo.dao;

import kr.co.Lemo.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    // @since 2023/03/11
    public int insertDiarySpot(DiarySpotVO spotVO);

    // @since 2023/03/13
    public List<ArticleDiaryVO> selectDiaryArticle(String uid);

    // @since 2023/03/13
    public List<DiarySpotVO> selectDiarySpot(int arti_no);

    // @since 2023/03/13
    public List<DiarySpotVO> selectDiary(String user_id);

    // @since 2023/03/27
    public ProductAccommodationVO selectProvinceAddr(int res_no);

    // @since 2023/03/24
    // public MyVO selectMyArticle(@Param("myCate") String myCate, @Param("user_id") String user_id);

    // @since 2023/03/27
    public List<CouponVO> selectMemberCoupons(String user_id);

    // @since 2023/03/27
    public List<CouponVO> selectProductCoupons(String user_id);

    // @since 2023/03/24
    public List<ProductAccommodationVO> selectPicks(String user_id);

    // @since 2023/03/24
    public List<ReservationVO> selectReservations(@Param("user_id")String user_id, @Param("myCate") String myCate);

    //@since 2023/03/27
    public List<PointVO> selectPoints(String user_id);

    //@since 2023/03/27
    public List<ReviewVO> selectReviews(String user_id);

    // @since 2023/03/27
    public int insertCoupon(CouponVO coupon);

}