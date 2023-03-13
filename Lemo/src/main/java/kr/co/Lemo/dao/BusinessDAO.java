package kr.co.Lemo.dao;

import kr.co.Lemo.domain.CouponVO;
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
    public void insertCoupon(CouponVO vo);

    // @since 2023/03/13
    public int deleteCoupon(@Param("cp_id") String cp_id);

    // @since 2023/03/13
    public List<CouponVO> selectAccOwned(String user_id);

}
