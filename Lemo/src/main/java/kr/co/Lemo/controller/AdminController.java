package kr.co.Lemo.controller;

import kr.co.Lemo.domain.*;
import kr.co.Lemo.domain.search.Admin_SearchVO;
import kr.co.Lemo.domain.search.Cs_SearchVO;
import kr.co.Lemo.service.AdminService;
import kr.co.Lemo.service.CsService;
import kr.co.Lemo.utils.PageHandler;
import kr.co.Lemo.utils.SearchCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
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
@PropertySource(value = "classpath:title.properties", encoding = "UTF-8")
@RequestMapping("admin/")
public class AdminController {

    @Autowired
   private Environment environment;
   private String group = "title.admin";

    @Autowired
    private AdminService service;

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

    // @since 2023/03/09 관리자 회원 - 회원 목록 (Form 전송)
    @GetMapping("user")
    public String user(Model model,
                       @RequestParam Map map,
                       @ModelAttribute Admin_SearchVO sc) {
        log.warn("GET user start...");

        sc.setMap(map);

        service.selectUser(model, sc);

        return "admin/user";
    }

    // @since 2023/03/24 관리자 회원 - 회원 목록 (Ajax 전송)

    @GetMapping("users")
    @ResponseBody
    public Map users(Model model,
                     @RequestParam Map map,
                     @RequestParam(required = false, value = "sorted") String sorted,
                     @ModelAttribute Admin_SearchVO sc) {

        log.warn("sorted : " + sorted);
        log.warn("sc : " + sc);

        sc.setMap(map);

        if(sorted.equals("1")){
            sc.setSort("isEnabledOn");
        }else if(sorted.equals("2")){
            sc.setSort("isEnabledOff");
        }else if(sorted.equals("4")){
            sc.setSort("levelOn");
        }else if(sorted.equals("5")){
            sc.setSort("levelOff");
        }else if(sorted.equals("7")){
            sc.setSort("typeOn");
        }else if(sorted.equals("8")){
            sc.setSort("typeOff");
        }else if(sorted.equals("10")){
            sc.setSort("rdateOn");
        }else if(sorted.equals("11")){
            sc.setSort("rdateOff");
        }
        //sc.setSort(sort);

        List<UserVO> users = service.selectUser(model, sc);
        log.warn("GET users : " + users);

        PageHandler ph = (PageHandler) model.getAttribute("ph");
        log.warn("ph : " + ph.getTotalCnt());

        map.put("users", users);
        map.put("totalCnt", ph.getTotalCnt());
        map.put("offset", ph.getSc().getOffset());
        map.put("beginPage", ph.getBeginPage());
        map.put("endPage", ph.getEndPage());
        map.put("showNext", ph.isShowNext());
        map.put("showPrev", ph.isShowPrev());
        map.put("page" , sc.getPage());

        return map;
    }

    // @since 2023/03/09 관리자 회원 - 메모 작성
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

    // @since 2023/03/10 관리자 회원 - 회원 차단
    @ResponseBody
    @PostMapping("updateIsLocked")
    public Map<String, Integer> updateIsLocked(@RequestBody Map map) throws Exception {
        String user_id = (String) map.get("user_id");
        int result = service.updateIsLocked(user_id);

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("result", result);

        return resultMap;
    }

    @ResponseBody
    @PostMapping("usaveClear")
    public Map<String, Integer> usaveClear(@RequestBody Map map) throws Exception {
        String user_id = (String) map.get("user_id");
        int result = service.usaveClear(user_id);

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("result", result);

        return resultMap;
    }

    // @since 2023/03/30 관리자 숙박 차단
    @ResponseBody
    @PostMapping("usaveDropAcc")
    public Map<String, Integer> usaveDropAcc(@RequestBody Map map) throws Exception {
        String acc_id = (String) map.get("acc_id");
        int result = service.usaveDropAcc(acc_id);

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("result", result);

        return resultMap;
    }

