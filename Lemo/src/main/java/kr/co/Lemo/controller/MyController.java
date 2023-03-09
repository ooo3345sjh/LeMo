package kr.co.Lemo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @since 2023/03/07
 * @author 황원진
 * @apiNote 마이페이지 controller
 */

@Controller
@PropertySource(value = "classpath:title.properties", encoding = "UTF-8")
@RequiredArgsConstructor
@RequestMapping("my/")
public class MyController {

    private final Environment environment;

    private String myGroup = "title.my";
    private String diaryGroup = "title.diary";

    @GetMapping("coupon")
    public String coupon(Model m){
        m.addAttribute("title", environment.getProperty(myGroup));
        return "my/coupon";
    }

    @GetMapping("info")
    public String info(Model m){
        m.addAttribute("title", environment.getProperty(myGroup));
        return "my/info";
    }

    @GetMapping("pick")
    public String pick(Model m){
        m.addAttribute("title", environment.getProperty(myGroup));
        return "my/pick";
    }

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


    /*
    *   @since : 2023/03/08
    *   @apiNote : 나의 리뷰
    * */
    @GetMapping("review/list")
    public  String reviewList(Model m) {
        m.addAttribute("title", environment.getProperty(myGroup));
        return "my/review/list";
    }

    @GetMapping("review/modify")
    public String reviewModify(Model m) {
        m.addAttribute("title", environment.getProperty(myGroup));
        return "my/review/modify";
    }

    @GetMapping("review/view")
    public String reviewView(Model m) {
        m.addAttribute("title", environment.getProperty(myGroup));
        return "my/review/view";
    }

    @GetMapping("review/write")
    public String reviewWrite(Model m) {
        m.addAttribute("title", environment.getProperty(myGroup));
        return  "my/review/write";
    }

    /*
     *   @since : 2023/03/08
     *   @apiNote : 나의 여행일기
     * */
    @GetMapping("diary/list")
    public String diary_list(Model m) {
        m.addAttribute("title", environment.getProperty(diaryGroup));
        return "my/diary/list";
    }

    /**
     * @since 2023/03/08
     * @author 박종협
     */
    @GetMapping("diary/write")
    public String diary_write(Model m) {
        m.addAttribute("title", environment.getProperty(diaryGroup));
        return "my/diary/write";
    }

    @GetMapping("diary/modify")
    public String diary_modify(Model m) {
        m.addAttribute("title", environment.getProperty(diaryGroup));
        return "my/diary/modify";
    }

}
