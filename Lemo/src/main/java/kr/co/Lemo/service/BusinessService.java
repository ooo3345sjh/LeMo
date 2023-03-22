package kr.co.Lemo.service;

import kr.co.Lemo.dao.BusinessDAO;
import kr.co.Lemo.domain.*;
import kr.co.Lemo.domain.search.Admin_SearchVO;
import kr.co.Lemo.utils.PageHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class BusinessService {

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
     * 판매자 쿠폰 - 쿠폰 등록
     * @since 2023/03/13
     * @param vo
     * */
    public void rsaveCupon(CouponVO vo) throws Exception {
        dao.insertCoupon(vo);
    }

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

        log.warn("here3-1: service info_rsave");
        log.warn("here3-2: param: " + param.toString());

        // 업로드 파일 가져오기
        Map<String,MultipartFile> fileMap = request.getFileMap();

        log.warn("here4 fileMap: " + fileMap.toString());

        // 파일명 변경
        List<String> fileRenames = new ArrayList<>();

        for(MultipartFile mf : fileMap.values()) {
            String oriName = mf.getOriginalFilename();
            String ext = oriName.substring(oriName.indexOf("."));
            String newName = UUID.randomUUID().toString() + ext;
            fileRenames.add(newName);
        }

        log.warn("here5 fileRenames: " + fileRenames.toString());

        // 파일 / join
        String newName = String.join("/", fileRenames);

        log.warn("here6 newName: " + newName);

        // 1. lemo_product_accommodation (숙소등록)

        param.put("user_id", uid);
        param.put("acc_thumbs", newName);

        dao.insertInfo(param);

        String acc_id = String.valueOf(param.get("acc_id"));
        // int acc_id = (Integer) param.get("acc_id"); -> Error: class java.math.BigInteger cannot be cast to class java.lang.Integer
        // 원인: Mysql: INT형 데이터 타입을 HashMap으로 받아 Java에서 사용하려 할 때 발생
        // 해결방법: String.valueOf 사용

        log.info("here7 acc_id: " + acc_id);


        // 파일 업로드
        String path = new File("/Users/yiwonjeong/Desktop/Workspace/LeMo/Lemo/img/acc/" + acc_id).getAbsolutePath();

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

       /* for(int i=0; i<service.length; i++) {
            // lemo_product_servicereginfo (서비스 등록)
            //dao.insertServiceRegInfo(regVO);
            dao.insertServiceRegInfo(param);
        }*/

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







}
