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
public class Product_SearchVO extends SearchCondition {

    private String keyword;
    private double lat; //위도
    private double lng; //경도

    private String accTypes;

    private String sort; // 정렬기준
    private int headcount; // 인원수
    private int maxPrice; // 최대가격
    private int minPrice; // 최소가격
    private String checkIn; // 체크인날짜
    private String checkOut; // 체크아웃날짜

}
