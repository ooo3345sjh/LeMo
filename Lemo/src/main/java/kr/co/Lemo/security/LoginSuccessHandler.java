package kr.co.Lemo.security;

import kr.co.Lemo.entity.SocialEntity;
import kr.co.Lemo.entity.UserEntity;
import kr.co.Lemo.entity.UserInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
/**
 * @since 2023/03/21
 * @author 서정현
 * @apiNote LoginSuccessHandler
 */
@Component
public class LoginSuccessHandler {

    protected final RequestCache requestCache = new HttpSessionRequestCache();
    protected final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    // @since 2023/03/21
    public String loginSuccessPage(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException
    {
        clearSession(request);

        SavedRequest savedRequest = requestCache.getRequest(request, response);

        /**
         * prevPage가 존재하는 경우 = 사용자가 직접 /login 경로로 로그인 요청
         * 기존 Session의 prevPage attribute 제거
         */
        String prevPage = (String) request.getSession().getAttribute("prevPage");
        if (prevPage != null) {
            request.getSession().removeAttribute("prevPage");
        }

        // 기본 URI
        String uri = "/";

        /**
         * savedRequest 존재하는 경우 = 인증 권한이 없는 페이지 접근
         * Security Filter가 인터셉트하여 savedRequest에 세션 저장
         */

        System.out.println("savedRequest = " + savedRequest);
        System.out.println("prevPage = " + prevPage);
        if (savedRequest != null) {
            uri = savedRequest.getRedirectUrl();
        }
        else if (prevPage != null && !prevPage.equals("")) {
            // 회원가입 - 로그인으로 넘어온 경우 "/"로 redirect
            if (prevPage.contains("/user/signup")) {
                uri = "/";
            } else {
                uri = prevPage;
            }
        }
        Object fromSignup = request.getSession().getAttribute("fromSignup");
        if(fromSignup != null){
            uri = "/";
            request.getSession().removeAttribute("fromSignup");
        }

        System.out.println("uri = " + uri);
        return uri;

    }

    /**
     * @since 2023/03/21
     * @param request
     * @apiNote 로그인 실패 후 성공 시 남아있는 에러 세션 제거
     */
    protected void clearSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}
