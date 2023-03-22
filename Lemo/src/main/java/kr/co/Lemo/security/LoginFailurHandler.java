package kr.co.Lemo.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @since 2023/03/22
 * @author 서정현
 * @apiNote LoginFailurHandler
 */
@Slf4j
@Component
public class LoginFailurHandler implements AuthenticationFailureHandler {

    protected final RequestCache requestCache = new HttpSessionRequestCache();
    protected final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.debug(exception.getMessage());
        log.debug(exception.getStackTrace().toString());
        log.debug(exception.getClass().toString());
        log.debug("isLockedException : "+String.valueOf(exception.getClass() == LockedException.class));
        log.debug("isDisabledException : "+String.valueOf(exception.getClass() == DisabledException.class));

        redirectStrategy.sendRedirect(request, response, "/user/login?error=L");
    }
}
