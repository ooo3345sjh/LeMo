package kr.co.Lemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @since 2023/03/13
 * @author 박종협
 * @apiNote lemo_product_reservation VO
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationVO {
    private int res_no;
    private int acc_id;
    private int room_id;
    private String user_id;
    private int res_price;
    private int res_disPrice;
    private String res_name;
    private String res_hp;
    private int res_payment;
    private String res_date;
    private String res_checkIn;
    private String res_checkOut;
    private int res_state;
    private String res_memo;

    // 추가

    // @since 2023/03/27
    private int province_no;
    private String acc_thumbs;
    private String room_checkIn;
    private String room_checkOut;
    private String acc_name;

    // @since 2023/03/29
    private String type;
    private int night;
    private String room_name;


}
