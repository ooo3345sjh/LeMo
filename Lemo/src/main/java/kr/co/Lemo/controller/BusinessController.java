package kr.co.Lemo.controller;

import kr.co.Lemo.domain.*;
import kr.co.Lemo.domain.search.Admin_SearchVO;
import kr.co.Lemo.repository.VisitorsLogRepo;
import kr.co.Lemo.service.BusinessService;
import kr.co.Lemo.utils.PageHandler;
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

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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
@PropertySource(value = "classpath:title.properties", encoding = "UTF-8")
@RequestMapping("business/")
public class BusinessController {

    @Autowired
    private Environment environment;
    private String group = "title.admin";

    @Autowired
    private BusinessService service;

    @Autowired
    private VisitorsLogRepo visitorsLogRepo;

    @GetMapping(value = {"", "index"})
    public String index_business(Model model,
                                 @AuthenticationPrincipal UserVO myUser,
                                 Map map) {

        model.addAttribute("title", environment.getProperty(group));

        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        String user_id = "";
        if(myUser != null){
            user_id = myUser.getUser_id();
        }

        map.put("user_id", user_id);

        // 숙소 리스트
        List<ProductAccommodationVO> accs = service.selectAccsList(map);

        int visitorTotal = 0;

        for (ProductAccommodationVO vo : accs) {
            String acc_id = String.valueOf(vo.getAcc_id());
            // 방문자수
            int visitorCount = visitorsLogRepo.countVisitors(start, end, acc_id);
            visitorTotal += visitorCount;
        }


        // 당일 누적 판매량
        List<ReservationVO> todaySales = service.findAllTodaySales(map);


        if(todaySales.size() == 0){

        }

        // 당일 예약 개수
        int daySales = service.countDaySales(map);
        // 당일 예약 취소 개수
        int dayCanceled = service.countDayCancel(map);
        // 당일 미배정 객실 (전체 객실-체크인 객실/전체 객실)
        int totalRoom = service.countTotalRoom(map);
        int checkRoom = service.countCheckInRoom(map);
        // 당일 판매자 문의수
        int dayQna = service.countDayQna(map);
        // 당일 리뷰수
        int dayReview = service.countDayReview(map);
        // 최신 리뷰
        List<ReviewVO> reviews = service.findAllReviewLatest(map);
        // 결제 수단 현황
        List<ReservationVO> pays = service.findAllPayment(map);
        Map<Integer, List<ReservationVO>> paysMap = pays.stream().collect(Collectors.groupingBy(ReservationVO::getRes_payment));
        // 객실 예약 현황
        List<ReservationVO> roomPercent = service.selectWeeksRoom(map);


        model.addAttribute("todaySales", todaySales);
        model.addAttribute("daySales", daySales);
        model.addAttribute("dayCanceled", dayCanceled);
        model.addAttribute("totalRoom", totalRoom);
        model.addAttribute("checkRoom", checkRoom);
        model.addAttribute("reviews", reviews);
        model.addAttribute("dayQna", dayQna);
        model.addAttribute("dayReview", dayReview);
        model.addAttribute("paysMap", paysMap);
        model.addAttribute("roomPercent", roomPercent);
        model.addAttribute("visitorTotal", visitorTotal);

        return "business/index";
    }

    // @since 2023/03/13
    @GetMapping("coupon/list")
    public String manageCoupon(@RequestParam Map map,
                               @AuthenticationPrincipal UserVO myUser,
                               @ModelAttribute Admin_SearchVO sc,
                               Model model) {
        log.debug("GET manage Coupon in business");

        model.addAttribute("title", environment.getProperty(group));

        String user_id = "";
        if(myUser != null) {
            user_id = myUser.getUser_id();
        }

        sc.setMap(map);
        sc.setUser_id(user_id);

        int totalCnt = service.countCoupon(sc);
        int totalPage = (int)Math.ceil(totalCnt / (double)sc.getPageSize());
        if(sc.getPage() > totalPage) sc.setPage(totalPage);

        PageHandler pageHandler = new PageHandler(totalCnt, sc);

        //log.info("select Coupon Service: " + sc.toString());

        List<CouponVO> coupons = service.selectCoupon(sc);

        model.addAttribute("coupons", coupons);
        model.addAttribute("ph", pageHandler);
        model.addAttribute("totalCoupons", pageHandler.getTotalCnt());

        return "business/coupon/list";
    }


