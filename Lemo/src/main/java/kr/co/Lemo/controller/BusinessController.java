package kr.co.Lemo.controller;

import kr.co.Lemo.domain.*;
import kr.co.Lemo.domain.search.Admin_SearchVO;
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

    @GetMapping(value = {"", "index"})
    public String index_business() {
        return "business/index";
    }

    // @since 2023/03/13
    @GetMapping("coupon/list")
    public String manageCoupon(@RequestParam Map map,
                               @AuthenticationPrincipal UserVO myUser,
                               @ModelAttribute Admin_SearchVO sc,
                               Model model) {
        log.warn("GET manage Coupon in business");

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

        log.info("Selected coupons: " + coupons.toString());

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

        log.warn("hi");

        String user_id = myUser.getUser_id();

        log.info("myUser : " + myUser);
        log.info("user_id : " + user_id);

        param.put("user_id", user_id);

        log.warn("acc_id: " + param.get("user_id"));
        log.info("param : "+param);

        service.rsaveCoupon(param);

        return "redirect:/business/coupon/list";
    }

    @ResponseBody
    @DeleteMapping("coupon/delete")
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
    public ResponseEntity<List<String>> findAccOwned(@AuthenticationPrincipal UserVO myUser) {

        String user_id = myUser.getUser_id();

        log.info("myUser2 : " + myUser);
        log.info("user_id2 : " + user_id);

        log.warn("GET findAccOwned in business");

        // stream().map().collect(): 이름들만 모아서 새로운 String 리스트를 만들어 낸다
        List<String> accs = service.findAccOwned(user_id).stream().map(CouponVO::getAcc_name).collect(Collectors.toList());

        log.warn("after service : " + accs);
        return ResponseEntity.ok(accs);
    }

    // @since 2023/04/04 판매자 리뷰 목록
    @GetMapping("review/list")
    public String review_list(@RequestParam Map map,
                              @AuthenticationPrincipal UserVO myUser,
                              @ModelAttribute Admin_SearchVO sc,
                              Model model){
        log.warn("GET review list...");

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
        log.info("Selected reviews: " + reviews.toString());

        model.addAttribute("reviews", reviews);
        model.addAttribute("ph", pageHandler);
        model.addAttribute("totalReviews", pageHandler.getTotalCnt());

        return "business/review/list";
    }

    // 판매자 리뷰 보기
    @GetMapping("review/view")
    public String review_view(Model model, @RequestParam("revi_id") Integer revi_id) throws Exception{

        ReviewVO review = service.findReview(revi_id);

        log.warn("selected review: " + review);

        model.addAttribute("review", review);
        model.addAttribute("revi_id", revi_id);

        return "business/review/view";
    }

