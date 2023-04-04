package kr.co.Lemo.dao;

import kr.co.Lemo.domain.*;
import kr.co.Lemo.utils.SearchCondition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @since  2023/03/13
 * @author 이원정
 * @apiNote 판매자 DAO
 */


@Repository
@Mapper
public interface BusinessDAO {

    // @since 2023/03/13
   public List<CouponVO> selectCoupon(SearchCondition sc);

    // @since 2023/03/13
    public int countCoupon(SearchCondition sc);

    // @since 2023/03/13
    public List<CouponVO> selectAccOwned(String user_id);

    // @since 2023/03/16
    public List<ReviewVO> selectReview(SearchCondition sc);

    // @since 2023/03/16
    public int countReview(SearchCondition sc);

    // @since 2023/03/16
    public ReviewVO viewReview(Integer revi_id);

    // @since 2023/03/16
    public List<ReviewVO> selectAccOwnedForReview(String user_id);

    // @since 2023/03/17
    public List<ServiceCateVO> selectService();

    // @since 2023/03/20
    public List<ProvinceVO> selectProvince();

    // @since 2023/03/23 판매자 숙박 목록
    public List<ProductAccommodationVO> selectAccForInfo(SearchCondition sc);

    // @since 2023/03/23 판매자 숙박 목록 페이징
    public int countAcc(SearchCondition sc);

    // @since 2023/03/23 판매자 소유 숙박 목록 보기
   public List<ProductAccommodationVO> selectAccOwnedForInfo(String user_id);

   // @since 2023/03/31 판매자 숙박 보기
    public ProductAccommodationVO viewAcc(Integer acc_id);

    // @since 2023/03/31 판매자 숙박 보기 - 서비스 카테고리
    public List<ServicereginfoVO> selectServiceInAcc(Integer acc_id);

    // @since 2023/04/02 판매자 객실 목록
    public List<ProductRoomVO> selectRoom(SearchCondition sc);

    // @since 2023/04/02 판매자 객실 목록 페이징
    public int countRoom(SearchCondition sc);

    // @since 2023/04/03 (판매자 객실 보기)
    public ProductRoomVO viewRoom(Integer room_id);

    // @since 2023/03/13
    public void insertCoupon(Map<String, Object> param);

    // @since 2023/03/20 판매자 숙소 등록
    public int insertInfo(Map<String, Object> param);

    // @since 2023/03/20 판매자 숙소 등록 (할인율)
    public int insertRatePolicy(Map<String, Object> param);

    // @since 2023/03/20 판매자 숙소 등록 (서비스)
    public int insertServiceRegInfo(Map<String, Object> param);

    // @since 2023/04/02 판매자 객실 등록
    public int insertRoom(Map<String, Object> param);

    // @since 2023/04/01 판매자 숙소 수정
    public int updateInfo(Map<String, Object> param);

    // @since 2023/04/01 판매자 숙소 수정 (할인율)
    public int updateRatePolicy(Map<String, Object> param);

    // @since 2023/04/01 판매자 숙소 수정 (서비스)
    public int updateServiceRegInfo(Map<String, Object> param);

    // @since 2023/03/16
    public int updateReply(@Param("revi_reply") String revi_reply, @Param("revi_id") String revi_id);

    // @since 2023/04/01
    public int deleteServiceRegInfo(Map<String, Object> param);

    // @since 2023/03/13
    public int deleteCoupon(@Param("cp_id") String cp_id);

   // @since 2023/03/16
   public int deleteReview(@Param("revi_id") String revi_id);

   // @since 2023/03/23
   public int deleteAcc(@Param("acc_id") String acc_id);

   // @since 2023/04/04
   public int deleteRoom(@Param("room_id") String room_id);


}