    // @since 2023/03/13
    @GetMapping("coupon/coupon")
    public String insertCoupon() {
        return "business/coupon/coupon";
    }

    // @since 2023/03/13

    @PostMapping("coupon/post")
    public String rsaveCoupon(@RequestParam Map<String,Object> param,
                                    @AuthenticationPrincipal UserVO myUser,
                                    Model model) throws Exception {

        model.addAttribute("title", environment.getProperty(group));

        String user_id = myUser.getUser_id();

        param.put("user_id", user_id);

        service.rsaveCoupon(param);

        return "redirect:/business/coupon/list";
    }

    @ResponseBody
    @DeleteMapping("coupon/delete")
    public Map<String, Integer> removeCoupon(@RequestBody Map map) throws Exception {

        String cp_id = (String) map.get("cp_id");

        log.debug("GET remove Coupon");

        int result = service.removeCoupon(cp_id);


        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("result", result);

        return resultMap;
    }

    @GetMapping("coupon/findAccOwned")
    public ResponseEntity<List<String>> findAccOwned(@AuthenticationPrincipal UserVO myUser) {

        String user_id = myUser.getUser_id();

        log.info("myUser2 : " + myUser);
        log.info("user_id2 : " + user_id);

        log.debug("GET findAccOwned in business");

        // stream().map().collect(): 이름들만 모아서 새로운 String 리스트를 만들어 낸다
        List<String> accs = service.findAccOwned(user_id).stream().map(CouponVO::getAcc_name).collect(Collectors.toList());

        return ResponseEntity.ok(accs);
    }

    // @since 2023/04/04 판매자 리뷰 목록
    @GetMapping("review/list")
    public String review_list(@RequestParam Map map,
                              @AuthenticationPrincipal UserVO myUser,
                              @ModelAttribute Admin_SearchVO sc,
                              Model model){
        log.debug("GET review list...");

        model.addAttribute("title", environment.getProperty(group));

        String user_id = "";
        if(myUser != null) {
            user_id = myUser.getUser_id();
        }

        sc.setMap(map);
        sc.setUser_id(user_id);

        int totalCnt = service.countReview(sc);
        int totalPage = (int)Math.ceil(totalCnt / (double)sc.getPageSize());
        if(sc.getPage() > totalPage) sc.setPage(totalPage);

        PageHandler pageHandler = new PageHandler(totalCnt, sc);

        List<ReviewVO> reviews = service.findAllReview(sc);

        model.addAttribute("reviews", reviews);
        model.addAttribute("ph", pageHandler);
        model.addAttribute("totalReviews", pageHandler.getTotalCnt());

        return "business/review/list";
    }

    // 판매자 기
    @GetMapping("review/view")
    public String review_view(Model model, @RequestParam("revi_id") Integer revi_id) throws Exception{

        ReviewVO review = service.findReview(revi_id);


        model.addAttribute("review", review);
        model.addAttribute("revi_id", revi_id);

        return "business/review/view";
    }



    // @since 2023/03/16 판매자 리뷰 답변 작성
    @ResponseBody
    @PostMapping("reply")
    public Map<String, Integer> usaveReply(@RequestBody Map map) throws Exception {

        String revi_id = (String)map.get("revi_id");
        String revi_reply = (String)map.get("revi_reply");

        int result = service.usaveReply(revi_reply, revi_id);

        Map resultMap = new HashMap<>();
        resultMap.put("result", result);

        return resultMap;

    }

