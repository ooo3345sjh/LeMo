package kr.co.Lemo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @since 2023/03/19
 * @author 서정현
 * @apiNote lemo_member_userinfo entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="lemo_member_userinfo")
public class UserInfoEntity implements UserDetails {

    @Id
    @Column(name="user_id", insertable = false, updatable = false)
    private String user_id;
    private String hp;
    private String nick;
    private String photo;
    private int type;
    private String role;
    private int level;
    private int point;
    private String regip;
    private int isenabled;
    private int isLocked;
    private int isPassNonExpired;
    private int isNoticeEnabled;
    private int isLocationEnabled;
    private int isPrivacySelected;
    private String memo;
    private String udate;
    private String rdate;
    private String wdate;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name="user_id"))
    private UserEntity userEntity;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 계정이 갖는 권한 목록
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        return authorities;
    }

    @Override
    public String getPassword() {
        return userEntity.getPass();
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
        return isenabled == 1;
    }
}
