package kr.co.Lemo.controller;

import kr.co.Lemo.domain.*;
import kr.co.Lemo.domain.search.My_SearchVO;
import kr.co.Lemo.domain.search.ProductDetail_SearchVO;
import kr.co.Lemo.service.MyService;

import kr.co.Lemo.utils.PageHandler;
import kr.co.Lemo.utils.SearchCondition;
import kr.co.Lemo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    // 서정현
    private final UserService userService;

    // @since 2023/03/12
    @GetMapping("{myCate}")
    public String myCate(
            @PathVariable String myCate,
            @RequestParam Map map,
            @AuthenticationPrincipal UserVO myUser,
            @ModelAttribute My_SearchVO vo,
            Model m
    ) {
        log.debug("GET " + myCate + " start");
        m.addAttribute("title", environment.getProperty(myGroup));
        String user_id = myUser.getUser_id();
        vo.setUser_id( myUser.getUser_id() );
        vo.setMap(map);

        switch (myCate) {
            case "coupon" :
                m.addAttribute("cate", "coupon");

                List<CouponVO> memberCoupons = service.findMemberCoupons(user_id);
                m.addAttribute("members", memberCoupons);

                List<CouponVO> productCoupons = service.findProductCoupons(user_id);
                m.addAttribute("products", productCoupons);

                return "my/coupon";
            case "info" :
                m.addAttribute("cate", "info");
                return "my/info";

            case "pick" :
                m.addAttribute("cate", "pick");

                // 페이징
                int totalPick = service.findTotalPicks(vo);
                int totalPickPage = (int)Math.ceil(totalPick / (double)vo.getPageSize());
                if(vo.getPage() > totalPickPage) vo.setPage(totalPickPage);

                PageHandler pickPageHandler = new PageHandler(totalPick, vo);

                List<ProductAccommodationVO> picks = service.findPicks(vo);

                m.addAttribute("picks", picks);
                m.addAttribute("ph", pickPageHandler);

                return "my/pick";

            case "point" :
                m.addAttribute("cate", "point");

                // 페이징
                int totalPoint = service.findTotalPoints(vo);
                int totalPointPage = (int)Math.ceil(totalPoint / (double)vo.getPageSize());
                if(vo.getPage() > totalPointPage) vo.setPage(totalPointPage);

                PageHandler pointPageHandler = new PageHandler(totalPoint, vo);
                
                List<PointVO> points = service.findPoints(vo);
                m.addAttribute("points", points);
                m.addAttribute("ph", pointPageHandler);

                return "my/point";
        }

        return "my/info";
    }

    // @since 2023/03/27
    @ResponseBody
    @PostMapping("coupon")
    public int rsaveCoupon(@RequestBody CouponVO coupon, @AuthenticationPrincipal UserVO myUser) {
        coupon.setUser_id(myUser.getUser_id());
        log.debug("cp_id"+coupon.getCp_id());
        CouponVO cnt = service.findProductCouponCnt(coupon);

        if(cnt.getCp_limitedIssuance() == cnt.getCp_IssuedCnt()) { return 101; }

        int result = service.rsaveCoupon(coupon);

        return result;
    }

    // @since 2023/03/08
    @GetMapping("review/list")
    public  String reviewList(
            @RequestParam Map map,
            @AuthenticationPrincipal UserVO myUser,
            @ModelAttribute My_SearchVO vo,
            Model m
    ) {
        m.addAttribute("title", environment.getProperty(myGroup));
        m.addAttribute("cate", "review");

        String user_id = myUser.getUser_id();
        vo.setMap(map);
        vo.setUser_id(user_id);
        log.info("reviewStat : " + map.get("reviewStat"));

        // 페이징
        int totalReview = service.findTotalReviews(vo);
        int totalReviewPage = (int)Math.ceil(totalReview / (double)vo.getPageSize());
        if(vo.getPage() > totalReviewPage) vo.setPage(totalReviewPage);

        PageHandler ReviewPageHandler = new PageHandler(totalReview, vo);

        List<ReservationVO> reviews = service.findReviews(vo);

        m.addAttribute("reviews", reviews);
        m.addAttribute("ph", ReviewPageHandler);

        return "my/review/list";
    }

    // @since 2023/03/08
    @GetMapping("review/view")
    public String reviewView(
            @RequestParam int res_no,
            @AuthenticationPrincipal UserVO myUser,
            Model m
    ) {
        m.addAttribute("title", environment.getProperty(myGroup));
        m.addAttribute("cate", "review");

        String uid = myUser.getUser_id();

        ReviewVO review = service.findReview(res_no);
        m.addAttribute("review", review);

        log.debug(""+review.getThumbs());

        return "my/review/view";
    }

    // @since 2023/03/08
    @GetMapping("review/write")
    public String reviewWrite(
            @RequestParam(defaultValue = "0") int res_no,
            @RequestParam(defaultValue = "0") int acc_id,
            RedirectAttributes re,
            @AuthenticationPrincipal UserVO myUser,
            Model m
    ) {
        m.addAttribute("title", environment.getProperty(myGroup));
        m.addAttribute("cate", "review");

        if(res_no == 0 || acc_id == 0) { return "redirect:/my/review/list"; }

        int result = service.findCheckReview(res_no);

        if(result == 1) {
            re.addAttribute("res_no", res_no);
            return "redirect:/my/review/view";
        }

        String uid = myUser.getUser_id();

        ReviewVO review = service.findReviewAccommodation(res_no);
        m.addAttribute("review", review);

        return  "my/review/write";
    }

    // @since 2023/03/29
    @PostMapping("review/write")
    public void reviewWrite(
            @RequestParam Map<String, Object> param,
            MultipartHttpServletRequest request,
            HttpServletRequest req,
            @AuthenticationPrincipal UserVO myUser
    ) {
        log.debug("param : " + param);

        param.put("user_id", myUser.getUser_id());
        param.put("revi_regip", req.getRemoteAddr());

        service.rsavsReview(request, param);

    }

    // @since 2023/03/08
    @GetMapping("review/modify")
    public String reviewModify(
            @AuthenticationPrincipal UserVO myUser,
            Model m
    ) {
        m.addAttribute("title", environment.getProperty(myGroup));
        m.addAttribute("cate", "review");

        String uid = myUser.getUser_id();

        return "my/review/modify";
    }

    // @since 2023/03/08
    @GetMapping("diary/list")
    public String diary_list(
            @AuthenticationPrincipal UserVO myUser,
            Model m
    ) {
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
            @RequestParam(value = "res_no", defaultValue = "0") int res_no,
            @RequestParam(value = "acc_name") String acc_name,
            Model m
    ) {
        log.debug("GET diary/write start");

        if(res_no == 0 || acc_name == null) { return "redirect:/my/reservation"; }

        //int result = service.findCheckReview(res_no);

        m.addAttribute("cate", "diary");
        m.addAttribute("title", environment.getProperty(diaryGroup));
        m.addAttribute("acc_name", acc_name);

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

        log.debug("map : " + param);

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

    // @since 2023/03/29
    @GetMapping("reservation/list")
    public String reservationList(
            @RequestParam Map map,
            @AuthenticationPrincipal UserVO myUser,
            @ModelAttribute My_SearchVO vo,
            Model m
    ) {
        m.addAttribute("cate", "reservation");
        vo.setMap(map);
        vo.setUser_id(myUser.getUser_id());

        // 페이징
        int totalReservation = service.findTotalReservations(vo);
        int totalReservationPage = (int)Math.ceil(totalReservation / (double)vo.getPageSize());
        if(vo.getPage() > totalReservationPage) vo.setPage(totalReservationPage);

        PageHandler ReservationPageHandler = new PageHandler(totalReservation, vo);

        List<ReservationVO> reservations = service.findReservations(vo);
        m.addAttribute("reservations", reservations);
        m.addAttribute("ph", ReservationPageHandler);

        return "my/reservation/list";
    }

    @GetMapping("reservation/view")
    public String reservationView(Model m, @RequestParam(defaultValue = "0") int res_no) {

        if(res_no == 0) { return "redirect:/my/reservation/list"; }

        m.addAttribute("cate", "view");
        //service.findMyArticle(myCate, uid);

        ReservationVO reservation = service.findReservation(res_no);

        if(reservation == null) { return "redirect:/my/reservation/list"; }

        m.addAttribute("reservation", reservation);

        return "my/reservation/view";
    }

    @ResponseBody
    @DeleteMapping("reservation")
    public int reservationDelete(@RequestBody ReservationVO resVO) {

        log.debug("res_no : " + resVO.getRes_no());

        int result = service.removeUpdateReservation( resVO.getRes_no() );

        return result;
    }


    /**
     * @since 2023/03/27
     * @auhtor 서정현
     * @param photo 프로필 사진 이미지 파일
     * @return 결과값 1:성공 0:실패
     */
    @ResponseBody
    @PatchMapping("info/profile")
    public String uploadProfile(
            @RequestPart(value = "profileFile") MultipartFile photo,
            @AuthenticationPrincipal UserVO userVO
    ) throws  Exception {
        log.debug("MyService PATCH uploadProfile start...");
        log.debug(""+photo.toString());
        int result = userService.usaveProfile(photo, userVO);
        return result+"";
    }

    /**
     * @since 2023/03/27
     * @author 서정현
     * @return
     */
    @ResponseBody
    @DeleteMapping("info/profile")
    public Map removeProfile(
            @AuthenticationPrincipal UserVO userVO
    ) {
        log.debug("MyService DELETE deleteProfile start...");
        Map map = new HashMap();
        map.put("result", userService.removeProfile(userVO));
        return map;
    }

    /**
     * @since 2023/03/27
     * @author 서정현
     * @return
     */
    @ResponseBody
    @PatchMapping("info/nick")
    public Map updateNick(
            @AuthenticationPrincipal UserVO userVO,
            @RequestBody Map map
    ) throws  Exception {
        log.debug("MyService PATCH updateNick start...");

        String nick = (String)map.get("nick");
        int result = 0;
        if(nick != null)
            result = userService.usaveNick(nick, userVO);
        map.put("result", result);
        return map;
    }

    /**
     * @since 2023/03/28
     * @author 서정현
     * @return
     */
    @ResponseBody
    @PatchMapping("info/hp")
    public Map updateHp(
            @AuthenticationPrincipal UserVO userVO,
            @RequestBody Map map
    ) throws  Exception {
        log.debug("MyService PATCH updateHp start...");

        String hp = (String) map.get("hp");
        int result = 0;

        if(hp != null)
            result = userService.usaveHp(hp, userVO);

        map.put("result", result);
        return map;
    }

    /**
     * @since 2023/03/28
     * @author 서정현
     * @return
     */
    @ResponseBody
    @PatchMapping("info/isNoticeEnabled")
    public Map updateIsNoticeEnabled(
            @AuthenticationPrincipal UserVO userVO,
            @RequestBody Map map
    ) throws  Exception {
        log.debug("MyService PATCH isNoticeEnabled start...");

        Integer isNoticeEnabled = (Integer) map.get("isNoticeEnabled");
        int result = 0;

        if(isNoticeEnabled != null)
            result = userService.usaveIsNoticeEnabled(isNoticeEnabled, userVO);

        map.put("result", result);
        return map;
    }

}
