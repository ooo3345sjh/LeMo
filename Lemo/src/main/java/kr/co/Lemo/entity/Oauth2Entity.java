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
import java.time.LocalDateTime;
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
public class Oauth2Entity {

    @Id
    @Column(name="user_id", insertable = false, updatable = false)
    private String user_id;
    private String soci_typ;
    private String soci_token;
    private String email;
    private Provider provider;

    public static enum Provider {
        google{
            public Oauth2Entity convert(OAuth2User user){
                return Oauth2Entity.builder()
                        .user_id(format("%s_%s", name(), user.getAttribute("sub")))
                        .provider(google)
                        .email(user.getAttribute("email"))
                        .soci_typ("google")
                        .build();
            }
        },
        naver{
            public Oauth2Entity convert(OAuth2User user){
                Map<String, Object> resp = user.getAttribute("response");
                return Oauth2Entity.builder()
                        .user_id(format("%s_%s", name(), resp.get("id")))
                        .provider(naver)
                        .email(""+resp.get("email"))
                        .soci_typ("naver")
                        .build();
            }
        },
        kakao{
            public Oauth2Entity convert(OAuth2User user){
                Map<String, Object> resp = user.getAttribute("response");
                return Oauth2Entity.builder()
                        .user_id(format("%s_%s", name(), resp.get("id")))
                        .provider(kakao)
                        .email(""+resp.get("email"))
                        .soci_typ("kakao")
                        .build();
            }
        };

        public abstract Oauth2Entity convert(OAuth2User userInfo);
    }
}
