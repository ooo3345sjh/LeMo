package kr.co.Lemo.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import kr.co.Lemo.domain.ProductAccommodationVO;
import kr.co.Lemo.domain.ProductSearchVO;
import kr.co.Lemo.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.List;

/**
 * @since 2023/03/08
 * @author 이해빈
 * @apiNote 상품 controller
 */

@Slf4j
@Controller
@PropertySource(value = "classpath:title.properties", encoding = "UTF-8")
@RequestMapping("product/")
public class ProductController {

    @Autowired
    private Environment environment;
    private String group = "title.product";

    @Autowired
    private ProductService service;

    /**
     * @since 2023/03/08
     * @param vo (상품 검색 조건 vo)
     */
    @GetMapping("list")
    public String list(Model model, ProductSearchVO vo) throws Exception {

        String keyword = vo.getKeyword();
        double lng = vo.getLng();
        double lat = vo.getLat();

        log.info("keyword : " + keyword);
        log.info("lat : " + lat);
        log.info("lng : " + lng);

        String apiURL = "";
        String serviceKey = "";
        //String resultType = "json";

        // 키워드 검색일 경우
        String jsonData = null;
        if (keyword != null) {

            try {
                keyword = URLEncoder.encode(keyword, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                // 예외처리
            }

            apiURL = "http://dapi.kakao.com/v2/local/search/keyword.json";
            serviceKey = "b1e7614910a4b6cb075115cef338f033";

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

            log.info("result : " + result);

            // JSON 파싱
            jsonData = result.getBody();

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

            log.info("lng : " + lng);
            log.info("lat : " + lat);
        }

        // 장소 결과가 없는 경우 위도 경도 서울을 기준으로 세팅
        if (lat == 0.0 & lng == 0.0) {
            lat = 33.450701;
            lng = 126.570667;
        }

        log.info("최종 경도 : " + lng);
        log.info("최종 위도 : " + lat);

        vo.setLat(lat);
        vo.setLng(lng);

        List<ProductAccommodationVO> accs = service.selectAccommodations(vo);

        log.info("숙소 리스트 :" + accs);

//        }else { // 특정 장소로 검색했을 경우 (구글)
//            apiURL = "http://dapi.kakao.com/v2/local/search/keyword.json";
//            serviceKey = "AIzaSyBntl8pNwtscVPGQraeyZVdAJ9AaH9bWBw";
//        }

        log.info(String.valueOf(accs.size()));

        String json = new Gson().toJson(accs);

        log.info(json);


        model.addAttribute("lng", lng);
        model.addAttribute("lat", lat);
        model.addAttribute("accs", accs);
        model.addAttribute("json", json);

        model.addAttribute("title", environment.getProperty(group));

        return "product/list";
    }

    @GetMapping("view")
    public String view() throws Exception{
        return "product/view";
    }

    @GetMapping("reservation")
    public String reservation() throws Exception{
        return "product/reservation";
    }

    @GetMapping("result")
    public String result() throws Exception{
        return "product/result";
    }
}