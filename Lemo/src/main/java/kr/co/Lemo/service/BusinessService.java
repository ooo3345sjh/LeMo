package kr.co.Lemo.service;

import kr.co.Lemo.dao.BusinessDAO;
import kr.co.Lemo.domain.*;
import kr.co.Lemo.domain.search.Admin_SearchVO;
import kr.co.Lemo.utils.SearchCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Slf4j
@Service
public class BusinessService {

    @Autowired
    private BusinessDAO dao;

    /**
     * 판매자 쿠폰 - 쿠폰 목록
     * @since 2023/03/13
     * @param sc
     */
    public List<CouponVO> selectCoupon(Admin_SearchVO sc){
        return dao.selectCoupon(sc);
    }
    public int countCoupon(SearchCondition sc){
        return dao.countCoupon(sc);
    }

    /**
     * 판매자 쿠폰 - 쿠폰 목록 소유 숙소 선택
     * @since 2023/03/13
     * @param user_id
     */
    public List<CouponVO> findAccOwned(String user_id){

        log.warn("service findAccOwned");

        return dao.selectAccOwned(user_id);
    }

    /**
     * 관리자 리뷰 - 리뷰 목록
     * @since 2023/03/16, 2023/04/04(edit)
     * @param sc
     */
    public List<ReviewVO> findAllReview(Admin_SearchVO sc){
        return dao.selectReview(sc);
    }
    public int countReview(SearchCondition sc){
        return dao.countReview(sc);
    }

    /**
     * 판매자 쿠폰 - 리뷰 목록 소유 숙소 선택
     * @since 2023/03/16
     * @param user_id
     */
    public List<ReviewVO> findAccOwnedForReview(String user_id){
        return dao.selectAccOwnedForReview(user_id);
    }

    /**
     * 판매자 리뷰 - 리뷰 보기
     * @since 2023/03/16
     * @param revi_id
     */
    public ReviewVO findReview(Integer revi_id) throws Exception{
        return dao.viewReview(revi_id);
    }

    /**
    * 판매자 숙소 - 편의시설
    * @since 2023/03/17
    */
    public List<ServiceCateVO> findService(){
        return dao.selectService();
    }

    /**
     * 판매자 숙소 - 지역 선택
     * @since 2023/03/20
     */
    public List<ProvinceVO> findProvince(){ return dao.selectProvince();}


    /**
     * 판매자 숙소 - 목록
     * @since 2023/03/31
     * @param sc
     */
    public List<ProductAccommodationVO> findAllAccForInfo(Admin_SearchVO sc){

        log.warn("service findAllAccForInfo: " + sc.toString());
        log.warn("user_id: " + sc.getUser_id());

        return dao.selectAccForInfo(sc);
    }

    /**
     * 판매자 숙소 - 목록 페이징
     * @since 2023/03/31
     * @param sc
     */
     public int countAcc(SearchCondition sc){
        return dao.countAcc(sc);
     }

    /**
     * 판매자 숙소 - 숙소 목록 소유 숙소 선택
     * @param user_id
     */
    public List<ProductAccommodationVO> findAccOwnedForInfo(String user_id){
        return dao.selectAccOwnedForInfo(user_id);
    }

    /**
     * 판매자 - 숙소 보기
     * @since 2023/03/31
     * @param acc_id
     */
    public ProductAccommodationVO fincAcc(Integer acc_id) throws Exception{
        return dao.viewAcc(acc_id);
    }

    /**
     * 판매자 - 숙소 보기 - 서비스 카테고리
     * @since 2023/03/31
     * @param acc_id
     */
    public List<ServicereginfoVO> findServiceInAcc(Integer acc_id){

         log.warn("서비스 카테 here2");

         return dao.selectServiceInAcc(acc_id);
     }

     /**
     * 판매자 객실 - 객실 목록
     * @since 2023/04/02
     * @param sc
     */
     public List<ProductRoomVO> findAllRoom(Admin_SearchVO sc){

        log.warn("Selected rooms: " + sc.toString());
        log.warn("user_id: " + sc.getUser_id());

        return dao.selectRoom(sc);
     }
     public int countRoom(SearchCondition sc){
         return dao.countRoom(sc);
     }

