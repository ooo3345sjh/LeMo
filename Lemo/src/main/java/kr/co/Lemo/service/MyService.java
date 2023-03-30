package kr.co.Lemo.service;

import kr.co.Lemo.dao.MyDAO;
import kr.co.Lemo.domain.*;
import kr.co.Lemo.utils.SearchCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
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
    public Map<Integer, List<DiarySpotVO>> findDiaryArticle(String user_id) {

        List<DiarySpotVO> spotVO = dao.selectDiary(user_id);
        Map<Integer, List<DiarySpotVO>> map = spotVO.stream().collect(Collectors.groupingBy(DiarySpotVO::getArti_no));

        return map;
    }

    // @since 2023/03/10
    public int diary_rsave(
            Map<String, Object> param,
            List<MultipartFile> fileList,
            HttpServletRequest req,
            String user_id
    ) {
        // 파일 이름 수정 -> 이걸 먼저하는 이유는
        // 파일 업로드 시 경로는 img/diary/arti_no/파일 이름 이고
        // arti_no를 얻기 위해선 먼저 diary_article에 데이터를 입력해줘야됨
        // 따라서 먼저 이름을 수정함
        // 만약 파일 경로를 arti_no로 안한다면 fileUpload 함수에 포함시켜 한번에 처리할 수 있음
        // diary_spot 입력 데이터 분류

        int finalResult = 0;
        // 데이터 검증(필수로 입력되어야하는 부분이라 길이가 모두 똑같아야함)
        String[] title   = ((String) param.get("diarySpotTitle")).split("/");
        String[] content = ((String) param.get("diarySpotContent")).split("/");
        String[] lat     = ((String) param.get("diaryLat")).split("/");
        String[] lng     = ((String) param.get("diaryLng")).split("/");

        if(title.length != content.length && title.length != lat.length && title.length != lng.length
            && content.length != lat.length && content.length != lat.length
            && lat.length != lng.length)
        {
            finalResult = 2;
            return finalResult;
        }
        
        
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
                                .res_no( Integer.parseInt(String.valueOf(param.get("res_no"))) )
                                .user_id(user_id)
                                .arti_title((String) param.get("diaryTitle"))
                                .arti_thumb(newName)
                                .arti_regip(req.getRemoteAddr())
                                .arti_start((String) param.get("diaryStart"))
                                .arti_end((String) param.get("diaryEnd"))
                                .build();
        
        // diary_article 테이블 입력
        int result = dao.insertDiaryArticle(diaryVO);

        int arti_no = diaryVO.getArti_no();

        ProductAccommodationVO accommo = dao.selectProvinceAddr(Integer.parseInt(String.valueOf(param.get("res_no"))));

        // 파일 업로드
        if(result == 1) {
            finalResult = fileUpload(fileList, fileRenames, arti_no);
        }else {

        }
        
        String[] images  = newName.split("/");
        for(int i = 0; i<images.length; i++){
            DiarySpotVO spotVO = DiarySpotVO.builder()
                                .arti_no(arti_no)
                                .spot_longtitude(Double.parseDouble(lng[i]))
                                .spot_lattitude(Double.parseDouble(lat[i]))
                                .spot_title(title[i])
                                .spot_content(content[i])
                                .spot_thumb(images[i])
                                .province_name(accommo.getProvince_name())
                                .spot_addr(accommo.getAcc_addr()).build();

            // diary_spot 테이블 입력
            dao.insertDiarySpot(spotVO);
        }

        return finalResult;
    }

    // @since 2023/03/24
    public List<ProductAccommodationVO> findPicks(SearchCondition sc) {
        return dao.selectPicks(sc);
    }

    // @since 2023/03/27
    public List<CouponVO> findMemberCoupons(String user_id) {

        for(CouponVO cpVO : dao.selectMemberCoupons(user_id)) {
            LocalDate date = LocalDate.parse(cpVO.getMcp_end(), DateTimeFormatter.ISO_DATE);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
            String formatedNow = date.format(formatter);
            cpVO.setMcp_end(formatedNow);
        }

        return dao.selectMemberCoupons(user_id);
    }
    public List<CouponVO> findProductCoupons(String user_id) {

        for(CouponVO cpVO : dao.selectProductCoupons(user_id)) {
            LocalDate date = LocalDate.parse(cpVO.getCp_end(), DateTimeFormatter.ISO_DATE);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
            String formatedNow = date.format(formatter);
            cpVO.setCp_end(formatedNow);
        }

        return dao.selectProductCoupons(user_id);
    }
    public int rsaveCoupon(CouponVO coupon) {

        int result = dao.insertCoupon(coupon);

        if(result == 1) {
            result = dao.updateProductCoupon(coupon);
        }

        return result;
    }

    public CouponVO findProductCouponCnt(CouponVO coupon) {
        return dao.selectProductCouponCnt(coupon);
    }
    public List<ReservationVO> findReservations(SearchCondition sc) {

        for(ReservationVO resVO : dao.selectReservations(sc)) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime date = LocalDateTime.parse(resVO.getRes_date(), formatter);
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            resVO.setRes_date(resVO.getRes_date().substring(0,10)+" ("+dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.KOREAN)+")");

            LocalDate checkInDate = LocalDate.parse(resVO.getRes_checkIn(), DateTimeFormatter.ISO_DATE);
            DayOfWeek checkInWeek = checkInDate.getDayOfWeek();
            resVO.setRes_checkIn(resVO.getRes_checkIn()+" ("+checkInWeek.getDisplayName(TextStyle.NARROW, Locale.KOREAN)+")");

            LocalDate checkOutDate = LocalDate.parse(resVO.getRes_checkOut(), DateTimeFormatter.ISO_DATE);
            DayOfWeek checkOutWeek = checkOutDate.getDayOfWeek();
            resVO.setRes_checkOut(resVO.getRes_checkOut()+" ("+checkOutWeek.getDisplayName(TextStyle.NARROW, Locale.KOREAN)+")");

            Period period = Period.between(checkInDate, checkOutDate);
            resVO.setNight(period.getDays());
        }

        return dao.selectReservations(sc);
    }
    public List<PointVO> findPoints(SearchCondition sc) {
            return dao.selectPoints(sc);
        }
    public List<ReservationVO> findReviews(SearchCondition sc) {
        return dao.selectReviews(sc);
    }
    public int findTotalReservations(SearchCondition sc) {
            return dao.selectTotalReservations(sc);
        }
    public int findTotalPoints(SearchCondition sc) {
            return dao.selectTotalPoints(sc);
        }
    public int findTotalPicks(SearchCondition sc) {
        return dao.selectTotalPicks(sc);
    }

    // @since 2023/03/28
    public int findTotalReviews(SearchCondition sc) {
        return dao.selectTotalReviews(sc);
    }
    public ReviewVO findReview(int res_no) {
        ReviewVO reVO = dao.selectReview(res_no);
        String acc_thumb = reVO.getRevi_thumb();

        List<String> thumbs = Arrays.asList(acc_thumb.split("/"));
        reVO.setThumbs(thumbs);

        return dao.selectReview(res_no);
    }
    public ReviewVO findReviewAccommodation(int res_no) {
        return dao.selectReviewAccommodation(res_no);
    }
    public void rsavsReview(MultipartHttpServletRequest request, Map<String, Object> param) {
        Map<String, MultipartFile> fileMap = request.getFileMap();

        List<String> newNames = new ArrayList<>();
        List<String> oriNames = new ArrayList<>();

        for (MultipartFile multipartFile : fileMap.values()) {
            //파일 이름 추출 (확장자 제거)
            String fName = multipartFile.getOriginalFilename();
            String ext = fName.substring(fName.indexOf("."));
            String newName = UUID.randomUUID() + ext;
            newNames.add(newName);
            oriNames.add(fName);
        }

        log.debug("newNames : " + newNames);
        String dbNames = String.join("/", newNames);
        param.put("revi_thumb", dbNames);

        int insertResult = dao.insertReview(param);

        if(insertResult == 1) {
            reviewUpload(fileMap, param, newNames);
        }

    }

    // @since 2023/03/29
    public ReservationVO findReservation(int res_no) {

        ReservationVO resVO = dao.selectReservation(res_no);

        LocalDate checkInDate  = LocalDate.parse(resVO.getRes_checkIn(), DateTimeFormatter.ISO_DATE);
        LocalDate checkOutDate = LocalDate.parse(resVO.getRes_checkOut(), DateTimeFormatter.ISO_DATE);

        Period period = Period.between(checkInDate, checkOutDate);
        resVO.setNight(period.getDays());

        return dao.selectReservation(res_no);
    }

    public int removeUpdateReservation(int res_no) {
        int result = 0;

        result = dao.deleteReservation(res_no);

        if(result == 1) { result = dao.updateReservationState(res_no); }

        return result;
    }

    public int findCheckReview(int res_no) {
        return dao.selectCheckReview(res_no);
    }

    public int findCheckDiary(int res_no) {
        return dao.selectCheckReview(res_no);
    }

    // 기능

    // @since 2023/03/12
    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    // @since 2023/03/12
    public int fileUpload(List<MultipartFile> fileList, List<String> fileRenames, int arti_no) {

        String path = new File(uploadPath+"diary/"+arti_no).getAbsolutePath();

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
        int finalResult = 0;
        for(MultipartFile mf : fileList) {
            try {
                mf.transferTo(new File(path, fileRenames.get(count)));
                count++;
                finalResult = 1;
            } catch (IOException e) {
                finalResult = 3;
            }
        }
        return finalResult;
    }

    public int reviewUpload(Map<String, MultipartFile> fileMap, Map<String, Object> param, List<String> newNames) {

        String path = new File(uploadPath+"review/"+param.get("acc_id")).getAbsolutePath();

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

        for (MultipartFile mf : fileMap.values()) {
            // 파일 저장
            try {
                mf.transferTo(new File(path, newNames.get(count)));
                count ++;
            } catch (IllegalStateException e) {
                log.error(e.getMessage());
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }

        return 1;
    }

}
