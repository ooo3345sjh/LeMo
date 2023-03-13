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
    private int headcount; // 인원수
    private int maxPrice; // 최대가격
    private int minPrice; // 최소가격
    private String checkIn; // 체크인날짜
    private String checkOut; // 체크아웃날짜
}
