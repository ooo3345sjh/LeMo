package kr.co.Lemo.controller;

import kr.co.Lemo.domain.CouponVO;
import kr.co.Lemo.domain.ReviewVO;
import kr.co.Lemo.domain.search.Admin_SearchVO;
import kr.co.Lemo.service.BusinessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @since 2023/03/13
 * @author 이원정
 * @apiNote 판매자 controller
 */

@Slf4j
@Controller
@RequestMapping("business/")
public class BusinessController {

    @Autowired
    private BusinessService service;

    @GetMapping("index")
    public String index_business() {
        return "business/index";
    }

    // @since 2023/03/13
    @GetMapping("coupon/insertCoupon")
    public String insertCoupon() {
        return "business/coupon/insertCoupon";
    }

    // @since 2023/03/13
    @GetMapping("coupon/manageCoupon")
    public String manageCoupon(Model model,
                               @RequestParam Map map,
                               @ModelAttribute Admin_SearchVO sc) {
        log.warn("GET manage Coupon in business");

        service.selectCoupon(model, map, sc);

        return "business/coupon/manageCoupon";
    }

    // @since 2023/03/13
    @PostMapping("coupon/insertCoupon")
    public String rsaveCupon(CouponVO vo, RedirectAttributes redirectAttributes) throws Exception {

        log.warn("쿠폰명 :" + vo.getCp_subject());
        log.warn("쿠폰적용그룹 :" + vo.getCp_group());
        log.warn("쿠폰타입 :" + vo.getCp_type());
        log.warn("할인율 :" + vo.getCp_rate());
        log.warn("할인타입 :" + vo.getCp_disType());
        log.warn("발급수량 :" + vo.getCp_limitedIssuance());
        log.warn("최소주문금액: "+ vo.getCp_minimum());
        log.warn("최대주문금액: "+ vo.getCp_maximum());
        log.warn("배포시작일: "+vo.getCp_start());
        log.warn("배포시작일: "+vo.getCp_end());
        log.warn("이용가능일수:" + vo.getCp_daysAvailable());

        service.rsaveCupon(vo);
        redirectAttributes.addFlashAttribute("successMessage", "쿠폰이 등록되었습니다.");
        return "redirect:/business/coupon/insertCoupon";
    }

    @ResponseBody
    @PostMapping("coupon/removeCoupon")
    public Map<String, Integer> removeCoupon(@RequestBody Map map) throws Exception {

        String cp_id = (String) map.get("cp_id");

        log.warn("GET remove Coupon");

        int result = service.removeCoupon(cp_id);

        log.warn("after service : " + result);

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("result", result);

        return resultMap;
    }

    @GetMapping("coupon/findAccOwned")
    public ResponseEntity<List<String>> findAccOwned(String user_id) {

        log.warn("GET findAccOwned in business");

        // stream().map().collect(): 이름들만 모아서 새로운 String 리스트를 만들어 낸다
        List<String> accs = service.findAccOwned(user_id).stream().map(CouponVO::getAcc_name).collect(Collectors.toList());
        return ResponseEntity.ok(accs);
    }

    @GetMapping("review/list")
    public String review_list(Model model,
                              @RequestParam Map map,
                              @ModelAttribute Admin_SearchVO sc){
        sc.setMap(map);

        service.findAllReview(model, sc);
        return "business/review/list";
    }


    @GetMapping("review/findAccOwnedForReview")
    public ResponseEntity<List<ReviewVO>> findAccOwnedForReview(String user_id) {

        log.warn("GET findAccOwned in business");

        // stream().map().collect(): 이름들만 모아서 새로운 String 리스트를 만들어 낸다
        //List<String> accs = service.findAccOwnedForReview(user_id).stream().map(ReviewVO::getAcc_name).collect(Collectors.toList());
        List<ReviewVO> accs = service.findAccOwnedForReview(user_id);

        return ResponseEntity.ok(accs);
    }



}
