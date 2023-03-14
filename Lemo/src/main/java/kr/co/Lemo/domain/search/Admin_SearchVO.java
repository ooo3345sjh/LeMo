package kr.co.Lemo.domain.search;

import kr.co.Lemo.utils.SearchCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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


}
