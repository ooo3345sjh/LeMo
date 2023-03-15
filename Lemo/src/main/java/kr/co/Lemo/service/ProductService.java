package kr.co.Lemo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.Lemo.dao.ProductDAO;
import kr.co.Lemo.domain.ProductAccommodationVO;
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
import java.util.List;
import java.util.Map;

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

    // @since 2022/03/09
    public void findAllAccommodations(Model model, Map map, Product_SearchVO sc) throws Exception {

        log.info("maxPrice : " + sc.getMaxPrice());
        log.info("minPrice : " + sc.getMinPrice());
        log.info("지도 반경 좌표: " + sc.getB());


        String keyword = sc.getKeyword();
        double lng = sc.getLng();
        double lat = sc.getLat();

        log.info("keyword : " + keyword);
        log.info("lat : " + lat);
        log.info("lng : " + lng);

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

        log.info("최종 경도 : " + lng);
        log.info("최종 위도 : " + lat);

//        vo.setLat(lat);
//        vo.setLng(lng);

        // SearchCondition

        if(sc.getSort() == null){
            sc.setSort("review");
            map.put("sort", "review");
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

        log.info("total : " + totalCnt);

        // 숙박 업소 가져오기
        List<ProductAccommodationVO> accs = dao.selectAccommodations(sc);

        //log.info("숙소 리스트 :" + accs);

        model.addAttribute("accs", accs);
        model.addAttribute("ph", pageHandler);

    };


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
}