    // @since 2023/03/16 판매자 리뷰 삭제
    @ResponseBody
    @DeleteMapping("/review/delete")
    public Map<String, Integer> removeReview(@RequestBody Map map) throws Exception {
        String revi_id = (String)map.get("revi_id");

        int result = service.removeReview(revi_id);

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("result", result);

        return resultMap;
    }

    // @since 2023/03/23 판매자 숙소 목록
    @GetMapping("info/list")
    public String info_list(@RequestParam Map map,
                            @AuthenticationPrincipal UserVO myUser,
                            @ModelAttribute Admin_SearchVO sc,
                            Model model
                            ){

        model.addAttribute("title", environment.getProperty(group));


        String user_id = "";
        if(myUser != null) {
            user_id = myUser.getUser_id();
        }
        //String user_id = "1foodtax@within.co.kr";

        sc.setMap(map);
        sc.setUser_id(user_id);

        // 페이징
        int totalCnt = service.countAcc(sc);
        int totalPage = (int)Math.ceil(totalCnt / (double)sc.getPageSize());
        if(sc.getPage() > totalPage) sc.setPage(totalPage);

        PageHandler pageHandler = new PageHandler(totalCnt, sc);

        List<ProductAccommodationVO> accs = service.findAllAccForInfo(sc);


        model.addAttribute("accs", accs);
        model.addAttribute("ph", pageHandler);
        model.addAttribute("totalAccs", pageHandler.getTotalCnt());


        return "business/info/list";
    }

    @GetMapping("info/modify")
    public String info_modify(Model model, @RequestParam("acc_id") Integer acc_id) throws Exception{

        ProductAccommodationVO acc = service.fincAcc(acc_id);

        List<ServicereginfoVO> servicereginfos = service.findServiceInAcc(acc_id);


        model.addAttribute("acc", acc);
        model.addAttribute("acc_id", acc_id);
        model.addAttribute("serviceInfo", servicereginfos);

        return "business/info/modify";
    }

    @GetMapping("info/view")
    public String info_view(Model model, @RequestParam("acc_id") Integer acc_id) throws Exception{

        ProductAccommodationVO acc = service.fincAcc(acc_id);

        List<ServicereginfoVO> servicereginfos = service.findServiceInAcc(acc_id);


        model.addAttribute("acc", acc);
        model.addAttribute("acc_id", acc_id);
        model.addAttribute("serviceInfo", servicereginfos);

        return "business/info/view";
    }

    // 판매자 숙소 등록
    @GetMapping("info/write")
    public String info_write(){
        return "business/info/write";
    }

    // 판매자 숙소 등록
    @ResponseBody
    @PostMapping("info/post")
    public String info_rsave(@RequestParam Map<String,Object> param,
                             @AuthenticationPrincipal UserVO myUser,
                             MultipartHttpServletRequest request,
                             Model model) throws Exception {

        model.addAttribute("title", environment.getProperty(group));

        String uid = "";
        if(myUser!= null) {
            uid = myUser.getUser_id();
        }

        log.debug("here1: " + uid);
        log.info("request.getFileMap: "+ request.getFileMap());

        Map<String, MultipartFile> fileMap = request.getFileMap();


        if(fileMap != null){

            log.info("fileMap : " + fileMap);
            log.info("fileMap values : " + fileMap.values());
            log.info("fileMap size : " + fileMap.size());

            int count = 1;

            for(MultipartFile mf: fileMap.values()){

                log.debug("count : " + count);
                log.info("mf.getOriginalFilename() : " + mf.getOriginalFilename());
                log.info("mf.getSize() : " + mf.getSize());
                log.info("mf.getContentType() : " + mf.getContentType());
                count++;

            }

            log.info("param : "+param);

            // service: param, vo, fileMap, req, uid 보내기
            service.info_rsave(param, request, uid);



        }


        return "redirect:/business/info/list";
    }


