package kr.co.Lemo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.co.Lemo.domain.MessageVO;
import kr.co.Lemo.domain.SmsResponseVO;
import kr.co.Lemo.service.SmsService;
import kr.co.Lemo.utils.SearchCondition;
import kr.co.Lemo.utils.SearchCondition_v2;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

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
    private String group = "title.user";

    // @since 2023/03/08
    @GetMapping("login")
    public String login(Model m) {
        log.debug("GET login start...");
        m.addAttribute("title", environment.getProperty(group));
        return "user/login";
    }

    // @since 2023/03/08
    @GetMapping("terms")
    public String terms(Model m) {
        log.debug("GET terms start...");
        m.addAttribute("title", environment.getProperty(group));

        return "user/terms";
    }

    // @since 2023/03/08
    @PostMapping("terms")
    public String terms(String termsType_no, HttpServletRequest req){
        log.debug("POST terms start...");

        HttpSession session = req.getSession();
        session.setMaxInactiveInterval(60*30); // 세션 만료시간 30분 설정
        session.setAttribute("termsAuth", termsType_no);
        return "redirect:/user/join";
    }

    // @since 2023/03/08
    @GetMapping("join")
    public String join(Model m, HttpServletRequest req) {
        log.debug("GET join start...");

        m.addAttribute("title", environment.getProperty(group));
        String termsType_no = getTermsAuth(req);

        if(termsType_no == null){
            return "redirect:/user/terms";
        }

        return "user/join";
    }

    // @since 2023/03/10
    @GetMapping("hp/auth")
    public String hpAuthentication(Model m) {
        log.debug("GET hpAuthentication start...");
        m.addAttribute("title", environment.getProperty(group));


        return "user/hpAuth";
    }

    // @since 2023/03/10
    @GetMapping("{type}/signup")
    public String signup(
            @PathVariable String type,
            Model m,
            HttpServletRequest req
    ) {
        log.debug("GET signup start...");

        m.addAttribute("error", "R");
        m.addAttribute("title", environment.getProperty(group));
        String termsType_no = getTermsAuth(req);

        if(termsType_no == null)
            return "error/abnormalAccess";

        if("general".equals(type))
            return "user/signup_general";

        else if("business".equals(type))
            return "user/signup_business";

        return "error/abnormalAccess";
    }

    // @since 2023/03/08
    @GetMapping("checknick")
    public String checknick() {
        return "user/checknick";
    }

    // @since 2023/03/08
    @GetMapping("resetPw")
    public String resetPw() {
        return "user/resetPw";
    }

    // @since 2023/03/12
    @PostMapping("sms/send")
    @ResponseBody
    public Map sendSms(@RequestBody MessageVO messageVO) throws Exception {
        log.debug("POST sendSms start...");
        log.debug(messageVO.toString());
        Map map = new HashMap();
        try {
//            SmsResponseVO response = smsService.sendSms(messageVO);
            SmsResponseVO response = SmsResponseVO.builder()
                                                .code(123123)
                                                .build();
            log.debug(response.toString());
            map.put("result", response);
        } catch (Exception e){
            log.error(e.getMessage());
            map.put("result", "error");
        }

        return map;
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

    // @since 2023/03/13
    @GetMapping("test")
    public String test(SearchCondition_v2 sc) {
        log.debug("GET test start...");

        log.info(sc.toString());

        return "user/test";
    }
    @GetMapping("test2")
    public String test2(@ModelAttribute SearchCondition_v2 sc, @RequestParam Map map) {
        log.debug("GET test2 start...");

        log.info("map : " + map.toString());
        log.info("sc : " + sc.toString());
        log.info("query : " + sc.getQueryString());

        return "user/test";
    }
}
