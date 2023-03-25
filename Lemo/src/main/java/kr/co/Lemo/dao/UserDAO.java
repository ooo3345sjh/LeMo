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
    int rsaveUserInfo(UserVO user) throws Exception;

    // @since 2023/03/16
    int rsaveHomeUser(@Param("user_id")String user_id, @Param("pass")String pass) throws Exception;
    int rsaveBusinessInfo(BusinessInfoVO businessInfoVO) throws Exception;

    // select
    // @since 2023/03/26
    UserInfoEntity findByEmailAndType1 (@Param("email")String email) throws Exception;

    // @since 2023/03/16
    int countByEmail (@Param("email")String email) throws Exception;

    // @since 2023/03/26
    int countByEmailAndType1  (@Param("email")String email) throws Exception;

    // @since 2023/03/16
    int countByNick (@Param("nick")String nick) throws Exception;


    // update
    int usaveUserPw(@Param("user_id")String username, @Param("pass")String password) throws Exception;

    // delete


}
