package kr.co.Lemo.controller;

import kr.co.Lemo.domain.*;
import kr.co.Lemo.domain.search.ProductDetail_SearchVO;
import kr.co.Lemo.domain.search.Product_SearchVO;
import kr.co.Lemo.service.PaymentService;
import kr.co.Lemo.service.ProductService;
import kr.co.Lemo.utils.PageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @since 2023/03/08
 * @author 이해빈
 * @apiNote 상품 controller
 */

@Slf4j
@Controller
@PropertySource(value = "classpath:title.properties", encoding = "UTF-8")
@RequestMapping("product/")
public class ProductController {

    @Autowired
    private Environment environment;
    private String group = "title.product";

    @Autowired
    private ProductService service;
    @Autowired
    private PaymentService paymentservice;

    /**
     * @since 2023/03/08
     * @param vo (상품 검색 조건 )
     * @Map map (상품 검색 조건)
     */
    @GetMapping("list")
    public String list(Model model,
                       @RequestParam Map map,
                       @ModelAttribute Product_SearchVO vo) throws Exception {
        log.debug("GET list start");

        // 숙박업소 리스트 가져오기
        service.findAllAccommodations(model, map, vo);

        model.addAttribute("title", environment.getProperty(group));

        return "product/list";
    }

    // @since 2023/03/17 상품 보기
    @GetMapping("view")
    public String view(Model model,
                       int acc_id,
                       String checkIn,
                       String checkOut,
                       @AuthenticationPrincipal UserVO myUser
                       ) throws Exception {


        log.debug("Get view start");

        String uid = "";
        if(myUser != null) {
            uid = myUser.getUser_id();
        }


        // 숙소 정보 가져오기
        List<ProductAccommodationVO> rooms = service.findAccommodation(acc_id, checkIn, checkOut);

        int acc_state = rooms.get(0).getAcc_state();
        
        // 차단 * 삭제된 숙소인 경우 잘못된 접근 페이지로 이동
        if(acc_state != 1){
            model.addAttribute("error", "B");
            return "error/abnormalAccess";
        }

        log.info("room정보 : " + rooms);


        String user_id = rooms.get(0).getUser_id();
        
        // 서비스 카테 가져오기
        List<ServiceCateVO> scs = service.findAllServiceCate(acc_id);

        // 판매자 정보 가져오기
        BusinessInfoVO bv = service.findBusinessInfo(user_id);

        // 숙소 찜 여부 가져오기
        int result = 0;

        if(uid != ""){
            result = service.findProductPick(acc_id, uid);
        }

        model.addAttribute("result", result);
        model.addAttribute("uid", uid);
        model.addAttribute("scs", scs);
        model.addAttribute("bv", bv);
        model.addAttribute("rooms", rooms);
        model.addAttribute("checkIn", checkIn);
        model.addAttribute("checkOut", checkOut);
        model.addAttribute("title", environment.getProperty(group));

        return "product/view";
    }

    @ResponseBody
    @PostMapping("reservation")
    public Map reservation(@RequestBody Map map, HttpSession session) {

        session.setAttribute("map", map);

        return map;
    }

    // @since 2023/03/28
    @GetMapping("reservation")
    public String reservation(Model model,
                              HttpSession session,
                              @AuthenticationPrincipal UserVO myUser) throws Exception {

        Map map = (Map) session.getAttribute("map");

        // 맵에 값이 존재하지 않으면 product/list로 redirect
        if(map == null){
            return "redirect:/product/list";
        }

        // 유저 정보 가져오기
        String user_id = myUser.getUser_id();
        UserVO user = service.findUser(user_id);

        map.put("user_id", user_id);

        // 객실 정보 가져오기
        map = service.findRoomForReservation(map);
        ProductAccommodationVO room = (ProductAccommodationVO) map.get("room");

        // 쿠폰 가져오기
        List<CouponVO> cps = service.findAllCouponsForReservation(map);
        map.put("cps", cps);

        // 판매자 정보 가져오기
        String bis_uid = room.getUser_id();
        BusinessInfoVO bv = service.findBusinessInfo(bis_uid);

        // 추후 결제 데이터 정보와 비교하기 위해 세션에 다시 저장
        session.setAttribute("resultmap", map);

        model.addAttribute("map", map);
        model.addAttribute("user", user);
        model.addAttribute("room", room);
        model.addAttribute("cps", cps);
        model.addAttribute("bv",bv);
        model.addAttribute("title", environment.getProperty(group));

        return "product/reservation";
    }


