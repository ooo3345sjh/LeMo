package kr.co.Lemo.controller;

import kr.co.Lemo.domain.BusinessInfoVO;
import kr.co.Lemo.domain.MessageVO;
import kr.co.Lemo.domain.SmsResponseVO;
import kr.co.Lemo.domain.UserVO;
import kr.co.Lemo.domain.search.SearchconditionTestVO_sjh;
import kr.co.Lemo.entity.SocialEntity;
import kr.co.Lemo.entity.UserInfoEntity;
import kr.co.Lemo.service.SmsService;
import kr.co.Lemo.service.UserService;
import kr.co.Lemo.utils.SearchCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @since 2023/03/08
 * @author 서정현
 * @apiNote 회원 controller
 */
@Slf4j
@Controller
@PropertySource(value = "classpath:title.properties", encoding = "UTF-8")
@RequiredArgsConstructor
@RequestMapping("user/")
public class UserController {

    private final Environment environment;
    private final SmsService smsService;
    private final UserService userService;
    private String group = "title.user";

    // @since 2023/03/08
    @GetMapping("login")
    public String login(Model m, HttpServletRequest req) {
        log.debug("GET login start...");
        
        // 회원가입 페이지로부터 로그인 페이지로 온 경우 실행
        String referer = req.getHeader("Referer");
        if(referer != null && referer.contains("/user/signup")){
            req.getSession().setAttribute("fromSignup", true);
        }

        m.addAttribute("title", environment.getProperty(group));
        return "user/login";
    }

    /**
     * @since 2023/03/23
     * @param error error 파라미터 값 L:차단된 회원, W:탈퇴한 회원
     * @apiNote 로그인시 에러가 발생할 경우 넘어오는 uri
     */
    @GetMapping("login/error")
    public String login(
            Model m,
            @RequestParam(defaultValue = "null") String error,
            RedirectAttributes rttr
    ) {
        log.debug("GET login start...");

        rttr.addFlashAttribute("error", error);

        return "redirect:/user/login";
    }

    // @since 2023/03/08
    @GetMapping("terms")
    public String terms(
            Model m,
            @RequestParam(defaultValue = "null") String type
    ) {
        log.debug("GET terms start...");

        if("social".equals(type)){}
        else if(!"general".equals(type) && !"business".equals(type))
            return "redirect:/user/join";


        m.addAttribute("title", environment.getProperty(group));
        m.addAttribute("type", type);

        return "user/terms";
    }

    // @since 2023/03/08
    @PostMapping("terms")
    public String terms(
            String termsType_no,
            @RequestParam(defaultValue = "null") String type,
            HttpServletRequest req,
            Model m
    ){
        log.debug("POST terms start...");
        log.debug(type);
        if(!"general".equals(type) && !"business".equals(type))
            return "redirect:/user/join";


        m.addAttribute("type", type);
        HttpSession session = req.getSession();
        session.setMaxInactiveInterval(60*30); // 세션 만료시간 30분 설정
        session.setAttribute("termsAuth", termsType_no);
        return "redirect:/user/hp/auth?type=" + type;
    }

    // @since 2023/03/08
    @GetMapping("join")
    public String join(Model m) {
        log.debug("GET join start...");

        m.addAttribute("title", environment.getProperty(group));

        return "user/join";
    }

    // @since 2023/03/10
    @GetMapping("hp/auth")
    public String hpAuthentication(
            Model m,
            HttpServletRequest req,
            @RequestParam(defaultValue = "null") String type
    ) {
        log.debug("GET hpAuthentication start...");
        log.debug(type);
        if(!"general".equals(type) && !"business".equals(type))
            return "redirect:/user/join";

        String termsType_no = getTermsAuth(req);

        if(termsType_no == null){
            return "redirect:/user/terms?type="+type;
        }

        m.addAttribute("title", environment.getProperty(group));
        m.addAttribute("type", type);

        return "user/hpAuth";
    }

    // @since 2023/03/21
    @GetMapping("social/signup")
    public String singnup_social(Model m, HttpServletRequest req){
        log.debug("GET singnup_social start...");
        HttpSession session = req.getSession();
        Object principal = session.getAttribute("principal");

        if(principal == null){
            return "redirect:/user/login";
        }

        m.addAttribute("title", environment.getProperty(group));
        return "user/signup_social";
    }

