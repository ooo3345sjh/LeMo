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

/**
 * @since 2023/03/19
 * @author 서정현
 * @apiNote lemo_member_user entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="lemo_member_user")
public class UserEntity implements UserDetails {
    @Id
    @Column(name="user_id", insertable = false, updatable = false)
    private String user_id;
    private String pass;
    private String pass_udate;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name="user_id"))
    private UserInfoEntity userInfoEntity;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name="user_id"))
    private BusinessInfoEntity businessInfoEntity;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 계정이 갖는 권한 목록
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + userInfoEntity.getRole()));
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
        return userInfoEntity.getIsLocked() == 1;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return userInfoEntity.getIsPassNonExpired() == 1;
    }

    @Override
    public boolean isEnabled() {
        return userInfoEntity.getIsEnabled() == 1;
    }
}
