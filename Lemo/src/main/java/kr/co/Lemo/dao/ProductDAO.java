package kr.co.Lemo.dao;

import kr.co.Lemo.domain.*;
import kr.co.Lemo.utils.SearchCondition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @since 2023/03/09
 * @author 이해빈
 * @apiNote product dao
 */

@Mapper
@Repository
public interface ProductDAO {

    // insert

    // @since 2023/03/21
    public int insertQna(ProductQnaVO qna);

    // @since 2023/03/24
    public int insertProductPick(Map map);

    // @since 2023/03/24
    public int insertMemberCoupon(Map map);

    // @since 2023/03/31
    public int insertProductReservation(OrderInfoVO vo);

    // @since 2023/03/31
    public int insetProductReservedRoom(OrderInfoVO vo);

    // @since 2023/03/31
    public int insertMemberCouponLog(OrderInfoVO vo);

    //@since 2023/03/31
    public int insertMemberPointLog(PointVO vo);

    //@since 2023/03/31
    public int insertMemberPointDetailLog(PointDetailVO vo);

    //@since 2023/04/19
    public void insertSavePointLog();


    // select
    public int countTotal(SearchCondition sc);

    // @since 2023/03/09
    public List<ProductAccommodationVO> selectAccommodations(SearchCondition sc);

    // @since 2023/03/17
    public List<ProductAccommodationVO> selectAccommodation(@Param("acc_id") int acc_id,
                                                            @Param("checkIn")String checkIn, @Param("checkOut") String checkOut);

    // @since 2023/03/19
    public List<ServiceCateVO> selectServiceCates(@Param("acc_id") int acc_id);

    // @since 2023/03/19
    public BusinessInfoVO selectBusinessInfo(@Param("user_id") String user_id);

    // @since 2023/03/20
    public List<ArticleDiaryVO> selectProductDiaries(@Param("acc_id") int acc_id);

    // @since 2023/03/22
    public List<ProductQnaVO> selectProductQnas(SearchCondition sc);

    // @since 2023/03/26
    public List<ReviewVO> selectProductReviews(SearchCondition sc);

    // @since 2023/03/26
    public UserVO selectBusiness(@Param("acc_id") int acc_id);


    // @since 2023/03/22
    public int getTotalProductQna(SearchCondition sc);

    // @since 2023/03/24
    public int getTotalProductDiary(SearchCondition sc);

    // @since 2023/03/26
    public int getTotalProductReview(SearchCondition sc);

    // @since 2023/03/26
    public List<ReviewVO> getTotalProductReviewReply(SearchCondition sc);

    // @since 2023/03/24
    public int selectProductPick(@Param("acc_id")int acc_id, @Param("user_id") String user_id);

    // @since 2023/03/24
    public List<ArticleDiaryVO> selectProductDiaries (SearchCondition sc);

    // @since 2023/03/27
    public List<CouponVO> selectCoupons(Map map);

    // @since 2023/03/28
    public CouponVO getCoupon(Map map);

    // @since 2023/03/29
    public List<CouponVO> selectCouponsForReservation(Map map);

    // @since 2023/03/28
    public ProductAccommodationVO selectRoomForReservation(Map map);

    // @since 2023/04/04
    public TermVO selectTerm(@Param("termsType_no") int termsType_no);

    // @since 2023/04/05
    public UserVO selectUser(@Param("user_id") String user_id);

    // @since 2023/04/05
    public int selectResNo(@Param("user_id") String user_id, @Param("res_no") long res_no);

    // @since 2023/04/06
    public ReservationVO selectOrderInfo( @Param("res_no") long res_no);

    // update

    // @since 2023/03/28
    public int updateProductCoupon(Map map);

    // @since 2023/03/31
    public int updateMemberCoupon(OrderInfoVO vo);

    //@since 2023/03/31
    public int updateMemberUserInfo(@Param("user_id") String user_id);

    /**
     * @since 2023/04/08
     * @author 서정현
     * @apiNote 매일 자정 각 숙소의 평균 별점을 계산후에 업데이트 시키기는 쿼리문
     */
    int updateAvgRate() throws Exception;

    /**
     * @since 2023/04/08
     * @author 서정현
     * @apiNote 매일 자정 예약번호 yyyyMMdd000000로 업데이트
     */
    int usaveResNo(@Param("res_no") Long res_no) throws Exception;

    /**
     * @since 2023/04/23
     * @author 서정현
     * @apiNote 매일 자정에 숙박완료 내역 포인트 적립
     */
    int insertSaveDetailPointLog();
    int updateDetailpointSaveId();

    /**
     * @since 2023/04/23
     * @author 서정현
     * @apiNote 매일 자정에 만료포인트 업데이트
     */
    int expiredPointAll();
    int expriedDetailPointAll();

    /**
     * @since 2023/04/23
     * @author 서정현
     * @apiNote 매일 자정에 모든 회원의 포인트 최신화
     */
    int updateUsersInfoPoint();

    // delete

    // @since 2023/03/24
    public int deleteProductPick(Map map);

    // @since 2023/04/19
    public void updateProductReservation();

}
