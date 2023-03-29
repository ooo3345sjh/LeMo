package kr.co.Lemo.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.Lemo.dao.MainDAO;
import kr.co.Lemo.dao.UserDAO;
import kr.co.Lemo.domain.ArticleDiaryVO;
import kr.co.Lemo.domain.BusinessInfoVO;
import kr.co.Lemo.domain.ProductAccommodationVO;
import kr.co.Lemo.domain.UserVO;
import kr.co.Lemo.entity.BusinessInfoEntity;
import kr.co.Lemo.entity.SocialEntity;
import kr.co.Lemo.entity.UserEntity;
import kr.co.Lemo.entity.UserInfoEntity;
import kr.co.Lemo.repository.SocialRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @since 2023/03/29
 * @author 서정현
 * @apiNote MainService
 */

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class MainService {

    private final MainDAO mainDAO;

    // @since 2023/03/29
    public List<Object> findRevisit() throws Exception {
        LocalDate now = LocalDate.now();
        return mainDAO.selectRevisit().stream().map(p -> {
            if(p.getAcc_season()==1){
                int now_price = 0;
                if(now.getDayOfWeek().getValue() == 5 || now.getDayOfWeek().getValue() == 6){
                    now_price = (int)(p.getRoom_price() * (1-(p.getRp_offSeason_weekend()/100.0)));
                } else {
                    now_price = (int)(p.getRoom_price() * (1-(p.getRp_offSeason_weekday()/100.0)));
                }
                p.setNow_price(now_price);
            }

            else {
                int now_price = 0;

                if(now.getDayOfWeek().getValue() == 5 || now.getDayOfWeek().getValue() == 6){
                    now_price = (int)(p.getRoom_price() * (1-(p.getRp_peakSeason_weekend()/100.0)));
                } else {
                    now_price = (int)(p.getRoom_price() * (1-(p.getRp_peakSeason_weekday()/100.0)));
                }
                p.setNow_price(now_price);
            }
            return p;
        }).collect(Collectors.toList());
    }

    // @since 2023/03/14
    public List findBest() throws Exception {
        LocalDate now = LocalDate.now();
        return mainDAO.selectBest().stream().map(p -> {
            if(p.getAcc_season()==1){
                int now_price = 0;

                if(now.getDayOfWeek().getValue() == 5 || now.getDayOfWeek().getValue() == 6){
                    now_price = (int)(p.getRoom_price() * (1-(p.getRp_offSeason_weekend()/100.0)));
                } else {
                    now_price = (int)(p.getRoom_price() * (1-(p.getRp_offSeason_weekday()/100.0)));
                }
                p.setNow_price(now_price);
            }

            else {
                int now_price = 0;

                if(now.getDayOfWeek().getValue() == 5 || now.getDayOfWeek().getValue() == 6){
                    now_price = (int)(p.getRoom_price() * (1-(p.getRp_peakSeason_weekend()/100.0)));
                } else {
                    now_price = (int)(p.getRoom_price() * (1-(p.getRp_peakSeason_weekday()/100.0)));
                }
                p.setNow_price(now_price);
            }
            return p;
        }).collect(Collectors.toList());
    }

    public List<ArticleDiaryVO> findBestDiary() throws Exception {
        return mainDAO.selectBestDiary();
    }

    // @since 2023/03/14
    public void findMain(Map map) throws Exception {
        map.put("revisit", findRevisit());
        map.put("best", findBest());
        map.put("diary", findBestDiary());
    }


}