    @ResponseBody
    @DeleteMapping("info/delete")
    public Map<String,Integer> removeAcc(@RequestBody Map map) throws Exception {
        String acc_id = (String)map.get("acc_id");

        log.debug("GET removeAcc");

        int result = service.removeAcc(acc_id);

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("result", result);

        return resultMap;
    }



    @GetMapping("info/service")
    public ResponseEntity<List<ServiceCateVO>> findService(){
        List<ServiceCateVO> services = service.findService();
        return ResponseEntity.ok(services);
    }

    @GetMapping("info/province")
    public ResponseEntity<List<ProvinceVO>> findProvince(){
        List<ProvinceVO> provinces = service.findProvince();
        return ResponseEntity.ok(provinces);
    }
/* ( 확인 후 삭제 예정)

    @GetMapping("MapTest")
    public String MapTest() {
        return "business/MapTest";
    }

    @GetMapping("dragNdropTest")
    public String summernoteTest(){
        return "business/dragNdropTest";
    }
*/

    // @since 2023/04/02 판매자 객실 목록
    @GetMapping("room/list")
    public String roonInfo_list(@RequestParam Map map,
                                @AuthenticationPrincipal UserVO myUser,
                                @ModelAttribute Admin_SearchVO sc,
                                Model model
                                ){

        model.addAttribute("title", environment.getProperty(group));

        String user_id = "";
        if(myUser != null) {
            user_id = myUser.getUser_id();
        }

        sc.setMap(map);
        sc.setUser_id(user_id);

        int totalCnt = service.countRoom(sc);
        int totalPage = (int)Math.ceil(totalCnt / (double)sc.getPageSize());
        if(sc.getPage() > totalPage) sc.setPage(totalPage);

        PageHandler pageHandler = new PageHandler(totalCnt, sc);

        List<ProductRoomVO> rooms = service.findAllRoom(sc);


        model.addAttribute("rooms", rooms);
        model.addAttribute("ph", pageHandler);
        model.addAttribute("totalRoom", pageHandler.getTotalCnt());

        return "business/room/list";
    }

    // @since 2023/04/02 판매자 소유 숙박 목록
    @GetMapping("find-acc")
    public ResponseEntity<List<ProductAccommodationVO>> findAllAccOwnedForInfo(@AuthenticationPrincipal UserVO myUser){

         log.debug("GET findAccOwned...");

         String user_id = myUser.getUser_id();

         log.debug("user_id = " + user_id);

        //List<String> accs = service.finaAllAccOwnedForInfo(user_id).stream().map(ProductAccommodationVO::getAcc_name).collect(Collectors.toList());
        List<ProductAccommodationVO> accs = service.findAllAccOwnedForInfo(user_id);
        return ResponseEntity.ok(accs);
    }

    // @since 2023/04/02 판매자 객실 등록
    @GetMapping("room/room")
    public String roomInfo_write(){
       return "business/room/write";
    }

    // @since 2023/04/02 판매자 객실 등록
    @PostMapping("room/post")
    public String rsaveRoom(@RequestParam Map<String, Object> param,
                            MultipartHttpServletRequest request,
                            Model model) throws Exception{

        log.debug("POST rsaveRoom...");
        model.addAttribute("title", environment.getProperty(group));

        Map<String, MultipartFile> fileMap = request.getFileMap();


        if(fileMap != null) {

            log.info("fileMap : " + fileMap);
            log.info("fileMap values : " + fileMap.values());
            log.info("fileMap size : " + fileMap.size());

            int count = 1;

            for (MultipartFile mf : fileMap.values()) {

                log.debug("count : " + count);
                log.info("mf.getOriginalFilename() : " + mf.getOriginalFilename());
                log.info("mf.getSize() : " + mf.getSize());
                log.info("mf.getContentType() : " + mf.getContentType());
                count++;

            }

            log.info("param : " + param);

            service.rsaveRoom(param, request);
        }
        return "redirect:/business/room/list";

    }

