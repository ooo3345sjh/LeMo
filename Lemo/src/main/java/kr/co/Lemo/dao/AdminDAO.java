package kr.co.Lemo.dao;

import kr.co.Lemo.domain.*;
import kr.co.Lemo.utils.SearchCondition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/*
    날짜 : 2023/03/08
    이름 : 이원정
    내용 : 관리자 DAO
*/
@Repository
@Mapper
public interface AdminDAO {
    // @since 2023/03/09
    public List<UserVO> selectUser(SearchCondition sc);

    // @since 2023/03/09
    public int countUser(SearchCondition sc);

    // @since 2023/03/12
    public List<CouponVO> selectCoupon(SearchCondition sc);

    // @since 2023/03/12
    public int countCoupon(SearchCondition sc);

    // @since 2023/03/12
    public List<CouponVO> selectAccOwned(String user_id);

    // @since 2023/03/14
    public List<ReviewVO> selectReview(SearchCondition sc);

    // @since 2023/03/14
    public int countReview(SearchCondition sc);

    // @since 2023/03/15
    public ReviewVO viewReview(Integer revi_id);

    // @since 2023/03/28
    public List<ProductRoomVO> selectRoom(SearchCondition sc);

    // @since 2023/03/28
    public int countRoom(SearchCondition sc);

    // @since 2023/03/29
    public ProductRoomVO viewRoom(Integer room_id);

    // @since 2023/03/29
    public List<ProductAccommodationVO> selectAccs(SearchCondition sc);

    // @since 2023/03/29
    public int countAccs(SearchCondition sc);

     // @since 2023/03/29
    public List<ProvinceVO> selectProvince();

    // @since 2023/03/30
    public ProductAccommodationVO viewAcc(Integer acc_id);

    // @since 2023/03/30
    public List<ServicereginfoVO> selectServiceInAcc(Integer acc_id);

    // @since 2023/03/30
    public List<ReservationVO> selectReservaitons(SearchCondition sc);

    // @since 2023/03/30
    public int countReservations(SearchCondition sc);

    // @since 2023/04/07
    public List<ReservationVO> selectTimeline();

    // @since 2023/03/11
    public void insertCoupon(CouponVO vo);

    // @since 2023/03/10
    public int updateMemo(@Param("memo") String memo, @Param("user_id") String user_id);

    // @since 2023/03/31
    public int updateMemoInRes(@Param("res_memo") String res_memo, @Param("res_no") String res_no);

    // @since 2023/03/10
    public int updateIsLocked(@Param("user_id") String user_id);

    // @since 2023/03/23
    public int updateIsLockedClear(@Param("user_id") String user_id);

    // @since 2023/03/15
    public int updateReply(@Param("revi_reply") String revi_reply, @Param("revi_id") String revi_id);

    // @since 2023/03/30
    public int updateDropAcc(@Param("acc_id") String acc_id);

    // @since 2023/03/30
    public int updateClearAcc(@Param("acc_id") String acc_id);

    // @since 2023/03/12
    public int deleteCoupon(@Param("cp_id") String cp_id);

    // @since 2023/03/15
    public int deleteReview(@Param("revi_id") String revi_id);





    // 관리자 - 메인 - 일별 누적 판매량 (당일)
    public List<ReservationVO> selectTodaySales(Map map);
    // 관리자 - 메인 - 취소 건수 (당일)
    public int countDayCancel();
    // 관리자 - 메인 - 1:1문의
    public int countDayQna();
    // 관리자 - 메인 - 상품등록
    public int countDayAcc();
    // 관리자 - 메인 - 회원가입
    public int countDayUser();
    // 관리자 - 메인 - 판매량 그래프 (기간 고정)
    public List<ReservationVO> selectDaySale();
    //  관리자 - 메인 - 결제 방법 결제 현황
    public List<ReservationVO> selectPaymentDay(Map map);
    // 관리자  - 메인 - 베스트 숙소
    public List<ProductAccommodationVO> selectBestAcc(Map map);


    // 관리자 - 통계관리 - 예약 건수 (기간 변동, 기본: 일주일)
    public int countWeeksSales(Map map);
    // 관리자 - 통계관리 - 취소 건수 (기간 변동, 기본: 일주일)
    public int countWeeksCancel(Map map);
    // 관리자 - 통계관리 - 1:1 문의수 (기간 변동, 기본: 일주일)
    public int countWeeksQna(Map map);
    // 관리자 - 통계관리 - 상품 등록 수 (기간 변동, 기본: 일주일)
    public int countWeeksAcc(Map map);
    // 관리자 - 회원가입 수 (기간 변동, 기본: 일주일)
    public int countWeeksUser(Map map);
    // 관리자 - 통계관리 - 일별 누적 판매량 (일주일)
    public List<ReservationVO> selectDaySales(Map map);
    // 관리자 - 통계관리 - 결제방법 결제 현황 (기간 변동)
    public List<ReservationVO> selectPayment(Map map);
    // 관리자 - 월별 매출 근황 그래프(기간 고정, 4month)
    public List<ReservationVO> selectMonthSales(Map map);
    // 관리자 - 통계관리 연 판매량 그래프 (기간 고정, 1year)
    public List<ReservationVO> selectYearSales(Map map);



































    /**
     * @since 2023/03/31
     * @author 박종협
     * @apiNote 매출 차트 테스트
     */
    public List<ReservationVO> selectSales(Map map);

}