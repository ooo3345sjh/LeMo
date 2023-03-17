package kr.co.Lemo.service;

import kr.co.Lemo.dao.BusinessDAO;
import kr.co.Lemo.domain.CouponVO;
import kr.co.Lemo.domain.ReviewVO;
import kr.co.Lemo.domain.search.Admin_SearchVO;
import kr.co.Lemo.utils.PageHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

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
     * 관리자 리뷰 - 리뷰 보기
     * @since 2023/03/16
     * @param revi_id
     */
    public ReviewVO findReview(Integer revi_id) throws Exception{
        return dao.viewReview(revi_id);
    }


    /**
     * 판매자 쿠폰 - 쿠폰 등록
     * @since 2023/03/13
     * @param vo
     * */
    public void rsaveCupon(CouponVO vo) throws Exception {
        dao.insertCoupon(vo);
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
