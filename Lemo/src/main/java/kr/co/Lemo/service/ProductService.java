package kr.co.Lemo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.Lemo.dao.ProductDAO;
import kr.co.Lemo.domain.BusinessVO;
import kr.co.Lemo.domain.ProductAccommodationVO;
import kr.co.Lemo.domain.ServiceCateVO;
import kr.co.Lemo.domain.search.Product_SearchVO;
import kr.co.Lemo.utils.PageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.time.LocalDate;
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

    // insert

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
    public BusinessVO findBusinessInfo(String user_id){
        return dao.selectBusinessInfo(user_id);
    }

    // update

    // delete


    /* 키워드의 위도 경도 값 반환 */
    public double[] getLatLng(String keyword) throws Exception {

        double LatLng[] = new double[2];
        double lat = 0.0;
        double lng = 0.0;

        keyword = URLEncoder.encode(keyword, "UTF-8");

        String apiURL = "http://dapi.kakao.com/v2/local/search/keyword.json";
        String serviceKey = "b1e7614910a4b6cb075115cef338f033";

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

        if(sc.getCheckIn() == null && sc.getCheckOut() == null) {
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

        for(LocalDate date: dates) {

            // 요일 구하기
            int day = date.getDayOfWeek().getValue();

            for(int i = 0; i < accs.size(); i ++) {
                int season = accs.get(i).getAcc_season(); // 성수기, 비성수기
                int avg_price = accs.get(i).getAvg_price(); // 숙박기간의 평균 가격
                int room_price = accs.get(i).getRoom_price();


                if(season == 1) { // 성수기일때
                    if(day == 5 || day ==6) { // 주말
                        avg_price += room_price * (100 - accs.get(i).getRp_peakSeason_weekend())/100;
                    }else { // 주중
                        avg_price += room_price * (100 - accs.get(i).getRp_peakSeason_weekday())/100;
                    }
                }else if(season == 2){ // 비성수기일때
                    if(day == 5 || day ==6) { // 주말
                        avg_price += room_price * (100 - accs.get(i).getRp_offSeason_weekend())/100;
                    }else { // 주중
                        avg_price += room_price * (100 - accs.get(i).getRp_offSeason_weekday())/100;
                    }
                }

                accs.get(i).setAvg_price(avg_price);

            }
        }
        for(int i = 0; i < accs.size(); i ++) {
            int avg = accs.get(i).getAvg_price() / (int)days;

            avg = avg / 10 * 10;

            accs.get(i).setAvg_price(avg);

            log.info("최종 가격 : " + avg);
        }

        return accs;
    }

}