/* (확인 후 삭제 예정)

    @GetMapping("review/findAccOwnedForReview")
    public ResponseEntity<List<ReviewVO>> findAccOwnedForReview(String user_id) {

        log.warn("GET findAccOwned in business");

        // stream().map().collect(): 이름들만 모아서 새로운 String 리스트를 만들어 낸다
        //List<String> accs = service.findAccOwnedForReview(user_id).stream().map(ReviewVO::getAcc_name).collect(Collectors.toList());
        List<ReviewVO> accs = service.findAccOwnedForReview(user_id);

        return ResponseEntity.ok(accs);
    }
*/

    // @since 2023/03/16 판매자 리뷰 답변 작성
    @ResponseBody
    @PostMapping("reply")
    public Map<String, Integer> usaveReply(@RequestBody Map map) throws Exception {
        log.warn(map.toString());

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

         log.warn("accs: " + accs.toString());

        model.addAttribute("accs", accs);
        model.addAttribute("ph", pageHandler);
        model.addAttribute("totalAccs", pageHandler.getTotalCnt());


        return "business/info/list";
    }

    @GetMapping("info/modify")
    public String info_modify(Model model, @RequestParam("acc_id") Integer acc_id) throws Exception{

        ProductAccommodationVO acc = service.fincAcc(acc_id);

        List<ServicereginfoVO> servicereginfos = service.findServiceInAcc(acc_id);

        log.warn("selected acc: " + acc);
        log.warn("serviceInfo: " + servicereginfos);

        model.addAttribute("acc", acc);
        model.addAttribute("acc_id", acc_id);
        model.addAttribute("serviceInfo", servicereginfos);

        return "business/info/modify";
    }

    @GetMapping("info/view")
    public String info_view(Model model, @RequestParam("acc_id") Integer acc_id) throws Exception{

        ProductAccommodationVO acc = service.fincAcc(acc_id);

        List<ServicereginfoVO> servicereginfos = service.findServiceInAcc(acc_id);

        log.warn("selected acc: " + acc);
        log.warn("serviceInfo: " + servicereginfos);

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

        log.warn("here1: " + uid);
        log.info("request.getFileMap: "+ request.getFileMap());

        Map<String, MultipartFile> fileMap = request.getFileMap();

        log.warn("here2 : " + fileMap);

        if(fileMap != null){

            log.info("fileMap : " + fileMap);
            log.info("fileMap values : " + fileMap.values());
            log.info("fileMap size : " + fileMap.size());

            int count = 1;

            for(MultipartFile mf: fileMap.values()){

                log.warn("count : " + count);
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
    @PostMapping("info/usave")
    public String info_usave(@RequestParam Map<String,Object> param,
                             MultipartHttpServletRequest request,
                             Model model) throws Exception {

        model.addAttribute("title", environment.getProperty(group));

        log.info("here1 "+ request.getFileMap());
        log.info("param1 : " + param);
        log.warn("here: " + param.get("acc_id"));
        log.warn("param-acc_info :" + param.get("acc_info"));

        Map<String, MultipartFile> fileMap = request.getFileMap();


        log.warn("here3 : " + fileMap);

        if(fileMap != null) {

            log.info("fileMap : " + fileMap);
            log.info("fileMap values : " + fileMap.values());
            log.info("fileMap size : " + fileMap.size());

            int count = 1;

            for (MultipartFile mf : fileMap.values()) {

                log.warn("count : " + count);
                log.info("mf.getOriginalFilename() : " + mf.getOriginalFilename());
                log.info("mf.getSize() : " + mf.getSize());
                log.info("mf.getContentType() : " + mf.getContentType());
                count++;

            }

            log.info("param2 : " + param);

            service.info_usave(param, request);

        }



        return "redirect:/business/info/list";
    }


    @ResponseBody
    @DeleteMapping("info/delete")
    public Map<String,Integer> removeAcc(@RequestBody Map map) throws Exception {
        String acc_id = (String)map.get("acc_id");

        log.warn("GET removeAcc");

        int result = service.removeAcc(acc_id);

        log.warn("after service : " + result);

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

        int totalCnt = service.countAcc(sc);
        int totalPage = (int)Math.ceil(totalCnt / (double)sc.getPageSize());
        if(sc.getPage() > totalPage) sc.setPage(totalPage);

        PageHandler pageHandler = new PageHandler(totalCnt, sc);

        List<ProductRoomVO> rooms = service.findAllRoom(sc);

        log.warn("rooms: " + rooms.toString());

        model.addAttribute("rooms", rooms);
        model.addAttribute("ph", pageHandler);
        model.addAttribute("totalRoom", pageHandler.getTotalCnt());

        return "business/room/list";
    }

    // @since 2023/04/02 판매자 소유 숙박 목록
    @GetMapping("find-acc")
    public ResponseEntity<List<ProductAccommodationVO>> findAllAccOwnedForInfo(@AuthenticationPrincipal UserVO myUser){

         log.warn("GET findAccOwned...");

         String user_id = myUser.getUser_id();

         log.warn("user_id = " + user_id);

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

        log.warn("POST rsaveRoom...");
        model.addAttribute("title", environment.getProperty(group));

        Map<String, MultipartFile> fileMap = request.getFileMap();

        log.warn("here1 : " + fileMap);

        if(fileMap != null) {

            log.info("fileMap : " + fileMap);
            log.info("fileMap values : " + fileMap.values());
            log.info("fileMap size : " + fileMap.size());

            int count = 1;

            for (MultipartFile mf : fileMap.values()) {

                log.warn("count : " + count);
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

        log.warn("selected room: " + room);

        model.addAttribute("room", room);
        model.addAttribute("room_id", room_id);

       return "business/room/view";
    }

    // 판매자 - 객실 삭제
    @ResponseBody
    @DeleteMapping("room/delete")
    public Map<String, Integer> removeRoom(@RequestBody Map map) throws Exception {
        String room_id = (String) map.get("room_id");

        log.warn("GET removeRoom");

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
        log.warn("GET reservation list...");

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
        log.info("Selected reservations: " + reservations.toString());

        model.addAttribute("reservations", reservations);
        model.addAttribute("ph", pageHandler);
        model.addAttribute("totalReservations", pageHandler.getTotalCnt());

        return "business/reservation/list";
    }

    // @since 2023/04/05 판매자 예약 - 메모 작성
    @ResponseBody
    @PostMapping("reservation/memo")
    public Map<String, Integer> usaveMemoInRes(@RequestBody Map map) throws Exception {
        log.warn(map.toString());

        String res_no = (String) map.get("res_no");
        String res_memo = (String) map.get("res_memo");

        int result = service.usaveMemoInRes(res_memo, res_no);

        Map resultMap = new HashMap<>();
        resultMap.put("result", result);

        return resultMap;
    }


     @GetMapping("reservation/timeline")
    public String reservation_timeline(){
        return "business/reservation/timeline";
    }

    @GetMapping("stats")
    public String stats(Model model,
                        @AuthenticationPrincipal UserVO myUser,
                        Map map){

        model.addAttribute("title", environment.getProperty(group));

        String user_id = "";
        if(myUser != null){
            user_id = myUser.getUser_id();
        }

        map.put("user_id", user_id);

        // 일별 매출 현황
        List<ReservationVO> stats = service.findAllDaySales(map);

        // 결제 현황
        List<ReservationVO> pays = service.findAllPayment(map);
        Map<Integer, List<ReservationVO>> paysMap = pays.stream().collect(Collectors.groupingBy(ReservationVO::getRes_payment));

        // 총 매출 건수
        int total = service.countWeeksSales(map);

        log.warn("total: " + total);

        model.addAttribute("stats", stats);
        model.addAttribute("paysMap", paysMap);
        model.addAttribute("totalSales", total);

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
}