    @GetMapping("result")
    public String result(HttpSession session,
                         Model model,
                         @AuthenticationPrincipal UserVO myUser) throws Exception{

        // 세션에서 주문정보 가져오기
        OrderInfoVO vo = (OrderInfoVO) session.getAttribute("orderinfo");

        // vo 가 null 이면 product/list로 redirect
        if(vo == null){
            return "redirect:/product/list";
        }

        String user_id = myUser.getUser_id();
        int res_no = vo.getRes_no();

        // 로그인한 유저 id와 예약번호(res_no)로 조회하여 예약내역이 있는지 검증
        int result = service.findResNo(user_id, res_no);
        if(result == 0){
            throw new Exception();
        }

        String payment = vo.getPayment();

        switch(payment) {
            case "1" :
                payment = "신용/카드결제";
                break;
            case "2" :
                payment = "토스페이";
                break;
            case "3" :
                payment = "PAYCO";
                break;
            case "4":
                payment = "카카오페이";
                break;
            case "5":
                payment = "계좌이체";
                break;
        }

        vo.setPayment(payment);
        model.addAttribute("orderinfo", vo);
        model.addAttribute("title", environment.getProperty(group));

        return "product/result";
    }

    @GetMapping("detaildiary")
    public String detailDiary(Model model,
                              @RequestParam Map map,
                              @ModelAttribute ProductDetail_SearchVO vo) {


        log.info("vo : " +vo);
        log.info("map: " + map);

        vo.setMap(map);
        List<ArticleDiaryVO> diaries = service.findAllProductDiaries(vo);

        // 페이징
        int totalCnt = service.getTotalProductDiary(vo); // 전체 게시물 개수
        int totalPage = (int)Math.ceil(totalCnt / (double)vo.getPageSize());  // 전체 페이지의 수
        if(vo.getPage() > totalPage) vo.setPage(totalPage);

        PageHandler pageHandler = new PageHandler(totalCnt, vo);

        model.addAttribute("ph", pageHandler);
        model.addAttribute("diaries", diaries);

        return "product/data/detailDiary";
    }

    // @since 2023/03/25
    @GetMapping("detailreview")
    public String detailReview(Model model,
                               @RequestParam Map map,
                               @ModelAttribute ProductDetail_SearchVO vo) {

        vo.setMap(map);
        List<ReviewVO> reviews = service.findAllReviews(vo);

        // 해당 숙소의 전체 리뷰 답변 갯수
        service.getTotalProductReviewReply(model,vo);

        // 판매자 정보 가져오기
        UserVO business = service.findBusiness(vo.getAcc_id());

        // 페이징
        int totalCnt = service.getTotalProductReview(vo); // 전체 게시물 개수
        int totalPage = (int)Math.ceil(totalCnt / (double)vo.getPageSize());  // 전체 페이지의 수
        if(vo.getPage() > totalPage) vo.setPage(totalPage);

        PageHandler pageHandler = new PageHandler(totalCnt, vo);

        model.addAttribute("ph", pageHandler);
        model.addAttribute("reviews", reviews);
        model.addAttribute("business", business);

        return "product/data/detailReview";
    }

    // @since 2023/03/22
    @GetMapping("detailqna")
    public String detailQna(Model model,
                              @RequestParam Map map,
                              @ModelAttribute ProductDetail_SearchVO vo,
                              @AuthenticationPrincipal UserVO myUser) throws Exception {

        String uid = "";
        if(myUser != null) {
            uid = myUser.getUser_id();
        }

        vo.setMap(map);
        List<ProductQnaVO> qnas = service.findAllProductQna(vo);

        // 페이징
        int totalCnt = service.getTotalProductQna(vo); // 전체 게시물 개수
        int totalPage = (int)Math.ceil(totalCnt / (double)vo.getPageSize());  // 전체 페이지의 수
        if(vo.getPage() > totalPage) vo.setPage(totalPage);

        PageHandler pageHandler = new PageHandler(totalCnt, vo);


        model.addAttribute("user_id", uid);
        model.addAttribute("totalCnt", totalCnt);
        model.addAttribute("ph", pageHandler);
        model.addAttribute("qnas", qnas);

        return "product/data/detailQna";
    }

