package kr.co.Lemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @since 2023/03/14
 * @author 이원정
 * @apiNote lemo_product_review (상품 리뷰 ) VO
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewVO {

    private int revi_id;
    private int res_no;
    private int revi_rate;
    private String revi_title;
    private String revi_content;
    private String revi_reply;
    private String revi_regip;
    private String revi_rdate;
    private String revi_thumb;

}
