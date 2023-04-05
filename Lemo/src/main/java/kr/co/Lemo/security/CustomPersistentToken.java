package kr.co.Lemo.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * @since 2023/03/24
 * @author 서정현
 * @apiNote CustomPersistentToken
 */
public class CustomPersistentToken extends PersistentTokenBasedRememberMeServices {

    public CustomPersistentToken(String key, UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository) {
        super(key, userDetailsService, tokenRepository);
    }

    public void addCookie(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication){
        super.onLoginSuccess(request, response, successfulAuthentication);
    }
}
