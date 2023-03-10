package kr.co.Lemo.controller;

import kr.co.Lemo.domain.CsVO;
import kr.co.Lemo.service.CsService;
import kr.co.Lemo.utils.SearchCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @since 2023/03/07
 * @author 이해빈
 * @apiNote csController
 */

@Slf4j
@Controller
@PropertySource(value = "classpath:title.properties", encoding = "UTF-8")
@RequiredArgsConstructor
@RequestMapping("cs/")
public class CsController {

    private final Environment environment;
    private String group = "cs";

    @Autowired
    private CsService service;

    @GetMapping("{cs_cate}")
    public String notice(@PathVariable("cs_cate") String cs_cate, Model model, SearchCondition sc){

        model.addAttribute("title", environment.getProperty(group));
        service.selectNoticeArticles(sc, model);

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

    @GetMapping("{cs_cate}/list")
    public String event_list(@PathVariable("cs_cate") String cs_cate, Model model, SearchCondition sc){

        model.addAttribute("title", environment.getProperty(group));
        service.selectEventArticles(sc, model);

        return "cs/event/list";
    }

    @GetMapping("event/view")
    public String event_view(int cs_no, Model model){
        log.info("no : " + cs_no);

        CsVO eventView = service.selectEventArticle(cs_no);

        model.addAttribute("title", environment.getProperty(group));
        model.addAttribute("cs_no", cs_no);
        model.addAttribute("eventArticle", eventView);

        return "cs/event/view";
    }

}
