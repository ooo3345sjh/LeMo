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
import java.net.http.HttpRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @GetMapping("login")
    public String login() {
        return "user/login";
    }

    @GetMapping("terms")
    public String terms(Model m) {
        m.addAttribute("title", environment.getProperty(group));

        return "user/terms";
    }
    @PostMapping("terms")
    public String terms(String termsType_no, HttpServletRequest req){
        log.info(termsType_no);
        HttpSession session = req.getSession();
        session.setMaxInactiveInterval(60*10); // 세션 만료시간 10분 설정
        session.setAttribute("termsType_no", termsType_no);
        return "redirect:/user/join";
    }

    @GetMapping("join")
    public String join() {
        return "user/join";
    }

    @GetMapping("certification")
    public String certification() {
        return "user/certification";
    }

    @GetMapping("checknick")
    public String checknick() {
        return "user/checknick";
    }

    @GetMapping("signup_business")
    public String signup_business() {
        return "user/signup_business";
    }
    @GetMapping("signup_general")
    public String signup_general() {
        return "user/signup_general";
    }

    @GetMapping("resetPw")
    public String resetPw() {
        return "user/resetPw";
    }

}
