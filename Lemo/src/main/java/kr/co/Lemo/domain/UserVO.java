package kr.co.Lemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @since 2023/03/08
 * @author 이원정
 * @apiNote lemo_member_user (회원) VO
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserVO {
    private String userId_id;
    private String user_pass;
    private String user_photo;
    private String user_hp;
    private int user_hp_certi;
    private String user_role;
    private int user_level;
    private int user_point;
    private String user_regip;
    private String user_udate;
    private String user_rdate;
    private String user_wdate;
    private int user_isEnabled;
    private int user_isLocked;
    private int user_isPassNonExpired;
    private int user_isNoticeEnabled;
    private int user_isLocationEnabled;
    private int user_isPrivacySelected;
    private String user_memo;





}
