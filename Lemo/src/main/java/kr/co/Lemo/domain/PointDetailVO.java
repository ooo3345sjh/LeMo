package kr.co.Lemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @since 2023/03/13
 * @author 박종협
 * @apiNote lemo_member_point_detail_log VO
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PointDetailVO {
    private int poid_id;
    private String user_id;
    private String poid_content;
    private int poid_point;
    private int poi_save_id;
    private int poi_id;
    private Date poid_maximun;
    private Date poid_rdate;
}
