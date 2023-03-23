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
            Model m
    ) {
        log.debug("GET " + myCate + " start");
        m.addAttribute("title", environment.getProperty(myGroup));

        String uid = "test@test.com";

        switch (myCate) {
            case "coupon" :
                m.addAttribute("cate", "coupon");

                return "my/coupon";
            case "info" :
                m.addAttribute("cate", "info");

                return "my/info";
            case "pick" :
                m.addAttribute("cate", "pick");

                return "my/pick";
            case "point" :
                m.addAttribute("cate", "point");

                return "my/point";
            case "reservation" :
                m.addAttribute("cate", "reservation");


                return "my/reservation";
            case "view" :
                m.addAttribute("cate", "view");

                return "my/view";
            case "review" :
                m.addAttribute("cate", "review");

                return "my/review/list";
        }

        return "my/info";
    }

    // @since 2023/03/08
    @GetMapping("review/list")
    public  String reviewList(Model m) {
        m.addAttribute("title", environment.getProperty(myGroup));
        m.addAttribute("cate", "review");

        return "my/review/list";
    }

    // @since 2023/03/08
    @GetMapping("review/modify")
    public String reviewModify(Model m) {
        m.addAttribute("title", environment.getProperty(myGroup));
        m.addAttribute("cate", "review");

        return "my/review/modify";
    }

    // @since 2023/03/08
    @GetMapping("review/view")
    public String reviewView(Model m) {
        m.addAttribute("title", environment.getProperty(myGroup));
        m.addAttribute("cate", "review");

        return "my/review/view";
    }

    // @since 2023/03/08
    @GetMapping("review/write")
    public String reviewWrite(Model m) {
        m.addAttribute("title", environment.getProperty(myGroup));
        m.addAttribute("cate", "review");

        return  "my/review/write";
    }

    // @since 2023/03/08
    @GetMapping("diary/list")
    public String diary_list(Model m) {
        log.debug("GET diary/list start");
        m.addAttribute("title", environment.getProperty(diaryGroup));
        m.addAttribute("cate", "diary");

        String uid = "test@test.com";

        Map<Integer, List<DiarySpotVO>> map = service.findDiaryArticle(uid);
        m.addAttribute("map", map);

        return "my/diary/list";
    }

    // @since 2023/03/09
    @GetMapping("diary/write")
    public String diary_write(Model m) {
        log.debug("GET diary/write start");
        m.addAttribute("cate", "diary");

        m.addAttribute("title", environment.getProperty(diaryGroup));
        return "my/diary/write";
    }

    /**
     * @since 2023/03/10
     * @apiNote @RequestPart는 multipart/form-data에 특화된 어노테이션
     */
    @ResponseBody
    @PostMapping("diary/rsave")
    public String diary_rsave(
            @RequestPart(value = "key") Map<String, Object> param,
            @RequestPart(value = "file", required = false) List<MultipartFile> fileList,
            HttpServletRequest req
    ) throws Exception {
        log.debug("POST diary/rsave start");
        
        // 이후에 principal로 대체
        String uid = "test@test.com";

        service.diary_rsave(param, fileList, req, uid);


        return "redirect:/my/diary/list";
    }

    // @since 2023/03/09
    @GetMapping("diary/modify")
    public String diary_modify(Model m) {
        m.addAttribute("title", environment.getProperty(diaryGroup));

        String uid = "test@test.com";



        return "my/diary/modify";
    }

}
