package kr.co.Lemo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.Lemo.dao.ProductDAO;
import kr.co.Lemo.domain.*;
import kr.co.Lemo.domain.search.ProductDetail_SearchVO;
import kr.co.Lemo.domain.search.Product_SearchVO;
import kr.co.Lemo.utils.PageHandler;
import kr.co.Lemo.utils.SearchCondition;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpSession;
import java.net.URI;
import java.net.URLEncoder;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @since 2023/03/09
 * @author 이해빈
 * @apiNote product service
 */
@Slf4j
@Service
public class ProductService {

    @Autowired
    private ProductDAO dao;

    @Value("${kakaoMap.AdminKey}")
    private String serviceKey;

    // insert
    // @since 2023/03/21
    public int rsaveQna(ProductQnaVO qna) {
        return dao.insertQna(qna);
    }

    // @since 2023/03/24
    public int saveProductPick(Map map){
        return dao.insertProductPick(map);
    };

    // select

    // @since 2023/03/09
    public void findAllAccommodations(Model model, Map map, Product_SearchVO sc) throws Exception {

        String keyword = sc.getKeyword();
        double lng = sc.getLng();
        double lat = sc.getLat();
        String checkIn = sc.getCheckIn();
        String checkOut = sc.getCheckOut();


        // 메인 -> 키워드 검색일 경우
        if (keyword != null) {
            double LatLng[] = getLatLng(keyword);
            lat = LatLng[0];
            lng = LatLng[1];

        }

        // 장소 결과가 없는 경우 위도 경도 서울을 기준으로 세팅
        if (lat == 0.0 & lng == 0.0 & keyword == null) {
            lat = 37.566824194479864;
            lng = 126.9786069825986;

        }

        // SearchCondition
        if(sc.getSort() == null){
            sc.setSort("review");
            map.put("sort", "review");
        }

        // 체크인 체크아웃 날짜가 없을 경우
        if(checkIn == null && checkOut == null) {
            checkIn = String.valueOf(LocalDate.now());
            checkOut = String.valueOf(LocalDate.now().plusDays(1));

            sc.setCheckIn(checkIn);
            sc.setCheckOut(checkOut);
            map.put("checkIn", checkIn);
            map.put("checkOut", checkOut);
        }

        sc.setLat(lat);
        sc.setLng(lng);
        map.put("lat", String.valueOf(lat));
        map.put("lng", String.valueOf(lng));

        sc.setMap(map);

        // 페이징
        int totalCnt = dao.countTotal(sc); // 전체 게시물 개수
        int totalPage = (int)Math.ceil(totalCnt / (double)sc.getPageSize());  // 전체 페이지의 수
        if(sc.getPage() > totalPage) sc.setPage(totalPage);

        PageHandler pageHandler = new PageHandler(totalCnt, sc);


        // 숙박 업소 가져오기
        List<ProductAccommodationVO> accs = dao.selectAccommodations(sc);
        
        // 가격 데이터 가공
        accs = setAvgPrice(sc, accs);

        model.addAttribute("checkIn", checkIn);
        model.addAttribute("checkOut", checkOut);
        model.addAttribute("accs", accs);
        model.addAttribute("ph", pageHandler);

    };

    // @since 2023/03/17
    public List<ProductAccommodationVO> findAccommodation(int acc_id, String checkIn, String checkOut) throws Exception {

        Product_SearchVO sc = new Product_SearchVO();
        sc.setCheckIn(checkIn);
        sc.setCheckOut(checkOut);

        List<ProductAccommodationVO> accs = dao.selectAccommodation(acc_id, checkIn, checkOut);

        // 가격 데이터 가공
        accs = setAvgPrice(sc, accs);

        return accs;
    }

    // @since 2023/03/19
    public List<ServiceCateVO> findAllServiceCate(int acc_id){
        return dao.selectServiceCates(acc_id);
    }

    // @since 2023/03/19
    public BusinessInfoVO findBusinessInfo(String user_id){

        BusinessInfoVO vo = dao.selectBusinessInfo(user_id);

        String acc_address = "";
        String addr_detail = vo.getBis_addrDetail();
        
        // 상세주소가 있는경우 acc_address 에 상세주소까지 포함
        if(addr_detail != null && !addr_detail.equals("") && !addr_detail.equals("None")){
            acc_address = vo.getBis_addr() + " " + addr_detail;
        } else {
            acc_address = vo.getBis_addr();
        }

        vo.setBis_address(acc_address);

        return vo;
    }

