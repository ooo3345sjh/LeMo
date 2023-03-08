package kr.co.Lemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("user/")
public class UserController {

    @GetMapping("login")
    public String login() {
        return "user/login";
    }

    @GetMapping("join")
    public String join() {
        return "user/join";
    }
    @GetMapping("terms")
    public String terms() {

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
