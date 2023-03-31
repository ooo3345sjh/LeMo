package kr.co.Lemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @since 2023/03/09
 * @author 이해빈
 * @apiNote 상품 숙소 vo
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductAccommodationVO {

    private int acc_id;
    private String user_id;
    private String acc_name;
    private int accType_no;
    private int province_no;
    private String acc_city;
    private String acc_zip;
    private String acc_addr;
    private String acc_addrDetail;
    private double acc_longtitude;
    private double acc_lattitude;
    private String acc_info;
    private String acc_mainInfo;
    private String acc_comment;
    private String acc_thumbs;
    private int acc_rate;
    private int acc_review;
    private int acc_discount;
    private String acc_checkIn;
    private String acc_checkOut;
    private int acc_season;

    // 추가
    private String acc_rdate;
    private int acc_state;


    /* 추가 필드 room 정보 */
    private int room_price;
    private int room_stock;
    private int room_id;

    private String room_thumb;
    private String room_name;
    private int room_addPrice;
    private String room_info;
    private String room_checkIn;
    private String room_checkOut;


    private int empty_room_stock;
    private int sum_room_stock;
    private String accType_type;
    private int rp_offSeason_weekday;
    private int rp_offSeason_weekend;
    private int rp_peakSeason_weekday;
    private int rp_peakSeason_weekend;

    private int avg_price;
    /* sql에서 가공해서 가져온 room용 checkIn, checkOut */
    private String checkInTime;
    private String checkOutTime;

    
    /**
     * @since 2023/03/27
     * @author 박종협
     */
    private String province_name;

    /**
     * @since 2023/03/27
     * @author 서정현
     * @apiNote 오늘 가격
     */
    private int now_price;
            
}