    // @since 2023/03/30 관리자 숙박 차단 해제
    @ResponseBody
    @PostMapping("usaveClearAcc")
    public Map<String, Integer> usaveClearAcc(@RequestBody Map map) throws Exception {
         String acc_id = (String) map.get("acc_id");
        int result = service.usaveClearAcc(acc_id);

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
    @PostMapping("coupon/rsaveCupon")
    public String rsaveCupon(CouponVO vo, RedirectAttributes redirectAttributes) throws Exception {

        log.warn("rsaveCupon : " + vo);

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

    // 관리자 - 리뷰 목록
    @GetMapping("review/list")
    public String review_list(Model model,
                              @RequestParam Map map,
                              @ModelAttribute SearchCondition sc){
        sc.setMap(map);
        service.findAllReview(model, sc);

        //PageHandler ph = (PageHandler) model.getAttribute("ph");
        //log.debug("ph : " + ph.getTotalCnt());



        return "admin/review/list";
    }

    // 관리자 - 리뷰 보기
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

    // 관리자 - 객실 목록
    @GetMapping("roomInfo/list")
    public String roomInfo_list(Model model,
                                @RequestParam Map map,
                                @ModelAttribute Admin_SearchVO sc){

        sc.setMap(map);
        service.findAllRoom(model, sc);

        return "admin/roomInfo/list";
    }

    // 관리자 - 객실 보기
    @GetMapping("roomInfo/view")
    public String roomInfo_view(Model model,
                                @RequestParam("room_id") Integer room_id) throws Exception{

        ProductRoomVO room = service.findRoom(room_id);

        log.warn("selected room: " + room);

        model.addAttribute("room", room);
        model.addAttribute("room_id", room_id);

       return "admin/roomInfo/view";
    }

    @GetMapping("roomInfo/modify")
    public String roomInfo_modify(){
       return "admin/roomInfo/modify";
    }



    @GetMapping("roomInfo/write")
    public String roomInfo_write(){
       return "admin/roomInfo/write";
    }

    @GetMapping("info/list")
    public String info_list(Model model,
                            @RequestParam Map map,
                            @ModelAttribute Admin_SearchVO sc){

        sc.setMap(map);
        service.findAllAccs(model, sc);

        return "admin/info/list";
    }

    @GetMapping("info/mapTest")
    public String mapTest(){
        return "admin/info/mapTest";
    }

    @GetMapping("info/province")
    public ResponseEntity<List<ProvinceVO>> findProvince(){
        List<ProvinceVO> provinces = service.findProvince();
        return ResponseEntity.ok(provinces);
    }

    @GetMapping("info/modify")
    public String info_modify(){
        return "admin/info/modify";
    }

    // 관리자 - 숙소 보기
    @GetMapping("info/view")
    public String info_view(Model model, @RequestParam("acc_id") Integer acc_id) throws Exception {

        ProductAccommodationVO acc = service.findAcc(acc_id);

        List<ServicereginfoVO> servicereginfos = service.findServiceInAcc(acc_id);
        log.warn("selected acc service: " + servicereginfos);

        //log.warn("selected acc: " + acc);

        model.addAttribute("acc", acc);
        model.addAttribute("acc_id", acc_id);
        model.addAttribute("serviceInfo", servicereginfos);

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







    // @since 2023/03/20 황원진
    @GetMapping("cs/{cs_cate}/list")
    public String findAllCs_list(@PathVariable("cs_cate") String cs_cate,
                                 String cs_type,
                                 Model model,
                                 @RequestParam Map map,
                                 @ModelAttribute Cs_SearchVO sc){

        log.info("admin/cs/.../list...");

        sc.setMap(map);
        if("event".equals(cs_cate)){
            log.info("admin/cs/event/list");

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








    /**
     * @since 2023/03/16
     * @author 황원진
     */
    @ResponseBody
    @DeleteMapping("cs/{cs_cate}/articleRemove")
    public Map<String, Integer> removeAdminArticle(@PathVariable("cs_cate") String cs_cate, @RequestBody Map map) throws Exception {

        int cs_no = Integer.parseInt(String.valueOf(map.get("cs_no")));

        int result = csService.removeAdminArticle(cs_no);

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("result", result);

        return resultMap;
    }

    /**
     * @since 2023/03/27
     * @author 황원진
     */
    @ResponseBody
    @DeleteMapping("cs/qna/listRemove")
    public Map<String, Integer> removeQnaList(@RequestParam(value = "checkList[]") List<String> checkList){
        log.info("listRemoveStart");

        String user_id = "b1848@naver.com";
       int result = csService.removeQnaList(checkList, user_id);

       Map<String, Integer> map = new HashMap<>();
       map.put("result", result);

       return map;
    }


    /**
     * @since 2023/03/14
     * @author 황원진
     */
    @GetMapping("cs/{cs_cate}/modify")
    public String usaveCsArticle(@PathVariable("cs_cate") String cs_cate, int cs_no, Model model){
        if("notice".equals(cs_cate)){
            log.info("noticeModify");
            CsVO noticeArticle = csService.findAdminCsArticle(cs_cate, cs_no);
            model.addAttribute("mNotice", noticeArticle);
            model.addAttribute("cs_no", cs_no);

            return "admin/cs/notice/modify";

        }else if("faq".equals(cs_cate)){
            log.info("faqModify");
            CsVO faqArticle = csService.findAdminCsArticle(cs_cate, cs_no);
            log.info("faqContent : " +faqArticle.getCs_content());
            log.info("faqTitle : " +faqArticle.getCs_title());

            model.addAttribute("mFaq", faqArticle);
            model.addAttribute("cs_no", cs_no);

            return "admin/cs/faq/modify";
        }else if("event".equals(cs_cate)){
            log.info("eventModify");
            CsVO eventArticle = csService.findAdminCsArticle(cs_cate, cs_no);
            log.info("eventContent : " +eventArticle.getCs_content());
            log.info("eventTitle : " +eventArticle.getCs_title());

            model.addAttribute("mEvent", eventArticle);
            model.addAttribute("cs_no", cs_no);
        }
        return "admin/cs/event/modify";
    }

    /**
     * @since 2023/03/15
     * @author 황원진
     */

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


    /**
     * @since 2023/03/16
     * @author 황원진
     */
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


    /**
     * @since 2023/03/10
     * @author 황원진
     */
    @PostMapping("cs/{cs_cate}/write")
    public String rsaveNoticeArticle(@PathVariable("cs_cate") String cs_cate,
                                     CsVO vo,
                                     @RequestPart(value = "cs_eventBanner", required = false) MultipartFile cs_eventBanner,
                                     @RequestParam Map<String, Object> parameter,
                                     MultipartHttpServletRequest request,
                                     HttpServletRequest req) {

        if("notice".equals(cs_cate)){
            vo.setUser_id("b1848@naver.com");
            vo.setCs_regip(req.getRemoteAddr());

            csService.rsaveNoticeArticle(vo);
            return "redirect:/admin/cs/notice/list";

        }else if("faq".equals(cs_cate)){
            vo.setUser_id("b1848@naver.com");
            vo.setCs_regip(req.getRemoteAddr());

            csService.rsaveFaqArticle(vo);
            return "redirect:/admin/cs/faq/list";
        }else if("event".equals(cs_cate)){

            parameter.put("user_id", "b1848@naver.com");
            parameter.put("cs_regip", req.getRemoteAddr());
            parameter.put("cs_cate", "event");


            log.info("param : " + parameter);
            log.info("cs_eventBanner : " + cs_eventBanner);

            Map<String, MultipartFile> fileMap = request.getFileMap();
            for (MultipartFile multipartFile : fileMap.values()) {
                log.debug(""+multipartFile.getOriginalFilename());
            }

            int result = csService.rsaveEventArticle(request, parameter, cs_eventBanner);
        }
        return "redirect:/admin/cs/event/list";
    }


    /**
     * @since 2023/03/12
     * @author 황원진
     */
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
            return "admin/cs/notice/view";

        }else if("event".equals(cs_cate)){
            log.info("admin/event");
            CsVO eventArticle = csService.findAdminCsArticle(cs_cate, cs_no);
            log.info("eventArticle viewImg : " + eventArticle.getCs_eventViewImg());
            model.addAttribute("event", eventArticle);
            model.addAttribute("cs_no", cs_no);
            model.addAttribute("cs_cate", cs_cate);

            return "admin/cs/event/view";
        }
        return "admin/cs/event/view";
    }

    /**
     * @since 2023/03/14
     * @author 황원진
     */
    @PostMapping("cs/{cs_cate}/view")
    public String usaveQnaArticle(String cs_reply, int cs_no){
        log.info("cs_reply : " + cs_reply);
        log.info("cs_no : " + cs_no);

        csService.usaveQnaArticle(cs_reply, cs_no);

        return "redirect:/admin/cs/qna/list";
    }


    /**
     * @since 2023/03/17
     * @author 황원진
     * @apiNote 관리자 이벤트 활성화
     */
    @ResponseBody
    @PostMapping("cs/event/eventOn")
    public Map<String, Integer> usaveOnEvent(@RequestBody Map map){

        int cs_no = Integer.parseInt(String.valueOf(map.get("cs_no")));

        int result = csService.usaveOnEvent(cs_no);;

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("result", result);

        return resultMap;
    }

    /**
     * @since 2023/03/17
     * @author 황원진
     * @apiNote 관리자 이벤트 비활성화
     */
    @ResponseBody
    @PostMapping("cs/event/eventEnd")
    public Map<String, Integer> usaveEndEvent(@RequestBody Map map){

        int cs_no = Integer.parseInt(String.valueOf(map.get("cs_no")));

        int result = csService.usaveEndEvent(cs_no);

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("result", result);

        return resultMap;
    }

    /**
     * @since 2023/03/29
     * @author 황원진
     * @apiNote 관리자 이벤트 메인베너
     */
    @ResponseBody
    @PostMapping("cs/event/mainBanner")
    public Map<String, Integer> updateMainBannerState(@RequestBody Map map){
        log.debug("event update mainBannerState start...");

        int cs_no = Integer.parseInt(String.valueOf(map.get("cs_no")));
        Integer cs_eventMainBannerState = (Integer) map.get("cs_eventMainBannerState");

        int result = csService.usaveMainBanner(cs_eventMainBannerState, cs_no);

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("result", result);

        return resultMap;
    }

    /**
     * @since 2023/03/30
     * @author 황원진
     * @apiNote 관리자 이벤트 베너
     */
    @ResponseBody
    @PostMapping("cs/event/banner")
    public Map<String, Integer> updateBannerState(@RequestBody Map map){
        log.debug("event update eventBannerState start...");

        int cs_no = Integer.parseInt(String.valueOf(map.get("cs_no")));
        Integer cs_eventBannerState = (Integer) map.get("cs_eventBannerState");

        int result = csService.usaveEventBanner(cs_eventBannerState, cs_no);

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("result", result);

        return resultMap;
    }

    @GetMapping("cs/terms/test")
    public String terms_list(){
        return "admin/cs/terms/test";
    }

    @GetMapping("cs/terms/write")
    public String terms_write(){
        return "admin/cs/terms/write";
    }










}