    // 판매자 - 객실 보기
    @GetMapping("room/view")
    public String roomInfo_view(Model model,
                                @RequestParam("room_id") Integer room_id) throws Exception{

        ProductRoomVO room = service.findRoom(room_id);

        model.addAttribute("room", room);
        model.addAttribute("room_id", room_id);

       return "business/room/view";
    }

    // 판매자 - 객실 삭제
    @ResponseBody
    @DeleteMapping("room/delete")
    public Map<String, Integer> removeRoom(@RequestBody Map map) throws Exception {
        String room_id = (String) map.get("room_id");

        log.debug("GET removeRoom");

        int result = service.removeRoom(room_id);

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("result", result);

        return resultMap;
    }

    // 판매자 - 예약 관리
    @GetMapping("reservation/list")
    public String reservation_list(@RequestParam Map map,
                                   @AuthenticationPrincipal UserVO myUser,
                                   @ModelAttribute Admin_SearchVO sc,
                                   Model model){
        log.debug("GET reservation list...");

        model.addAttribute("title", environment.getProperty(group));

        String user_id = "";
        if(myUser != null) {
            user_id = myUser.getUser_id();
        }

        sc.setMap(map);
        sc.setUser_id(user_id);

        int totalCnt = service.countReservations(sc);
        int totalPage = (int)Math.ceil(totalCnt / (double)sc.getPageSize());
        if(sc.getPage() > totalPage) sc.setPage(totalPage);

        PageHandler pageHandler = new PageHandler(totalCnt, sc);

        List<ReservationVO> reservations = service.findAllReservaitons(sc);

        model.addAttribute("reservations", reservations);
        model.addAttribute("ph", pageHandler);
        model.addAttribute("totalReservations", pageHandler.getTotalCnt());

        return "business/reservation/list";
    }

    // @since 2023/04/05 판매자 예약 - 메모 작성
    @ResponseBody
    @PostMapping("reservation/memo")
    public Map<String, Integer> usaveMemoInRes(@RequestBody Map map) throws Exception {

        String res_no = (String) map.get("res_no");
        String res_memo = (String) map.get("res_memo");

        int result = service.usaveMemoInRes(res_memo, res_no);

        Map resultMap = new HashMap<>();
        resultMap.put("result", result);

        return resultMap;
    }


    @GetMapping("reservation/timeline")
    public String reservation_timeline(Model model,
                                       @AuthenticationPrincipal UserVO myUser,
                                       @RequestParam Map<String, String> map,
                                       @RequestParam(required = false) Integer acc_id,
                                       @RequestParam(required = false) String cal_date){

        model.addAttribute("title", environment.getProperty(group));

        String user_id = "";
        if(myUser != null){
            user_id = myUser.getUser_id();
        }

        map.put("user_id", user_id);

        if (cal_date != null) {

            String cal_year = cal_date.substring(0, 4);
            String cal_month = cal_date.substring(5, 7);

            map.put("cal_year", cal_year);
            map.put("cal_month", cal_month);
        }else {
            map.put("cal_year", "null");
            map.put("cal_month", "null");
        }


        List<ReservationVO> timelines = service.findAllTimeline(map);

        model.addAttribute("timelines", timelines);

        List<ProductAccommodationVO> accs = service.findAllAccOwnedForInfo(user_id);
        model.addAttribute("accs", accs);
        return "business/reservation/timeline";
    }

    @GetMapping("reservation/timeline_reservation")
    @ResponseBody
    public List<ReservationVO> timeline_reservation(@RequestParam(value = "res_no") String res_no){
        List<ReservationVO> rv = service.findReservation(res_no);
        return rv;
    }

