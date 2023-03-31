package kr.co.Lemo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

/**
 * @since 2023/03/31
 * @author 이해빈
 * @apiNote 결제관련 service
 */
@Slf4j
@Service
public class PaymentService {

    @Value("${import.key}")
    private String impKey;

    @Value("${import.secretkey}")
    private String impSecretKey;

    @Data
    private class Response{
        private PaymentInfo response;
    }

    @Data
    private class PaymentInfo{
        private int amount;
    }


    public String getToken() throws IOException {

        String apiURL = "https://api.iamport.kr/users/getToken";

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("imp_key", impKey);
        map.add("imp_secret", impSecretKey);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        URI uri = UriComponentsBuilder
                .fromUriString(apiURL)
                .build(true)
                .toUri();

        RequestEntity<MultiValueMap<String, String>> req = new RequestEntity<>(map, headers, HttpMethod.POST, uri);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate.exchange(req, String.class);

        // Json 파싱
        String jsonData = result.getBody();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(jsonData);

        // 파싱한 데이터에서 필요한 정보 추출
        JsonNode response = node.get("response");

        String token = String.valueOf(response.get("access_token"));
        token = token.substring(1, token.length() - 1);

        return token;
    }

    public int paymentInfo(String token, String imp_uid) throws Exception {

        String apiURL = "https://api.iamport.kr/payments/" + imp_uid;

        URI uri = UriComponentsBuilder
                .fromUriString(apiURL)
                .queryParam("_token", token)
                .build(true)
                .toUri();


        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        RequestEntity<Void> req = new RequestEntity<>(headers, HttpMethod.GET, uri);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate.exchange(req, String.class);

        // Json 파싱
        String jsonData = result.getBody();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(jsonData);

        // 파싱한 데이터에서 필요한 정보 추출
        JsonNode response = node.get("response");
        int amount = response.get("amount").asInt();;

        log.info("amount : " + amount);

        return amount;
    }

    public int paymentCancel(String token, String imp_uid) throws Exception {

        String apiURL = "https://api.iamport.kr/payments/cancel";

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("imp_uid", imp_uid);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        URI uri = UriComponentsBuilder
                .fromUriString(apiURL)
                .build(true)
                .toUri();

        RequestEntity<MultiValueMap<String, String>> req = new RequestEntity<>(map, headers, HttpMethod.POST, uri);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate.exchange(req, String.class);

        // Json 파싱
        String jsonData = result.getBody();

        log.info("취소 jsonData :" + jsonData);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(jsonData);

        // 파싱한 데이터에서 필요한 정보 추출
        int code = node.get("code").asInt();

        log.info("status :" + code);

        return code;
    }

}
