package kr.co.Lemo.controller;

import kr.co.Lemo.domain.*;
import kr.co.Lemo.service.MyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    // @since 2023/03/12
    @GetMapping("{myCate}")
    public String myCate(
            @PathVariable String myCate,
            @AuthenticationPrincipal UserVO myUser,
            Model m
    ) {
        log.debug("GET " + myCate + " start");
        m.addAttribute("title", environment.getProperty(myGroup));

        String user_id = myUser.getUser_id();

        switch (myCate) {
            case "coupon" :
                m.addAttribute("cate", "coupon");
                //MyVO myVO = service.findMyArticle(myCate, user_id);
                //log.debug("myVO : " + myVO);
                List<CouponVO> coupons = service.findCoupons(user_id);
                m.addAttribute("articles", coupons);

                return "my/coupon";
            case "info" :
                m.addAttribute("cate", "info");


                return "my/info";
            case "pick" :
                m.addAttribute("cate", "pick");
                //service.findMyArticle(myCate, uid);
                List<PickVO> picks = service.findPicks(user_id);
                m.addAttribute("articles", picks);
                log.debug("picks : " + picks);

                return "my/pick";
            case "point" :
                m.addAttribute("cate", "point");
                //service.findMyArticle(myCate, uid);

                return "my/point";
            case "reservation" :
                m.addAttribute("cate", "reservation");
                //service.findMyArticle(myCate, uid);
                List<ReservationVO> reservations = service.findReservations(user_id);
                m.addAttribute("articles", reservations);
                log.debug("reservations : " + reservations);

                return "my/reservation";
            case "view" :
                m.addAttribute("cate", "view");
                //service.findMyArticle(myCate, uid);

                return "my/view";
            case "review" :
                m.addAttribute("cate", "review");
                //service.findMyArticle(myCate, uid);

                return "my/review/list";
        }

        return "my/info";
    }

    // @since 2023/03/08
    @GetMapping("review/list")
    public  String reviewList(
            @AuthenticationPrincipal UserVO myUser,
            Model m
    ) {
        m.addAttribute("title", environment.getProperty(myGroup));
        m.addAttribute("cate", "review");

        String uid = myUser.getUser_id();

        return "my/review/list";
    }

    // @since 2023/03/08
    @GetMapping("review/modify")
    public String reviewModify(
            @AuthenticationPrincipal UserVO myUser,
            Model m) {
        m.addAttribute("title", environment.getProperty(myGroup));
        m.addAttribute("cate", "review");

        String uid = myUser.getUser_id();

        return "my/review/modify";
    }

    // @since 2023/03/08
    @GetMapping("review/view")
    public String reviewView(
            @AuthenticationPrincipal UserVO myUser,
            Model m) {
        m.addAttribute("title", environment.getProperty(myGroup));
        m.addAttribute("cate", "review");

        String uid = myUser.getUser_id();

        return "my/review/view";
    }

    // @since 2023/03/08
    @GetMapping("review/write")
    public String reviewWrite(
            @AuthenticationPrincipal UserVO myUser,
            Model m) {
        m.addAttribute("title", environment.getProperty(myGroup));
        m.addAttribute("cate", "review");

        String uid = myUser.getUser_id();

        return  "my/review/write";
    }

    // @since 2023/03/08
    @GetMapping("diary/list")
    public String diary_list(
            @AuthenticationPrincipal UserVO myUser,
            Model m) {
        log.debug("GET diary/list start");
        m.addAttribute("title", environment.getProperty(diaryGroup));
        m.addAttribute("cate", "diary");

        String user_id = myUser.getUser_id();

        Map<Integer, List<DiarySpotVO>> map = service.findDiaryArticle(user_id);
        m.addAttribute("map", map);

        return "my/diary/list";
    }

    // @since 2023/03/09
    @GetMapping("diary/write")
    public String diary_write(
            @AuthenticationPrincipal UserVO myUser,
            Model m
    ) {
        log.debug("GET diary/write start");
        m.addAttribute("cate", "diary");
        m.addAttribute("title", environment.getProperty(diaryGroup));

        String uid = myUser.getUser_id();

        return "my/diary/write";
    }

    /**
     * @since 2023/03/10
     * @apiNote @RequestPart는 multipart/form-data에 특화된 어노테이션
     */
    @ResponseBody
    @PostMapping("diary/rsave")
    public Map<String, Integer> diary_rsave(
            @AuthenticationPrincipal UserVO myUser,
            @RequestPart(value = "key") Map<String, Object> param,
            @RequestPart(value = "file", required = false) List<MultipartFile> fileList,
            HttpServletRequest req
    ) throws Exception {
        log.debug("POST diary/rsave start");
        
        // 이후에 principal로 대체
        String user_id = myUser.getUser_id();

        int result = service.diary_rsave(param, fileList, req, user_id);

        Map<String, Integer> map = new HashMap<>();
        map.put("result", result);

        return map;
    }

    // @since 2023/03/09
    @GetMapping("diary/modify")
    public String diary_modify(
            @AuthenticationPrincipal UserVO myUser,
            Model m
    ) {
        m.addAttribute("title", environment.getProperty(diaryGroup));

        String uid = myUser.getUser_id();



        return "my/diary/modify";
    }

}
