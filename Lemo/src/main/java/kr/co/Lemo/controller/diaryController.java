package kr.co.Lemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("diary/")
public class diaryController {

    @GetMapping("list")
    public String list(){
        return "diary/list";
    }

    @GetMapping("view")
    public String view(){
        return "diary/view";
    }

}
