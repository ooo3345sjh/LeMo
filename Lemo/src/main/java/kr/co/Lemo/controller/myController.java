package kr.co.Lemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @since 2023/03/07
 * @author 황원진
 * @apiNote 마이페이지 controller
 */

@Controller
@RequestMapping("my/")
public class myController {

    @GetMapping("coupon")
    public String coupon(){
        return "my/coupon";
    }

    @GetMapping("info")
    public String info(){
        return "my/info";
    }

    @GetMapping("pick")
    public String pick(){
        return "my/pick";
    }

    @GetMapping("point")
    public String point() {
        return "my/point";
    }

    // @since 2023/03/08
    @GetMapping("reserveration")
    public String reserveration() {return "my/reserveration";}

    // @since 2023/03/08
    @GetMapping("view")
    public String view() {return "my/view";}


    /*
    *   @since : 2023/03/08
    *   @apiNote : 나의 리뷰
    * */
    @GetMapping("review/list")
    public  String reviewList() {return "my/review/list";}

    @GetMapping("review/modify")
    public String reviewModify() {return "my/review/modify";}

    @GetMapping("review/view")
    public String reviewView() {return "my/review/view";}

    @GetMapping("review/write")
    public String reviewWrite() {return  "my/review/write";}

    /*
     *   @since : 2023/03/08
     *   @apiNote : 나의 여행일기
     * */
    @GetMapping("diary/list")
    public String diary_list() {return "my/diary/list";}

}