     /**
     * 판매자 객실 - 객실 보기
     * @since 2023/04/03
     * @param room_id
     */
    public ProductRoomVO findRoom(Integer room_id) throws Exception {
        return dao.viewRoom(room_id);
    }

    /**
     * 판매자 소유 숙박 목록
     * @since 2023/04/02
     * @param user_id
     */
     public List<ProductAccommodationVO> findAllAccOwnedForInfo(String user_id){
        return dao.selectAccOwnedForInfo(user_id);
     }

    /**
     * @since 2023/04/04
     * @param sc
     * @apiNote 판매자 예약 정보 목록
     */
     public List<ReservationVO> findAllReservaitons(Admin_SearchVO sc){
         return  dao.selectReservaitons(sc);
     }
     public int countReservations(SearchCondition sc){
         return dao.countReservations(sc);
     }

     /**
     * @since 2023/04/14
     * @apiNote 판매자 - 메인 - 일별 누적 판매량 (당일)
     */
     public List<ReservationVO> findAllTodaySales(Map map){
        return dao.selectTodaySales(map);
     }

     /**
     * @since 2023/04/15
     * @apiNote 판매자 - 메인 - 최신 리뷰 (당일)
     */
     public List<ReviewVO> findAllReviewLatest(Map map){ return dao.selectReviewLatest(map); }

     /**
     * @since 2023/04/16
     * @apiNote 판매자 - 메인 - 숙소 리스트
     */
    public List<ProductAccommodationVO> selectAccsList(Map map){return dao.selectAccsList(map);}

    /**
     * @since 2023/04/08
     * @param map
     * @apiNote 판매자 - 통계관리 - 일별 누적 판매량 (일주일)
     */
     public List<ReservationVO> findAllDaySales (Map map){return dao.selectDaySales(map);}

    /**
     * @since 2023/04/16
     * @param map
     * @apiNote 판매자 - 통계관리 - 당일 시간대별 판매량
     */
    public List<ReservationVO> selectTimeSales(Map map){return dao.selectTimeSales(map);}

    /**
     * @since 2023/04/15
     * @param map
     * @apiNote 판매자 통계관리 월별 누적 판매량 (4달 기준)
     */
    public List<ReservationVO> findAllMonthSales(Map map){

        int total = 0;
        double percent = 0;

        List<ReservationVO> mp = dao.selectMonthSales(map);

        for(ReservationVO vo : mp){
            total += vo.getTot_res_price();
        }

        //log.warn("total sales: " + total);

        for(ReservationVO vo : mp){
            double tot_month_percent = ((vo.getTot_res_price()+0.0)/(total+0.0))*100;

            //log.warn("tot_month_percent: " + tot_month_percent);
            vo.setTot_month_percent(tot_month_percent);
        }

        // vo 확인용 로그 출력
        for(ReservationVO vo : mp){
            double test = vo.getTot_month_percent();
            //log.warn("test : " + test);
        }
        return mp;
    }

    /**
     * @since 2023/04/16
     * @param map
     * @apiNote 판매자 - 통계관리 연 판매량
     */
    public List<ReservationVO> findAllYearSales(Map map){
        return dao.selectYearSales(map);
    }


     /**
     * @since 2023/04/08
     * @param map
     * @apiNote 판매자 - 결제방법 결제 현황 (일주일)
     */
    public List<ReservationVO> findAllPayment(Map map){return dao.selectPayment(map);}

    /**
     * @since 2023/04/10
     * @param map
     * @apiNote 판매자 - 객실별 예약 현황 (일주일)
     */
    public List<ReservationVO> selectWeeksRoom(Map map){return dao.selectWeeksRoom(map);}

    /**
    * @since 2023/04/06
    * @apiNote 판매자 카운트
     */
    public int countWeeksSales(Map map){
        log.warn("periodType : " + map.get("periodType"));
        return dao.countWeeksSales(map);}
    public int countDaySales(Map map) { return dao.countDaySales(map); }
    public int countDayCancel(Map map){return dao.countDayCancel(map); }
    public int countWeeksCancel(Map map){ return dao.countWeeksCancel(map); }
     public int countTotalRoom(Map map){return dao.countTotalRoom(map); }
    public int countCheckInRoom(Map map){return dao.countCheckInRoom(map);}
    public int countWeeksQna(Map map){ return dao.countWeeksQna(map); }
    public int countWeeksAcc(Map map){return dao.countWeeksAcc(map);}
    public int countWeeksReview(Map map){return dao.countWeeksReview(map);}
    public int countDayQna(Map map){return dao.countDayQna(map);}
    public int countDayReview(Map map){return dao.countDayReview(map);}


