package kr.co.Lemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @since 2023/03/20
 * @author 이원정
 * @apiNote lemo_product_servicereginfo (편의시설 서비스 등록정보) VO
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServicereginfoVO {

    private int srg_id;
    private int sc_no;
    private int acc_id;

    // 추가
    private String sc_name;

}
