package kr.co.Lemo.service;

import kr.co.Lemo.dao.AdminDAO;
import kr.co.Lemo.domain.CouponVO;
import kr.co.Lemo.domain.ReviewVO;
import kr.co.Lemo.domain.UserVO;
import kr.co.Lemo.domain.search.Admin_SearchVO;
import kr.co.Lemo.repository.AdminRepo;
import kr.co.Lemo.utils.PageHandler;
import kr.co.Lemo.utils.SearchCondition;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class AdminService {

    private AdminDAO dao;
    private AdminRepo repo;

    /**
     * 관리자 회원 - 회원 목록
     * @since 2023/03/09
     * @param model
     * @param sc
     */
    public List<UserVO> selectUser(Model model, Admin_SearchVO sc){
        int totalCnt = dao.countUser(sc); // 전체 게시물 개수
        int totalPage = (int)Math.ceil(totalCnt / (double)sc.getPageSize());  // 전체 페이지의 수
        if(sc.getPage() > totalPage) sc.setPage(totalPage);

        PageHandler pageHandler = new PageHandler(totalCnt, sc); // 페이징 처리

        log.info("select User Service: "+sc.toString());

        List<UserVO> users = dao.selectUser(sc);

        model.addAttribute("users", users);
        model.addAttribute("ph", pageHandler);

        return users;
    }

    /**
     * 관리자 쿠폰 - 쿠폰 목록
     * @since 2023/03/12
     * @param model
     * @param sc
     */
    public List<CouponVO> selectCoupon(Model model, Admin_SearchVO sc){
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
     * 관리자 쿠폰 - 쿠폰 목록 소유 숙소 선택
     * @since 2023/03/12
     * @param user_id
     */
    public List<CouponVO> findAccOwned(String user_id){

        log.warn("service findAccOwned");

        return dao.selectAccOwned(user_id);
    }

    /**
     * 관리자 리뷰 - 리뷰 목록
     * @since 2023/03/14
     * @param model
     * @param sc
     */
    public List<ReviewVO> findAllReview(Model model, SearchCondition sc){
        int totalCnt = dao.countReview(sc);
        int totalPage = (int)Math.ceil(totalCnt / (double)sc.getPageSize());
        if(sc.getPage() > totalPage) sc.setPage(totalPage);

        PageHandler pageHandler = new PageHandler(totalCnt, sc);

        List<ReviewVO> reviews = dao.selectReview(sc);

        log.warn("Selected reviews: " + reviews.toString());

        model.addAttribute("reviews", reviews);
        model.addAttribute("ph", pageHandler);

        return reviews;
    }


    /**
     * 관리자 리뷰 - 리뷰 보기
     * @since 2023/03/15
     * @param revi_id
     */
    public ReviewVO findReview(Integer revi_id) throws Exception{
        return dao.viewReview(revi_id);
    }

    /**
     * 관리자 쿠폰 - 쿠폰 등록
     * @since 2023/03/11
     * @param vo
    * */
    public void rsaveCupon(CouponVO vo) throws Exception {
        dao.insertCoupon(vo);
    }


    /**
     * 관리자 회원 - 메모 입력
     * @since 2023/03/10
     * @param memo
     * @param user_id
     */
    public int updateMemo(String memo, String user_id) { return dao.updateMemo(memo, user_id); }

    /**
     * 관리자 회원 - 회원 차단
     * @since 2023/03/10
     * @param user_id
     */
    public int updateIsLocked(String user_id) { return dao.updateIsLocked(user_id); }

    /**
     * 관리자 회원 - 회원 삭제
     * @since 2023/03/10
     * @param user_id
     */
    public int updateIsEnabled(String user_id){ return dao.updateIsEnabled(user_id); }

    /**
     * 관리자 쿠폰 - 쿠폰 삭제
     * @param cp_id
     */
    public int removeCoupon(String cp_id){

        log.warn("here4 service");

        return dao.deleteCoupon(cp_id);
    }






}
