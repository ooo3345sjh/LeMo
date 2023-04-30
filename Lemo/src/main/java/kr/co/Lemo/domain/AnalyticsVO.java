package kr.co.Lemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @since 2023/04/29
 * @author 이원정
 * @apiNote Google Analytics Data API 측정 Dimensions, Metrics
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnalyticsVO {

    private String country;
    private int activeUsers;

}
