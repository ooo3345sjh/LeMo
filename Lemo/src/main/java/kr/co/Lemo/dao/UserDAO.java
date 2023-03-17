package kr.co.Lemo.dao;

import kr.co.Lemo.domain.ProductAccommodationVO;
import kr.co.Lemo.domain.UserVO;
import kr.co.Lemo.utils.SearchCondition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @since 2023/03/14
 * @author 서정현
 * @apiNote user dao
 */

@Mapper
@Repository
public interface UserDAO {

    // insert
    int saveUserInfo(UserVO user) throws Exception;
    int saveHomeUser(@Param("user_id")String user_id, @Param("pass")String pass) throws Exception;

    // select
    int countByEmail (@Param("email")String email) throws Exception;
    int countByNick (@Param("nick")String nick) throws Exception;

    // @since 2022/03/09

    // update

    // delete


}