    @GetMapping("stats")
    public String stats(Model model,
                        @AuthenticationPrincipal UserVO myUser,
                        @RequestParam Map<String, String> map,
                        @RequestParam(required = false) String acc_id){

        // 타이틀 설정
        model.addAttribute("title", environment.getProperty(group));

        // 판매자 아이디 -> map에 저장
        String user_id = "";
        if(myUser != null){
            user_id = myUser.getUser_id();
        }
        map.put("user_id", user_id);

        // 숙소 아이디 가져오기 (view -> controller)
        acc_id = (String) map.get("acc_id");

        //log.warn("map: " + map);
        //log.warn("get acc_id in map :" + acc_id);

        if(acc_id == null || acc_id.isBlank()){
            map.put("acc_id", null);
        }

        // 전역변수 선언
        int visitorTotal = 0;
        int total = 0;
        int totalCanceled = 0;
        int sum_res_price = 0;
        List<ReservationVO> stats = new ArrayList<>();
        int totalQna = 0;
        int totalAcc = 0;
        int totalReview = 0;
        List<ReservationVO> roomPercent = new ArrayList<>();
        List<ReservationVO> pays = new ArrayList<>();
        Map<Integer, List<ReservationVO>> paysMap = new HashMap<>();
        int visitorCount = 0;
        int avg_res_price = 0;
        int room_sale_percent = 0;
        String periodType = map.get("periodType");
        String dateStart = map.get("dateStart");
        String dateEnd = map.get("dateEnd");
        List<ProductAccommodationVO> rates = new ArrayList<>();

        rates = service.findAllRates(map);
        float reteSum = 0;
        float rateAvg = 0;


        int rates_sum = 0;

        for (int i = 0; i < rates.size(); i++) {
            reteSum += rates.get(i).getAcc_rate();
        }
        rateAvg = reteSum / rates.size();
        String formattedRateAvg = String.format("%.1f", rateAvg); // 소수점 첫째 자리까지 나타내기 위한 포맷팅

        //log.warn("formattedRateAvg: " + formattedRateAvg);




        //log.warn("rateSum: " + reteSum);
        //log.warn("rateAvg: " + rateAvg);

        if (map.get("dateStart") == null || map.get("dateStart").isBlank() && map.get("dateEnd") == null || map.get("dateEnd").isBlank()) {
            map.put("dateStart",null);
            map.put("dateEnd",null);
        }else {
            //log.warn("dateStart type: " + dateStart.getClass().getName());
        }

        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        // 기간 미 설정시 기본 -> 일주일
        if(periodType == null){
            map.put("periodType", "week");

            // 방문자수 (기간 설정, 일주일)
            start = end.minusDays(6);
            // 방문자수 (일주일)
            if(acc_id == null || acc_id.equals("")){
                 List<ProductAccommodationVO> accs = service.selectAccsList(map);
                for (ProductAccommodationVO vo : accs) {
                    //log.warn("acc_id : " + vo.getAcc_id());
                    acc_id = String.valueOf(vo.getAcc_id());
                    visitorCount = visitorsLogRepo.countVisitors(start, end, acc_id);
                    visitorTotal += visitorCount;
                }
            }else {
                visitorCount = visitorsLogRepo.countVisitors(start, end, acc_id);
                visitorTotal = visitorCount;
            }


        // 기간 설정 시 -> 기간 설정에 따른 데이터 조회
        }else if(periodType != null){

            map.put("periodType", periodType);

            // 방문자수
            if(periodType == "day"){
                start = end.minusDays(0);
            }else if(periodType == "month"){
                start = end.minusDays(30);
            }else if(periodType == "year"){
                start = end.minusDays(365);
            }
            if(acc_id == null || acc_id.equals("")){
                 List<ProductAccommodationVO> accs = service.selectAccsList(map);
                for (ProductAccommodationVO vo : accs) {
                    //log.warn("acc_id : " + vo.getAcc_id());
                    acc_id = String.valueOf(vo.getAcc_id());
                    visitorCount = visitorsLogRepo.countVisitors(start, end, acc_id);
                    visitorTotal += visitorCount;
                }
            }else {
                visitorCount = visitorsLogRepo.countVisitors(start, end, acc_id);
                visitorTotal = visitorCount;
            }

        }



         // 총 매출 건수
        total = service.countWeeksSales(map);
         // 취소 건수 (일주일)
        totalCanceled = service.countWeeksCancel(map);
        // 1:1 문의 수 (일주일)
        totalQna = service.countWeeksQna(map);
        // 상품 등록 수  (일주일)
        totalAcc = service.countWeeksAcc(map);
        // 리뷰 등록 수  (일주일)
        totalReview = service.countWeeksReview(map);
        // 일별 매출 현황 (대표 그래프)
        stats = service.findAllDaySales(map);

        //log.warn("stats: " + stats.size());

        if(stats.size() == 0) {
            sum_res_price = 0;
            avg_res_price = 0;
        }else {
                for ( int i=0; i<stats.size(); i++ ){
                sum_res_price += stats.get(i).getTot_res_price();
            }
            avg_res_price = sum_res_price / stats.size();
        }


        // 결제 수단 현황 (일주일)
        pays = service.findAllPayment(map);
        paysMap = pays.stream().collect(Collectors.groupingBy(ReservationVO::getRes_payment));
        // 객실 예약 현황  (일주일)
        roomPercent = service.selectWeeksRoom(map);

        //log.warn("RoomPercent : " + roomPercent);

        // 단위 기간별 매출현황 (기간 검색 미적용, 숙소선택 적용)
        // 당일 누적 판매량
        List<ReservationVO> todaySales = service.findAllTodaySales(map);

        //log.warn("todaySales : " + todaySales);

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

        //log.warn("yearSales : " + yearSales);

        int yearSum = 0;

        for (ReservationVO mAvg : yearSales) {
            //log.warn("here2 : " + mAvg.getTot_res_price());
            yearSum += mAvg.getTot_res_price();
        }

        int yearAvg = yearSum/3;          // 3년 년평균 매출

        //log.warn("yearAvg : " + yearAvg);




        model.addAttribute("stats", stats);
        model.addAttribute("todaySales", todaySales);
        model.addAttribute("paysMap", paysMap);
        model.addAttribute("totalSales", total);
        model.addAttribute("totalCanceled", totalCanceled);
        model.addAttribute("totalQna", totalQna);
        model.addAttribute("totalAcc", totalAcc);
        model.addAttribute("totalReview", totalReview);
        model.addAttribute("roomPercent", roomPercent);
        model.addAttribute("statsMonth", statsMonth);
        model.addAttribute("monthAvg", monthAvg);
        model.addAttribute("yearSales",yearSales);
        model.addAttribute("yearAvg",yearAvg);
        model.addAttribute("visitorTotal",visitorTotal);
        model.addAttribute("sum_res_price",sum_res_price);
        model.addAttribute("avg_res_price",avg_res_price);
        model.addAttribute("dateStart",dateStart);
        model.addAttribute("dateEnd",dateEnd);
        model.addAttribute("rateAvg",rateAvg);
        model.addAttribute("formattedRateAvg",formattedRateAvg);


        return "business/stats";
    }




