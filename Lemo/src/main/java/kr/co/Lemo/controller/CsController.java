package kr.co.Lemo.controller;

import kr.co.Lemo.domain.CsVO;
import kr.co.Lemo.service.CsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @since 2023/03/07
 * @author 이해빈
 * @apiNote csController
 */

@Slf4j
@Controller
@RequestMapping("cs/")
public class CsController {

    @Autowired
    private CsService service;

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

    /**
     *  @since 2023/03/09
     *  @author 황원진
     *  @apiNote cs/event
     *
     */

    @GetMapping("event/list")
    public String event_list(String cs_cate, Model model){

        log.info("cate : " +cs_cate);

        List<CsVO> eventLists = service.selectEventArticles(cs_cate);


        log.info("event : " +eventLists);

        model.addAttribute("articles", eventLists);
        model.addAttribute("cs_cate", cs_cate);

        return "cs/event/list";
    }

    @GetMapping("event/view")
    public String event_view(int cs_no, Model model){
        log.info("no : " + cs_no);

        CsVO eventView = service.selectEventArticle(cs_no);

        model.addAttribute("cs_no", cs_no);
        model.addAttribute("eventArticle", eventView);

        return "cs/event/view";
    }

}
