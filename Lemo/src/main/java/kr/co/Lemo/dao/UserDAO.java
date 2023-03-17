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

    // @since 2023/03/16
    int saveUserInfo(UserVO user) throws Exception;

    // @since 2023/03/16
    int saveHomeUser(@Param("user_id")String user_id, @Param("pass")String pass) throws Exception;

    // select

    // @since 2023/03/16
    int countByEmail (@Param("email")String email) throws Exception;

    // @since 2023/03/16
    int countByNick (@Param("nick")String nick) throws Exception;


    // update

    // delete


}
