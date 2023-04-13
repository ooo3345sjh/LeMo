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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.util.Optional;
import java.util.UUID;

/**
 * @since 2023/03/16
 * @author 서정현
 * @apiNote UserService
 */

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class UserService {

    private final SocialRepo socialRepo;
    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;

    // @since 2023/03/16
    public int countByEmail(String email) throws Exception{
        return userDAO.countByEmail(email);
    }

    // @since 2023/03/26
    public int countByEmailAndType1(String email) throws Exception{
        return userDAO.countByEmailAndType1(email);
    }

    // @since 2023/03/26
    public UserInfoEntity findByEmailAndType1(String email) throws Exception{
        return userDAO.findByEmailAndType1(email);
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
    public int rsaveUser(UserVO user) throws Exception {
        user.setPass(passwordEncoder.encode(user.getPass()));
        log.debug(user.toString());
        int result = 0;
        result = userDAO.rsaveUserInfo(user);
        result = userDAO.rsaveHomeUser(user.getUser_id(), user.getPass());

        if("BUSINESS".equals(user.getRole())){
            userDAO.rsaveBusinessInfo(user.getBusinessInfoVO());
        }

        return result;
    }

    // @since 2023/03/18
    public SocialEntity rsaveSocial(SocialEntity socialEntity){
        return socialRepo.save(socialEntity);
    }

    // @since 2023/03/25
    public int usaveUserPw(String username, String password) throws Exception {
        password = passwordEncoder.encode(password);
        return userDAO.usaveUserPw(username, password);
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
                    .soci_type(user.getSoci_typ())
                    .build();
            return userVO;

        }
    }


    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    // @since 2023/03/27
    public String getUploadPath(String pathDir)throws Exception{
        log.debug("Myservice getUploadPath...");

        File profileDir = new File(uploadPath+"/"+pathDir);
        if(!profileDir.exists())
            Files.createDirectories(profileDir.toPath());

        return profileDir.getAbsolutePath();
    }

    // @since 2023/03/27
    public int usaveProfile(MultipartFile photo, UserVO userVO) {
        log.debug("Myservice usaveProfile...");

        int result = 0;
        String newName = null;
        try{
            String oriName = photo.getOriginalFilename();
            String ext = oriName.substring(oriName.indexOf("."));
            newName = UUID.randomUUID() + ext;

            removeFile(userVO.getPhoto());
            userDAO.updateProfile(newName, userVO.getUser_id());
            photo.transferTo(new File(getUploadPath("profile"), newName));
            result = 1;
        } catch (Exception e){
            result = 0;
            log.error(e.getMessage());
            log.error(e.toString());
        } finally {
            if(result == 1){
                userVO.setPhoto(newName);
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(userVO, null, userVO.getAuthorities())
                );
            }
        }
        return result;
    }

    // @since 2023/03/27
    public int removeProfile(UserVO userVO) {
        int result = 0;
        try{
            result = userDAO.updateProfile(null, userVO.getUser_id());

            if(result == 1)
                removeFile(userVO.getPhoto());
        } catch (Exception e){
            log.error(e.getMessage());
            log.error(e.getLocalizedMessage());
            result = 0;
        } finally {
            if(result == 1)
                userVO.setPhoto(null);
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(userVO, null, userVO.getAuthorities())
            );
        }
        return result;
    }


    // @since 2023/03/27
    public void removeFile(String fileName) throws Exception {
        log.debug("Myservice removeFile...");

        if(fileName != null){
            // 시스템 경로
            File file = new File(getUploadPath("profile"), fileName);
            if(file.exists())
                file.delete();
        }
    }

    // @since 2023/03/27
    public int usaveNick(String nick, UserVO userVO) throws Exception {
        log.debug("Myservice usaveNick...");


        int result = userDAO.updateNick(nick, userVO.getUser_id());

        if(result == 1){
            userVO.setNick(nick);
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(userVO, null, userVO.getAuthorities())
            );
        }

        return result;
    }

    // @since 2023/03/28
    public int usaveHp(String hp, UserVO userVO) throws Exception {
        log.debug("Myservice usaveHp...");

        int result = userDAO.updateHp(hp, userVO.getUser_id());

        if(result == 1){
            userVO.setHp(hp);
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(userVO, null, userVO.getAuthorities())
            );
        }

        return result;
    }

    // @since 2023/03/28
    public int usaveIsNoticeEnabled(int isNoticeEnabled, UserVO userVO) throws Exception {
        log.debug("Myservice usaveIsNoticeEnabled...");

        int result = userDAO.updateIsNoticeEnabled(isNoticeEnabled, userVO.getUser_id());

        if(result == 1){
            userVO.setIsNoticeEnabled(isNoticeEnabled);
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(userVO, null, userVO.getAuthorities())
            );
        }

        return result;
    }

    // @since 2023/03/28
    public int findByBizRegNum(String bizRegNum) throws Exception {
        return userDAO.countByBizRegNum(bizRegNum);
    }

    /**
     * @since 2023/04/12
     * @author 서정현
     * @apiNote 매일 한시간마다 탈퇴한지 24시간이 지난 회원ID 업데이트
     */
    @Scheduled(cron = "0 0 0/1 * * * ")
    public void usaveWithdrawUserId() throws Exception {
        log.debug("usaveWithdrawUserId start...");
        userDAO.updateWithdrawUserId(UUID.randomUUID().toString());
    }
}
