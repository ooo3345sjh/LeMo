package kr.co.Lemo.domain.search;

import kr.co.Lemo.utils.SearchCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @since 2023/03/22
 * @author 이해빈
 * @apiNote ProductQna_SearchVO 상품 보기 - 문의하기
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDetail_SearchVO extends SearchCondition {
    private int acc_id;
    private String cate;

}
