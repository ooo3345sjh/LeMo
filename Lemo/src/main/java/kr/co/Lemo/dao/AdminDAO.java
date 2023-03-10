package kr.co.Lemo.dao;

import kr.co.Lemo.domain.UserVO;
import kr.co.Lemo.utils.SearchCondition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AdminDAO {
    public List<UserVO> selectUser(SearchCondition sc);
    public int countUser(SearchCondition sc);
    public int updateMemo(@Param("memo") String memo, @Param("user_id") String user_id);
    public int updateIsLocked(@Param("user_id") String user_id);
    public int updateIsEnabled(@Param("user_id") String user_id);

}