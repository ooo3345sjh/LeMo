package kr.co.Lemo.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.Collection;


/**
 * @since 2023/03/24
 * @author 서정현
 * @apiNote CustomAuthenticationToken
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomAuthenticationToken implements Authentication {

    private UserDetails principal;
    private String  credentials;
    private Object details;
    private boolean authenticated;
    private String userName;
    private String password;

    public CustomAuthenticationToken(String userName, String password){
        this.userName = userName;
        this.password = password;
        setAuthenticated(false);
    }

    public void setDetailes(String remoteAddress, String sessionId){
        WebAuthenticationDetails details = new WebAuthenticationDetails(remoteAddress, sessionId);
        this.details = details;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return principal.getAuthorities();
    }

    @Override
    public String getName() {
        return principal == null ? "" : principal.getUsername();
    }
}
