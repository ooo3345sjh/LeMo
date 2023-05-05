package kr.co.Lemo.dao;

import kr.co.Lemo.domain.*;
import kr.co.Lemo.utils.SearchCondition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @since  2023/03/13
 * @author 이원정
 * @apiNote 판매자 DAO
 */


@Repository
@Mapper
public interface BusinessDAO {

    // @since 2023/03/13 판매자 쿠폰 목록
   public List<CouponVO> selectCoupon(SearchCondition sc);

    // @since 2023/03/13 판매자 쿠폰 목록 페이징
    public int countCoupon(SearchCondition sc);

    // @since 2023/03/13
    public List<CouponVO> selectAccOwned(String user_id);

    // @since 2023/03/16 판매자 리뷰 목록
    public List<ReviewVO> selectReview(SearchCondition sc);

    // @since 2023/03/16 판매자 리뷰 목록 페이징
    public int countReview(SearchCondition sc);

    // @since 2023/03/16 판매자 리뷰 보기
    public ReviewVO viewReview(Integer revi_id);

    // @since 2023/03/16
    public List<ReviewVO> selectAccOwnedForReview(String user_id);

    // @since 2023/03/17
    public List<ServiceCateVO> selectService();

    // @since 2023/03/20
    public List<ProvinceVO> selectProvince();

    // @since 2023/03/23 판매자 숙박 목록
    public List<ProductAccommodationVO> selectAccForInfo(SearchCondition sc);

    // @since 2023/03/23 판매자 숙박 목록 페이징
    public int countAcc(SearchCondition sc);

    // @since 2023/03/23 판매자 소유 숙박 목록 보기
   public List<ProductAccommodationVO> selectAccOwnedForInfo(String user_id);

   // @since 2023/03/31 판매자 숙박 보기
    public ProductAccommodationVO viewAcc(Integer acc_id);

    // @since 2023/03/31 판매자 숙박 보기 - 서비스 카테고리
    public List<ServicereginfoVO> selectServiceInAcc(Integer acc_id);

    // @since 2023/04/02 판매자 객실 목록
    public List<ProductRoomVO> selectRoom(SearchCondition sc);

    // @since 2023/04/02 판매자 객실 목록 페이징
    public int countRoom(SearchCondition sc);

    // @since 2023/04/03 판매자 객실 보기
    public ProductRoomVO viewRoom(Integer room_id);

    // @since 2023/04/04 판매자 예약 정보 목록
    public List<ReservationVO> selectReservaitons(SearchCondition sc);

    // @since 2023/04/04 판매자 예약 정보 목록 페이징
    public int countReservations(SearchCondition sc);

    // @since 2023/04/14 판매자 - 메인 - 일별 누적 판매량 (당일)
    public List<ReservationVO> selectTodaySales(Map map);

    // @since 2023/04/15 판매자 - 메인 - 최신 리뷰 (당일)
    public List<ReviewVO> selectReviewLatest(Map map);

    // @since 2023/04/15 판매자 - 메인 - 문의수 (당일)
    public int countDayQna(Map map);

    // @since 2023/04/15 판매자 - 메인 - 리뷰수 (당일)
    public int countDayReview(Map map);

    // @since 2023/04/16 판매자 - 메인 - 숙소 리스트
    public List<ProductAccommodationVO> selectAccsList(Map map);

    // @since 2023/04/08 판매자 - 통계관리 - 일별 누적 판매량 (일주일)
    public List<ReservationVO> selectDaySales(Map map);

    // @since 2023/04/16 판매자 - 통계관리 - 당일 시간대별 판매량
    public List<ReservationVO> selectTimeSales(Map map);

     // @since 2023/04/015 판매자 - 월별 판매량 (4달 기준)
    public List<ReservationVO> selectMonthSales(Map map);

    // @since 2023/04/16 판매자 - 연별 판매량 (3년 기준)
    public List<ReservationVO> selectYearSales(Map map);

    // @since 2023/04/08 판매자 - 결제방법 결제 현황 (일주일)
    public List<ReservationVO> selectPayment(Map map);

    // @since 2023/04/10 판매자 - 객실별 예약 현황 (일주일)
    public List<ReservationVO> selectWeeksRoom(Map map);

