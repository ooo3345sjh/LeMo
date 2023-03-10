package kr.co.Lemo.controller;

import kr.co.Lemo.domain.ArticleDiaryVO;
import kr.co.Lemo.domain.DiarySpotVO;
import kr.co.Lemo.service.MyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @since 2023/03/07
 * @author 박종협
 * @apiNote 마이페이지 controller
 */

@Slf4j
@Controller
@PropertySource(value = "classpath:title.properties", encoding = "UTF-8")
@RequiredArgsConstructor
@RequestMapping("my/")
public class MyController {
    private final Environment environment;
    // 박종협
    private String myGroup = "title.my";
    private String diaryGroup = "title.diary";
    private final MyService service;

    // @since 2023/03/07
    @GetMapping("coupon")
    public String coupon(Model m){
        m.addAttribute("title", environment.getProperty(myGroup));
        return "my/coupon";
    }

    // @since 2023/03/07
    @GetMapping("info")
    public String info(Model m){
        m.addAttribute("title", environment.getProperty(myGroup));
        return "my/info";
    }
    // @since 2023/03/07
    @GetMapping("pick")
    public String pick(Model m){
        m.addAttribute("title", environment.getProperty(myGroup));
        return "my/pick";
    }

    // @since 2023/03/07
    @GetMapping("point")
    public String point(Model m) {
        m.addAttribute("title", environment.getProperty(myGroup));
        return "my/point";
    }

    // @since 2023/03/08
    @GetMapping("reservation")
    public String reservation(Model m) {
        m.addAttribute("title", environment.getProperty(myGroup));
        return "my/reservation";
    }

    // @since 2023/03/08
    @GetMapping("view")
    public String view(Model m) {
        m.addAttribute("title", environment.getProperty(myGroup));
        return "my/view";
    }

    // @since 2023/03/08
    @GetMapping("review/list")
    public  String reviewList(Model m) {
        m.addAttribute("title", environment.getProperty(myGroup));
        return "my/review/list";
    }

    // @since 2023/03/08
    @GetMapping("review/modify")
    public String reviewModify(Model m) {
        m.addAttribute("title", environment.getProperty(myGroup));
        return "my/review/modify";
    }

    // @since 2023/03/08
    @GetMapping("review/view")
    public String reviewView(Model m) {
        m.addAttribute("title", environment.getProperty(myGroup));
        return "my/review/view";
    }

    // @since 2023/03/08
    @GetMapping("review/write")
    public String reviewWrite(Model m) {
        m.addAttribute("title", environment.getProperty(myGroup));
        return  "my/review/write";
    }

    // @since 2023/03/08
    @GetMapping("diary/list")
    public String diary_list(Model m) {
        m.addAttribute("title", environment.getProperty(diaryGroup));
        return "my/diary/list";
    }

    // @since 2023/03/09
    @GetMapping("diary/write")
    public String diary_write(Model m) {
        log.debug("GET diary/write start");

        m.addAttribute("title", environment.getProperty(diaryGroup));
        return "my/diary/write";
    }

    // @since 2023/03/10
    @PostMapping("diary/rsave")
    public String diary_rsave(ArticleDiaryVO adVO, DiarySpotVO dsVO, HttpServletRequest req) {
        adVO.setArti_regip(req.getRemoteAddr());

        service.diary_rsave(adVO, dsVO);

        log.debug(dsVO.getSpot_content().replace(",","/"));
        String[] content = dsVO.getSpot_content().split(",");
        for(int i = 0; i < content.length; i++){
            log.debug("content " + content[i]);
        }
        log.debug(""+dsVO.getSpot_lattitude().replace(",","/"));

        String[] lat = dsVO.getSpot_lattitude().split(",");
        for(int i = 0; i < lat.length; i++){
            log.debug("lat " + lat[i]);
        }
        log.debug(""+dsVO.getSpot_longtitude().replace(",","/"));

        String[] lng = dsVO.getSpot_lattitude().split(",");
        for(int i = 0; i < lng.length; i++){
            log.debug("lng " + lng[i]);
        }

        return "redirect:/my/diary/write";
    }

    // @since 2023/03/09
    @GetMapping("diary/modify")
    public String diary_modify(Model m) {
        m.addAttribute("title", environment.getProperty(diaryGroup));
        return "my/diary/modify";
    }

}
