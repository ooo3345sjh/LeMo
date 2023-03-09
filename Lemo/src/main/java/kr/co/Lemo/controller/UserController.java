package kr.co.Lemo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@PropertySource(value = "classpath:title.properties", encoding = "UTF-8")
@RequiredArgsConstructor
@RequestMapping("user/")
public class UserController {

    private final Environment environment;
    private String group = "user";

    @GetMapping("login")
    public String login() {
        return "user/login";
    }

    @GetMapping("join")
    public String join() {
        return "user/join";
    }
    @GetMapping("terms")
    public String terms(Model m) {
        m.addAttribute("title", environment.getProperty(group));
        return "user/terms";
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
