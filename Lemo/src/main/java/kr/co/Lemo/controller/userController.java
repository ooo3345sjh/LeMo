package kr.co.Lemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class userController {

    @GetMapping("user/login")
    public String login() {
        return "user/login";
    }

    @GetMapping("user/join")
    public String join() {
        return "user/join";

    }
    @GetMapping("user/terms")
    public String terms() {
        return "user/terms";
    }

    @GetMapping("user/certification")
    public String certification() {
        return "user/certification";
    }

    @GetMapping("user/checknick")
    public String checknick() {
        return "user/checknick";
    }

    @GetMapping("user/signup_business")
    public String signup_business() {
        return "user/signup_business";
    }
    @GetMapping("user/signup_general")
    public String signup_general() {
        return "user/signup_general";
    }

    @GetMapping("user/resetPw")
    public String resetPw() {
        return "user/resetPw";
    }

}
