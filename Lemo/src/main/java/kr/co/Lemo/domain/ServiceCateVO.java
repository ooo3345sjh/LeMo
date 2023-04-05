package kr.co.Lemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @since 2023/03/08
 * @author 이원정
 * @apiNote lemo_product_serviceRegInfo(편의시설/서비스 카테고리) VO
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceCateVO {
    private int sc_no;
    private String sc_name;


    /* 추가필드 */
    private int srg_id;
}
