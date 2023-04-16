package kr.co.Lemo.domain.search;

import kr.co.Lemo.utils.SearchCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.util.UriComponentsBuilder;

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
    private String sort;                    // 회원 검색 - 회원정렬 - 활성화 여부 (정상/차단/탈퇴)
    private String sortLevel;               // 회원 검색 - 회원정렬 - 등급 (일반/엘리트)
    private String sortType;                // 회원 검색 - 회원정렬 (홈회원/SNS회원)
    private String sortRdate;               // 회원 검색 - 회원정렬 (날짜)
    private String searchCouponRole;
    private String searchAccName;
    private Integer acc_id;
    private String searchAccType;
    private String searchIsNoticeEnabled;   // 회원 검색 - 알림동의여부
    private String searchAccProvince;       // 숙소 검색 (지역)
    private Integer province_no;
    private String user_id;                 // 판매자 - 숙박 목록

    /**
     * @since 2023/04/16
     * @apiNote 통계 데이터 기간 조회
     */
    private String periodType;              // 판매자 - 통계관리 - 기간별 조회 (기간 설정: 당일, 1개월, 1년)
    private String dateStart;               // 판매자 - 통계관리 - 기간별 직접 조회
    private String dateEnd;               // 판매자 - 통계관리 - 기간별 직접 조회

    public String getQueryString(){

        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();

        if(periodType != null){
            builder.queryParam("periodType", periodType);
            if("calen".equals(periodType)){
                builder.queryParam("dateStart", dateStart)
                        .queryParam("dateEnd", dateEnd);

            }
        }
        return builder.toUriString();
    }



}
