package kr.co.Lemo.controller;

import kr.co.Lemo.domain.CsVO;
import kr.co.Lemo.domain.UserVO;
import kr.co.Lemo.domain.search.Cs_SearchVO;
import kr.co.Lemo.service.CsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 *  @since 2023/03/09
 *  @author 황원진
 *  @apiNote cs total
 *
 */

@Slf4j
@Controller
@PropertySource(value = "classpath:title.properties", encoding = "UTF-8")
@RequiredArgsConstructor
@RequestMapping("cs/")
public class CsController {

    private final Environment environment;
    private String group = "title.cs";

    @Autowired
    private CsService service;

    // @since 2023/03/09
    @GetMapping("{cs_cate}")
    public String findAllCsArticles(@PathVariable("cs_cate") String cs_cate,
                                    @AuthenticationPrincipal UserVO myUser,
                                    Model model,
                                    CsVO vo,
                                    @RequestParam Map map,
                                    @ModelAttribute Cs_SearchVO sc){

        sc.setMap(map);
        model.addAttribute("title", environment.getProperty(group));
        if("notice".equals(cs_cate)) {

            service.findAllCsArticles(sc, model);

            return "cs/notice";
        }else if("qna".equals(cs_cate)) {
            if (myUser == null || myUser.getUser_id() == null) {
                return "redirect:/user/login";
            }
            service.findAllQnaArticles(vo, myUser, model);

            return "cs/qna";
        }else if("terms".equals(cs_cate)){
            service.findTerms(map);

            model.addAttribute("map", map);

        }
        return "cs/terms";
    }

    // @since 2023/03/11
    @GetMapping("{cs_cate}/{cs_type}")
    public String findAllFaqArticles(
            @PathVariable("cs_cate") String cs_cate,
            @PathVariable("cs_type") String cs_type,
            Model model,
            @RequestParam Map map,
            @ModelAttribute Cs_SearchVO sc
    ){

        sc.setMap(map);
        model.addAttribute("title", environment.getProperty(group));

        service.findAllFaqArticles(sc, model);
        model.addAttribute("type", cs_type);
        return "cs/faq";
    }


    // @since 2023/03/10
    @PostMapping("{cs_cate}")
    public String rsaveArticleQna(@PathVariable("cs_cate") String cs_cate,
                                  CsVO vo,
                                  @AuthenticationPrincipal UserVO myUser,
                                  HttpServletRequest req) {

        vo.setUser_id(myUser.getUser_id());
        vo.setCs_regip(req.getRemoteAddr());


        service.rsaveArticleQna(vo);

        return "redirect:/cs/qna";
    }

    // @since 2023/04/04
//    @GetMapping("terms")
//    public String terms(Model model){
//        model.addAttribute("title", environment.getProperty(group));
//        List<TermVO> terms =service.findTerm();
//        List<TermVO> location =service.findLocation();
//        TermVO privacy = service.findPrivacy();
//
//        model.addAttribute("terms", terms);
//        model.addAttribute("location", location);
//        model.addAttribute("privacy", privacy);
//
//        return "cs/terms";
//    }

    // @since 2023/03/08
    @GetMapping("event/list")
    public String findAllEvent_list(
                                    Model model,
                                    @RequestParam Map map,
                                    @ModelAttribute Cs_SearchVO sc){
        sc.setMap(map);
        sc.setCs_cate("event");
        model.addAttribute("title", environment.getProperty(group));
        model.addAttribute("cs_cate", "event");

        service.findAllCsArticles(sc, model);

        return "cs/event/list";
    }

    // @since 2023/03/08
    @GetMapping("event/view")
    public String event_view( int cs_no, Model model){
        log.info("no : " + cs_no);

        String cs_cate = "event";

        CsVO eventView = service.findCsArticle(cs_no);
        CsVO eventPrev = service.findEventPrev(cs_cate, cs_no);
        CsVO eventNext = service.findEventNext(cs_cate, cs_no);

        //log.info("prevCs_cate : " + eventPrev.getCs_cate());
        //log.info("nextCs_cate : " + eventNext.getCs_cate());


        model.addAttribute("title", environment.getProperty(group));
        model.addAttribute("cs_no", cs_no);
        model.addAttribute("eventArticle", eventView);
        model.addAttribute("eventPrev", eventPrev);
        model.addAttribute("eventNext", eventNext);
        model.addAttribute("cs_cate", cs_cate);


        return "cs/event/view";
    }



    /** update **/




}
