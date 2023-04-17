package kr.co.Lemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @since 2023/03/13
 * @author 박종협
 * @apiNote lemo_member_point_log VO
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PointVO {
    private int poi_id;
    private String user_id;
    private long res_no;
    private String poi_content;
    private int poi_point;
    private String poi_maximum;
    private int poi_state;
    private String poi_rdate;

    // @since 2023/04/17
    private int poi_parent;
    private int usedExpirationPoint;
    private int insertPoint;

}