    // @since 2023/03/15
    @PostMapping("hp/auth")
    public String hpAuthentication(
            @RequestParam(name = "authcode", defaultValue = "null") Integer code,
            @RequestParam(defaultValue = "null") String type,
            @RequestParam(defaultValue = "null") String hp,
            Model m,
            HttpServletRequest req
    ) {
        log.debug("POST hpAuthentication start...");
        log.debug("code : " + code.toString());
        log.debug("hp : " + hp);
        log.debug("type : " + type);

        if(!"general".equals(type) && !"business".equals(type))
            return "redirect:/user/join";

        String termsType_no = getTermsAuth(req);
        if(termsType_no == null){
            return "redirect:/user/terms?type="+type;
        }

        HttpSession session = req.getSession();
        Integer authCode = session.getAttribute("authCode") == null? null:(Integer) session.getAttribute("authCode");

        if(hp == null || !Pattern.matches("^01(?:0|1|[6-9])(?:\\d{4})\\d{4}$", hp))
            return "redirect:/user/hp/auth?error=hp&type="+type;
        else if(authCode == null || !authCode.equals(code))
            return "redirect:/user/hp/auth?error=code&type="+type;

        session.setAttribute("authHp", hp);

        m.addAttribute("title", environment.getProperty(group));

        return "redirect:/user/signup?type=" + type;
    }

    // @since 2023/03/10
    @GetMapping("signup")
    public String signup(
            @RequestParam(defaultValue = "null") String type,
            Model m,
            HttpServletRequest req
    ) {
        log.debug("GET signup start...");

        if(!"general".equals(type) && !"business".equals(type))
            return "redirect:/user/join";

        m.addAttribute("error", "R");
        m.addAttribute("title", environment.getProperty(group));
        m.addAttribute("type", type);
        String termsType_no = getTermsAuth(req);

        if(termsType_no == null)
            return "redirect:/user/terms?type="+type;

        String hp = (String)req.getSession().getAttribute("authHp");

        if(hp == null)
            return  "redirect:/user/hp/auth?type="+type;

        if("general".equals(type))
            return "user/signup_general";

        else if("business".equals(type))
            return "user/signup_business";

        return "error/abnormalAccess";
    }

    // @since 2023/03/16
    @PostMapping("signup")
    public String signup(
            @ModelAttribute UserVO userVO,
            @ModelAttribute BusinessInfoVO businessInfoVO,
            @RequestParam(name = "userType", defaultValue = "null") String type,
            Model m,
            HttpServletRequest req
    ) throws Exception {
        log.debug("POST signup start...");
        log.debug(userVO.toString());
        userVO.setBusinessInfoVO(businessInfoVO);

        if(!"general".equals(type) && !"business".equals(type))
            return "redirect:/user/join";

        setUserRole(userVO, type);

        m.addAttribute("type", type);
        String termsType_no = getTermsAuth(req);

        if(termsType_no == null)
            return "redirect:/user/terms?type="+type;

        String hp = (String)req.getSession().getAttribute("authHp");

        if(hp == null)
            return  "redirect:/user/hp/auth?type="+type;

        setSelectedTerms(userVO, termsType_no);
        userVO.setRegip(req.getRemoteAddr());
        userVO.setHp(hp);
        userService.saveUser(userVO);

        log.debug(termsType_no);
        return "redirect:/user/login";
    }

    // @since 2023/03/16
    @ResponseBody
    @PostMapping("social/signup")
    public Map signup_social(@RequestBody Map map, HttpServletRequest req){
        log.debug("POST signup_social start...");
        HttpSession session = req.getSession();
        Object principal = session.getAttribute("principal");

        int result = 0;

        SocialEntity socialEntity = setSocialObj(map, req, principal);
        if(socialEntity != null){
            result = 1;
            userService.saveSocial((SocialEntity) principal);
        }

        map.put("result", result);
        return map;

    }

    // @since 2023/03/16
    @ResponseBody
    @GetMapping("nick/create")
    public Map createNick() throws Exception {
        log.debug("GET createNick start...");
        String nick = null;
        try {
            nick = userService.getNick();
            while(true){
                int result = userService.countByNick(nick);
                if(result == 0) break;
            }
        } catch (Exception e){
            log.error(e.getMessage());
            nick = "error";
        }

        Map map = new HashMap();
        map.put("nick", nick);

        return map;
    }

    // @since 2023/03/16
    @ResponseBody
    @GetMapping("nick/duplicate")
    public Map checkNick(@RequestParam String nick) throws Exception {
        log.debug("GET checkNick start...");
        int result = 0;
        try {
            result = userService.countByNick(nick);
        } catch (Exception e){
            log.error(e.getMessage());
            result = -99999;
        }

        Map map = new HashMap();
        map.put("result", result);

        return map;
    }

