package kr.co.Lemo.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.Lemo.dao.UserDAO;
import kr.co.Lemo.domain.BusinessInfoVO;
import kr.co.Lemo.domain.UserVO;
import kr.co.Lemo.entity.BusinessInfoEntity;
import kr.co.Lemo.entity.SocialEntity;
import kr.co.Lemo.entity.UserEntity;
import kr.co.Lemo.entity.UserInfoEntity;
import kr.co.Lemo.repository.SocialRepo;
import kr.co.Lemo.repository.UserInfoRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.Optional;

/**
 * @since 2023/03/16
 * @author 서정현
 * @apiNote UserService
 */

@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class UserService {

    private SocialRepo socialRepo;
    private UserDAO userDAO;
    private PasswordEncoder passwordEncoder;

    // @since 2023/03/16
    public int countByEmail(String email) throws Exception{
        return userDAO.countByEmail(email);
    }

    // @since 2023/03/16
    public String getNick() throws JsonProcessingException {
        String apiURL = "https://nickname.hwanmoo.kr/";

        URI uri = UriComponentsBuilder
                .fromUriString(apiURL)
                .queryParam("format", "json")
                .queryParam("max_length", 8)
                .build()
                .toUri();

        RequestEntity<Void> req = new RequestEntity<>(HttpMethod.GET, uri);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate.exchange(req, String.class);

        // JSON 파싱
        String jsonData = result.getBody();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(jsonData);
        JsonNode words = node.get("words");

        String nick = null;
        if(words != null){
            nick = words.get(0).asText();
        } else {
            nick = "오류";
        }
        System.out.println("nick = " + nick);
        return nick.replaceAll("[^a-zA-Z0-9가-힣]", "").replaceAll(" ", "");
    }

    // @since 2023/03/16
    public int countByNick(String nick) throws Exception {
        return userDAO.countByNick(nick);
    }

    // @since 2023/03/18
    public int saveUser(UserVO user) throws Exception {
        user.setPass(passwordEncoder.encode(user.getPass()));
        log.debug(user.toString());
        int result = 0;
        result = userDAO.saveUserInfo(user);
        result = userDAO.saveHomeUser(user.getUser_id(), user.getPass());

        if("BUSINESS".equals(user.getRole())){
            userDAO.saveBusinessInfo(user.getBusinessInfoVO());
        }

        return result;
    }

    public SocialEntity saveSocial(SocialEntity socialEntity){
        return socialRepo.save(socialEntity);
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
