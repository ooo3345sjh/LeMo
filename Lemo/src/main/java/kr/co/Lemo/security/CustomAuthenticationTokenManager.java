package kr.co.Lemo.security;

import kr.co.Lemo.domain.BusinessInfoVO;
import kr.co.Lemo.domain.UserVO;
import kr.co.Lemo.entity.BusinessInfoEntity;
import kr.co.Lemo.entity.SocialEntity;
import kr.co.Lemo.entity.UserEntity;
import kr.co.Lemo.entity.UserInfoEntity;
import kr.co.Lemo.repository.SocialRepo;
import kr.co.Lemo.repository.UserRepo;
import kr.co.Lemo.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * @since 2023/03/24
 * @author 서정현
 * @apiNote CustomAuthenticationTokenManager
 */
@Configuration
@AllArgsConstructor
@NoArgsConstructor
public class CustomAuthenticationTokenManager implements AuthenticationProvider {

    @Autowired
    private UserRepo repo;

    @Autowired
    private SocialRepo socialRepo;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        CustomAuthenticationToken token = (CustomAuthenticationToken)authentication;
        UserEntity user = repo.findById(token.getUserName()).orElse(null);
        WebAuthenticationDetails details = (WebAuthenticationDetails)(authentication.getDetails());

        if(user == null){
            SocialEntity socialUser = socialRepo.findById(token.getUserName()).orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));
            UserVO userVO = userVoConvert(socialUser);
            return CustomAuthenticationToken.builder()
                    .principal(userVO)
                    .details(details)
                    .authenticated(true)
                    .build();
        } else {
            UserEntity userEntity = (UserEntity)user;
            UserVO userVO = userVoConvert(userEntity);
            String inputPass = token.getPassword(); // 입력된 패스워드
            String dbPass = userEntity.getPass(); // DB에 저장된 패스워드
            if(inputPass.equals(dbPass)){
                return CustomAuthenticationToken.builder()
                        .principal(userVO)
                        .details(details)
                        .authenticated(true)
                        .build();
            }
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == CustomAuthenticationToken.class;
    }

    /**
     *
     * @since 2023/03/24
     * @param principal 회원 인증된 객체
     * @return userVO
     * @apiNote UserEntity -> UserVO 객체로 변환한 객체를 반환
     */
    public UserVO userVoConvert(Object principal) {
        if(principal instanceof  UserEntity){
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
        } else {
            SocialEntity user = (SocialEntity) principal;
            UserInfoEntity userInfo = user.getUserInfoEntity();

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
                    .build();
            return userVO;
        }
    }


}