    // @since 2023/03/20
    public List<ArticleDiaryVO> findAlProductlDiary(@Param("acc_id") int acc_id){
        return dao.selectProductDiaries(acc_id);
    }

    // @since 2023/03/22
    public List<ProductQnaVO> findAllProductQna(ProductDetail_SearchVO vo){
        return dao.selectProductQnas(vo);
    }

    // @since 2023/03/26
    public List<ReviewVO> findAllReviews(ProductDetail_SearchVO vo){

        List<ReviewVO> reviews = dao.selectProductReviews(vo);
        
        // 리뷰 답변 날짜 n일전, n개월전 .. 설정
        reviews = setReviewDateBF(reviews);

        return reviews;
    };

    // @since 2023/03/26
    public UserVO findBusiness(int acc_id){
        return dao.selectBusiness(acc_id);
    }

    // @since 2023/03/22
    public int getTotalProductQna(SearchCondition sc){
        return dao.getTotalProductQna(sc);
    }

    // @since 2023/03/24
    public int getTotalProductDiary(SearchCondition sc){
        return dao.getTotalProductDiary(sc);
    }

    // @since 2023/03/26
    public int getTotalProductReview(SearchCondition sc){
        return dao.getTotalProductReview(sc);
    };

    // @since 2023/03/26
    public void getTotalProductReviewReply(Model model, ProductDetail_SearchVO vo){

        List<ReviewVO> reviews = dao.getTotalProductReviewReply(vo);

        int totalReplies = 0;

        for(int i = 0; i < reviews.size(); i++){

            if(reviews.get(i).getRevi_reply() != null ){
                totalReplies ++;
            }
        }

        model.addAttribute("totalReplies", totalReplies);
    }

    // @since 2023/03/24
    public int findProductPick(int acc_id, String user_id){
        return dao.selectProductPick(acc_id, user_id);
    }

    // @since 2023/03/24
    public List<ArticleDiaryVO> findAllProductDiaries (SearchCondition sc){
        return dao.selectProductDiaries(sc);
    }

    // @since 2023/03/27
    public List<CouponVO> findAllCoupons(Map map){
        return dao.selectCoupons(map);
    }

    // @since 2023/03/29
    public List<CouponVO> findAllCouponsForReservation(Map map){

        List<CouponVO> cps = dao.selectCouponsForReservation(map);
        /* 쿠폰 할인가격 설정 */
        cps = setCouponDisprice(map, cps);
        
        return cps;
    }

    // @since 2023/03/27
    @Transactional
    public int getCoupon(Map map){

        int result = 0;

        CouponVO cp = dao.getCoupon(map);

        int limitedIssuance = cp.getCp_limitedIssuance();
        int issuedCnt = cp.getCp_IssuedCnt();
        int mcp_id = cp.getMcp_id();
        int daysAvailable = cp.getCp_daysAvailable();

        if(limitedIssuance - issuedCnt == 0){ // 쿠폰 수량이 마감일 경우
            result = 1;
        }

        if(mcp_id > 0){ // 쿠폰이 이미 발급된 경우
            result = 2;
        }

        if(result == 0){
            map.put("daysAvailable", daysAvailable);
            // 쿠폰 발급
            dao.insertMemberCoupon(map);
            // 쿠폰 사용횟수 +1
            dao.updateProductCoupon(map);
            result = 3;
        }

        log.info("result : " + result);

        return result;
    }

    // @since 2023/03/28
    public Map findRoomForReservation(Map map){

        ProductAccommodationVO room = dao.selectRoomForReservation(map);
        map = (Map) setRoomAvgPrice(map, room);

        return map;
    }

    // @since 2023/04/04
    public TermVO findTerm(int termsType_no){
        return dao.selectTerm(termsType_no);
    }

    // @since 2023/04/05
    public UserVO findUser(String user_id){
        return dao.selectUser(user_id);
    }

