package kr.co.Lemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("cs/")
public class csController {

    @GetMapping("notice")
    public String notice(){
        return "cs/notice";
    }

    @GetMapping("faq")
    public String faq(){
        return "cs/faq";
    }

    @GetMapping("qna")
    public String qna(){
        return "cs/qna";
    }

    @GetMapping("terms")
    public String terms(){
        return "cs/terms";
    }

    @GetMapping("event/list")
    public String event_list(){
        return "cs/event/list";
    }

    @GetMapping("event/view")
    public String event_view(){
        return "cs/event/view";
    }

}
