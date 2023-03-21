package kr.co.Lemo.security;

import kr.co.Lemo.entity.UserEntity;
import kr.co.Lemo.entity.UserInfoEntity;
import kr.co.Lemo.repository.SocialRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @since 2023/03/21
 * @author 서정현
 * @apiNote SocialLoginSuccessHandler
 */

@Component
public class SocialLoginSuccessHandler extends LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private SocialRepo socialRepo;

    // @since 2023/03/21
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException
    {
        Object principal = authentication.getPrincipal();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(principal, null, List.of(new SimpleGrantedAuthority("ROLE_USER")))
        );
        loginSuccessPage(request, response);

    }

    @Override
    public void loginSuccessPage(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        super.loginSuccessPage(request, response);
    }
}