    // @since 2023/04/05
    public int findResNo(@Param("user_id") String user_id, @Param("res_no") int res_no){
        return dao.selectResNo(user_id,res_no);
    }

    // @since 2023/04/06
    public ReservationVO findOrderInfo(int res_no){
        return dao.selectOrderInfo(res_no);
    };

    // update

    // delete
    // @since 2023/03/24
    public int deleteProductPick(Map map){
        return dao.deleteProductPick(map);
    };


    /* 키워드의 위도 경도 값 반환 */
    public double[] getLatLng(String keyword) throws Exception {

        double LatLng[] = new double[2];
        double lat = 0.0;
        double lng = 0.0;

        keyword = URLEncoder.encode(keyword, "UTF-8");

        String apiURL = "http://dapi.kakao.com/v2/local/search/keyword.json";

        URI uri = UriComponentsBuilder
                .fromUriString(apiURL)
                .queryParam("size", 1)
                .queryParam("query", keyword)
                .build(true)
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + serviceKey);

        RequestEntity<Void> req = new RequestEntity<>(headers, HttpMethod.GET, uri);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate.exchange(req, String.class);

        // JSON 파싱
        String jsonData = result.getBody();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(jsonData);

        // 파싱한 데이터에서 필요한 정보 추출
        JsonNode documents = node.get("documents");

        // 검색 결과가 있는 경우
        if (documents.size() > 0) {
            JsonNode firstDocument = documents.get(0);

            lng = Double.parseDouble(firstDocument.get("x").asText());
            lat = Double.parseDouble(firstDocument.get("y").asText());
        }

        LatLng[0] = lat;
        LatLng[1] = lng;

