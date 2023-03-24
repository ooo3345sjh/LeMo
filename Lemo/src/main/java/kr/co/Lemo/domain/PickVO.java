package kr.co.Lemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @since 2023/03/24
 * @author 박종협
 * @apiNote lemo_product_pick 테이블 VO
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PickVO {

    private String acc_id;
    private String user_id;

    // 추가
    private List<ProductAccommodationVO> accomVO;
}
