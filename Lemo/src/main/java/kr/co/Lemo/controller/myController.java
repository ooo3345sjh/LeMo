package kr.co.Lemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @since 2023/03/07
 * @author 황원진
 * @apiNote 마이페이지 controller
 */

@Controller
public class myController {

    @GetMapping("my/coupon")
    public String coupon(){
        return "my/coupon";
    }

    @GetMapping("my/info")
    public String info(){
        return "my/info";
    }

    @GetMapping("my/pick")
    public String pick(){
        return "my/pick";
    }
}
