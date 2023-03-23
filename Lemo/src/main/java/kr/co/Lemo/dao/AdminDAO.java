package kr.co.Lemo.dao;

import kr.co.Lemo.domain.CouponVO;
import kr.co.Lemo.domain.ReviewVO;
import kr.co.Lemo.domain.UserVO;
import kr.co.Lemo.utils.SearchCondition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
/*
    날짜 : 2023/03/08
    이름 : 이원정
    내용 : 관리자 DAO
*/
@Repository
@Mapper
public interface AdminDAO {
    // @since 2023/03/09
    public List<UserVO> selectUser(SearchCondition sc);

    // @since 2023/03/09
    public int countUser(SearchCondition sc);

    // @since 2023/03/12
    public List<CouponVO> selectCoupon(SearchCondition sc);

    // @since 2023/03/12
    public int countCoupon(SearchCondition sc);

    // @since 2023/03/12
    public List<CouponVO> selectAccOwned(String user_id);

    // @since 2023/03/14
    public List<ReviewVO> selectReview(SearchCondition sc);

    // @since 2023/03/14
    public int countReview(SearchCondition sc);

    // @since 2023/03/15
    public ReviewVO viewReview(Integer revi_id);

    // @since 2023/03/11
    public void insertCoupon(CouponVO vo);

    // @since 2023/03/10
    public int updateMemo(@Param("memo") String memo, @Param("user_id") String user_id);

    // @since 2023/03/10
    public int updateIsLocked(@Param("user_id") String user_id);

    // @since 2023/03/23
    public int updateIsLockedClear(@Param("user_id") String user_id);



    // @since 2023/03/15
    public int updateReply(@Param("revi_reply") String revi_reply, @Param("revi_id") String revi_id);

    // @since 2023/03/12
    public int deleteCoupon(@Param("cp_id") String cp_id);

    // @since 2023/03/15
    public int deleteReview(@Param("revi_id") String revi_id);

}