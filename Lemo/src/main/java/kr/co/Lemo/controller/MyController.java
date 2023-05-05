package kr.co.Lemo.controller;

import kr.co.Lemo.domain.*;
import kr.co.Lemo.domain.search.My_SearchVO;
import kr.co.Lemo.entity.WithdrawLogEntity;
import kr.co.Lemo.repository.WithdrawLogRepo;
import kr.co.Lemo.service.*;
import kr.co.Lemo.utils.PageHandler;
import kr.co.Lemo.utils.RemoteAddrHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private String diaryGroup = "title.my.diary";
    private final MyService service;
    private final ProductService productService;
    private final DiaryService diaryService;

    // @since 2023/03/31
    @Autowired
    private PaymentService paymentservice;

    // 서정현
    private final UserService userService;
    private final WithdrawLogRepo withdrawLogRepo;

    // @since 2023/03/12
    @GetMapping("{myCate}")
    public String myCate(
            @PathVariable String myCate,
            @RequestParam Map map,
            @AuthenticationPrincipal UserVO myUser,
            @ModelAttribute My_SearchVO vo,
            Model m
    ) {
        log.info("GET " + myCate + " start");
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

                int point = service.findMemberPoint(user_id);

                // 페이징
                int totalPoint = service.findTotalPoints(vo);
                int totalPointPage = (int)Math.ceil(totalPoint / (double)vo.getPageSize());
                if(vo.getPage() > totalPointPage) vo.setPage(totalPointPage);

                PageHandler pointPageHandler = new PageHandler(totalPoint, vo);
                
                List<PointVO> points = service.findPoints(vo);

                m.addAttribute("points", points);
                m.addAttribute("ph", pointPageHandler);
                m.addAttribute("point", point);

                return "my/point";
            case "qna" :
                m.addAttribute("cate", "qna");

                // 페이징
                int totalQna = service.findDiaryQnaCnt(vo);
                int totalQnaPage = (int)Math.ceil(totalQna / (double)vo.getPageSize());
                if(vo.getPage() > totalQnaPage) vo.setPage(totalQnaPage);

                PageHandler qnaPageHandler = new PageHandler(totalQna, vo);

                List<ProductQnaVO> csVO = service.findDiaryQna(vo);
                m.addAttribute("cses", csVO);
                m.addAttribute("ph", qnaPageHandler);

                return "my/qna";
        }

        return "my/info";
    }

    // @since 2023/03/27
    @ResponseBody
    @PostMapping("coupon")
    public int rsaveCoupon(@RequestBody CouponVO coupon, @AuthenticationPrincipal UserVO myUser) {
        log.info("POST coupon start");

        coupon.setUser_id(myUser.getUser_id());
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
        log.info("GET review/list start");

        m.addAttribute("title", environment.getProperty(myGroup));
        m.addAttribute("cate", "review");

        String user_id = myUser.getUser_id();
        vo.setMap(map);
        vo.setUser_id(user_id);

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
            @RequestParam(defaultValue = "0") long res_no,
            @AuthenticationPrincipal UserVO myUser,
            Model m
    ) {
        log.info("GET review/view start");

        m.addAttribute("title", environment.getProperty(myGroup));
        m.addAttribute("cate", "review");
        if(res_no == 0) { return "redirect:/my/review/list"; }
        String uid = myUser.getUser_id();

        ReviewVO review = service.findReview(res_no, myUser.getUser_id());

        if(review == null) { return "redirect:/my/review/list"; }
        if(!uid.equals(review.getUser_id())) { return "redirect:/my/review/list"; }

        log.debug(""+review.getThumbs());

        m.addAttribute("review", review);

        return "my/review/view";
    }

    // @since 2023/03/08
    @GetMapping("review/write")
    public String reviewWrite(
            @RequestParam(defaultValue = "0") long res_no,
            @RequestParam(defaultValue = "0") int acc_id,
            RedirectAttributes re,
            @AuthenticationPrincipal UserVO myUser,
            Model m
    ) {
        log.info("GET review/write start");

        m.addAttribute("title", environment.getProperty(myGroup));
        m.addAttribute("cate", "review");

        if(res_no == 0 || acc_id == 0) { return "redirect:/my/review/list"; }

        int result = service.findCheckReview(res_no);

        if(result == 1) {
            re.addAttribute("res_no", res_no);
            return "redirect:/my/review/view";
        }

        String uid = myUser.getUser_id();

        ReviewVO review = service.findReviewAccommodation(res_no, myUser.getUser_id());

        if(review == null) { return "redirect:/my/review/list"; }
        if(!uid.equals(review.getUser_id())) { return "redirect:/my/review/list"; }

        m.addAttribute("review", review);

        return  "my/review/write";
    }

    // @since 2023/03/29
    @ResponseBody
    @PostMapping("review/write")
    public Map<String, String> reviewWrite(
            @RequestParam Map<String, Object> param,
            MultipartHttpServletRequest request,
            HttpServletRequest req,
            @AuthenticationPrincipal UserVO myUser
    ) {
        log.info("POST review/wrtie start");

        param.put("user_id", myUser.getUser_id());
        param.put("revi_regip", req.getRemoteAddr());

        int result = service.rsavsReview(request, param);

        Map<String, String> map = new HashMap<>();

        String finalResult = "usaveWriteFail";

        if(result == 1) {
            finalResult = "usavaWriteSuccess";
        }

        map.put("result", finalResult);

        return map;
    }

    // @since 2023/03/08
    @GetMapping("review/modify")
    public String reviewModify(
            @AuthenticationPrincipal UserVO myUser,
            Model m,
            @RequestParam(defaultValue = "0") long res_no
    ) {
        log.info("GET review/modify start");

        m.addAttribute("title", environment.getProperty(myGroup));
        m.addAttribute("cate", "review");

        if(res_no == 0) { return "redirect:/my/review/list"; }

        String uid = myUser.getUser_id();

        ReviewVO review = service.findReview(res_no, myUser.getUser_id());
        if(review == null) { return "redirect:/my/review/list"; }
        if(!uid.equals(review.getUser_id())) { return "redirect:/my/review/list"; }

        m.addAttribute("review", review);

        return "my/review/modify";
    }

    // @since 2023/04/05
    @ResponseBody
    @PostMapping("review/usave")
    public String usaveReview(
            @RequestParam Map<String, Object> param,
            HttpServletRequest req,
            @AuthenticationPrincipal UserVO myUser,
            MultipartHttpServletRequest request
    ) {
        log.info("POST review/usave start");

        Map<String, MultipartFile> fileMap = request.getFileMap();
        param.put("revi_regip", req.getRemoteAddr());

        String user_id = myUser.getUser_id();
        long res_no = Long.parseLong(String.valueOf(param.get("res_no")));
        String review_id = service.findCheckReviewId(res_no);

        if(!user_id.equals(review_id)) { return "usaveImageFail"; }

        String result = service.usaveReview(param, fileMap);

        return result;
    }

    // @since 2023/04/06
    @ResponseBody
    @DeleteMapping("review")
    public int removeReview(
            @RequestBody Map map,
            @AuthenticationPrincipal UserVO myUser
    ) {
        log.info("DELETE review start");

        long res_no = Long.parseLong(String.valueOf(map.get("res_no")));
        String user_id = myUser.getUser_id();
        String review_id = service.findCheckReviewId(res_no);

        if(!user_id.equals(review_id)) { return 101; }

        int result = service.removeReview(res_no);

        return result;
    }

    // @since 2023/03/08
    @GetMapping("diary/list")
    public String diary_list(
            @AuthenticationPrincipal UserVO myUser,
            @ModelAttribute My_SearchVO vo,
            Model m
    ) {
        log.info("GET diary/list start");
        m.addAttribute("title", environment.getProperty(diaryGroup));
        m.addAttribute("cate", "diary");
        vo.setUser_id(myUser.getUser_id());

        List<ArticleDiaryVO> articles = service.findDiaryArticles(vo);

        int totalDiary = service.findTotalDiary(vo);
        int totalDiaryPage = (int)Math.ceil(totalDiary / (double)vo.getPageSize());
        if(vo.getPage() > totalDiaryPage) vo.setPage(totalDiaryPage);

        PageHandler qnaPageHandler = new PageHandler(totalDiary, vo);

        m.addAttribute("articles", articles);
        m.addAttribute("ph", qnaPageHandler);

        return "my/diary/list";
    }

    // @since 2023/03/09
    @GetMapping("diary/write")
    public String diary_write(
            @AuthenticationPrincipal UserVO myUser,
            @RequestParam(value = "res_no", defaultValue = "0") long res_no,
            Model m
    ) {
        log.info("GET diary/write start");

        if(res_no == 0) { return "redirect:/my/reservation/list"; }

        ProductAccommodationVO accommo = service.findeDiaryXY(res_no);

        if(accommo.getRes_state() != 2) { return "redirect:/my/reservation/list"; }
        if(!accommo.getUser_id().equals(myUser.getUser_id())) { return "redirect:/my/reservation/list"; }

        m.addAttribute("cate", "diary");
        m.addAttribute("title", environment.getProperty(diaryGroup));
        m.addAttribute("accommo", accommo);

        String uid = myUser.getUser_id();

        return "my/diary/write";
    }

    // @since 2023/03/09
    @GetMapping("diary/modify")
    public String diary_modify(
            @AuthenticationPrincipal UserVO myUser,
            @RequestParam(defaultValue = "0") int arti_no,
            @RequestParam(defaultValue = "0") long res_no,
            Model m
    ) {
        log.info("GET diary/modify start");

        m.addAttribute("title", environment.getProperty(diaryGroup));

        if(arti_no == 0) { return "redirect:/my/reservation/list"; }

        String uid = myUser.getUser_id();

        ProductAccommodationVO accommo = service.findeDiaryXY(res_no);
        m.addAttribute("accommo", accommo);

        List<DiarySpotVO> spotVO = diaryService.usaveDiarySpot(arti_no);
        m.addAttribute("article", spotVO);

        return "my/diary/modify";
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
        log.info("POST diary/rsave start");
        log.debug(param.toString());
        log.debug(fileList.toString());
        // 이후에 principal로 대체
        String user_id = myUser.getUser_id();

        int result = service.diary_rsave(param, fileList, req, user_id);

        Map<String, Integer> map = new HashMap<>();
        map.put("result", result);

        return map;
    }

    // @since 2023/04/11
    @ResponseBody
    @PostMapping("diary/usave")
    public Map<String, String> diary_usave(
            @AuthenticationPrincipal UserVO myUser,
            @RequestPart(value = "key") Map<String, Object> param,
            @RequestPart(value = "file", required = false) List<MultipartFile> fileList,
            HttpServletRequest req
    ) {
        log.info("POST diary/usave start");

        String user_id = myUser.getUser_id();

        service.removeDiarySpot(param);

        int result = service.diary_usave(param, fileList, req, user_id);

        Map<String, String> map = new HashMap<>();
        String finalResult = "usaveDairyFail";
        switch (result) {
            case 1 :
                finalResult = "usaveDairySuccess";
        }

        map.put("result", finalResult);

        return map;
    }

    // @since 2023/03/29
    @GetMapping("reservation/list")
    public String reservationList(
            @RequestParam Map map,
            @AuthenticationPrincipal UserVO myUser,
            @ModelAttribute My_SearchVO vo,
            Model m
    ) {
        log.info("GET reservation/list start");

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
        m.addAttribute("title", environment.getProperty(myGroup));

        return "my/reservation/list";
    }

    @GetMapping("reservation/view")
    public String reservationView(
            Model m,
            @RequestParam(defaultValue = "0") long res_no,
            @AuthenticationPrincipal UserVO myUser
    ) {
        log.info("GET reservation/view start");

        m.addAttribute("cate", "reservation");
        m.addAttribute("title", environment.getProperty(myGroup));

        if(res_no == 0) { return "redirect:/my/reservation/list"; }

        ReservationVO reservation = service.findReservation(res_no, myUser.getUser_id());

        log.info(reservation + "");
        if(reservation == null) { return "redirect:/my/reservation/list"; }

        // 예약 정보 가져오기
        ReservationVO vo = productService.findOrderInfo(res_no);

        int res_payment = vo.getRes_payment();
        String payment = "";

        switch(res_payment) {
            case 1 :
                payment = "신용/카드결제";
                break;
            case 2 :
                payment = "토스페이";
                break;
            case 3 :
                payment = "PAYCO";
                break;
            case 4:
                payment = "카카오페이";
                break;
            case 5:
                payment = "계좌이체";
                break;
        }

        vo.setPayment(payment);
        m.addAttribute("oi", vo);
        log.debug("oi : " + vo);

        return "my/reservation/view";
    }

    @ResponseBody
    @Transactional
    @DeleteMapping("reservation")
    public int reservationDelete(@RequestBody Map map, @AuthenticationPrincipal UserVO myUser) throws Exception {
        log.info("DELETE reservation start");

        int result = 0;

        if(map.get("res_no") == null){
            return result;
        }

        long res_no = Long.parseLong((String)map.get("res_no"));

        result = service.removeUpdateReservation( res_no, myUser.getUser_id() );

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
        log.info("MyController PATCH uploadProfile start...");
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
        log.info("MyController DELETE deleteProfile start...");
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
        log.info("MyController PATCH updateNick start...");

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
        log.info("MyController PATCH updateHp start...");

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
    @PatchMapping("info/notification")
    public Map updateIsNoticeEnabled(
            @AuthenticationPrincipal UserVO userVO,
            @RequestBody Map map
    ) throws  Exception {
        log.info("MyController PATCH notification start...");

        Integer isNoticeEnabled = (Integer) map.get("isNoticeEnabled");
        int result = 0;

        if(isNoticeEnabled != null)
            result = userService.usaveIsNoticeEnabled(isNoticeEnabled, userVO);

        map.put("result", result);
        return map;
    }

    /**
     * @since 2023/04/11
     * @author 서정현
     * @apiNote 회원탈퇴 화면 출력
     */
    @GetMapping("withdraw")
    public String withdrawUser(@AuthenticationPrincipal UserVO user, Model m){
        log.info("MyController GET withdrawUser start...");
        service.findUserPointAndCouponCnt(m, user.getUser_id());
        return "my/withdraw";
    }

    /**
     * @since 2023/04/11
     * @author 서정현
     * @apiNote 회원탈퇴 등록
     */
    @ResponseBody
    @PostMapping("withdraw")
    public Map withdrawUser(
            @AuthenticationPrincipal UserVO user,
            @RequestBody Map map,
            HttpServletRequest req
    ){
        log.info("MyController POST withdrawUser start...");
        
        // 회원 탈퇴 처리(포인트 소멸, 쿠폰 삭제 및 userinfo.isEnabled = 0 처리)
        int result = service.usaveWithdrawUser(user.getUser_id());
        
        // mongodb에 회원탈퇴 로그 저장
        WithdrawLogEntity withdrawLogEntity = WithdrawLogEntity.builder()
                .username(user.getUser_id())
                .ip(RemoteAddrHandler.getRemoteAddr(req))
                .text((String)map.get("text"))
                .rdate(new Date())
                .build();
        withdrawLogEntity = withdrawLogRepo.save(withdrawLogEntity);

        map.put("result", result);
        return map;
    }

    /**
     * @since 2023/04/28
     * @author 서정현
     * @apiNote 회원의 숙박완료 건수 조회
     */
    @ResponseBody
    @GetMapping("reservation-cnt")
    public Map countUserReservation(@AuthenticationPrincipal UserVO user){
        int count = 0;

        if(user != null){
            count = service.countUserReservation(user.getUser_id());
        }

        Map map = new HashMap();
        map.put("count", count);
        return map;
    }

    /**
     * @since 2023/04/11
     * @author 이해빈
     * @apiNote 찜한 숙소 삭제
     */
    @ResponseBody
    @DeleteMapping("pick")
    public Map removePick(@RequestBody Map map, @AuthenticationPrincipal UserVO myUser){

        String user_id = myUser.getUser_id();
        map.put("user_id", user_id);

        int result = service.removePick(map);
        map.put("result", result);

        return map;
    }


    /**
     * @since 2023/04/12
     * @author 이해빈
     * @apiNote 숙박 문의 삭제
     */
    @ResponseBody
    @DeleteMapping("qna")
    public Map removeQna(@RequestBody Map map){

        int result = service.removeQna(map);
        map.put("result", 1);

        return map;
    }


}
