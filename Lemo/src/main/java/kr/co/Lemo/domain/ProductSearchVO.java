package kr.co.Lemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @since 2023/03/09
 * @author 이해빈
 * @apiNote product 검색 조건
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductSearchVO {

    private String keyword;
    private double lat;
    private double lng;


}