        return LatLng;
    }

    // @since 2023/03/17
    public List<ProductAccommodationVO> setAvgPrice(Product_SearchVO sc, List<ProductAccommodationVO> accs) {

        LocalDate CI;
        LocalDate CO;

        if (sc.getCheckIn() == null && sc.getCheckOut() == null) {
            CI = LocalDate.now();
            CO = LocalDate.now().plusDays(1);

        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            CI = LocalDate.parse(sc.getCheckIn(), formatter);
            CO = LocalDate.parse(sc.getCheckOut(), formatter);
        }

        // 몇박인지 구하기
        long days = ChronoUnit.DAYS.between(CI, CO);

        List<LocalDate> dates = CI.datesUntil(CO).collect(Collectors.toList());

        for (LocalDate date : dates) {

            // 요일 구하기
            int day = date.getDayOfWeek().getValue();

            for (int i = 0; i < accs.size(); i++) {
                int season = accs.get(i).getAcc_season(); // 성수기, 비성수기
                int avg_price = accs.get(i).getAvg_price(); // 숙박기간의 평균 가격
                int room_price = accs.get(i).getRoom_price();

                if (season == 2) { // 성수기일때
                    if (day == 5 || day == 6) { // 주말
                        avg_price += room_price * (100 - accs.get(i).getRp_peakSeason_weekend()) / 100;
                    } else { // 주중
                        avg_price += room_price * (100 - accs.get(i).getRp_peakSeason_weekday()) / 100;
                    }
                } else if (season == 1) { // 비성수기일때
                    if (day == 5 || day == 6) { // 주말
                        avg_price += room_price * (100 - accs.get(i).getRp_offSeason_weekend()) / 100;
                    } else { // 주중
                        avg_price += room_price * (100 - accs.get(i).getRp_offSeason_weekday()) / 100;
                    }
                }

                accs.get(i).setAvg_price(avg_price);

            }
        }
        for (int i = 0; i < accs.size(); i++) {
            int avg = accs.get(i).getAvg_price() / (int) days;

            avg = avg / 10 * 10;

            accs.get(i).setAvg_price(avg);

        }

        return accs;
    }

    // @since 2023/03/28
    public Map setRoomAvgPrice(Map map, ProductAccommodationVO room){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate CI = LocalDate.parse(map.get("checkIn").toString(), formatter);
        LocalDate CO = LocalDate.parse(map.get("checkOut").toString(), formatter);


        // 몇박인지 구하기
        long days = ChronoUnit.DAYS.between(CI, CO);

        List<LocalDate> dates = CI.datesUntil(CO).collect(Collectors.toList());

        for (LocalDate date : dates) {

            // 요일 구하기
            int day = date.getDayOfWeek().getValue();

            int season = room.getAcc_season(); // 성수기, 비성수기
            int avg_price = room.getAvg_price(); // 숙박기간의 평균 가격
            int room_price = room.getRoom_price();

            if (season == 2) { // 성수기일때
                if (day == 5 || day == 6) { // 주말
                    avg_price += room_price * (100 - room.getRp_peakSeason_weekend()) / 100;
                } else { // 주중
                    avg_price += room_price * (100 - room.getRp_peakSeason_weekday()) / 100;
                }
            } else if (season == 1) { // 비성수기일때
                if (day == 5 || day == 6) { // 주말
                    avg_price += room_price * (100 - room.getRp_offSeason_weekend()) / 100;
                } else { // 주중
                    avg_price += room_price * (100 - room.getRp_offSeason_weekday()) / 100;
                }
            }
            room.setAvg_price(avg_price);
        }

        int avg = room.getAvg_price() / (int) days;
        avg = avg / 10 * 10;
        room.setAvg_price(avg);
        
        // 체크인, 체크아웃 요일 구하기

        map.put("days", days);
        map.put("room", room);
        map.put("CIday", getDay(CI.getDayOfWeek().getValue())); // 체크인 요일
        map.put("COday", getDay(CO.getDayOfWeek().getValue())); // 체크아웃 요일

        return map;
    }

    // @since 2023/03/26
    public List<ReviewVO> setReviewDateBF(List<ReviewVO> reviews){

        LocalDateTime today = LocalDateTime.now();

        for(int i = 0; i < reviews.size(); i++){

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            String revi_date = reviews.get(i).getRevi_reply_rdate();

            if (revi_date != null) {
                LocalDateTime review_date = LocalDateTime.parse(revi_date, formatter);
                long days = Duration.between(review_date, today).toDays();
                long months = Period.between(review_date.toLocalDate(), today.toLocalDate()).toTotalMonths();
                long years = Period.between(review_date.toLocalDate(), today.toLocalDate()).getYears();

                String display = "";
                if (years > 0) {
                    display = years + "년 전";
                } else if (months > 0) {
                    display = months + "개월 전";
                } else if (days >= 30) {
                    int diff = (int) (days / 30);
                    display = diff + "개월 전";
                } else if (days > 0) {
                    display = days + "일 전";
                } else {
                    display = "오늘";
                }

                reviews.get(i).setRevi_dateBF(display);

                System.out.println("리뷰 작성 날짜 : " + revi_date + ", " + display);
            }
        }

        return reviews;
    }

    // @since 2023/03/30
    public List<CouponVO> setCouponDisprice(Map map, List<CouponVO> cps){

        ProductAccommodationVO room = (ProductAccommodationVO) map.get("room");

        int avg_price = room.getAvg_price();
        int days = Integer.parseInt(String.valueOf(map.get("days")));
        int price = avg_price * days;

        for(int i = 0; i < cps.size(); i++){

            int cp_rate = cps.get(i).getCp_rate();
            int cp_price = cps.get(i).getCp_price();
            int min = cps.get(i).getCp_minimum();
            int max = cps.get(i).getCp_maximum();
            int disprice = 0;

            if(min <= price){ // 숙박금액이 최소주문 금액보다 크면

                if(cp_rate > 0 && cp_price ==0){ // 할인율 쿠폰일때

                    if(max < price) { // 최대주문 금액보다 큰 경우 최대주문금액 기준으로 n%할인
                        disprice = max * cp_rate / 100;
                    }else{
                        disprice = price * cp_rate / 100;
                    }

                }else if(cp_rate == 0 && cp_price > 0){ // 할인 금액 쿠폰일 때
                    disprice = cp_price;
                }
            }

            cps.get(i).setCp_dis_price(disprice);
        }

        return cps;
    }

    public String getDay(int d){

        String day ="";
        switch(d) {
            case 1 :
                day = "월";
                break;
            case 2 :
                day = "화";
                break;
            case 3 :
                day = "수";
                break;
            case 4 :
                day = "목";
                break;
            case 5 :
                day = "금";
                break;
            case 6 :
                day = "토";
                break;
            case 7 :
                day = "일";
                break;
        }
        return day;
    }

    // @since 2023/03/31
    public OrderInfoVO dataValidation(OrderInfoVO vo, HttpSession session){

        int price = 0; // 할인 전 가격
        int status = 0; // 데이터 검증 상태변수
        int point = 0; // 결제 때 사용한 포인트
        int user_point = vo.getUser_point(); // 유저가 실제로 보유한 포인트
        int cp_disprice = 0; // 쿠폰으로 할인받은 금액
        int po_disprice = 0; // 포인트로 할인받은 금액
        int disprice = 0; // 총 할인받은 금액
        int amount = vo.getAmount(); // 실제 결제한 금액
        String cp_id = vo.getCp_id(); // 사용한 쿠폰 아이디

        /* 결제한 숙박 관련한 map */
        Map map = (Map) session.getAttribute("resultmap");
        
        
        /* 쿠폰 검증 */
        List<CouponVO> cps = (List<CouponVO>) map.get("cps");
        
        if(cp_id != null && cp_id !=""){ // 쿠폰 사용내역이 있으면

            int stat = 0;

            for(int i = 0; i < cps.size(); i++){
                if(cps.get(i).getCp_id() == Integer.parseInt(cp_id)){ // 내가 보유한 쿠폰 아이디와 결제로 넘어온 쿠폰 아이디 비교
                    vo.setMcp_id(cps.get(i).getMcp_id()); // 멤버 쿠폰 id 값 저장
                    cp_disprice = cps.get(i).getCp_dis_price(); // 쿠폰 할인 가격
                    stat = 1;
                }
            }

            if(stat == 0){ // 쿠폰 아이디가 존재하지 않으면
                vo.setStatus(0);
                return vo;
            }
        }

        /* 포인트 검증 */
        if(vo.getPoint() != null && vo.getPoint() !=""){ // 포인트 사용내역이 있으면
            point = Integer.parseInt(vo.getPoint());
            
            if(user_point < point){ // 유저가 실제 보유한 포인트보다 사용한 포인트가 많을 경우
                vo.setStatus(0);
                return vo;
            }else {
                po_disprice = point;
            }
        }

        /* 가격 검증 */
        ProductAccommodationVO room = (ProductAccommodationVO) map.get("room");

        int avg_price = room.getAvg_price();
        int days = Integer.parseInt(String.valueOf(map.get("days")));
        price = avg_price * days;
        disprice = cp_disprice + po_disprice;

        if(amount != price - disprice) { // 주문금액과 실제 금액이 일치하지 않는 경우
            vo.setStatus(0);
            return vo;
        }

        // 결제 정보 vo에 저장
        vo.setPrice(price);
        vo.setAcc_id(room.getAcc_id());
        vo.setRoom_id((room.getRoom_id()));
        vo.setRoom_name((room.getRoom_name()));
        vo.setAcc_name((room.getAcc_name()));
        vo.setDays(days);
        vo.setCheckIn((String) map.get("checkIn"));
        vo.setCheckOut((String) map.get("checkOut"));
        vo.setCheckInTime(room.getCheckInTime());
        vo.setCheckOutTime(room.getCheckOutTime());
        vo.setCp_disprice(cp_disprice);
        vo.setDisprice(disprice);
        vo.setStatus(1);

        return vo;
    }

    // @since 2023/03/31
    @Transactional
    public void reservation(OrderInfoVO vo) throws Exception {

        // 예약 내역 등록
        dao.insertProductReservation(vo);

        // 예약 객실 등록
        dao.insetProductReservedRoom(vo);
        
        // 쿠폰 사용내역이 있으면
        if(vo.getCp_id() != null && vo.getCp_id() != ""){
            // 쿠폰 업데이트
            dao.updateMemberCoupon(vo);
            
            // 쿠폰 로그 등록
            dao.insertMemberCouponLog(vo);

        }
        

        // 포인트 사용내역이 있으면
        if(vo.getPoint() != null && vo.getPoint() !="") {
            // 포인트 로그 등록
            dao.insertMemberPointLog(vo);
            // 유저 정보에 포인트 업데이트
            dao.updateMemberUserInfo(vo);
        }



    }
}
