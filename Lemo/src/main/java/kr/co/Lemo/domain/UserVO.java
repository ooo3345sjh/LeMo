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
    // lemo_member_userinfo
    private String user_id;
    private String hp;
    private String nick;
    private String photo;
    private int type = 1;
    private String role;
    private int level;
    private int point;
    private String regip;
    private int isEnabled = 1;
    private int isLocked = 1;
    private int isPassNonExpired = 1;
    private int isNoticeEnabled = 0;
    private int isLocationEnabled = 0;
    private int isPrivacySelected = 0;
    private String memo;
    private String udate;
    private String rdate;
    private String wdate;

    // lemo_member_user
    private String pass;

    // lemo_member_social
    private String id;
    private String soci_type;
    private String soci_token;
    private String email;
}
