package kr.co.Lemo.dao;

import kr.co.Lemo.domain.*;
import kr.co.Lemo.utils.SearchCondition;
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
    public List<ReviewVO> selectReviews(String user_id);
    public int selectTotalPicks(SearchCondition sc);

    public int insertCoupon(CouponVO coupon);
    public int updateProductCoupon(CouponVO coupon);
    public int selectProductCouponCnt(CouponVO coupon);

    // update
    int updateProfile(@Param("photo") String photo, @Param("username")String username) throws Exception;

}