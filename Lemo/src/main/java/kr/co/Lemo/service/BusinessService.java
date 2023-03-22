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
    public void info_rsave(HashMap<String,Object> param,
                           MultipartHttpServletRequest request,
                           String uid
                          ){

        log.warn("here3: service info_rsave");

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

        String accType_no_Str = ((String) param.get("accType_no"));
        int accType_no = Integer.parseInt(accType_no_Str);

        String province_no_Str = ((String) param.get("province_no"));
        int province_no = Integer.parseInt(province_no_Str);

        String acc_season_Str = ((String) param.get("acc_season"));
        int acc_season = Integer.parseInt(acc_season_Str);

        String acc_longtitude_Str = ((String) param.get("acc_longtitude"));
        double acc_longtitude = Double.parseDouble(acc_longtitude_Str);

        String acc_lattitude_Str = ((String) param.get("acc_lattitude"));
        double acc_lattitude = Double.parseDouble(acc_lattitude_Str);

        ProductAccommodationVO infoVO = ProductAccommodationVO.builder()
                                    .user_id(uid)
                                    .acc_name((String) param.get("acc_name"))
                                    //.accType_no((Integer).parse param.get("accType_no"))
                                    .accType_no(accType_no)
                                    .province_no(province_no)
                                    .acc_city((String) param.get("acc_city"))
                                    .acc_zip((String) param.get("acc_zip"))
                                    .acc_addr((String) param.get("acc_addr"))
                                    .acc_addrDetail((String) param.get("acc_addrDetail"))
                                    .acc_longtitude(acc_longtitude)
                                    .acc_lattitude(acc_lattitude)
                                    .acc_mainInfo((String) param.get("acc_mainInfo"))
                                    .acc_info((String) param.get("acc_info"))
                                    .acc_comment((String) param.get("acc_comment"))
                                    .acc_thumbs(newName)
                                    .acc_checkIn((String) param.get("acc_checkIn"))
                                    .acc_checkOut((String) param.get("acc_checkOut"))
                                    .acc_season(acc_season)
                                    .build();

        // lemo_product_accommodation (숙소등록)
        dao.insertInfo(infoVO);

        int acc_id = infoVO.getAcc_id();

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

        //

        ProductAccommodationVO rateVO = ProductAccommodationVO.builder()
                                        .acc_id(acc_id)
                                        .rp_offSeason_weekday((Integer) param.get("rp_offSeason_weekday"))
                                        .rp_offSeason_weekend((Integer) param.get("rp_offSeason_weekend"))
                                        .rp_peakSeason_weekday((Integer) param.get("rp_peakSeason_weekday"))
                                        .rp_peakSeason_weekend((Integer) param.get("rp_peakSeason_weekend"))
                                        .build();

        // lemo_product_ratepolicy (할인율 등록)
        dao.insertRatePolicy(rateVO);

        String[] service = ((String) param.get("sc_no")).split("/");
        String[] images  = newName.split("/");

        for(int i=0; i<images.length; i++) {
            ServicereginfoVO regVO = ServicereginfoVO.builder()
                                    .acc_id(acc_id)
                                    .sc_no(Integer.parseInt(service[i]))
                                    .build();

            // lemo_product_servicereginfo (서비스 등록)
            dao.insertServiceRegInfo(regVO);
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







}
