package kr.co.Lemo.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

/**
 * @since 2023/03/07
 * @author 박종협
 * @apiNote 상품 controller
 */

@Slf4j
@Controller
@RequestMapping("product/")
public class ProductController {

    // 2023/03/08
    @GetMapping("list")
    public String list(Model model, @RequestParam(value="keyword", required = false) String keyword,
                                    @RequestParam(value="lat", required = false, defaultValue = "0") double lat,
                                    @RequestParam(value="lng", required = false, defaultValue = "0") double lng) throws Exception {

        log.info("keyword : " + keyword );
        log.info("lat : " + lat );
        log.info("lng : " + lng );

        String apiURL = "";
        String serviceKey = "";
        //String resultType = "json";

        // 키워드 검색일 경우
        if(keyword != "" || keyword != null) {
            try {
                keyword = URLEncoder.encode(keyword, "UTF-8");
            }catch(UnsupportedEncodingException e){
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
            String jsonData = result.getBody();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(jsonData);

            // 파싱한 데이터에서 필요한 정보 추출
            JsonNode documents = node.get("documents");
            JsonNode firstDocument = documents.get(0);

            lng = Double.parseDouble(firstDocument.get("x").asText());
            lat = Double.parseDouble(firstDocument.get("y").asText());


            log.info("lng : " + lng);
            log.info("lat : " + lat);


        }else { // 특정 장소로 검색했을 경우 (구글)
            apiURL = "http://dapi.kakao.com/v2/local/search/keyword.json";
            serviceKey = "AIzaSyBntl8pNwtscVPGQraeyZVdAJ9AaH9bWBw";

        }
        
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