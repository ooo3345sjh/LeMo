package kr.co.Lemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

/**
 * @since 2023/03/08
 * @author 박종협
 * @apiNote my/diary SpotVO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiarySpotVO {
    private int spot_no;
    private int arti_no;
    private String spot_longtitude;
    private String spot_lattitude;
    private Point spot_xy;
    private String spot_title;
    private String spot_content;
    private String spot_thumb;
    private String province_name;
    private String spot_addr;
}
