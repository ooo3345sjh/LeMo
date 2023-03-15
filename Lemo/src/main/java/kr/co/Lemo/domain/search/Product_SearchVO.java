package kr.co.Lemo.domain.search;

import kr.co.Lemo.utils.SearchCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @since 2023/03/14
 * @author 이해빈
 * @apiNote Product_SearchVO 상품 검색 관련 vo
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product_SearchVO extends SearchCondition {

    private String keyword; // 검색 키워드
    private double lat; //위도
    private double lng; //경도
    private String accTypes; // 숙소유형
    private String sort; // 정렬기준
    private int headcount; // 인원수
    private int maxPrice; // 최대가격
    private int minPrice; // 최소가격
    private String checkIn; // 체크인날짜
    private String checkOut; // 체크아웃날짜
    private String b; // 지도 반경 좌표
    private int level;

}
