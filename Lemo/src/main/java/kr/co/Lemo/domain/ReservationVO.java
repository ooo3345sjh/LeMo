package kr.co.Lemo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    /**
     * @since 2023/04/07
     * @author 이원정
     * @apiNote JsonProperty 추가
     */
    @JsonProperty("groupId")
    public long res_no;
    private int acc_id;
    private int room_id;
    private String user_id;
    private int res_price;
    private int res_disPrice;
    @JsonProperty("title")
    private String res_name;
    private String res_hp;
    private int res_payment;
    private String res_date;
    @JsonProperty("start")
    private String res_checkIn;
    @JsonProperty("end")
    private String res_checkOut;
    private int res_state;

    @JsonProperty("color")
    private String color;

    @JsonProperty("textColor")
    private String textColor;
    public void setRes_state(int res_state) {
        this.res_state = res_state;

        if(res_state == 0){
            this.color = "#d7d7d7";
            this.textColor = "#252525";
        }else if(res_state == 1){
            this.color = "#BAD7E9";
            this.textColor = "#252525";
        }else if(res_state == 2){
            this.color = "#ff95a6";
            this.textColor = "#252525";
        }

    }

    private String res_memo;
    private String imp_uid;

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

    // @since 2023/03/30
    private int diaryStat;
    private int reviewStat;

    // @since 2023/03/31
    private int room_price;
    private int poi_point;
    private int dis_price;
    private int sales;
    private int acc_state;

    // @since 2023/04/05 @author 이원정
    private String acc_checkIn;
    private String acc_checkOut;
    private int tot_res_price;
    private double tot_month_percent;
    private int tot_month_sales;
    private int count_sales;
    private int room_stock;
    private int room_sales_percent;


    // @since 2023/04/06 @author 이해빈;
    private String payment;
    private int cp_disprice;
    private int res_month;


}
