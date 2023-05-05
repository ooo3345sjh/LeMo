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

        List<CouponVO> coupons = dao.selectCoupon(sc);

        model.addAttribute("coupons", coupons);
        model.addAttribute("ph", pageHandler);
        model.addAttribute("totalCoupon", pageHandler.getTotalCnt());

        return coupons;
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

        model.addAttribute("accs", accs);
        model.addAttribute("ph", pageHandler);
        model.addAttribute("totalAccs", pageHandler.getTotalCnt());
        model.addAttribute("totalCnt", totalCnt);

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

         model.addAttribute("reservations", reservations);
         model.addAttribute("ph", pageHandler);
         model.addAttribute("totalReservations", pageHandler.getTotalCnt());

         return reservations;
     }

     public List<ReservationVO> findAllTimeline(Model model){
         List<ReservationVO> timelines = dao.selectTimeline();

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
        return dao.deleteCoupon(cp_id);
    }

    /**
     * 관리자 리뷰 - 리뷰 삭제
     * @param revi_id
     */
    public int removeReview(String revi_id){
        return dao.deleteReview(revi_id);
    }


    // 관리자 메인
    // 관리자 - 메인 - 일별 누적 판매량 (당일)
    public List<ReservationVO> findAllTodaySales(Map map){return dao.selectTodaySales(map);}
    // 관리자 - 메인 - 취소 건수 (당일)
    public int countDayCancel() {return dao.countDayCancel();}
    // 관리자 - 메인 - 1:1문의
    public int countDayQna(){return dao.countDayQna();}
    // 관리자 - 메인 - 상품등록
    public int countDayAcc() {return dao.countDayAcc();}
    // 관리자 - 메인 - 회원가입
    public int countDayUser(){return dao.countDayUser(); }
    // 관리자 - 메인 - 판매량 그래프 (기간 고정)
    public List<ReservationVO> findAllDaySale(){return dao.selectDaySale();}
    //  관리자 - 메인 - 결제 방법 결제 현황
    public List<ReservationVO> findAllPaymentDay(Map map){return dao.selectPaymentDay(map);}
    // 관리자  - 메인 - 베스트 숙소
    public List<ProductAccommodationVO> findAllBestAcc(Map map){return dao.selectBestAcc(map);}



    // 관리자 - 통계관리 - 예약 건수 (기간 변동, 기본: 일주일)
    public int countWeeksSales(Map map){
        return dao.countWeeksSales(map);
    }
    // 관리자 - 취소 건수 (기간 변동, 기본: 일주일)
    public int countWeeksCancel(Map map){
    return dao.countWeeksCancel(map);
    }
    // 관리자 - 통계관리 - 1:1 문의수 (기간 변동, 기본: 일주일)
    public int countWeeksQna(Map map){
        return dao.countWeeksQna(map);
     }
    // 관리자 - 통계관리 - 상품 등록 수 (기간 변동, 기본: 일주일)
    public int countWeeksAcc(Map map){ return dao.countWeeksAcc(map); }
    // 관리자 - 회원가입 수 (기간 변동, 기본: 일주일)
    public int countWeeksUser(Map map){ return dao.countWeeksUser(map); }
    // 관리자 - 통계관리 - 일별 누적 판매량 (일주일)
    public List<ReservationVO> findAllDaySales(Map map){ return dao.selectDaySales(map); }
    // 관리자 - 통계관리 - 결제방법 결제 현황 (기간 변동)
    public List<ReservationVO> findAllPayment(Map map){ return dao.selectPayment(map); }
    // 관리자 - 월별 매출 근황 그래프(기간 고정, 4month)
    public List<ReservationVO> findAllMonthSales(Map map){

        int total = 0;
        double percent = 0;

        List<ReservationVO> mp = dao.selectMonthSales(map);

        for(ReservationVO vo : mp){
            total += vo.getTot_res_price();
        }


        for(ReservationVO vo : mp){
            double tot_month_percent = ((vo.getTot_res_price()+0.0)/(total+0.0))*100;

            vo.setTot_month_percent(tot_month_percent);
        }

        // vo 확인용 로그 출력
        for(ReservationVO vo : mp){
            double test = vo.getTot_month_percent();
        }
        return mp;
    }

    // 관리자 - 통계관리 연 판매량 그래프 (기간 고정, 1year)
    public List<ReservationVO> findAllYearSales(Map map){return dao.selectYearSales(map); }
























    /**
     * @since 2023/03/31
     * @author 박종협
     * @apiNote 매출 차트 테스트
     */
    public List<ReservationVO> findSales(Map map) {
        return dao.selectSales(map);
    }





}
