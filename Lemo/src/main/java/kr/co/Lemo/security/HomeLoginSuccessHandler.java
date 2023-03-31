package kr.co.Lemo.security;

import kr.co.Lemo.domain.BusinessInfoVO;
import kr.co.Lemo.domain.UserVO;
import kr.co.Lemo.entity.BusinessInfoEntity;
import kr.co.Lemo.entity.UserEntity;
import kr.co.Lemo.entity.UserInfoEntity;
import kr.co.Lemo.utils.RemoteAddrHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
/**
 * @since 2023/03/21
 * @author 서정현
 * @apiNote HomeLoginSuccessHandler
 */
@Slf4j
@Component
public class HomeLoginSuccessHandler extends LoginSuccessHandler implements AuthenticationSuccessHandler {

    // @since 2023/03/21
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException
    {
        Object principal = authentication.getPrincipal();
        UserEntity userEntity = (UserEntity)principal;
        UserVO user = userVoConvert(userEntity);
        user.setDetails(new WebAuthenticationDetails(RemoteAddrHandler.getRemoteAddr(request), request.getSession().getId()));

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole())))
        );

        Calendar passExpiredTime = Calendar.getInstance();
        passExpiredTime.setTime(userEntity.getPass_udate()); // 패스워드 설정 날짜
        passExpiredTime.add(Calendar.MONTH, 3);       // 3개월 후 날짜

        // 비밀번호 설정 후 3개월이 지났을 때 재설정 안내
        if(passExpiredTime.getTimeInMillis() > System.currentTimeMillis()){
            String uri = loginSuccessPage(request, response);
            HttpSession session = request.getSession();
            session.setAttribute("toUri", uri);
            log.debug("uri : "+ uri);
            redirectStrategy.sendRedirect(request, response, "/user/login/error?error=PNE");
            return;
        }

        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if(savedRequest != null){
            String uri = isUriManagement(savedRequest.getRedirectUrl(), request, user.getRole());
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
     * @param user 회원 인증된 객체
     * @return userVO
     * @apiNote UserEntity -> UserVO 객체로 변환한 객체를 반환
     */
    private static UserVO userVoConvert(UserEntity user) {
        UserInfoEntity userInfo = user.getUserInfoEntity();
        BusinessInfoEntity businessInfo = user.getBusinessInfoEntity();

        BusinessInfoVO businessInfoVO = null;
        if(businessInfo != null){
            businessInfoVO = BusinessInfoVO.builder()
                    .user_id(user.getUser_id())
                    .bis_company(businessInfo.getBis_company())
                    .bis_ceo(businessInfo.getBis_ceo())
                    .bis_openDate(businessInfo.getBis_openDate())
                    .bis_bizRegNum(businessInfo.getBis_bizRegNum())
                    .bis_tel(businessInfo.getBis_tel())
                    .bis_zip(businessInfo.getBis_zip())
                    .bis_addr(businessInfo.getBis_addr())
                    .bis_addrDetail(businessInfo.getBis_addrDetail())
                    .build();
        }

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
                .rdate(userInfo.getUdate())
                .wdate(userInfo.getWdate())
                .businessInfoVO(businessInfoVO)
                .build();
        return userVO;
    }

}
