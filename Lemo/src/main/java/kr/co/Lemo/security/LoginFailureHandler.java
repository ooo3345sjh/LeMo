package kr.co.Lemo.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @since 2023/03/22
 * @author 서정현
 * @apiNote LoginFailurHandler
 */
@Slf4j
@Component
@Configuration
public class LoginFailureHandler implements AuthenticationFailureHandler {

    protected final RequestCache requestCache = new HttpSessionRequestCache();
    protected final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.debug(exception.getMessage());
        log.debug(exception.getStackTrace().toString());
        log.debug(exception.getClass().toString());
        log.debug("isLockedException : "+String.valueOf(exception.getClass() == LockedException.class));
        log.debug("isDisabledException : "+String.valueOf(exception.getClass() == DisabledException.class));
        String error = null;

        if(exception.getClass() == DisabledException.class)
            error = "W";

        else if(exception.getClass() == LockedException.class)
            error = "L";

        else
            error = "500";

        redirectStrategy.sendRedirect(request, response, "/user/login/error?error="+error);
    }
}
