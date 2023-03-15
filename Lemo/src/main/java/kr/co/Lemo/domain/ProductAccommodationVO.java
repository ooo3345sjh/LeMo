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


    /* 추가 필드 room 정보 */
    private int price;
    private int roomstock;

}
