package kr.co.Lemo.domain.search;

import kr.co.Lemo.utils.SearchCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @since 2023/03/14
 * @author 황원진
 * @apiNote Cs_SearchVO 고객센터 관련 VO
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cs_SearchVO extends SearchCondition {

    private String cs_cate;
    private String cs_type;
    private String user_id;
}
