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

    // @since 2023/04/14
    int countByBizRegNum (@Param("bizRegNum")String bizRegNum) throws Exception;


    // update
    // @since 2023/03/25
    int usaveUserPw(@Param("user_id")String username, @Param("pass")String password) throws Exception;

    // @since 2023/03/27
    int updateProfile(@Param("photo") String photo, @Param("username")String username) throws Exception;
    int updateNick(@Param("nick") String nick, @Param("username")String username) throws Exception;
    int updateHp(@Param("hp") String hp, @Param("username")String username) throws Exception;
    int updateIsNoticeEnabled(@Param("isNoticeEnabled") int isNoticeEnabled, @Param("username")String username) throws Exception;

    /**
     * @since 2023/04/12
     * @apiNote 매시간마다 탈퇴한지 24시간이 지난 회원ID 변경
     */
    int updateWithdrawUserId(@Param("username")String username) throws Exception;

    /**
     * @since 2023/04/28
     * @apiNote 매일 자정 숙박완료 10회이상 회원 level 업데이트
     */
    int usaveLevel() throws Exception;
}
