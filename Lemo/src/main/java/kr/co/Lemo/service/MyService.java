package kr.co.Lemo.service;

import kr.co.Lemo.dao.DiaryDAO;
import kr.co.Lemo.dao.MyDAO;
import kr.co.Lemo.domain.*;
import kr.co.Lemo.utils.RemoteAddrHandler;
import kr.co.Lemo.utils.SearchCondition;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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

    @Autowired
    private DiaryDAO diaryDAO;

    @Autowired
    private PaymentService paymentservice;

    // @since 2023/03/12
    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    // @since 2023/03/13
    public List<ArticleDiaryVO> findDiaryArticles(SearchCondition sc) {
        List<ArticleDiaryVO> articles = dao.selectDiaryArticles(sc);
        List<DiarySpotVO> spots = dao.selectDiarySpots();
        List<ArticleDiaryVO> picks = diaryDAO.selectDiaryLikes(sc);


        Map<Integer, List<DiarySpotVO>> map = spots.stream().collect(Collectors.groupingBy(DiarySpotVO::getArti_no));
        Map<Integer, List<ArticleDiaryVO>> maps = picks.stream().collect(Collectors.groupingBy(ArticleDiaryVO::getArti_no));

        for(ArticleDiaryVO artiVO : articles) {
            artiVO.setSpotVO(map.get(artiVO.getArti_no()));
            if(maps.get(artiVO.getArti_no()) != null) {
                artiVO.setUser_id(maps.get(artiVO.getArti_no()).get(0).getUser_id());
            }
        }

        return articles;
    }

    // @since 2023/04/20
    public int findTotalDiary(SearchCondition sc) {
        return dao.selectTotalDiary(sc);
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
                .res_no( Long.parseLong(String.valueOf(param.get("res_no"))) )
                .user_id(user_id)
                .arti_title((String) param.get("diaryTitle"))
                .arti_thumb(newName)
                .arti_regip(RemoteAddrHandler.getRemoteAddr(req))
                .arti_start((String) param.get("diaryStart"))
                .arti_end((String) param.get("diaryEnd"))
                .build();

        // diary_article 테이블 입력
        int result = dao.insertDiaryArticle(diaryVO);

        int arti_no = diaryVO.getArti_no();

        ProductAccommodationVO accommo = dao.selectProvinceAddr(Long.parseLong(String.valueOf(param.get("res_no"))));

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

    // @since 2023/04/11
    public int diary_usave(
            Map<String, Object> param,
            List<MultipartFile> fileList,
            HttpServletRequest req,
            String user_id
    ) {
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
                .arti_no(Integer.parseInt(String.valueOf(param.get("arti_no"))))
                .arti_title((String) param.get("diaryTitle"))
                .arti_thumb(newName)
                .arti_regip(RemoteAddrHandler.getRemoteAddr(req))
                .arti_start((String) param.get("diaryStart"))
                .arti_end((String) param.get("diaryEnd"))
                .build();

        // diary_article 테이블 입력
        int result = dao.updateDiaryArticle(diaryVO);

        int arti_no = diaryVO.getArti_no();

        ProductAccommodationVO accommo = dao.selectProvinceAddr(Long.parseLong(String.valueOf(param.get("res_no"))));

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
    public ReviewVO findReview(long res_no, String user_id) {
        ReviewVO reVO = dao.selectReview(res_no, user_id);

        if(reVO != null) {
            String acc_thumb = reVO.getRevi_thumb();
            log.debug("acc_thumb : " + acc_thumb);
            if(acc_thumb != null) {
                List<String> thumbs = Arrays.asList(acc_thumb.split("/"));
                reVO.setThumbs(thumbs);
            }
        }

        return reVO;
    }
    public ReviewVO findReviewAccommodation(long res_no, String user_id) {
        return dao.selectReviewAccommodation(res_no, user_id);
    }
    public int rsavsReview(MultipartHttpServletRequest request, Map<String, Object> param) {
        Map<String, MultipartFile> fileMap = request.getFileMap();

        int insertResult = 0;

        if(!fileMap.isEmpty()) {
            List<String> newNames = new ArrayList<>();
            List<String> oriNames = new ArrayList<>();

            for (MultipartFile multipartFile : fileMap.values()) {
                String fName = multipartFile.getOriginalFilename();
                String ext = fName.substring(fName.indexOf("."));
                String newName = UUID.randomUUID() + ext;
                newNames.add(newName);
                oriNames.add(fName);
            }

            String dbNames = String.join("/", newNames);
            param.put("revi_thumb", dbNames);

            insertResult = dao.insertReview(param);

            if(insertResult == 1) {
                reviewUpload(fileMap, param, newNames);
            }
        }else {
            param.put("revi_thumb", null);
            insertResult = dao.insertReview(param);

            String path = new File(uploadPath+"review/"+param.get("res_no")).getAbsolutePath();

            File checkFolder = new File(path);
            if(!checkFolder.exists()){
                try {
                    Files.createDirectories(checkFolder.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return insertResult;

    }

    // @since 2023/03/29
    public ReservationVO findReservation(long res_no, String user_id) {

        ReservationVO resVO = dao.selectReservation(res_no, user_id);

        if(resVO != null) {
            LocalDate checkInDate  = LocalDate.parse(resVO.getRes_checkIn(), DateTimeFormatter.ISO_DATE);
            LocalDate checkOutDate = LocalDate.parse(resVO.getRes_checkOut(), DateTimeFormatter.ISO_DATE);

            Period period = Period.between(checkInDate, checkOutDate);
            resVO.setNight(period.getDays());
        }
        return resVO;
    }

    public int removeUpdateReservation(long res_no, String user_id) throws Exception {
        int result = 0;

        /* 토큰 발행 */
        String token = paymentservice.getToken();
        ReservationVO reservationVO = dao.selectReservation(res_no, user_id);

        if(reservationVO.getRes_state() != 1){
            return -9999;
        }

        /* 결제 취소 */
        paymentservice.paymentCancel(token, reservationVO.getImp_uid());

        result = dao.deleteReservation(res_no);

        if(result == 1) { result = dao.updateReservationState(res_no); }

        PointVO pointVO = PointVO.builder()
                .user_id(user_id)
                .res_no(res_no)
                .build();
        /**
         * @since 2023/04/23
         * @author 서정현
         * @apiNote 예약취소시 포인트 반환
         */
        // 포인트 반환
        dao.insertReturnPoint(pointVO);
        if(pointVO.getPoi_id() != 0){
            dao.insertReturnDetailPoint(pointVO.getPoi_id());

            // 만료포인트 업데이트
            dao.expiredPoint(user_id);
            dao.expiredDetailPoint(user_id);

            // 유저 포인트 정보 최신화
            dao.updateMemberUserInfo(user_id);
        }
        /**
         * @since 2023/04/23
         * @author 서정현
         * @apiNote 예약취소시 쿠폰 반환
         */
        int resultCoupon = dao.updatreMemberCoupon(user_id, res_no);
        if(resultCoupon > 0)
            dao.deleteMemberCouponLog(user_id, res_no);

        return result;
    }

    public int findCheckReview(long res_no) {
        return dao.selectCheckReview(res_no);
    }

    // @since 2023/03/30
    public ProductAccommodationVO findeDiaryXY(long res_no) {
        return dao.selectDiaryXY(res_no);
    }

    // @since 2023/04/05
    public List<ProductQnaVO> findDiaryQna(SearchCondition sc) {
        return dao.selectDiaryQna(sc);
    }

    public int findDiaryQnaCnt(SearchCondition sc) {
        return dao.selectDiaryQnaCnt(sc);
    }

    // @since 2023/04/06
    public String findCheckReviewId(long res_no) {
        return dao.selectCheckReviewId(res_no);
    }

    // @since 2023/04/17
    public int findMemberPoint(String user_id) {
        return dao.selectMemberPoint(user_id);
    };

    public String usaveReview(Map<String, Object> param, Map<String, MultipartFile> fileMap) {
        List<String> fileName = checkReivewFile(param, fileMap);

        if(!fileName.isEmpty()) {
            String files = String.join("/", fileName);
            param.put("revi_thumb", files);
        }else {
            param.put("revi_thumb", null);
        }

        int result = dao.updateReview(param);
        String success = "usaveImageFail";
        switch (result) {
            case 1 :
                success = "usaveImageSuccess";
                break;
            case 0 :
                success = "usaveImageFail";
                break;
        }

        return success;
    }

    public int removeReview(long res_no) {

        String path = new File(uploadPath+"review/"+res_no).getAbsolutePath();

        File deleteFolder = new File(path);

        try {
            FileUtils.cleanDirectory(deleteFolder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        deleteFolder.delete();

        return dao.deleteReview(res_no);
    }

    // 기능

    // @since 2023/03/12
    public int fileUpload(List<MultipartFile> fileList, List<String> fileRenames, int arti_no) {

        String path = new File(uploadPath+"diary/"+arti_no).getAbsolutePath();

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

        String path = new File(uploadPath+"review/"+param.get("res_no")).getAbsolutePath();

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

    public List<String> checkReivewFile(Map<String, Object> param, Map<String, MultipartFile> fileMap) {

        String dirPath = new File(uploadPath+"review/"+param.get("res_no")).getAbsolutePath();

        File dir = new File(dirPath);
        File Files[] = dir.listFiles();

        List<String> oriReview = new ArrayList<>();
        List<String> newReview = new ArrayList<>();
        List<String> oriRemoveReview = new ArrayList<>();
        List<String> newRemoveReview = new ArrayList<>();

        for(File fname : Files) {
            oriReview.add(fname.getName());
            oriRemoveReview.add(fname.getName());
        }

        for(MultipartFile mf : fileMap.values()) {
            newReview.add(mf.getOriginalFilename());
            newRemoveReview.add(mf.getOriginalFilename());
        }

        // 저장되어질 reviewImage
        newReview.removeAll(oriRemoveReview);
        log.debug("저장 : " + newReview);

        // 삭제되어질 reviewImage
        oriReview.removeAll(newRemoveReview);
        log.debug("삭제 : " + oriReview);

        if(oriReview.size() != 0) {
            // 삭제
            for(String deleteFile : oriReview) {

                File df = new File(dirPath + "/" + deleteFile);
                if(df.exists()) { df.delete(); }
            }
        }

        List<String> changeSaveFile = new ArrayList<>();

        if(newReview.size() != 0) {
            // 저장
            for(String saveFile : newReview) {
                String ext = saveFile.substring(saveFile.indexOf("."));
                String newName = UUID.randomUUID() + ext;
                changeSaveFile.add(newName);

                MultipartFile mf = fileMap.get(saveFile);

                try {
                    mf.transferTo(new File(dirPath, newName));
                } catch (IllegalStateException e) {
                    log.error(e.getMessage());
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }

        // db에 저장될 리스트 이름
        oriRemoveReview.removeAll(oriReview);
        oriRemoveReview.addAll(changeSaveFile);
        log.debug("DB : " + oriRemoveReview);

        return oriRemoveReview;

    }

    public int removeDiarySpot(Map<String, Object> param) {
        String dirPath = new File(uploadPath+"diary/"+param.get("arti_no")).getAbsolutePath();

        File deleteFolder = new File(dirPath);

        try {
            FileUtils.cleanDirectory(deleteFolder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int reuslt = diaryDAO.deleteDiarySpot(Integer.parseInt(String.valueOf(param.get("arti_no"))));

        return reuslt;
    }

    /**
     * @since 2023/04/11
     * @author 서정현
     * @apiNote 회원 포인트 및 보유 쿠폰 갯수 조회
     */
    public void findUserPointAndCouponCnt(Model m, String user_id){
        m.addAttribute("point", dao.selectUserPoint(user_id));
        m.addAttribute("coupon", dao.selectUserCoupon(user_id));
    }

    /**
     * @since 2023/04/28
     * @author 서정현
     * @apiNote 회원의 숙박완료 건수 조회
     */
    public int countUserReservation(String user_id){
        return dao.selectUserReservation(user_id);
    }

    /**
     * @since 2023/04/11
     * @author 서정현
     * @apiNote 회원 탈퇴 업데이트
     */
    public int usaveWithdrawUser(String user_id){
        dao.deleteUserCoupon(user_id);
        int result = dao.updateWithdrawUser(user_id);
        return  result;
    }

    /***
     * @since 2023/04/11
     * @author 이해빈
     * @apNote 찜한 숙소 삭제
     */
    public int removePick(Map map){
        return dao.deletePick(map);
    }

    /***
     * @since 2023/04/12
     * @author 이해빈
     * @apNote 숙박 문의 삭제
     */
    public int removeQna(Map map){
        return dao.deleteQna(map);
    }

}
