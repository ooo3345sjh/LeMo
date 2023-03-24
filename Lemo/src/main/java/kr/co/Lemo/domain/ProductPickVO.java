package kr.co.Lemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @since 2023/03/24
 * @author 이해빈
 * @apiNote 상품(숙소) 찜 VO
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductPickVO {

    private int acc_id;
    private String user_id;

}