    /**
     * @since 2023/04/05
     * @author 황원진
     * @apiNote 판매자 상품문의 controller
     */

    // @since 2023/04/06 황원진 판매자 상품문의 목록
    @GetMapping("qna/list")
    public String qna_list(@RequestParam Map map,
                           @AuthenticationPrincipal UserVO myUser,
                           @ModelAttribute Admin_SearchVO sc,
                           Model model){

        model.addAttribute("title", environment.getProperty(group));

        String user_id = "";
        if(myUser != null){
            user_id = myUser.getUser_id();
        }

        sc.setMap(map);
        sc.setUser_id(user_id);

        int totalCnt = service.countQna(sc);
        int totalPage = (int) Math.ceil(totalCnt / (double)sc.getPageSize());
        if(sc.getPage() > totalPage) sc.setPage(totalPage);

        PageHandler pageHandler = new PageHandler(totalCnt, sc);

        List<ProductQnaVO> qnaArticles = service.findAllQna(sc);

        model.addAttribute("qnaArticles", qnaArticles);
        model.addAttribute("ph", pageHandler);
        model.addAttribute("totalCnt", totalCnt);
        model.addAttribute("sc", sc);

        return null;
    }

    // @since 2023/04/06 황원진 select option 추가
    @GetMapping("find-all-product-qna")
    public ResponseEntity<List<ProductQnaVO>> findAllAccOwnedForQna(@AuthenticationPrincipal UserVO myUser){

        log.debug("findAllAccOwnedForQna");

        String user_id = myUser.getUser_id();

        List<ProductQnaVO> accs = service.findAllAccOwnedForQna(user_id);
        return ResponseEntity.ok(accs);
    }

