package kr.co.Lemo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

/**
 * @since 2023/03/19
 * @author 서정현
 * @apiNote lemo_member_social entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="lemo_member_social")
public class SocialEntity implements UserDetails, OAuth2User {

    @Id
    @Column(name="user_id", insertable = false, updatable = false)
    private String user_id;
    private String soci_typ;
    private String soci_token;
    private String email;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name="user_id"))
    private UserInfoEntity userInfoEntity;

    @Transient
    private Provider provider;

    @Transient
    private Map<String, Object> attrubutes;

    @Override
    public Map<String, Object> getAttributes() {
        return attrubutes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 계정이 갖는 권한 목록
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
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

    @Override
    public String getName() {
        return soci_typ.equals("google")? attrubutes.get("sub").toString() : attrubutes.get("id").toString();
    }

    public static enum Provider {
        google{
            public SocialEntity convert(OAuth2User user){
                return SocialEntity.builder()
                        .user_id(format("%s_%s", name(), user.getAttribute("sub")))
                        .provider(google)
                        .email(user.getAttribute("email"))
                        .soci_typ("google")
                        .attrubutes(user.getAttributes())
                        .build();
            }
        },
        naver{
            public SocialEntity convert(OAuth2User user){
                Map<String, Object> resp = user.getAttribute("response");
                return SocialEntity.builder()
                        .user_id(format("%s_%s", name(), resp.get("id")))
                        .provider(naver)
                        .email(""+resp.get("email"))
                        .soci_typ("naver")
                        .attrubutes(resp)
                        .build();
            }
        },
        kakao{
            public SocialEntity convert(OAuth2User user){
                return SocialEntity.builder()
                        .user_id(format("%s_%s", name(), user.getAttribute("id")))
                        .provider(kakao)
                        .email(""+user.getAttribute("email"))
                        .soci_typ("kakao")
                        .attrubutes(user.getAttributes())
                        .build();
            }
        };

        public abstract SocialEntity convert(OAuth2User userInfo);
    }
}
