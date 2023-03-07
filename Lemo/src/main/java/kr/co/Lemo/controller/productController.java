package kr.co.Lemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @since 2023/03/07
 * @author 박종협
 * @apiNote 상품 controller
 */

@Controller
public class productController {

    @GetMapping("layouts/default-layout")
    public String test(){
        return "layouts/default-layout";
    }
}