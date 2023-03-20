package kr.co.Lemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @since 2023/03/18
 * @author 이해빈
 * @apiNote 상품 숙소 vo
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRoomVO {
    private int room_id;
    private int acc_id;
    private String room_name;
    private int room_stock;
    private int room_defNum;
    private int room_maxNum;
    private int room_addPrice;
    private int room_price;
    private String room_info;
    private String room_thumb;
    private String room_checkIn;
    private String room_checkOut;

}
