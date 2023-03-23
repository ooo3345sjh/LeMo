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

    // @since 2023/03/23
    public List<ProductAccommodationVO> selectAccForInfo(SearchCondition sc);

    // @since 2023/03/23
    public int countAcc(SearchCondition sc);

    // @since 2023/03/23
   public List<ProductAccommodationVO> selectAccOwnedForInfo(String user_id);

    // @since 2023/03/13
    public void insertCoupon(CouponVO vo);

    // @since 2023/03/20
    public int insertInfo(Map<String, Object> param);

    // @since 2023/03/20
    public int insertRatePolicy(Map<String, Object> param);

    // @since 2023/03/20
    public int insertServiceRegInfo(Map<String, Object> param);

    // @since 2023/03/16
    public int updateReply(@Param("revi_reply") String revi_reply, @Param("revi_id") String revi_id);

    // @since 2023/03/13
    public int deleteCoupon(@Param("cp_id") String cp_id);

   // @since 2023/03/16
   public int deleteReview(@Param("revi_id") String revi_id);

   // @since 2023/03/23
   public int deleteAcc(@Param("acc_id") String acc_id);


}
