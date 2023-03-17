package kr.co.Lemo.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.Lemo.dao.UserDAO;
import kr.co.Lemo.domain.UserVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.net.URI;

/**
 * @since 2023/03/16
 * @author 서정현
 * @apiNote UserService
 */

@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class UserService {

    private UserDAO userDAO;
    private PasswordEncoder passwordEncoder;

    // @since 2023/03/16
    public int countByEmail(String email) throws Exception{
        return userDAO.countByEmail(email);
    }

    // @since 2023/03/16
    public String getNick() throws JsonProcessingException {
        String apiURL = "https://nickname.hwanmoo.kr/";

        URI uri = UriComponentsBuilder
                .fromUriString(apiURL)
                .queryParam("format", "json")
                .queryParam("max_length", 8)
                .build()
                .toUri();

        RequestEntity<Void> req = new RequestEntity<>(HttpMethod.GET, uri);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate.exchange(req, String.class);

        // JSON 파싱
        String jsonData = result.getBody();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(jsonData);
        JsonNode words = node.get("words");

        String nick = null;
        if(words != null){
            nick = words.get(0).asText();
        } else {
            nick = "오류";
        }
        System.out.println("nick = " + nick);
        return nick.replaceAll(" ", "");
    }

    // @since 2023/03/16
    public int countByNick(String nick) throws Exception {
        return userDAO.countByNick(nick);
    }

    public int saveUser(UserVO user) throws Exception {
        user.setPass(passwordEncoder.encode(user.getPass()));
        log.debug(user.toString());
        int result = 0;
//        result = userDAO.saveUserInfo(user);
//        result = userDAO.saveHomeUser(user.getUser_id(), user.getPass());
        return result;
    }

}
