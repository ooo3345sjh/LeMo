package kr.co.Lemo.security;

import kr.co.Lemo.domain.BusinessInfoVO;
import kr.co.Lemo.domain.UserVO;
import kr.co.Lemo.entity.BusinessInfoEntity;
import kr.co.Lemo.entity.UserEntity;
import kr.co.Lemo.entity.UserInfoEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
 * @apiNote HomeLoginSuccessHandler
 */
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

        UserVO user = userVoConvert(principal);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole())))
        );
        loginSuccessPage(request, response);
    }

    /**
     *
     * @since 2023/03/21
     * @param principal 회원 인증된 객체
     * @return userVO
     * @apiNote UserEntity -> UserVO 객체로 변환한 객체를 반환
     */
    private static UserVO userVoConvert(Object principal) {
        UserEntity user = (UserEntity) principal;
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
