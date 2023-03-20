package kr.co.Lemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @since 2023/03/20
 * @author 이원정
 * @apiNote lemo_product_province (도, 광역시) VO
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProvinceVO {

    private int province_no;
    private String province_name;

}
