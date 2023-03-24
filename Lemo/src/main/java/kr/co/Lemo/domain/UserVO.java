package kr.co.Lemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @since 2023/03/08
 * @author 이원정
 * @apiNote lemo_member_user (회원) VO
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserVO implements UserDetails {
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
    private String soci_type;
    private String soci_token;
    private String soci_email;

    // lemo_member_businessinfo
    private BusinessInfoVO businessInfoVO;

    private Object details;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_"+role));
        return authorities;
    }

    @Override
    public String getPassword() {
        return pass;
    }

    @Override
    public String getUsername() {
        return user_id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isLocked == 1;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isPassNonExpired == 1;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled == 1;
    }

}
