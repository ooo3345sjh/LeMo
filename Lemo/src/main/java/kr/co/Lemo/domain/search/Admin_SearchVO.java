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

    private Integer searchIsEnabled;        // 회원 검색 - 계정활성화여부
    private Integer searchLevel;            // 회원 검색 - 회원등급 (일반/엘리트)
    private Integer searchType;             // 회원 검색 - 회원구분 (일반/판매자)
    private String sort;               // 회원 검색 - 회원정렬
    private String searchCouponRole;
    private String searchAccName;
    private Integer acc_id;
    private String searchAccType;
    private String searchIsNoticeEnabled;   // 회원 검색 - 알림동의여부


}
