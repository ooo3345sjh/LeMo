package kr.co.Lemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @since 2023/03/07
 * @author 박종협
 * @apiNote 상품 controller
 */

@Controller
@RequestMapping("product/")
public class ProductController {

    @GetMapping("list")
    public String list() throws Exception{
        return "product/list";
    }

    @GetMapping("view")
    public String view() throws Exception{
        return "product/view";
    }

    @GetMapping("reservation")
    public String reservation() throws Exception{
        return "product/reservation";
    }

    @GetMapping("result")
    public String result() throws Exception{
        return "product/result";
    }
}