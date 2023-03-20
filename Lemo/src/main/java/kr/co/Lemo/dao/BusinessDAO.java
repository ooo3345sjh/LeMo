package kr.co.Lemo.dao;

import kr.co.Lemo.domain.CouponVO;
import kr.co.Lemo.domain.ProvinceVO;
import kr.co.Lemo.domain.ReviewVO;
import kr.co.Lemo.domain.ServiceCateVO;
import kr.co.Lemo.utils.SearchCondition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
    날짜 : 2023/03/13
    이름 : 이원정
    내용 : 판매자 DAO
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

    // @since 2023/03/13
    public void insertCoupon(CouponVO vo);

    // @since 2023/03/016
    public int updateReply(@Param("revi_reply") String revi_reply, @Param("revi_id") String revi_id);

    // @since 2023/03/13
    public int deleteCoupon(@Param("cp_id") String cp_id);

   // @since 2023/03/16
   public int deleteReview(@Param("revi_id") String revi_id);


}
