package kr.co.Lemo.service;

import kr.co.Lemo.dao.AdminDAO;
import kr.co.Lemo.domain.*;
import kr.co.Lemo.domain.search.Admin_SearchVO;
import kr.co.Lemo.utils.PageHandler;
import kr.co.Lemo.utils.SearchCondition;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class AdminService {

    private AdminDAO dao;
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
        model.addAttribute("totalCoupon", pageHandler.getTotalCnt());

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

        log.warn("ph : " + pageHandler.getTotalCnt());

        model.addAttribute("reviews", reviews);
        model.addAttribute("ph", pageHandler);
        model.addAttribute("totalReview", pageHandler.getTotalCnt());

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
     * 관리자 객실 - 객실 목록
     * @since 2023/03/28
     * @param model
     * @param sc
     */
    public List<ProductRoomVO> findAllRoom(Model model, SearchCondition sc){
        int totalCnt = dao.countRoom(sc);
        int totalPage = (int)Math.ceil(totalCnt / (double)sc.getPageSize());
        if(sc.getPage() > totalPage) sc.setPage(totalPage);

        PageHandler pageHandler = new PageHandler(totalCnt, sc);

        List<ProductRoomVO> rooms = dao.selectRoom(sc);

        log.warn("Selected rooms: " + rooms.toString());
        log.warn("ph : " + pageHandler.getTotalCnt());

        model.addAttribute("rooms", rooms);
        model.addAttribute("ph", pageHandler);
        model.addAttribute("totalRoom", pageHandler.getTotalCnt());

        return rooms;
    }

    /**
     * 관리자 객실 - 객실 보기
     * @since 2023/03/29
     * @param room_id
     */
    public ProductRoomVO findRoom(Integer room_id) throws Exception {
        return dao.viewRoom(room_id);
    }

    /**
     * 관리자 숙소 - 숙소 목록
     * @since 2023/03/29
     * @param model
     * @param sc
     */
    public List<ProductAccommodationVO> findAllAccs(Model model, SearchCondition sc){
        int totalCnt = dao.countAccs(sc);
        int totalPage = (int)Math.ceil(totalCnt / (double)sc.getPageSize());
        if(sc.getPage() > totalPage) sc.setPage(totalPage);

        PageHandler pageHandler = new PageHandler(totalCnt, sc);

        List<ProductAccommodationVO> accs = dao.selectAccs(sc);

        log.warn("Selected accs: " + accs.toString());
        log.warn("ph : " + pageHandler.getTotalCnt());

        model.addAttribute("accs", accs);
        model.addAttribute("ph", pageHandler);
        model.addAttribute("totalAccs", pageHandler.getTotalCnt());

        return accs;
    }

    /**
    * 관리자 숙소 - 지역 선택
    * @since 2023/03/30
    */
    public List<ProvinceVO> findProvince(){ return dao.selectProvince();}

    /**
    * 관리자 숙소 - 숙소 보기
    * @since 2023/03/30
    * @param acc_id
    */
     public ProductAccommodationVO findAcc(Integer acc_id) throws Exception {
        return dao.viewAcc(acc_id);
     }

    /**
     * 관리자 숙소 - 숙소 보기 - 서비스
     * @since 2023/03/30
     * @param acc_id
     */
     public List<ServicereginfoVO> findServiceInAcc(Integer acc_id){

         log.warn("서비스 카테 here2");

         return dao.selectServiceInAcc(acc_id);
     }

    /**
     * 관리자 예약 목록
     * @since 2023/03/30
     * @param model
     * @param sc
     */
     public List<ReservationVO> findAllReservaitons(Model model, SearchCondition sc){
         int totalCnt = dao.countReservations(sc);
         int totalPage = (int)Math.ceil(totalCnt / (double)sc.getPageSize());
         if(sc.getPage() > totalPage) sc.setPage(totalPage);

         PageHandler pageHandler = new PageHandler(totalCnt, sc);

         List<ReservationVO> reservations = dao.selectReservaitons(sc);

         log.warn("Selected reservations: " + reservations.toString());
         log.warn("ph : " + pageHandler.getTotalCnt());

         model.addAttribute("reservations", reservations);
         model.addAttribute("ph", pageHandler);
         model.addAttribute("totalReservations", pageHandler.getTotalCnt());

         return reservations;
     }

     public List<ReservationVO> findAllTimeline(Model model){
         List<ReservationVO> timelines = dao.selectTimeline();

         //log.warn("timelines : " + timelines);

         model.addAttribute("timelines", timelines);

         return timelines;
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
     * 관리자 예약 - 메모 입력
     * @since 2023/03/31
     * @param res_memo
     * @param res_no
     */
    public int usaveMemoInRes(String res_memo, String res_no){
        return dao.updateMemoInRes(res_memo, res_no);
    }

    /**
     * 관리자 회원 - 회원 차단
     * @since 2023/03/10
     * @param user_id
     */
    public int updateIsLocked(String user_id) { return dao.updateIsLocked(user_id); }

    /**
     * 관리자 회원 - 회원 차단 해제
     * @since 2023/03/23
     * @param user_id
     */
    public int usaveClear(String user_id){
        return dao.updateIsLockedClear(user_id);
    }

    /**
     * 관리자 리뷰 - 리뷰 답변
     * @since 2023/03/15
     * @param revi_reply
     * @param revi_id
     */
    public int usaveReply(String revi_reply, String revi_id){ return dao.updateReply(revi_reply, revi_id); }

    /**
     * 관리자 숙박 - 숙박 차단
     * @param acc_id
     */
    public int usaveDropAcc(String acc_id){
        return dao.updateDropAcc(acc_id);
    }

    /**
     * 관리자 숙박 - 숙박 차단 해제
     * @param acc_id
     */
    public int usaveClearAcc(String acc_id){
        return dao.updateClearAcc(acc_id);
    }

    /**
     * 관리자 쿠폰 - 쿠폰 삭제
     * @param cp_id
     */
    public int removeCoupon(String cp_id){

        log.warn("here4 service");

        return dao.deleteCoupon(cp_id);
    }

    /**
     * 관리자 리뷰 - 리뷰 삭제
     * @param revi_id
     */
    public int removeReview(String revi_id){
        return dao.deleteReview(revi_id);
    }

    /**
     * @since 2023/04/05
     * @param map
     * @apiNote 관리자 통계관리 일별 누적 판매량
     */
    public List<ReservationVO> findAllDaySales(Map map){
        return dao.selectDaySales(map);
    }

    /**
     * @since 2023/04/06
     * @param map
     * @apiNote 관리자 통계관리 월별 누적 판매량
     */
    public List<ReservationVO> findAllMonthSales(Map map){

        int total = 0;
        double percent = 0;

        List<ReservationVO> mp = dao.selectMonthSales(map);

        for(ReservationVO vo : mp){
            total += vo.getTot_res_price();
        }

        log.warn("total sales: " + total);

        for(ReservationVO vo : mp){
            double tot_month_percent = ((vo.getTot_res_price()+0.0)/(total+0.0))*100;

            log.warn("tot_month_percent: " + tot_month_percent);
            vo.setTot_month_percent(tot_month_percent);
        }

        // vo 확인용 로그 출력
        for(ReservationVO vo : mp){
            double test = vo.getTot_month_percent();
            log.warn("test : " + test);
        }
        return mp;
    }



    /**
    * @since 2023/04/06
    * @apiNote 관리자 카운트
     */
    public int countWeeksSales(){
        return dao.countWeeksSales();
    }
     public int countWeeksCancel(){
        return dao.countWeeksCancel();
     }
     public int countWeeksQna(){
        return dao.countWeeksQna();
     }
     public int countWeeksAcc(){
        return dao.countWeeksAcc();
     }
     public int countWeeksUser(){
        return dao.countWeeksUser();
     }

    /**
     * @since 2023/04/06
     * @param map
     * @apiNote 관리자 결제방법 결제 현황
     */
    public List<ReservationVO> findAllPayment(Map map){
        return dao.selectPayment(map);
    }


    /**
     * @since 2023/03/31
     * @author 박종협
     * @apiNote 매출 차트 테스트
     */
    public List<ReservationVO> findSales(Map map) {
        return dao.selectSales(map);
    }



}
