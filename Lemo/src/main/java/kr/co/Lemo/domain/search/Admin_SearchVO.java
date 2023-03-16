package kr.co.Lemo.domain.search;

import kr.co.Lemo.utils.SearchCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @since 2023/03/14
 * @author 이원정
 * @apiNote Admin_SearchVO 관리자 검색 관련 vo
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Admin_SearchVO extends SearchCondition {

    private Integer searchIsEnabled;
    private Integer searchLevel;
    private Integer searchType;
    private String searchCouponRole;
    private String searchAccName;
    private Integer acc_id;


}
