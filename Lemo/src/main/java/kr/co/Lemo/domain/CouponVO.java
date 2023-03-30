package kr.co.Lemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @since 2023/03/11
 * @author 이원정
 * @apiNote lemo_product_coupon (쿠폰) VO
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponVO {
    // lemo_product_coupon
    private int cp_id;
    private String user_id;
    private String user_role;
    private String cp_subject;
    private String cp_group;
    private String cp_type;
    private int cp_rate;
    private int cp_price;
    private int cp_disType;
    private int cp_minimum;
    private int cp_maximum;
    private String cp_start;
    private String cp_end;
    private int cp_daysAvailable;
    private int cp_limitedIssuance;
    private int cp_IssuedCnt;

    // lemo_member_coupon
    private int mcp_id;
    private int mcp_isUsed;
    private String mcp_start;
    private String mcp_end;
    private String mcp_rdate;

    // lemo_member_coupon_log
    private int cl_id;
    private int res_no;
    private int dis_price;
    private String cl_datetime;

    // add
    private String acc_name;

    // lemo_product_reservation;
    private int cp_dis_price;

}
