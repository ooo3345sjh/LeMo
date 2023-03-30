package kr.co.Lemo.security;

import kr.co.Lemo.domain.BusinessInfoVO;
import kr.co.Lemo.domain.UserVO;
import kr.co.Lemo.entity.BusinessInfoEntity;
import kr.co.Lemo.entity.SocialEntity;
import kr.co.Lemo.entity.UserEntity;
import kr.co.Lemo.entity.UserInfoEntity;
import kr.co.Lemo.repository.SocialRepo;
import kr.co.Lemo.utils.RemoteAddrHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

/**
 * @since 2023/03/21
 * @author 서정현
 * @apiNote SocialLoginSuccessHandler
 */

@Slf4j
@Component
public class SocialLoginSuccessHandler extends LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private SocialRepo socialRepo;

    @Autowired
    private DataSource dataSource;

    // @since 2023/03/21
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException
    {
        Object principal = authentication.getPrincipal();
        SocialEntity socialEntity = (SocialEntity) principal;
        SocialEntity user = socialRepo.findById(socialEntity.getUser_id()).orElse(null);
        HttpSession session = request.getSession();

        if(user == null){
            JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
            repository.setDataSource(dataSource);
            repository.removeUserTokens(socialEntity.getUser_id());

            SecurityContextHolder.getContext().setAuthentication(
                    new AnonymousAuthenticationToken("anonymousUser", principal, List.of(new SimpleGrantedAuthority("ROLE_ANONYMOUS")))
            );

            session.setMaxInactiveInterval(30*60);
            session.setAttribute("principal", principal);
            response.sendRedirect(request.getContextPath() + "/user/social/signup");
            return;
        }

        if(!user.isEnabled()){
            redirectStrategy.sendRedirect(request, response, "/user/login/error?error=W");
            return;
        }

        else if(!user.isAccountNonLocked()){
            redirectStrategy.sendRedirect(request, response, "/user/login/error?error=L");
            return;
        }

        session.removeAttribute("principal");
        UserVO userVO = userVoConvert(user);
        userVO.setDetails(new WebAuthenticationDetails(RemoteAddrHandler.getRemoteAddr(request), request.getSession().getId()));
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userVO, null, userVO.getAuthorities())
        );

        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if(savedRequest != null){
            String uri = isUriManagement(savedRequest.getRedirectUrl(), request, user.getUserInfoEntity().getRole());
            if(uri != null){
                redirectStrategy.sendRedirect(request, response, uri);
                return;
            }
        }

        String uri = loginSuccessPage(request, response);
        redirectStrategy.sendRedirect(request, response, uri);
    }

    /**
     *
     * @since 2023/03/21
     * @param principal 회원 인증된 객체
     * @return userVO
     * @apiNote SocialEntity -> UserVO 객체로 변환한 객체를 반환
     */
    private static UserVO userVoConvert(Object principal) {
        SocialEntity user = (SocialEntity) principal;
        UserInfoEntity userInfo = user.getUserInfoEntity();

        UserVO userVO = UserVO.builder()
                .user_id(user.getUser_id())
                .pass(null)
                .nick(userInfo.getNick())
                .hp(userInfo.getHp())
                .photo(userInfo.getPhoto())
                .type(userInfo.getType())
                .role(userInfo.getRole())
                .level(userInfo.getLevel())
                .point(userInfo.getPoint())
                .regip(userInfo.getRegip())
                .isEnabled(userInfo.getIsEnabled())
                .isLocked(userInfo.getIsLocked())
                .isPassNonExpired(userInfo.getIsPassNonExpired())
                .isNoticeEnabled(userInfo.getIsNoticeEnabled())
                .isLocationEnabled(userInfo.getIsLocationEnabled())
                .memo(userInfo.getMemo())
                .udate(userInfo.getUdate())
                .rdate(userInfo.getRdate())
                .wdate(userInfo.getWdate())
                .soci_type(user.getSoci_typ())
                .soci_email(user.getEmail())
                .build();
        return userVO;
    }
}