    // @since 2023/04/26 판매자 - 객실 사진 가져오기  // 이해빈
    public String selectRoomThumb(String room_id);

    // @since 2023/04/08 판매자 - 예약 건수 (일주일/당일)
    public int countWeeksSales(Map map);
    public int countDaySales(Map map);

    // @since 2023/04/09 판매자 - 취소 건수 (일주일/당일)
    public int countWeeksCancel(Map map);
    public int countDayCancel(Map map);

    // @since 2023/04/15 판매자 미배정 객실 (전체 객실수/당일)
    public int countTotalRoom(Map map);
    public int countCheckInRoom(Map map);

     // @since 2023/04/09 판매자 - 1:1 문의수 (일주일)
    public int countWeeksQna(Map map);

    // @since 2023/04/09 판매자 - 상품 등록 건수
    public int countWeeksAcc(Map map);

    // @since 2023/04/27 판매자 - 통계관리 - 평점
    public List<ProductAccommodationVO> selectRates(Map map);

    // @since 2023/04/09 판매자 - 등록 리뷰 수 (일주일)
    public int countWeeksReview(Map map);

    // @since 2023/04/11 판매자 - 타임라인
    public List<ReservationVO> selectTimeline(Map map);

    // @since 2023/04/17 판매자 타임라인 - 모달 이벤트
    public List<ReservationVO> selectReservation(String res_no);

    // @since 2023/03/13 판매자 쿠폰 등록
    public void insertCoupon(Map<String, Object> param);

    // @since 2023/03/20 판매자 숙소 등록
    public int insertInfo(Map<String, Object> param);

    // @since 2023/03/20 판매자 숙소 등록 (할인율)
    public int insertRatePolicy(Map<String, Object> param);

    // @since 2023/03/20 판매자 숙소 등록 (서비스)
    public int insertServiceRegInfo(Map<String, Object> param);

    // @since 2023/04/02 판매자 객실 등록
    public int insertRoom(Map<String, Object> param);

    // @since 2023/04/05
    public int updateMemoInRes(@Param("res_memo") String res_memo, @Param("res_no") String res_no);

    // @since 2023/04/01 판매자 숙소 수정
    public int updateInfo(Map<String, Object> param);

    // @since 2023/04/01 판매자 숙소 수정 (할인율)
    public int updateRatePolicy(Map<String, Object> param);

    // @since 2023/03/16
    public int updateReply(@Param("revi_reply") String revi_reply, @Param("revi_id") String revi_id);

    /**
     * @since 2023/04/16
     * @author 이해빈
     * @apiNote 객실 수정
     */
    public int updateRoom(Map<String, Object> param);

    // @since 2023/04/01
    public int deleteServiceRegInfo(Map<String, Object> param);

    // @since 2023/03/13
    public int deleteCoupon(@Param("cp_id") String cp_id);

   // @since 2023/03/16
   public int deleteReview(@Param("revi_id") String revi_id);

   // @since 2023/03/23
   public int deleteAcc(@Param("acc_id") String acc_id);

   // @since 2023/04/04
   public int deleteRoom(@Param("room_id") String room_id);


 /**
  * @since  2023/04/05
  * @author 황원진
  * @apiNote 판매자 qna DAO
  */

  // @since 2023/04/05 판매자 상품관리 목록
  public List<ProductQnaVO> selectQnaList(SearchCondition sc);

  //@since 2023/04/05 판매자 상품관리 페이징
  public int countQnas(SearchCondition sc);

  //@since 2023/04/05 판매자 소유상품 목록
  public List<ProductQnaVO> selectAccOwnedForQna(String user_id);

  // @since 2023/04/07 판매자 상품관리 목록 선택삭제
  public int deleteQnaList(@Param("checkList") List<String> checkList);

  // @since 2023/04/08 판매자 상품관리 상세보기
  public ProductQnaVO selectQnaArticle(int qna_no);

  // @since 2023/04/08 판매자 상품관리 답변 등록
  public int updateQnaReply (@Param("qna_reply") String qna_reply, @Param("qna_no") int qna_no);

  // @since 2023/04/08 판매자 상품관리 답변 수정
  public int updateQnaUdate (@Param("qna_reply") String qna_reply, @Param("qna_no") int qna_no);
}
