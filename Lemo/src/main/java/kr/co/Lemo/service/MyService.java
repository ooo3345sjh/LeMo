package kr.co.Lemo.service;

import kr.co.Lemo.dao.MyDAO;
import kr.co.Lemo.domain.ArticleDiaryVO;
import kr.co.Lemo.domain.DiarySpotVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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

    // @since 2023/03/13
    public List<ArticleDiaryVO> findDiaryArticle(String uid) {
        List<ArticleDiaryVO> diaryVO = dao.selectDiaryArticle(uid);

        List<DiarySpotVO> spotVO = dao.selectDiary(uid);
        Map<Integer, List<DiarySpotVO>> map = spotVO.stream().collect(Collectors.groupingBy(DiarySpotVO::getArti_no));

        for(ArticleDiaryVO vo : diaryVO) {
            vo.setSpotVO(map.get(vo.getArti_no()));
        }

        return diaryVO;
    }

    public List<DiarySpotVO> findDiarySpotPosition(String uid) {
        List<DiarySpotVO> spotVO = dao.selectDiary(uid);

        return spotVO;
    }

    // @since 2023/03/13

    // @since 2023/03/10
    public void diary_rsave(
            Map<String, Object> param,
            List<MultipartFile> fileList,
            HttpServletRequest req,
            String uid
    ) {
        // 파일 이름 수정 -> 이걸 먼저하는 이유는
        // 파일 업로드 시 경로는 img/diary/arti_no/파일 이름 이고
        // arti_no를 얻기 위해선 먼저 diary_article에 데이터를 입력해줘야됨
        // 따라서 먼저 이름을 수정함
        // 만약 파일 경로를 arti_no로 안한다면 fileUpload 함수에 포함시켜 한번에 처리할 수 있음
        List<String> fileRenames = new ArrayList<>();

        for(MultipartFile mf : fileList){
            String oriName = mf.getOriginalFilename();
            String ext = oriName.substring(oriName.indexOf("."));
            String newName = UUID.randomUUID().toString() + ext;
            fileRenames.add(newName);
        }

        String newName = String.join("/", fileRenames);

        // diary_article 입력 데이터 분류
        ArticleDiaryVO diaryVO = ArticleDiaryVO.builder()
                                .res_no(123123)
                                .user_id(uid)
                                .arti_title((String) param.get("diaryTitle"))
                                .arti_thumb(newName)
                                .arti_regip(req.getRemoteAddr())
                                .arti_start((String) param.get("diaryStart"))
                                .arti_end((String) param.get("diaryEnd"))
                                .build();
        
        // diary_article 테이블 입력
        dao.insertDiaryArticle(diaryVO);

        int arti_no = diaryVO.getArti_no();
        
        // 파일 업로드
        fileUpload(fileList, fileRenames, arti_no);

        // diary_spot 입력 데이터 분류
        String[] title   = ((String) param.get("diarySpotTitle")).split("/");
        String[] content = ((String) param.get("diarySpotContent")).split("/");
        String[] lat     = ((String) param.get("diaryLat")).split("/");
        String[] lng     = ((String) param.get("diaryLng")).split("/");
        String[] images  = newName.split("/");
        for(int i = 0; i<images.length; i++){
            DiarySpotVO spotVO = DiarySpotVO.builder()
                                .arti_no(arti_no)
                                .spot_longtitude(Double.parseDouble(lng[i]))
                                .spot_lattitude(Double.parseDouble(lat[i]))
                                .spot_title(title[i])
                                .spot_content(content[i])
                                .spot_thumb(images[i])
                                .province_name("test")
                                .spot_addr("test").build();

            // diary_spot 테이블 입력
            dao.insertDiarySpot(spotVO);
        }
    }







    // 기능

    // @since 2023/03/12
    //@Value("${spring.servlet.multipart.location}")
    // private String uploadPath;

    // @since 2023/03/12
    public void fileUpload(List<MultipartFile> fileList, List<String> fileRenames, int arti_no) {

        //String path = new File(uploadPath+"diary/"+arti_no).getAbsolutePath();

        String path = new File("C:/Users/java2/Desktop/Workspace/LeMo/Lemo/img/diary/" + arti_no).getAbsolutePath();

        log.info(path);
        // 저장 폴더가 없다면 생성
        File checkFolder = new File(path);
        if(!checkFolder.exists()){
            try {
                Files.createDirectories(checkFolder.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int count = 0;
        for(MultipartFile mf : fileList) {
            try {
                mf.transferTo(new File(path, fileRenames.get(count)));
                count++;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
