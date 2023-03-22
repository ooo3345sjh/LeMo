package kr.co.Lemo.security;

import kr.co.Lemo.entity.SocialEntity;
import kr.co.Lemo.repository.SocialRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
/**
 * @since 2023/03/21
 * @author 서정현
 * @apiNote SpOAuth2UserService
 */
@Slf4j
@Component
public class SocialUserService extends DefaultOAuth2UserService {

    @Autowired
    private SocialRepo socialRepo;

    // @since 2023/03/09
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId();
        SocialEntity socialEntity = null;
        log.info("userRequest : "+userRequest.toString());
        log.info("oAuth2User : "+oAuth2User.toString());
        log.info("provider : "+provider);
        switch (provider){
            case "google":
                socialEntity = SocialEntity.Provider.google.convert(oAuth2User);
                break;

            case "naver":
                socialEntity = SocialEntity.Provider.naver.convert(oAuth2User);
                break;

            case "kakao":
                socialEntity = SocialEntity.Provider.kakao.convert(oAuth2User);
                break;
        }

        log.debug(socialRepo.findById("0hotelthem1@gmail.com").toString());



        return socialEntity;
    }

    /**
     * @since 2023/03/21
     * @param oAuth2User
     * @return DB에 저장된 회원 데이터 
     * @apiNote 소셜회원이 Lemo의 회원인지 확인 후에 Lemo회원이 아니라면 DB에 저장한후 저장된 데이터 반환
     */
    public SocialEntity load(SocialEntity oAuth2User){
        SocialEntity dbUser = socialRepo.findById(oAuth2User.getUser_id())
                .orElseGet(()->{
                    SocialEntity newUser = SocialEntity.builder()
                                            .user_id(oAuth2User.getUser_id())
                                            .soci_typ(oAuth2User.getSoci_typ())
                                            .email(oAuth2User.getEmail())
                                            .userInfoEntity(oAuth2User.getUserInfoEntity())
                                            .build();
                    return  socialRepo.save(newUser);
                });
        log.info("dbUser : "+dbUser.toString());
        return dbUser;
    }


}
