package kr.co.Lemo.controller;

import kr.co.Lemo.domain.CouponVO;
import kr.co.Lemo.domain.CsVO;
import kr.co.Lemo.domain.ReviewVO;
import kr.co.Lemo.domain.search.Admin_SearchVO;
import kr.co.Lemo.domain.search.Cs_SearchVO;
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


    @GetMapping("index")
    public String index_admin() {
        return "admin/index";
    }



    @GetMapping("stats")
    public String stats() {
        return "admin/stats";
    }

    // @since 2023/03/09
    @GetMapping("user")
    public String user(Model model,
                       @RequestParam Map map,
                       @ModelAttribute Admin_SearchVO sc) {
        log.warn("GET user start...");

        sc.setMap(map);

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
    public String manageCoupon(Model model,
                               @RequestParam Map map,
                               @ModelAttribute Admin_SearchVO sc) {
        log.warn("GET manage Coupon...");

        sc.setMap(map);
        service.selectCoupon(model, sc);

        return "admin/coupon/manageCoupon";
    }

    // @since 2023/03/11
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
    @DeleteMapping("coupon/removeCoupon")
    public Map<String, Integer> removeCoupon(@RequestBody Map map) throws Exception {

        String cp_id = (String) map.get("cp_id");

        log.warn("GET remove Coupon");

        int result = service.removeCoupon(cp_id);

        log.warn("after service : " + result);

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("result", result);

        return resultMap;
    }

    @GetMapping("review/list")
    public String review_list(Model model,
                              @RequestParam Map map,
                              @ModelAttribute SearchCondition sc){
        sc.setMap(map);
        service.findAllReview(model, sc);
        return "admin/review/list";
    }

    @GetMapping("review/view")
    public String review_view(Model model, @RequestParam("revi_id") Integer revi_id) throws Exception{

        ReviewVO review = service.findReview(revi_id);

        log.warn("selected review: " + review);

        model.addAttribute("review", review);
        model.addAttribute("revi_id", revi_id);

        return "admin/review/view";
    }

    // @since 2023/03/15 관리자 쿠폰 답변 작성
    @ResponseBody
    @PostMapping("usaveReply")
    public Map<String, Integer> usaveReply(@RequestBody Map map) throws Exception {
        log.warn(map.toString());

        String revi_id = (String)map.get("revi_id");
        String revi_reply = (String)map.get("revi_reply");

        int result = service.usaveReply(revi_reply, revi_id);

        Map resultMap = new HashMap<>();
        resultMap.put("result", result);

        return resultMap;

    }

    // @since 2023/03/15 관리자 리뷰 삭제
    @ResponseBody
    @PostMapping("removeReview")
    public Map<String, Integer> removeReview(@RequestBody Map map) throws Exception {
        String revi_id = (String)map.get("revi_id");

        int result = service.removeReview(revi_id);

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("result", result);

        return resultMap;
    }







    // 황원진
    @GetMapping("cs/{cs_cate}/list")
    public String findAllCs_list(@PathVariable("cs_cate") String cs_cate,
                                 String cs_type,
                                 Model model,
                                 @RequestParam Map map,
                                 @ModelAttribute Cs_SearchVO sc){

       sc.setMap(map);
        if("event".equals(cs_cate)){
            csService.findAllCsArticles(sc, model);

            return "admin/cs/event/list";
        }else if("notice".equals(cs_cate)) {

            csService.findAllCsArticles(sc, model);
            return "admin/cs/notice/list";
        }else if("qna".equals(cs_cate)){
            csService.findAllAdminQnaArticles(sc, model);
            return "admin/cs/qna/list";
        }else if("faq".equals(cs_cate)){
            if(cs_type != null){
                    csService.findAllFaqArticles(sc, model);
                    return "admin/cs/faq/list";
            }else {
                csService.findAllCsArticles(sc, model);
            }
        }
        return "admin/cs/faq/list";
    }

    @GetMapping("cs/event/modify")
    public String event_modify(){
        return "admin/cs/event/modify";
    }

    @GetMapping("cs/event/view")
    public String event_view(){
        return "admin/cs/event/view";
    }



    //@since 2023/03/16
    @ResponseBody
    @DeleteMapping("cs/{cs_cate}/articleRemove")
    public Map<String, Integer> removeAdminArticle(@PathVariable("cs_cate") String cs_cate, @RequestBody Map map) throws Exception {

        int cs_no = Integer.parseInt(String.valueOf(map.get("cs_no")));

        int result = csService.removeAdminArticle(cs_no);

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("result", result);

        return resultMap;
    }

    @GetMapping("cs/{cs_cate}/modify")
    public String usaveCsArticle(@PathVariable("cs_cate") String cs_cate, int cs_no, Model Model){
        if("notice".equals(cs_cate)){
            log.info("noticeModify");
            CsVO noticeArticle = csService.findAdminCsArticle(cs_cate, cs_no);
            Model.addAttribute("mNotice", noticeArticle);
            Model.addAttribute("cs_no", cs_no);

            return "admin/cs/notice/modify";

        }else if("faq".equals(cs_cate)){
            log.info("faqModify");
           CsVO faqArticle = csService.findAdminCsArticle(cs_cate, cs_no);
           log.info("faqContent : " +faqArticle.getCs_content());
           log.info("faqTitle : " +faqArticle.getCs_title());

            Model.addAttribute("mFaq", faqArticle);
            Model.addAttribute("cs_no", cs_no);
        }
        return "admin/cs/faq/modify";
    }

    @PostMapping("cs/{cs_cate}/modify")
    public String usaveAdminNotice(@PathVariable("cs_cate") String cs_cate, CsVO vo){
        if ("notice".equals(cs_cate)) {
            log.info("modifyNoticeStart");
            csService.usaveAdminNotice(vo);
            return "redirect:/admin/cs/notice/list";
        }else if("faq".equals(cs_cate)){
            log.info("modifyFaqStart");
            csService.usaveFaqArticle(vo);
        }
        return "redirect:/admin/cs/faq/list";
    }

    // @since 2023/03/16
    @GetMapping("cs/{cs_cate}/write")
    public String notice_write(@PathVariable("cs_cate") String cs_cate){
        if("faq".equals(cs_cate)){
            return "admin/cs/faq/write";
        }else if("notice".equals(cs_cate)){
            return "admin/cs/notice/write";
        }else if("event".equals(cs_cate)){

        }
        return "admin/cs/event/write";
    }

    // @since 2023/03/10
    @PostMapping("cs/{cs_cate}/write")
    public String rsaveNoticeArticle(@PathVariable("cs_cate") String cs_cate, CsVO vo, HttpServletRequest req) {
        if("notice".equals(cs_cate)){
            vo.setUser_id("1002rsvn@plusn.co.kr");
            vo.setCs_regip(req.getRemoteAddr());

            csService.rsaveNoticeArticle(vo);
            return "redirect:/admin/cs/notice/list";

        }else if("faq".equals(cs_cate)){
            vo.setUser_id("1043pastel_tn@naver.com");
            vo.setCs_regip(req.getRemoteAddr());

            csService.rsaveFaqArticle(vo);
            return "redirect:/admin/cs/faq/list";
        }else if("event".equals(cs_cate)){
            vo.setUser_id("1043pastel_tn@naver.com");
            vo.setCs_regip(req.getRemoteAddr());

            csService.rsaveEventArticle(vo);
        }
        return "redirect:/admin/cs/event/list";
    }


    // @since 2023/03/12
    @GetMapping("cs/{cs_cate}/view")
    public String findAdminCsArticle(@PathVariable("cs_cate") String cs_cate, int cs_no, Model model){
        log.info("cs_view Start");

        if("qna".equals(cs_cate)){
            CsVO qnaArticle = csService.findAdminCsArticle(cs_cate, cs_no);
            model.addAttribute("qnaArticle", qnaArticle);
            model.addAttribute("cs_no", cs_no);
            return "admin/cs/qna/view";

        }else if("notice".equals(cs_cate)) {
            log.info("here2 notice");
            CsVO noticeArticle = csService.findAdminCsArticle(cs_cate, cs_no);
            log.info("noticeArticle" + noticeArticle.getCs_title());
            log.info("noticeArticle" + noticeArticle.getCs_content());

            model.addAttribute("notice", noticeArticle);
            model.addAttribute("cs_no", cs_no);
        }
        return "admin/cs/notice/view";
    }

    // @since 2023/03/14
    @PostMapping("cs/{cs_cate}/view")
    public String usaveUpdateQnaArticle(String cs_reply, int cs_no){
        log.info("cs_reply : " + cs_reply);
        log.info("cs_no : " + cs_no);

        csService.usaveQnaArticle(cs_reply, cs_no);

        return "redirect:/admin/cs/qna/list";
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
