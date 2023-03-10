package kr.co.Lemo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
}