    // @since 2023/04/06 황원진 상품목록 선택삭제
    @ResponseBody
    @DeleteMapping("qna")
    public Map removeQnaList(@RequestBody Map<String, List<String>> data){
        List<String> checkList = data.get("checkList");
        int result = service.removeQnaList(checkList);

        Map<String, Integer> map = new HashMap<>();
        map.put("result", result);

        return map;
    }

    // @since 2023/04/08 황원진 상품목록 상세보기
    @GetMapping("qna/view")
    public String findQnaArticle(int qna_no, int page, Model model){
        model.addAttribute("title", environment.getProperty(group));

       ProductQnaVO qnaArticle = service.findQnaArticle(qna_no);

       model.addAttribute("qnaArticle", qnaArticle);
       model.addAttribute("qna_no", qna_no);
       model.addAttribute("page", page);

        return "business/qna/view";
    }

    // @since 2023/04/08 황원진 상품목록 답변등록
    @PostMapping("qna/view")
    public String usaveQnaReply(String qna_reply, int qna_no, int page){
        service.usaveQnaReply(qna_reply, qna_no);
        return "redirect:/business/qna/view?qna_no="+qna_no+"&page="+page;
    }

    // @since 2023/04/08 황원진 상품목록 답변수정
    @ResponseBody
    @PatchMapping("qna/udate")
    public Map<String, Integer> usaveQnaUdate(@RequestBody Map map){
        String qna_reply = (String) map.get("qna_reply");
        int qna_no = Integer.parseInt(String.valueOf(map.get("qna_no")));

        log.debug(qna_reply);
        log.debug(String.valueOf(qna_no));

        int result =service.usaveQnaUdate(qna_reply, qna_no);

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("result", result);

        return resultMap;
    }


    /**
     * @since 2023/04/15
     * @author 이해빈
     * @apiNote 객실 수정
     */
    @GetMapping("room/modify")
    public String roomInfo_modify(int room_id, Model model) throws Exception {

        // 객실 정보 가져오기
        ProductRoomVO room = service.findRoom(room_id);
        model.addAttribute("room", room);

        return "business/room/modify";
    }

    /**
     * @since 2023/04/16
     * @author 이해빈
     * @apiNote 객실 수정
     */
    @ResponseBody
    @PostMapping("room/modify")
    public int modifyRoom(
            @RequestParam Map<String, Object> param,
            HttpServletRequest req,
            MultipartHttpServletRequest request
    ) throws Exception {

        Map<String, MultipartFile> fileMap = request.getFileMap();

        log.info("param : " + param);
        log.info("fileMap : " + fileMap);

        int result = service.usaveRoom(param, fileMap);

        return result;
    }

    /**
     * @since 2023/04/16
     * @author 이해빈
     * @apiNote 숙소 수정
     */

    @ResponseBody
    @PostMapping("info/usave")
    public int info_usave(@RequestParam Map<String,Object> param,
                             HttpServletRequest req,
                             MultipartHttpServletRequest request) throws Exception {

        Map<String, MultipartFile> fileMap = request.getFileMap();

        int result = service.info_usave(param, fileMap);

        return result;
    }
}





