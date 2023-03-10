package kr.co.Lemo.service;

import kr.co.Lemo.domain.ArticleDiaryVO;
import kr.co.Lemo.domain.DiarySpotVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @since 2023/03/10
 * @author 박종협
 * @apiNote 마이페이지 service
 */
@Slf4j
@Service
@Transactional(rollbackOn = Exception.class)
public class MyService {

    // @since 2023/03/10
    public void diary_rsave(
            ArticleDiaryVO adVO,
            DiarySpotVO dsVO
    ) {
        List<MultipartFile> thumbs = adVO.getThumb();
        // ArticleDiaryVO thumb가 MuiltipartFile이면 form 전송시 여러개가 전송이 안됨.
        // 따라서 List<Muliti~> 로 정하고 불러옴.
        // 이후 diary_article에 집어 넣기 위해서 파일의 기존 이름을 "/" 로 구분하고 한줄로 이루어진 string으로 만들었음
        String oriName = thumbs.get(0).getOriginalFilename();

        if(thumbs.size() > 1){
            for(int i= 1; i<thumbs.size(); i++){
                oriName += "/" + thumbs.get(i).getOriginalFilename();
            }
        }

        String[] content = dsVO.getSpot_content().split(",");
        String[] lat = dsVO.getSpot_lattitude().split(",");
        String[] lng = dsVO.getSpot_longtitude().split(",");
        String[] images = oriName.split("/");
        for(int i = 0; i<images.length; i++){
            log.debug("#"+i);
            log.debug(content[i]);
            log.debug(lat[i]);
            log.debug(lng[i]);
            log.debug(images[i]);
        }


        adVO.setArti_thumb(oriName);
        adVO.setRes_no(123); // 임시 생성



    }

    public void fileUpload() {

    }

}
