package kr.co.Lemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * @since 2023/03/07
 * @author 홍길동
 * @apiNote 삭제예정임다
 */
@Controller

public class exampleController {

    // @since 2023/03/10
    @GetMapping("test")
    public String test(String name) throws Exception{
        return "test";
    }


}