    // @since 2023/03/22
    @ResponseBody
    @PostMapping("rsaveQna")
    public Map<String, Integer> rsaveQna(@RequestBody ProductQnaVO qna, HttpServletRequest req) {

        log.debug("Get rsaveQna start");

        int result = 0;

        qna.setQna_regip(req.getRemoteAddr());

        result = service.rsaveQna(qna);

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("result", result);

        return resultMap;
    }

    // @since 2023/03/24
    @ResponseBody
    @PostMapping("pick")
    public Map<String, Integer> pick(@RequestBody Map map) {

        log.debug("Get pick start");

        int result = 0;

        Boolean sts = (Boolean) map.get("status");

        if(!sts){ // 찜하기가 되어있지 않은 경우
            result = service.saveProductPick(map);
        } else if(sts){ // 찜하기가 된 경우
            result = service.deleteProductPick(map);
        }

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("result", result);
        return resultMap;
    }

    // @since 2023/03/27
    @ResponseBody
    @PostMapping("coupon")
    public List<CouponVO> coupon(@RequestBody Map map){

        log.debug("Get product coupon start");

        List<CouponVO> coupons = service.findAllCoupons(map);
        log.info("coupons : " + coupons);

        return coupons;
    }


    // @since 2023/03/27
    @ResponseBody
    @PostMapping("getCoupon")
    public Map<String, Integer> getCoupon(@RequestBody Map map){

        log.debug("product getCoupon start");

        int result = 0;

        result = service.getCoupon(map);

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("result", result);

        return resultMap;
    }

    // @since 2023/03/31
    @ResponseBody
    @PostMapping("paymentComplete")
    public ResponseEntity<String> paymentComplete(HttpSession session,
                                                  @RequestBody OrderInfoVO vo,
                                                  @AuthenticationPrincipal UserVO myUser) throws Exception {

        log.info("here...");
        log.info("vo : " + vo);

        String user_id = myUser.getUser_id();
        vo.setUser_id(user_id);
        String imp_uid = vo.getImp_uid(); // 결제 고유 uid
        
        /* 결제 테스트 중지할때 사용
        imp_uid = "imp_00000000";
        vo.setImp_uid(imp_uid);
        int amount = 20000;*/

        /* 토큰 발행 */
        String token = paymentservice.getToken();

        /* 결제 정보 */
        int amount = paymentservice.paymentInfo(token, imp_uid);
        vo.setAmount(amount);

        // 데이터 검증
        vo.setUser_point(myUser.getPoint()); // 실제 유저가 가지고 있는 포인트
        vo = service.dataValidation(vo, session);

        int status = vo.getStatus();

        if(status == 0){ // 데이터 검증 실패시 결제 취소
            /* 결제 취소 */
            int code = paymentservice.paymentCancel(token, imp_uid);
            return new ResponseEntity<String>("결제 에러가 발생했습니다.", HttpStatus.BAD_GATEWAY);

        }else { // 결제 성공
            log.info( "최종 결제 정보 vo : " + vo);
            
            // 세션에 주문 정보 저장
            session.setAttribute("orderinfo", vo);

            /* 예약 진행 */
            service.reservation(vo);

            return new ResponseEntity<>("주문이 완료되었습니다", HttpStatus.OK);
        }
    }

    // @since 2023/04/04
    @ResponseBody
    @PostMapping("terms")
    public TermVO terms(@RequestBody Map map){

        log.debug("Get product terms start");
        
        // 가져올 약관 번호
        int termsType_no = Integer.parseInt((String) map.get("termsType_no"));

        TermVO term = service.findTerm(termsType_no);

        log.info("약관 번호 :" + termsType_no);

        log.info("약관 :" + term);

        return term;
    }

    @GetMapping("result2")
    public String result2(){
        return "/product/result2";
    }

}