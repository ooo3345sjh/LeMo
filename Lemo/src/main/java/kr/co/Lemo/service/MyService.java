package kr.co.Lemo.service;

import kr.co.Lemo.dao.MyDAO;
import kr.co.Lemo.domain.ArticleDiaryVO;
import kr.co.Lemo.domain.DiarySpotVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * @since 2023/03/10
 * @author 박종협
 * @apiNote 마이페이지 Service
 */
@Slf4j
@Service
@Transactional(rollbackOn = Exception.class)
public class MyService {

    // @since 2023/03/11
    @Autowired
    private MyDAO dao;

    // @since 2023/03/10
    public void diary_rsave(
            Map<String, Object> param,
            List<MultipartFile> fileList,
            HttpServletRequest req
    ) {
        log.info(""+param.get("diaryContent"));
        log.info(""+param.get("diaryLat"));
        log.info(""+param.get("diaryLng"));

        String oriName = fileList.get(0).getOriginalFilename();
        if(fileList.size() > 1) {
            for (int i = 1; i<fileList.size(); i++) {
                oriName += "/" + fileList.get(i).getOriginalFilename();
            }
        }

        ArticleDiaryVO diaryVO = ArticleDiaryVO.builder()
                                .res_no(123123)
                                .arti_title((String) param.get("diaryTitle"))
                                .arti_thumb(oriName)
                                .arti_regip(req.getRemoteAddr())
                                .arti_start((String) param.get("diaryStart"))
                                .arti_end((String) param.get("diaryEnd"))
                                .build();

        log.info("" + diaryVO);

        dao.insertDiaryArticle(diaryVO);
        int arti_no = diaryVO.getArti_no();
        log.info("" + arti_no);

        String[] content = ((String) param.get("diaryContent")).split("/");
        String[] lat     = ((String) param.get("diaryLat")).split("/");
        String[] lng     = ((String) param.get("diaryLng")).split("/");
        String[] images  = oriName.split("/");
        for(int i = 0; i<images.length; i++){
            log.debug("#"+i);
            log.debug(content[i]);
            log.debug(lat[i]);
            log.debug(lng[i]);
            log.debug(images[i]);
            DiarySpotVO spotVO = DiarySpotVO.builder()
                                .spot_content(content[i])
                                .spot_lattitude(lat[i])
                                .spot_longtitude(lng[i])
                                .spot_thumb(images[i]).build();
        }
    }

    public void fileUpload() {

    }

}
