package kr.co.Lemo.dao;

import kr.co.Lemo.domain.BusinessInfoVO;
import kr.co.Lemo.domain.UserVO;
import kr.co.Lemo.entity.UserEntity;
import kr.co.Lemo.entity.UserInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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
    int saveBusinessInfo(BusinessInfoVO businessInfoVO) throws Exception;

    // select
    // @since 2023/03/19
    UserInfoEntity findByEmail (@Param("email")String email) throws Exception;

    // @since 2023/03/16
    int countByEmail (@Param("email")String email) throws Exception;

    // @since 2023/03/16
    int countByNick (@Param("nick")String nick) throws Exception;


    // update

    // delete


}
