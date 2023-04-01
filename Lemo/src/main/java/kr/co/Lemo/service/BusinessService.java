package kr.co.Lemo.service;

import kr.co.Lemo.dao.BusinessDAO;
import kr.co.Lemo.domain.*;
import kr.co.Lemo.domain.search.Admin_SearchVO;
import kr.co.Lemo.utils.PageHandler;
import kr.co.Lemo.utils.SearchCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class BusinessService {

    @Autowired
    private BusinessDAO dao;

    /**
     * 판매자 쿠폰 - 쿠폰 목록
     * @since 2023/03/13
     * @param model
     * @param sc
     */
    public List<CouponVO> selectCoupon(Model model, Map map, Admin_SearchVO sc){
        int totalCnt = dao.countCoupon(sc);
        int totalPage = (int)Math.ceil(totalCnt / (double)sc.getPageSize());
        if(sc.getPage() > totalPage) sc.setPage(totalPage);

        PageHandler pageHandler = new PageHandler(totalCnt, sc);

        //log.info("select Coupon Service: " + sc.toString());

        List<CouponVO> coupons = dao.selectCoupon(sc);

        //log.info("Selected coupons: " + coupons.toString());

        model.addAttribute("coupons", coupons);
        model.addAttribute("ph", pageHandler);

        return coupons;
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
     * @since 2023/03/16
     * @param model
     * @param sc
     */
    public List<ReviewVO> findAllReview(Model model, Admin_SearchVO sc){
        int totalCnt = dao.countReview(sc);
        int totalPage = (int)Math.ceil(totalCnt / (double)sc.getPageSize());
        if(sc.getPage() > totalPage) sc.setPage(totalPage);

        PageHandler pageHandler = new PageHandler(totalCnt, sc);

        List<ReviewVO> reviews = dao.selectReview(sc);

        log.warn("Selected reviews in business: " + reviews.toString());

        model.addAttribute("reviews", reviews);
        model.addAttribute("ph", pageHandler);

        return reviews;
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
     * 판매자 쿠폰 - 쿠폰 등록
     * @since 2023/03/13
     * @param vo
     * */
    public void rsaveCupon(CouponVO vo, String user_id) throws Exception {
        dao.insertCoupon(vo, user_id);
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
     * 판매자 숙소 - 숙소 수정
     * @since 2023/04/01
     * @param param
     * @param request
     */
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







}
