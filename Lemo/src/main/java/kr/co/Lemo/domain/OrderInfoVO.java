package kr.co.Lemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @since 2023/03/30
 * @author 이해빈
 * @apiNote 결제 관련 데이터 vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderInfoVO {
    private String point; // 사용한 포인트
    private String cp_id; // 쿠폰 아이디
    private String name; // 예약자 이름
    private String hp; // 예약자 번호
    private String merchant_uid; // 결제 관련 아이디
    private String imp_uid; // 결제 고유 아이디
    private String payment; // 지불 방법
    private int amount; // 실제 결제 금액
    private int disprice; // 할인받은 금액 (쿠폰 + 포인트)
    private int user_point; // 유저가 실제로 보유한 포인트
    private int cp_disprice; // 쿠폰 할인가격
    private String checkIn; // 체크인 날짜
    private String checkOut; // 체크아웃 날짜
    private String checkInTime;
    private String checkOutTime;
    private String user_id; // 유저 아이디
    private int acc_id; // 숙소 아이디
    private int room_id; // 객실 아이디
    private int status; // 결제 상태

    private int days; // 묵은 날짜
    private String acc_name; // 숙소이름
    private int price; // 판매가격
    private String room_name;

    private int res_no; // 예약 번호
    private int mcp_id;

}