    /**
     * @since 2023/04/11
     * @param map
     * @apiNote 판매자 타임라인
     */
    public List<ReservationVO> findAllTimeline(Map map){
        return dao.selectTimeline(map);
    }

    /**
     * @since 2023/04/17
     * @apiNote 판매자 타임라인 - 모달 이벤트
     */
    public List<ReservationVO> findReservation(String res_no){ return dao.selectReservation(res_no); }

    /**
     * 판매자 쿠폰 - 쿠폰 등록
     * @since 2023/03/13
     * */
    public void rsaveCoupon(Map<String,Object> param) throws Exception {

         log.warn("param: " + param.toString());


        dao.insertCoupon(param);
    }

    //@since 2023/04/01
    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;


    /**
     * 판매자 숙소 - 숙소 등록
     * @since 2023/03/20
     * @param param
     * @param request
     * @param uid
     */
    public void info_rsave(Map<String,Object> param,
                           MultipartHttpServletRequest request,
                           String uid){

        log.warn("param: " + param.toString());

        // 업로드 파일 가져오기
        Map<String,MultipartFile> fileMap = request.getFileMap();

        log.warn("fileMap: " + fileMap.toString());

        // 파일명 변경
        List<String> fileRenames = new ArrayList<>();

        for(MultipartFile mf : fileMap.values()) {
            String oriName = mf.getOriginalFilename();
            String ext = oriName.substring(oriName.indexOf("."));
            String newName = UUID.randomUUID().toString() + ext;
            fileRenames.add(newName);
        }

        log.warn("fileRenames: " + fileRenames.toString());

        // 파일 / join
        String newName = String.join("/", fileRenames);

        log.warn("newName: " + newName);

        // 1. lemo_product_accommodation (숙소등록)

        param.put("user_id", uid);
        param.put("acc_thumbs", newName);

        dao.insertInfo(param);

        String acc_id = String.valueOf(param.get("acc_id"));
        String province_no = String.valueOf(param.get("province_no"));
        // int acc_id = (Integer) param.get("acc_id"); -> Error: class java.math.BigInteger cannot be cast to class java.lang.Integer
        // 원인: Mysql: INT형 데이터 타입을 HashMap으로 받아 Java에서 사용하려 할 때 발생
        // 해결방법: String.valueOf 사용

        log.info("here7 acc_id: " + acc_id);
        log.info("here8 province_no: " + province_no);


        // 파일 업로드
        //String path = new File("/Users/yiwonjeong/Desktop/Workspace/LeMo/Lemo/img/product/" + province_no + acc_id).getAbsolutePath();
        String path = new File(uploadPath+"product/"+province_no+"/"+acc_id).getAbsolutePath();

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
        for(MultipartFile mf : fileMap.values()) {
            try {
                mf.transferTo(new File(path, fileRenames.get(count)));
                count++;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // lemo_product_ratepolicy (할인율 등록)
        dao.insertRatePolicy(param);

        String[] service = ((String) param.get("sc_no")).split(",");
        log.warn("here8 service: " + service.toString());

        for(int i=0; i<service.length; i++) {
            // lemo_product_servicereginfo (서비스 등록)
            //dao.insertServiceRegInfo(regVO);
            param.put("sc_no", service[i]);
            dao.insertServiceRegInfo(param);
            log.warn("here9 service: " + service[i]);
        }

    }

    /**
     * 판매자 쿠폰 - 객실 등록
     * @since 2023/04/02
    * */
    public int rsaveRoom(Map<String, Object> param,
                         MultipartHttpServletRequest request) throws Exception{

        log.warn("here2 param: " + param.toString());

        // 업로드 파일 가져오기
        Map<String,MultipartFile> fileMap = request.getFileMap();
        log.warn("here3 fileMap: " + fileMap.toString());

        // 파일명 변경
        List<String> fileRenames = new ArrayList<>();

        for(MultipartFile mf : fileMap.values()) {
            String oriName = mf.getOriginalFilename();
            String ext = oriName.substring(oriName.indexOf("."));
            String newName = UUID.randomUUID().toString() + ext;
            fileRenames.add(newName);
        }
        log.warn("here4 fileRenames: " + fileRenames.toString());

        // 파일 / join
        String newName = String.join("/", fileRenames);
        log.warn("here5 newName: " + newName);

        param.put("room_thumb", newName);

        String acc_id = String.valueOf(param.get("acc_id"));
        String province_no = String.valueOf(param.get("province_no"));

        log.info("here6 acc_id: " + acc_id);
        log.info("here7 province_no: " + province_no);

        // 파일 업로드
        //String path = new File("/Users/yiwonjeong/Desktop/Workspace/LeMo/Lemo/img/product/" + province_no + acc_id).getAbsolutePath();
        String path = new File(uploadPath+"product/"+province_no+"/"+acc_id).getAbsolutePath();

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
        for(MultipartFile mf : fileMap.values()) {
            try {
                mf.transferTo(new File(path, fileRenames.get(count)));
                count++;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return dao.insertRoom(param);
    }

    /**
     * 판매자 예약 - 메모 입력
     * @since 2023/04/05
     * @param res_memo
     * @param res_no
     */
    public int usaveMemoInRes(String res_memo, String res_no){
        return dao.updateMemoInRes(res_memo, res_no);
    }

    /**
     * 판매자 숙소 - 숙소 수정
     * @since 2023/04/01
     * @param param
     * @param request
     */
    /*
    public void info_usave(Map<String, Object> param,
                           MultipartHttpServletRequest request){

        log.warn("here4 param: " + param.toString());

        // 업로드 파일 가져오기
         Map<String,MultipartFile> fileMap = request.getFileMap();

        log.warn("here6 fileMap: " + fileMap.toString());

        // 파일명 변경
        List<String> fileRenames = new ArrayList<>();

        for(MultipartFile mf : fileMap.values()) {
            String oriName = mf.getOriginalFilename();
            String ext = oriName.substring(oriName.indexOf("."));
            String newName = UUID.randomUUID().toString() + ext;
            fileRenames.add(newName);
        }

        log.warn("here7 fileRenames: " + fileRenames.toString());

        // 파일 / join
        String newName = String.join("/", fileRenames);

        log.warn("newName: " + newName);

        param.put("acc_thumbs", newName);

        dao.updateInfo(param);

        String acc_id = String.valueOf(param.get("acc_id"));
        String province_no = String.valueOf(param.get("province_no"));

        log.warn("here8 acc_id: " + acc_id);
        log.warn("here9 province_no: " + province_no);

         // 파일 업로드
        //String path = new File("/Users/yiwonjeong/Desktop/Workspace/LeMo/Lemo/img/product/" + province_no + acc_id).getAbsolutePath();
        String path = new File(uploadPath+"product/"+province_no+"/"+acc_id).getAbsolutePath();

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
        for(MultipartFile mf : fileMap.values()) {
            try {
                mf.transferTo(new File(path, fileRenames.get(count)));
                count++;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // lemo_product_ratepolicy (할인율 등록)
        dao.updateRatePolicy(param);

        String[] service = ((String) param.get("sc_no")).split(",");
        log.warn("here service1: " + service);
        log.warn("here service2: " + service.length);

        // 기존 서비스 비우기
        dao.deleteServiceRegInfo(param);

        for(int i = 0; i< service.length; i++) {
            log.warn("here service3: " + service[i]);
            param.put("sc_no", service[i]);
            log.warn("here service4: " + service[i]);

            dao.insertServiceRegInfo(param);
        }
    }
*/


    /**
     * 관리자 리뷰 - 리뷰 답변
     * @since 2023/03/16
     * @param revi_reply
     * @param revi_id
     */
    public int usaveReply(String revi_reply, String revi_id){ return dao.updateReply(revi_reply, revi_id); }


    /**
     * 판매자 쿠폰 - 쿠폰 삭제
     * @since 2023/03/13
     * @param cp_id
     */
    public int removeCoupon(String cp_id){
        return dao.deleteCoupon(cp_id);
    }

    /**
     * 판매자 리뷰 - 리뷰 삭제
     * @since 2023/03/16
     * @param revi_id
     */
    public int removeReview(String revi_id){
        return dao.deleteReview(revi_id);
    }


    /**
     * 판매자 숙소 - 숙소 삭제
     * @since 2023/03/23
     * @param acc_id
     */
    public int removeAcc(String acc_id){
        return dao.deleteAcc(acc_id);
    }

    /**
     * 판매자 객실 - 객실 삭제
     * @since 2023/04/04
     * @param room_id
     */
    @Transactional
    public int removeRoom(String room_id){

        ProductRoomVO room = dao.viewRoom(Integer.parseInt(room_id));
        String room_thumbs = room.getRoom_thumb();
        int province_no = room.getProvince_no();
        int acc_id = room.getAcc_id();

        // 객실 이미지 삭제
        List<String> images = new ArrayList<>();
        String dirPath = new File(uploadPath+"product/"+province_no+"/"+acc_id).getAbsolutePath();

        if (room_thumbs.contains("/")) {
            // "/" 기준으로 파일 경로를 나누어 파일 이름을 추출
            String[] fileNames = room_thumbs.split("/");
            images.addAll(Arrays.asList(fileNames));
        } else {
            // 파일 경로에 "/" 문자가 없으면 파일 이름 하나만 list에 추가
            images.add(room_thumbs);
        }

        for(String deleteFile : images) {
            File df = new File(dirPath + "/" + deleteFile);
            if(df.exists()) { df.delete(); }
        }

        int result = dao.deleteRoom(room_id);

        return result;
    }



    /**
     * @since  2023/04/05
     * @author 황원진
     * @apiNote 판매자 qna service
     */

    /**
     * 판매자 상품문의 - 문의목록
     * @since 2023/04/05
     * @param sc
     */
    public List<ProductQnaVO> findAllQna(Admin_SearchVO sc){
        return dao.selectQnaList(sc);
    }

    /**
     * 판매자 상품문의 - 목록 페이징
     * @since 2023/04/05
     * @param sc
     */
    public int countQna(SearchCondition sc){
        return dao.countQnas(sc);
    }

    /**
     * 판매자 소유 상품 목록
     * @since 2023/04/05
     * @param user_id
     */
    public List<ProductQnaVO> findAllAccOwnedForQna(String user_id){
        return dao.selectAccOwnedForQna(user_id);
    }

    /**
     * 판매자 상품문의 - 선택삭제
     * @since 2023/04/07
     * @param checkList
     */
    public int removeQnaList(@RequestParam("checkList[]") List checkList){
        log.info("serviceQnaRemove");
        return dao.deleteQnaList(checkList);
    }

    /***
     *  판매자 상품문의 - 상세보기
     *  @since 2023/04/08
     *  @param qna_no
     */
    public ProductQnaVO findQnaArticle(int qna_no){
        return dao.selectQnaArticle(qna_no);
    }

    /***
     *  판매자 상품문의 - 답변 등록
     *  @since 2023/04/08
     */
    public int usaveQnaReply(@RequestParam("qna_reply") String qna_reply, @RequestParam("qna_no") int qna_no){
        return dao.updateQnaReply(qna_reply, qna_no);
    }

    /***
     *  판매자 상품문의 - 답변 수정
     *  @since 2023/04/08
     */
    public int usaveQnaUdate(@RequestParam("qna_reply") String qna_reply, @RequestParam("qna_no") int qna_no){
        return dao.updateQnaUdate(qna_reply, qna_no);
    }

    /**
     * @since 2023/04/16
     * @author 이해빈
     * @apiNote 객실 수정
     */
    public int usaveRoom(Map<String, Object> param, Map<String, MultipartFile> fileMap) throws Exception {

        String thumbType = "room_thumbs";

        List<String> fileName = checkFile(param, fileMap, thumbType);
        if(!fileName.isEmpty()) {
            String files = String.join("/", fileName);
            param.put("room_thumb", files);
        }else {
            param.put("room_thumb", null);
        }

        int result = dao.updateRoom(param);

        return result;
    }

    /**
     * @since 2023/04/16
     * @author 이해빈
     * @apiNote 숙소 수정
     */
    @Transactional
    public int info_usave(Map<String, Object> param,
                           Map<String, MultipartFile> fileMap) throws Exception{

        String thumbType = "accThumbs";

        List<String> fileName = checkFile(param, fileMap, thumbType);
        if(!fileName.isEmpty()) {
            String files = String.join("/", fileName);
            param.put("acc_thumbs", files);
        }else {
            param.put("acc_thumbs", null);
        }

        // 숙소 수정
        int result = dao.updateInfo(param);

        // lemo_product_ratepolicy (할인율 등록)
        dao.updateRatePolicy(param);

        String[] service = ((String) param.get("sc_no")).split(",");
        log.warn("here service1: " + service);
        log.warn("here service2: " + service.length);

        // 기존 서비스 비우기
        dao.deleteServiceRegInfo(param);

        for(int i = 0; i< service.length; i++) {
            log.warn("here service3: " + service[i]);
            param.put("sc_no", service[i]);
            log.warn("here service4: " + service[i]);

            dao.insertServiceRegInfo(param);
        }

        return result;
    }

    /**
     * @since 2023/04/16
     * @author 이해빈
     * @apiNote 숙소, 객실 이미지 파일 검사
     */
    public List<String> checkFile(Map<String, Object> param,
                                  Map<String, MultipartFile> fileMap,
                                  String thumbType) throws Exception {

        String pv_no = (String) param.get("provinceNo");
        String province_no = (String) param.get("province_no");
        String acc_id = (String) param.get("acc_id");

        String dirPath = new File(uploadPath+"product/"+province_no+"/"+acc_id).getAbsolutePath();


        if (thumbType.equals("accThumbs")) { // 숙소 수정일 경우

            if (!pv_no.equals(province_no)) { // 기존 숙소의 지역과 수정된 숙소의 지역이 다를 경우

                dirPath = new File(uploadPath+"product/"+pv_no+"/"+acc_id).getAbsolutePath();

                File dir = new File(dirPath);
                File filelist[] = dir.listFiles();

                String newdirPath = new File(uploadPath + "product/" + province_no + "/" + acc_id).getAbsolutePath();

                // 저장 폴더가 없다면 생성
                File checkFolder = new File(newdirPath);
                if (!checkFolder.exists()) {
                    try {
                        Files.createDirectories(checkFolder.toPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    Files.move(dir.toPath(), new File(newdirPath).toPath(), StandardCopyOption.REPLACE_EXISTING);
                    log.info("이동 성공");
                }catch (IOException e) {
                    e.printStackTrace();
                    log.info("이동 실패" + e.getMessage());
                }

                dirPath = newdirPath; // dirPath 수정

            }
        }


        List<String> oriImage = new ArrayList<>();
        List<String> newImage = new ArrayList<>();
        List<String> oriRemoveImage = new ArrayList<>();
        List<String> newRemoveImage = new ArrayList<>();


        // 기존 파일 이름
        String thumbs = (String)param.get(thumbType);
        log.info("기존 파일 이름 : " + thumbs);

        if (thumbs.contains("/")) {
            // "/" 기준으로 파일 경로를 나누어 파일 이름을 추출
            String[] fileNames = thumbs.split("/");
            oriImage.addAll(Arrays.asList(fileNames));
            oriRemoveImage.addAll(Arrays.asList(fileNames));
        } else {
            // 파일 경로에 "/" 문자가 없으면 파일 이름 하나만 list에 추가
            oriImage.add(thumbs);
            oriRemoveImage.add(thumbs);
        }

        log.info("oriImage : " + oriImage);
        log.info("oriRemoveImage : " + oriRemoveImage);

        for(MultipartFile mf : fileMap.values()) {
            newImage.add(mf.getOriginalFilename());
            newRemoveImage.add(mf.getOriginalFilename());
        }

        log.info("newImage : " + newImage);
        log.info("newRemoveImage : " + newRemoveImage);


        // 저장되어질 이미지
        newImage.removeAll(oriRemoveImage);
        log.debug("저장 : " + newImage);


        // 삭제되어질 이미지
        oriImage.removeAll(newRemoveImage);
        log.debug("삭제 : " + oriImage);


        if(oriImage.size() != 0) {
            // 삭제
            for(String deleteFile : oriImage) {

                File df = new File(dirPath + "/" + deleteFile);
                if(df.exists()) { df.delete(); }
            }
        }

        List<String> changeSaveFile = new ArrayList<>();

        if(newImage.size() != 0) {
            // 저장
            for(String saveFile : newImage) {
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
        oriRemoveImage.removeAll(oriImage);
        oriRemoveImage.addAll(changeSaveFile);

        
        return oriRemoveImage;

    }
}
