package kr.co.Lemo.controller;

import kr.co.Lemo.domain.CouponVO;
import kr.co.Lemo.domain.CsVO;
import kr.co.Lemo.repository.AdminRepo;
import kr.co.Lemo.service.AdminService;
import kr.co.Lemo.service.CsService;
import kr.co.Lemo.utils.SearchCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @since 2023/03/07
 * @author 이원정
 * @apiNote 관리자 controller
 */

@Slf4j
@Controller
@RequestMapping("admin/")
public class AdminController {

    @Autowired
    private AdminService service;

    @Autowired
    private AdminRepo repo;

    @Autowired
    private CsService csService;


    @GetMapping("index_admin")
    public String index_admin() {
        return "admin/index_admin";
    }

    @GetMapping("index_business")
    public String index_business() {
        return "admin/index_business";
    }

    @GetMapping("stats")
    public String stats() {
        return "admin/stats";
    }

    // @since 2023/03/09
    @GetMapping("user")
    public String user(Model model, SearchCondition sc) {
        log.warn("GET user start...");
        sc.setGroup("admin");
        service.selectUser(model, sc);

        return "admin/user";
    }

    // @since 2023/03/09 관리자 회원 메모 작성
    @ResponseBody
    @PostMapping("updateMemo")
    public Map<String, Integer> updateMemo(@RequestBody Map map) throws Exception {
        log.warn(map.toString());

        String user_id = (String) map.get("user_id");
        String memo = (String) map.get("memo");

        int result = service.updateMemo(memo, user_id);

        Map resultMap = new HashMap<>();
        resultMap.put("result", result);

        return resultMap;
    }

    // @since 2023/03/10 관리자 회원 차단
    @ResponseBody
    @PostMapping("updateIsLocked")
    public Map<String, Integer> updateIsLocked(@RequestBody Map map) throws Exception {
        String user_id = (String) map.get("user_id");
        int result = service.updateIsLocked(user_id);

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("result", result);

        return resultMap;
    }

    // @since 2023/03/10
    @ResponseBody
    @PostMapping("updateIsEnabled")
    public Map<String, Integer> updateIsEnabled(@RequestBody Map map) throws Exception {
        String user_id = (String) map.get("user_id");
        int result = service.updateIsEnabled(user_id);

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("result", result);

        return resultMap;
    }


    // @since 2023/03/11
    @GetMapping("coupon/insertCoupon")
    public String insertCoupon() {
        return "admin/coupon/insertCoupon";
    }

    // @since 2023/03/12
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
        return "redirect:/admin/coupon/insertCoupon";
    }

    @GetMapping("coupon/manageCoupon")
    public String manageCoupon(Model model, SearchCondition sc) {
        log.warn("GET manage Coupon...");
        sc.setGroup("adminCoupon");

        service.selectCoupon(model, sc);

        return "admin/coupon/manageCoupon";
    }

    /*
    @GetMapping("coupon/findAccOwned")
    public String findAccOwned(String user_id, Model model){
        List<CouponVO> accs = service.findAccOwned(user_id);
        model.addAttribute("accs", accs);

        return "redirect:/admin/coupon/manageCoupon";
    }
     */

    @GetMapping("coupon/findAccOwned")
    public ResponseEntity<List<String>> findAccOwned(String user_id) {

        log.warn("GET findAccOwned...");

        // stream().map().collect(): 이름들만 모아서 새로운 String 리스트를 만들어 낸다
        List<String> accs = service.findAccOwned(user_id).stream().map(CouponVO::getAcc_name).collect(Collectors.toList());
        return ResponseEntity.ok(accs);
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


    @GetMapping("cs/{cs_cate}/list")
    public String event_list(@PathVariable("cs_cate") String cs_cate, Model model, SearchCondition sc){

        if("event".equals(cs_cate)){
            csService.findAllCsArticles(sc, model);

            return "admin/cs/event/list";
        }else if("notice".equals(cs_cate)) {

            csService.findAllCsArticles(sc, model);
        }
        return "admin/cs/notice/list";
    }

    @GetMapping("cs/event/modify")
    public String event_modify(){
        return "admin/cs/event/modify";
    }

    @GetMapping("cs/event/view")
    public String event_view(){
        return "admin/cs/event/view";
    }

    @GetMapping("cs/event/write")
    public String event_write(CsVO vo){ return "admin/cs/event/write"; }


    @GetMapping("cs/faq/list")
    public String faq_list(){
        return "admin/cs/faq/list";
    }

    @GetMapping("cs/faq/modify")
    public String faq_modify(){
        return "admin/cs/faq/modify";
    }

    @GetMapping("cs/faq/write")
    public String faq_write(){
        return "admin/cs/faq/write";
    }




    @GetMapping("cs/notice/modify")
    public String notice_modify(){
        return "admin/cs/notice/modify";
    }

    @GetMapping("cs/notice/view")
    public String notice_view(){
        return "admin/cs/notice/view";
    }

    @GetMapping("cs/notice/write")
    public String notice_write(){
        return "admin/cs/notice/write";
    }

    @PostMapping("cs/{cs_cate}/write")
    public String insertArticleNotice(@PathVariable("cs_cate") String cs_cate, CsVO vo, HttpServletRequest req) {

        vo.setUser_id("1002rsvn@plusn.co.kr");
        vo.setCs_regip(req.getRemoteAddr());

        csService.insertArticleNotice(vo);
        return "redirect:/admin/cs/notice/list";
    }


    @GetMapping("cs/qna/list")
    public String qna_list(){
        return "admin/cs/qna/list";
    }

    @GetMapping("cs/qna/view")
    public String qna_view(){
        return "admin/cs/qna/view";
    }

    @GetMapping("cs/terms/test")
    public String terms_list(){
        return "admin/cs/terms/test";
    }

    @GetMapping("cs/terms/write")
    public String terms_write(){
        return "admin/cs/terms/write";
    }


    @GetMapping("info/list")
    public String info_list(){
        return "admin/info/list";
    }

    @GetMapping("info/modify")
    public String info_modify(){
        return "admin/info/modify";
    }

    @GetMapping("info/view")
    public String info_view(){
        return "admin/info/view";
    }

    @GetMapping("info/write")
    public String info_write(){
        return "admin/info/write";
    }


    @GetMapping("reservation/list")
    public String reservation_list(){
        return "admin/reservation/list";
    }

    @GetMapping("reservation/timeline")
    public String reservation_timeline(){
        return "admin/reservation/timeline";
    }


    @GetMapping("review/list")
    public String review_list(){
        return "admin/review/list";
    }

    @GetMapping("review/view")
    public String review_view(){
        return "admin/review/view";
    }

    @GetMapping("roomInfo/list")
    public String roomInfo_list(){
        return "admin/roomInfo/list";
    }

    @GetMapping("roomInfo/modify")
    public String roomInfo_modify(){
        return "admin/roomInfo/modify";
    }

    @GetMapping("roomInfo/view")
    public String roomInfo_view(){
        return "admin/roomInfo/view";
    }

    @GetMapping("roomInfo/write")
    public String roomInfo_write(){
        return "admin/roomInfo/write";
    }




}