    // @since 2023/03/16
    @ResponseBody
    @GetMapping("email/duplicate")
    public Map checkEmail(@RequestParam String email) {
        log.debug("GET checkEmail start...");
        log.debug(email);
        int result = 0;
        try {
            result = userService.countByEmail(email);
            log.debug(String.valueOf(result));
        } catch (Exception e){
            result = -99999;
        }
        Map map = new HashMap();
        map.put("result", result);
        return map;
    }

    // @since 2023/03/24
    @GetMapping("pw/reset")
    public String resetPw(@RequestParam(required = false) String isPassNonExpired) {
        if(isPassNonExpired.equals("true")){
            return "user/resetPw_isPassNonExpired";
        }
        return "user/resetPw";
    }

    // @since 2023/03/12
    @PostMapping("sms/send")
    @ResponseBody
    public Map sendSms(@RequestBody MessageVO messageVO, HttpServletRequest req) throws Exception {
        log.debug("POST sendSms start...");
        log.debug(messageVO.toString());
        Map map = new HashMap();
        try {
//            SmsResponseVO response = smsService.sendSms(messageVO);
            SmsResponseVO response = SmsResponseVO.builder()
                                                .code(123123)
                                                .build();

            HttpSession session = req.getSession();
            session.setAttribute("authCode", response.getCode());

            log.debug(response.toString());
            map.put("result", response);
        } catch (Exception e){
            log.error(e.getMessage());
            map.put("result", "error");
        }

        return map;
    }

    /**
     * @since 2023/03/22
     * @param map hp, nick, termsList 값이 들어있는 Map객체
     * @param principal 회원 정보 객체
     * @return SocialEntity 데이터를 셋팅한 객체
     */
    private SocialEntity setSocialObj(Map map, HttpServletRequest req, Object principal) {
        String hp = (String)map.get("hp");
        String nick = (String)map.get("nick");
        Object terms = map.get("termsList");

        SocialEntity socialEntity = null;

        if(hp != null && nick != null && terms != null){
            List<Integer> termsList = (List<Integer>)terms;
            if(principal != null){
                socialEntity = (SocialEntity) principal;
                String rdate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                socialEntity.setUserInfoEntity(UserInfoEntity.builder()
                        .user_id(socialEntity.getUser_id())
                        .hp(hp)
                        .nick(nick)
                        .type(2)
                        .role("USER")
                        .regip(req.getRemoteAddr())
                        .isLocked(1)
                        .isEnabled(1)
                        .isPassNonExpired(1)
                        .isNoticeEnabled(termsList.contains("5")? 1:0)
                        .isLocationEnabled(termsList.contains("6")? 1:0)
                        .isPrivacySelected(termsList.contains("4")? 1:0)
                        .rdate(rdate)
                        .udate(rdate)
                        .build());
            }
        }
        return socialEntity;
    }

    /**
     * @since 2023/03/10
     * @param req
     * @return 세션에 저장된 약관동의에서 체킹한 정보들을 리턴
     */
    private static String getTermsAuth(HttpServletRequest req) {
        HttpSession session = req.getSession();
        String termsType_no = (String)session.getAttribute("termsAuth");
        return termsType_no;
    }

    /**
     * @since 2023/03/17
     * @param userVO
     * @param termsType_no
     * @apiNote 약관동의(선택)를 체크해서 userVo 객체에 저장(체크o:1, 체크x:0)
     */
    private static void setSelectedTerms(UserVO userVO, String termsType_no) {
        if(termsType_no.contains("4"))
            userVO.setIsPrivacySelected(1);
        if(termsType_no.contains("5"))
            userVO.setIsNoticeEnabled(1);
        if(termsType_no.contains("6"))
            userVO.setIsLocationEnabled(1);
    }

    /**
     * @since 2023/03/17
     * @param userVO
     * @param type
     * @apiNote type값에 따라 userVO객체의 role 변수값 설정
     */
    private static void setUserRole(UserVO userVO, String type) {
        if("general".equals(type))
            userVO.setRole("USER");
        else if("business".equals(type))
            userVO.setRole("BUSINESS");
    }

    // @since 2023/03/13
    @GetMapping("test")
    public String test(SearchCondition sc) {
        log.debug("GET test start...");

        log.info(sc.toString());

        return "user/test";
    }
    @GetMapping("test2")
    public String test2(@ModelAttribute SearchconditionTestVO_sjh sc, @RequestParam Map map) {
        log.debug("GET test2 start...");
        log.info("map : " + map.toString());
        sc.setMap(map);
        log.info("sc : " + sc.toString());
        log.info("query : " + sc.getQueryString());
        return "user/test";
    }
}
