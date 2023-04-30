package kr.co.Lemo.controller;

import kr.co.Lemo.domain.*;
import kr.co.Lemo.domain.search.Admin_SearchVO;
import kr.co.Lemo.domain.search.Cs_SearchVO;
import kr.co.Lemo.service.AdminService;
import kr.co.Lemo.service.CsService;
import kr.co.Lemo.service.HelloAnalyticsReportingService;
import kr.co.Lemo.utils.PageHandler;
import kr.co.Lemo.utils.RemoteAddrHandler;
import kr.co.Lemo.utils.SearchCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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

    @Autowired
    private HelloAnalyticsReportingService reportingService;


    // 관리자 - 메인
    @GetMapping(value = {"", "index"})
     public String index_admin(
            Model model,
            Map map) throws Exception {

        model.addAttribute("title", environment.getProperty(group));

        log.debug("GET index...");

        // 전역변수 선언
        int sum_res_price = 0;
        int avg_res_price = 0;
        int visitors = 0;
        int totalVisitors = 0;

        // 구글 애널리틱스
        List<AnalyticsVO> reportData = reportingService.getData();

        log.warn("reportData :" + reportData);

        for (AnalyticsVO vo : reportData) {
            log.warn("activeUsers : " + vo.getActiveUsers());
            visitors = vo.getActiveUsers();
            totalVisitors += visitors;
        }

        log.warn("visitors : " + visitors);
        log.warn("totalVisitors : " + totalVisitors);

        // 일별 누적 판매량 (당일)
        List<ReservationVO> todaySales = service.findAllTodaySales(map);
        // 공지사항 황원진
        List<CsVO> notice = csService.findNoticeArticle();
        // 1:1문의 황원진
        List<CsVO> qnaArticles = csService.findQnaArticles();
        // 취소 건수
        int totalCanceled = service.countDayCancel();
        // 1:1 문의 수
        int totalQna = service.countDayQna();
        // 상품 등록 수
        int totalAcc = service.countDayAcc();
        // 회원가입 수
        int totalUser = service.countDayUser();
        // 판매량 그래프
        List<ReservationVO> stats = service.findAllDaySale();

        if(stats.size() != 0) {
            for (int i=0; i<stats.size(); i++) {
                sum_res_price += stats.get(i).getTot_res_price();
            }
            avg_res_price = sum_res_price / stats.size();
        }else if (stats.size() == 0) {
            sum_res_price = 0;
            avg_res_price = 0;
        }

        // 결제 방법 결제 현황
        List<ReservationVO> pays = service.findAllPaymentDay(map);
        Map<Integer, List<ReservationVO>> paysMap = pays.stream().collect(Collectors.groupingBy(ReservationVO::getRes_payment));
        // 베스트 숙소
        List<ProductAccommodationVO> bestAccs = service.findAllBestAcc(map);

        // 모델 전송
        model.addAttribute("todaySales", todaySales);
        model.addAttribute("totalCanceled", totalCanceled);
        model.addAttribute("totalQna", totalQna);
        model.addAttribute("totalAcc", totalAcc);
        model.addAttribute("totalUser", totalUser);
        model.addAttribute("stats", stats);
        model.addAttribute("avg_res_price", avg_res_price);
        model.addAttribute("paysMap", paysMap);
        model.addAttribute("bestAccs", bestAccs);
        model.addAttribute("notice", notice);
        model.addAttribute("qnaArticles", qnaArticles);
        model.addAttribute("totalVisitors", totalVisitors);

        return "admin/index";
    }

    // 관리자 - 통계 관리
    @GetMapping("stats")
    public String stats(Model model,
                        @RequestParam Map<String, String> map) {

        // 타이틀 설정
        model.addAttribute("title", environment.getProperty(group));

        log.debug("GET stats...");

        // 전역변수 선언
        int total = 0;
        int totalCanceled = 0;
        int totalQna = 0;
        int totalAcc = 0;
        int totalRegister = 0;
        List<ReservationVO> stats = new ArrayList<>();
        int sum_res_price = 0;
        int avg_res_price = 0;
        String periodType = map.get("periodType");
        String dateStart = map.get("dateStart");
        String dateEnd = map.get("dateEnd");
        List<ReservationVO> pays = new ArrayList<>();
        Map<Integer, List<ReservationVO>> paysMap = new HashMap<>();


        /**
         * dateStart, dateEnd 어떻게 받는지 확인
         * 1. 첫 페이지 로딩, 폼 전송X - map : {}
         * 2. 폼 전송 (periodType) - map : {periodType=day, dateStart=, dateEnd=}
         * 3. 폼 전송 (date)       - map : {periodType=, dateStart=2023-04-17, dateEnd=2023-04-20}
         */


        /**
        * 1. 첫 페이지 로딩, 폼 전송X - dateStart: null
        * 2. 폼 전송 (periodType) - dateStart:
        * 3. 폼 전송 (date)       - dateStart type: java.lang.String
        * 결론 - mapper의 조건 !=null을 만족시키기 위해서는 null의 경우, 빈 문자열의 경우 -> 확실하게 null을 입력
        */
        if (map.get("dateStart") == null || map.get("dateStart").isBlank() && map.get("dateEnd") == null || map.get("dateEnd").isBlank()) {
            log.warn("dateStart: " + map.get("dateStart"));

            map.put("dateStart",null);
            map.put("dateEnd",null);
        }else {
            log.warn("dateStart type: " + dateStart.getClass().getName());
        }



        // 기간 미 설정시 기본 -> 일주일
        if(map.get("periodType") == null){
            map.put("periodType", "week");

        // 기간 설정 시 -> 기간 설정에 따른 데이터 조회
        }else if(map.get("periodType") != null){
            map.put("periodType", periodType);
        }

        log.warn("periodType: " + periodType);

        // 예약 건수
        total = service.countWeeksSales(map);

        log.warn("total: " + total);

        // 취소 건수
        totalCanceled = service.countWeeksCancel(map);
        // 1:1 문의 수
        totalQna = service.countWeeksQna(map);
        // 상품 등록 수
        totalAcc = service.countWeeksAcc(map);
        // 회원가입 수
        totalRegister = service.countWeeksUser(map);
        // 매출 현황 그래프
        stats = service.findAllDaySales(map);

        log.warn("stats: " + stats);

        for ( int i=0; i<stats.size(); i++ ){
            log.warn("stats price: " + stats.get(i).getTot_res_price());
            sum_res_price += stats.get(i).getTot_res_price();
        }
        if(stats.size() == 0){
            avg_res_price = sum_res_price / 1;
        }else if (stats.size() != 0){
            avg_res_price = sum_res_price / stats.size();
        }

        log.warn("avg_res_price :" + avg_res_price);

        // 결제 수단 현황
        pays = service.findAllPayment(map);
        paysMap = pays.stream().collect(Collectors.groupingBy(ReservationVO::getRes_payment));

        // 단위 기간별 매출현황 (기간 검색 미적용)
        // 당일 누적 판매량
        List<ReservationVO> todaySales = service.findAllTodaySales(map);

        // 월별 매출 현황
        List<ReservationVO> statsMonth = service.findAllMonthSales(map);

        List<Double> monthPercentList = new ArrayList<>();
        int monthSum = 0;

        for (ReservationVO vo : statsMonth) {
            double totMonthPercent = vo.getTot_month_percent();
            monthPercentList.add(totMonthPercent);
        }
        for (ReservationVO vo : statsMonth) {
            monthSum += vo.getTot_res_price();
        }
        int monthAvg = monthSum / 4;     // 4개월 평균 매출

        // 연별 매출 현황
        List<ReservationVO> yearSales = service.findAllYearSales(map);

        int yearSum = 0;

        for (ReservationVO mAvg : yearSales) {

            yearSum += mAvg.getTot_res_price();
        }

        int yearAvg = yearSum/3;          // 3년 년평균 매출


        model.addAttribute("totalSales", total);
        model.addAttribute("totalCanceled", totalCanceled);
        model.addAttribute("totalQna", totalQna);
        model.addAttribute("totalAcc", totalAcc);
        model.addAttribute("totalRegister", totalRegister);
        model.addAttribute("stats", stats);
        model.addAttribute("sum_res_price",sum_res_price);
        model.addAttribute("avg_res_price", avg_res_price);
        model.addAttribute("dateStart",dateStart);
        model.addAttribute("dateEnd",dateEnd);
        model.addAttribute("paysMap", paysMap);
        model.addAttribute("todaySales", todaySales);
        model.addAttribute("statsMonth", statsMonth);
        model.addAttribute("monthAvg", monthAvg);
        model.addAttribute("yearSales",yearSales);
        model.addAttribute("yearAvg",yearAvg);

        return "admin/stats";
    }

    // @since 2023/03/09 관리자 회원 - 회원 목록 (Form 전송)
    @GetMapping("user")
    public String user(Model model,
                       @RequestParam Map map,
                       @ModelAttribute Admin_SearchVO sc) {

        log.debug("GET user...");

        model.addAttribute("title", environment.getProperty(group));

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

        log.debug("GET users");

        log.debug("sorted : " + sorted);
        log.debug("sc : " + sc);

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

        List<UserVO> users = service.selectUser(model, sc);

        PageHandler ph = (PageHandler) model.getAttribute("ph");

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
    @PostMapping("user/memo")
    public Map<String, Integer> updateMemo(@RequestBody Map map) throws Exception {

        log.debug("POST user memo...");

        String user_id = (String) map.get("user_id");
        String memo = (String) map.get("memo");

        int result = service.updateMemo(memo, user_id);

        Map resultMap = new HashMap<>();
        resultMap.put("result", result);

        return resultMap;
    }

    // @since 2023/03/30 관리자 예약 - 메모 작성
    @ResponseBody
    @PostMapping("reservation/memo")
    public Map<String, Integer> usaveMemoInRes(@RequestBody Map map) throws Exception {

        log.debug("POST reservation memo...");

        String res_no = (String) map.get("res_no");
        String res_memo = (String) map.get("res_memo");

        int result = service.usaveMemoInRes(res_memo, res_no);

        Map resultMap = new HashMap<>();
        resultMap.put("result", result);

        return resultMap;
    }

    // @since 2023/03/10 관리자 회원 - 회원 차단
    @ResponseBody
    @PostMapping("user/block")
    public Map<String, Integer> updateIsLocked(@RequestBody Map map) throws Exception {

        log.debug("POST user block...");

        String user_id = (String) map.get("user_id");
        int result = service.updateIsLocked(user_id);

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("result", result);

        return resultMap;
    }

    @ResponseBody
    @PostMapping("user/unblock")
    public Map<String, Integer> usaveClear(@RequestBody Map map) throws Exception {

        log.debug("POST user unblock...");

        String user_id = (String) map.get("user_id");
        int result = service.usaveClear(user_id);

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("result", result);

        return resultMap;
    }

    // @since 2023/03/30 관리자 숙박 차단
    @ResponseBody
    @PostMapping("/info/block")
    public Map<String, Integer> usaveDropAcc(@RequestBody Map map) throws Exception {

        log.debug("POST info block...");

        String acc_id = (String) map.get("acc_id");
        int result = service.usaveDropAcc(acc_id);

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("result", result);

        return resultMap;
    }

    // @since 2023/03/30 관리자 숙박 차단 해제
    @ResponseBody
    @PostMapping("/info/unblock")
    public Map<String, Integer> usaveClearAcc(@RequestBody Map map) throws Exception {

        log.debug("POST info unblock...");

        String acc_id = (String) map.get("acc_id");
        int result = service.usaveClearAcc(acc_id);

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("result", result);

        return resultMap;
    }


    // @since 2023/03/11
    @GetMapping("coupon/coupon")
    public String coupon(Model model) {
        model.addAttribute("title", environment.getProperty(group));
        return "admin/coupon/coupon";
    }

    // @since 2023/03/12
    @PostMapping("coupon/post")
    public String rsaveCupon(CouponVO vo, RedirectAttributes redirectAttributes) throws Exception {

        log.debug("POST coupon...");

        service.rsaveCupon(vo);
        redirectAttributes.addFlashAttribute("successMessage", "쿠폰이 등록되었습니다.");
        return "redirect:/admin/coupon/coupon";
    }

    @GetMapping("coupon/list")
    public String list(Model model,
                       @RequestParam Map map,
                       @ModelAttribute Admin_SearchVO sc) {

        model.addAttribute("title", environment.getProperty(group));

        log.debug("GET coupon...");

        sc.setMap(map);
        service.selectCoupon(model, sc);

        return "admin/coupon/list";
    }

    @ResponseBody
    @DeleteMapping("coupon/delete")
    public Map<String, Integer> removeCoupon(@RequestBody Map map) throws Exception {

        String cp_id = (String) map.get("cp_id");

        log.debug("GET coupon delete...");

        int result = service.removeCoupon(cp_id);

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("result", result);

        return resultMap;
    }

    // 관리자 - 리뷰 목록
    @GetMapping("review/list")
    public String review_list(Model model,
                              @RequestParam Map map,
                              @ModelAttribute SearchCondition sc){

        log.debug("GET review list...");

        model.addAttribute("title", environment.getProperty(group));

        sc.setMap(map);
        service.findAllReview(model, sc);


        return "admin/review/list";
    }

    // 관리자 - 리뷰 보기
    @GetMapping("review/view")
    public String review_view(Model model, @RequestParam("revi_id") Integer revi_id) throws Exception{

        log.debug("GET review view...");

        model.addAttribute("title", environment.getProperty(group));

        ReviewVO review = service.findReview(revi_id);

        model.addAttribute("review", review);
        model.addAttribute("revi_id", revi_id);

        return "admin/review/view";
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
    @GetMapping("room/list")
    public String roomInfo_list(Model model,
                                @RequestParam Map map,
                                @ModelAttribute Admin_SearchVO sc){
        model.addAttribute("title", environment.getProperty(group));
        sc.setMap(map);
        service.findAllRoom(model, sc);

        return "admin/room/list";
    }

    // 관리자 - 객실 보기
    @GetMapping("room/view")
    public String roomInfo_view(Model model,
                                @RequestParam("room_id") Integer room_id) throws Exception{

        ProductRoomVO room = service.findRoom(room_id);

        model.addAttribute("title", environment.getProperty(group));

        model.addAttribute("room", room);
        model.addAttribute("room_id", room_id);

       return "admin/room/view";
    }


    @GetMapping("info/list")
    public String info_list(Model model,
                            @RequestParam Map map,
                            @ModelAttribute Admin_SearchVO sc){

        model.addAttribute("title", environment.getProperty(group));

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
    public String info_modify(Model model){
        model.addAttribute("title", environment.getProperty(group));
        return "admin/info/modify";
    }

    // 관리자 - 숙소 보기
    @GetMapping("info/view")
    public String info_view(Model model, @RequestParam("acc_id") Integer acc_id) throws Exception {

        ProductAccommodationVO acc = service.findAcc(acc_id);

        List<ServicereginfoVO> servicereginfos = service.findServiceInAcc(acc_id);

        model.addAttribute("title", environment.getProperty(group));
        model.addAttribute("acc", acc);
        model.addAttribute("acc_id", acc_id);
        model.addAttribute("serviceInfo", servicereginfos);

        return "admin/info/view";
    }



    @GetMapping("info/write")
    public String info_write(Model model){
        model.addAttribute("title", environment.getProperty(group));
        return "admin/info/write";
    }


    @GetMapping("reservation/list")
    public String reservation_list(Model model,
                                   @RequestParam Map map,
                                   @ModelAttribute Admin_SearchVO sc){
        model.addAttribute("title", environment.getProperty(group));
        sc.setMap(map);
        service.findAllReservaitons(model, sc);

        return "admin/reservation/list";
    }

    @GetMapping("reservation/timeline")
    public String reservation_timeline(Model model){
        model.addAttribute("title", environment.getProperty(group));
        service.findAllTimeline(model);
        return "admin/reservation/timeline";
    }







    // @since 2023/03/20 황원진
    @GetMapping("cs/{cs_cate}/list")
    public String findAllCs_list(@PathVariable("cs_cate") String cs_cate,
                                 String cs_type,
                                 Model model,
                                 @RequestParam Map map,
                                 @ModelAttribute Cs_SearchVO sc){


        sc.setMap(map);
        model.addAttribute("title", environment.getProperty(group));

        if("event".equals(cs_cate)){
            log.info("GET EventList start....");

            csService.findAllEventArticles(sc, model);
            return "admin/cs/event/list";
        }else if("notice".equals(cs_cate)) {
            log.info("GET NoticeList start....");

            csService.findAllCsArticles(sc, model);
            return "admin/cs/notice/list";
        }else if("qna".equals(cs_cate)){
            log.info("GET QnaList start....");

            csService.findAllAdminQnaArticles(sc, model);
            return "admin/cs/qna/list";
        }else if("faq".equals(cs_cate)){
            if(cs_type != null){
                log.info("GET FaqAllList start....");

                csService.findAllFaqArticles(sc, model);
                return "admin/cs/faq/list";
            }else {
                log.info("GET FaqTypeList start....");

                csService.findAllCsArticles(sc, model);
            }
            return "admin/cs/faq/list";
        }else if("terms".equals(cs_cate)){
            log.info("GET TermsListStart");
            csService.findAllAdminTerms(sc, model);
        }
        return "admin/cs/terms/list";
    }

    /**
     * @return
     * @author 황원진
     * @apiNote 약관 select option 추가
     * @since 2023/04/11
     */
    @GetMapping("cs/terms/find-terms-type")
    public ResponseEntity<List<TermVO>> findTermsTypes(){

        List<TermVO> types = csService.findTermsTypes();
        return ResponseEntity.ok(types);
    }


    /**
     * @since 2023/04/12
     * @author 황원진
     * @apiNote terms 게시물 삭제
     */
    @ResponseBody
    @DeleteMapping("cs/term")
    public Map<String, Integer> removeTerm(@RequestBody Map map){
        log.info("termRemoveStart...");
        log.debug((String) map.get("terms_no"));

        int terms_no = Integer.parseInt(String.valueOf(map.get("term_no")));

        int result = csService.removeTerm(terms_no);

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("result", result);

        return resultMap;
    }

    /**
     * @since 2023/03/16
     * @author 황원진
     * @apiNote 단일 게시물 삭제
     */
    @ResponseBody
    @DeleteMapping("cs/{cs_cate}/article")
    public Map<String, Integer> removeAdminArticle(@PathVariable("cs_cate") String cs_cate, @RequestBody Map map) throws Exception {

        log.info("removeArticleStart");

        int cs_no = Integer.parseInt(String.valueOf(map.get("cs_no")));

        int result = csService.removeAdminArticle(cs_no);

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("result", result);

        return resultMap;
    }

    /**
     * @since 2023/03/27
     * @author 황원진
     * @apiNote 게시물 선택삭제
     */
    @ResponseBody
    @DeleteMapping("cs/qna")
    public Map removeQnaList(@RequestBody Map<String, List<String>> data){
        log.info("listRemoveStart...");

        List<String> checkList = data.get("checkList");
       int result = csService.removeQnaList(checkList);

       Map<String, Integer> map = new HashMap<>();
        map.put("result", result);

       return map;
    }


    /**
     * @since 2023/03/14
     * @author 황원진
     * @apiNote cs 수정
     */
    @GetMapping("cs/{cs_cate}/modify")
    public String usaveCsArticle(@PathVariable("cs_cate") String cs_cate,
                                 int cs_no,
                                 Model model,
                                 int page,
                                 HttpServletRequest request){

        model.addAttribute("title", environment.getProperty(group));

        if("notice".equals(cs_cate)){
            log.info("GET NoticeModifyStart....");

            CsVO noticeArticle = csService.findAdminCsArticle(cs_cate, cs_no);
            String uri =  request.getHeader("referer");
            String regexUri = request.getContextPath() + "/admin/cs/notice/list";
            if(uri.contains(regexUri)){
                model.addAttribute("reUri", 1);
            }else{
                model.addAttribute("reUri", 2);
            }
            log.debug(uri);
            model.addAttribute("mNotice", noticeArticle);
            model.addAttribute("cs_no", cs_no);
            model.addAttribute("page", page);

            return "admin/cs/notice/modify";

        }else if("faq".equals(cs_cate)){
            log.info("GET FaqModifyStart...");
            CsVO faqArticle = csService.findAdminCsArticle(cs_cate, cs_no);

            model.addAttribute("mFaq", faqArticle);
            model.addAttribute("cs_no", cs_no);
            model.addAttribute("page", page);

            return "admin/cs/faq/modify";
        }else if("event".equals(cs_cate)){
            log.info("GET EventModifyStart....");
            CsVO eventArticle = csService.findAdminEventArticle(cs_cate, cs_no);

            model.addAttribute("mEvent", eventArticle);
            model.addAttribute("cs_no", cs_no);
            model.addAttribute("page", page);
        }
        return "admin/cs/event/modify";
    }

    /**
     * @since 2023/04/13
     * @author 황원진
     * @apiNote 약관 수정 Get
     */
    @GetMapping("cs/terms/modify")
    public String usaveTermArticle(int terms_no,
                                   int page,
                                   Model model,
                                   HttpServletRequest request){
        log.info("GET TermsModifyStart");

        model.addAttribute("title", environment.getProperty(group));

        TermVO termArticle = csService.findTermArticle(terms_no);
        String uri = request.getHeader("referer");
        String  regexUri = request.getContextPath() + "/admin/cs/terms/list";

        if(uri.contains(regexUri)){
            model.addAttribute("reUri", 1);
        }else {
            model.addAttribute("reUri", 2);
        }

        model.addAttribute("mTerm", termArticle);
        model.addAttribute("terms_no", terms_no);
        model.addAttribute("page", page);

        return "admin/cs/terms/modify";
    }




    /**
     * @since 2023/03/15
     * @author 황원진
     *
     */

    @PostMapping("cs/{cs_cate}/modify")
    public String usaveAdminNotice(@PathVariable("cs_cate") String cs_cate,
                                   CsVO vo,
                                   int page,
                                   String reUri){
        if ("notice".equals(cs_cate)) {
            log.info("POST NoticeModifyStart");
            log.debug(reUri);

            csService.usaveAdminNotice(vo);
            if(reUri.equals("1") ){
                log.debug("noticeList");
                return "redirect:/admin/cs/notice/list?page="+page;
            }else{
                return "redirect:/admin/cs/notice/view?cs_no="+vo.getCs_no()+"&page="+page;
            }

        }else if("faq".equals(cs_cate)){
            log.info("POST FaqModifyStart");
            csService.usaveFaqArticle(vo);
        }
        return "redirect:/admin/cs/faq/list?page="+page;
    }

    /**
     * @since 2023/03/15
     * @author 황원진
     *
     */
    @PostMapping("cs/terms/modify")
    public String usaveTermArticle(TermVO vo, int page, String reUri){
        log.info("POST TermsModifyStart");

        csService.usaveTermArticle(vo);
        if(reUri.equals("1")){
            return "redirect:/admin/cs/terms/list?page="+page;
        }else{
            return "redirect:/admin/cs/terms/view?terms_no="+vo.getTerms_no()+"&page="+page;
        }
    }

    /**
     * @since 2023/04/18
     * @aythor 황원진
     * @apiNote 이벤트 수정  (multipart 문제)
     */
    @ResponseBody
    @PostMapping("cs/event/modify")
    public String usaveEventArticle(
                    @AuthenticationPrincipal UserVO myUser,
                    @RequestPart(value = "cs_eventBanner", required = false) MultipartFile cs_eventBanner,
                    @RequestParam Map<String, Object> param,
                    MultipartHttpServletRequest request,
                    HttpServletRequest req){

        log.info("POST EventModifyStart");

        param.put("user_id", myUser.getUser_id());
        param.put("cs_regip", req.getRemoteAddr());

        Map<String, MultipartFile> fileMap = request.getFileMap();

        String result = csService.usaveEventArticle(param, fileMap);

        log.debug("controller : " + result);

        return result;
    }

    /**
     * @since 2023/03/16
     * @author 황원진
     */
    @GetMapping("cs/{cs_cate}/write")
    public String notice_write(@PathVariable("cs_cate") String cs_cate, Model model) {

        model.addAttribute("title", environment.getProperty(group));

        if("faq".equals(cs_cate)){
            log.info("GET faqWriteStart..");

            return "admin/cs/faq/write";
        }else if("notice".equals(cs_cate)){
            log.info("GET noticeWriteStart..");

            return "admin/cs/notice/write";
        }else if("event".equals(cs_cate)){
            log.info("GET eventWriteStart..");

            return "admin/cs/event/write";
        }else if("terms".equals(cs_cate)){
            log.info("GET termsWriteStart..");
        }
        return "admin/cs/terms/write";
    }


    /**
     * @since 2023/03/10
     * @author 황원진
     */
    @PostMapping("cs/{cs_cate}/write")
    public String rsaveNoticeArticle(@PathVariable("cs_cate") String cs_cate,
                                     CsVO vo,
                                     TermVO termVO,
                                     @AuthenticationPrincipal UserVO myUser,
                                     HttpServletRequest req) {

        if("notice".equals(cs_cate)){
            log.info("POST noticeWriteStart..");

            vo.setUser_id(myUser.getUser_id());
            vo.setCs_regip(RemoteAddrHandler.getRemoteAddr(req));

            csService.rsaveNoticeArticle(vo);
            return "redirect:/admin/cs/notice/list";

        }else if("faq".equals(cs_cate)){
            log.info("POST faqWriteStart..");

            vo.setUser_id(myUser.getUser_id());
            vo.setCs_regip(RemoteAddrHandler.getRemoteAddr(req));

            csService.rsaveFaqArticle(vo);
            return "redirect:/admin/cs/faq/list";
        }else if("terms".equals(cs_cate)){
            log.info("POST termsWriteStart..");

            csService.rsaveTermArticle(termVO);
        }
        return "redirect:/admin/cs/terms/list";
    }

    /**
     * @since 2023/03/10
     * @author 황원진
     * @apiNote event/write 분리 (multipart 문제)
     */

    @PostMapping("cs/event/write")
    public String rsaveEventArticle(
                                     CsVO vo,
                                     @AuthenticationPrincipal UserVO myUser,
                                     @RequestPart(value = "cs_eventBanner", required = false) MultipartFile cs_eventBanner,
                                     @RequestParam Map<String, Object> parameter,
                                     MultipartHttpServletRequest request,
                                     HttpServletRequest req) {

            parameter.put("user_id", myUser.getUser_id());
            parameter.put("cs_regip", req.getRemoteAddr());
            parameter.put("cs_cate", "event");


            Map<String, MultipartFile> fileMap = request.getFileMap();
            for (MultipartFile multipartFile : fileMap.values()) {
                log.debug(""+multipartFile.getOriginalFilename());
            }

            int result = csService.rsaveEventArticle(request, parameter, cs_eventBanner);

        return "redirect:/admin/cs/event/list";
    }





    /**
     * @since 2023/03/12
     * @author 황원진
     */
    @GetMapping("cs/{cs_cate}/view")
    public String findAdminCsArticle(@PathVariable("cs_cate") String cs_cate,
                                     int cs_no,
                                     int page,
                                     Model model){

        model.addAttribute("title", environment.getProperty(group));

        if("qna".equals(cs_cate)){
            log.info("GET qnaViewStart..");

            CsVO qnaArticle = csService.findAdminCsArticle(cs_cate, cs_no);
            model.addAttribute("qnaArticle", qnaArticle);
            model.addAttribute("cs_no", cs_no);
            model.addAttribute("page", page);
            return "admin/cs/qna/view";

        }else if("notice".equals(cs_cate)) {
            log.info("GET noticeViewStart..");
            CsVO noticeArticle = csService.findAdminCsArticle(cs_cate, cs_no);
            log.info("noticeArticle" + noticeArticle.getCs_title());
            log.info("noticeArticle" + noticeArticle.getCs_content());

            model.addAttribute("notice", noticeArticle);
            model.addAttribute("cs_no", cs_no);
            model.addAttribute("page", page);
            return "admin/cs/notice/view";

        }else if("event".equals(cs_cate)){
            log.info("POST eventViewStart..");
            CsVO eventArticle = csService.findAdminCsArticle(cs_cate, cs_no);

            model.addAttribute("event", eventArticle);
            model.addAttribute("cs_no", cs_no);
            model.addAttribute("cs_cate", cs_cate);
            model.addAttribute("page", page);

        }
        return "admin/cs/event/view";
    }

    /**
     * @since 2023/04/12
     * @author 황원진
     * @apiNote term 약관 상세보기
     */
    @GetMapping("cs/terms/view")
    public String findTermArticle(int terms_no, int page, Model model){
        model.addAttribute("title", environment.getProperty(group));

        log.info("GET termsViewStart..");
        TermVO termArticle = csService.findTermArticle(terms_no);

        model.addAttribute("term", termArticle);
        model.addAttribute("terms_no", terms_no);
        model.addAttribute("page", page);

        return "admin/cs/terms/view";
    }


    /**
     * @since 2023/03/14
     * @author 황원진
     * @apiNote qna 답글 update
     */
    @PostMapping("cs/qna/reply")
    public String usaveQnaArticle(String cs_reply, int cs_no, int page){

        csService.usaveQnaArticle(cs_reply, cs_no);

        return "redirect:/admin/cs/qna/view?cs_no="+cs_no+"&page="+page;
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











}