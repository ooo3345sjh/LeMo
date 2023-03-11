package kr.co.Lemo.dao;

import kr.co.Lemo.domain.CouponVO;
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
    // select
    public List<UserVO> selectUser(SearchCondition sc);
    public int countUser(SearchCondition sc);

    // insert
    public void insertCupon(CouponVO vo);

    // update
    public int updateMemo(@Param("memo") String memo, @Param("user_id") String user_id);
    public int updateIsLocked(@Param("user_id") String user_id);
    public int updateIsEnabled(@Param("user_id") String user_id);

    // delete
}