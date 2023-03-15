package kr.co.Lemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

/**
 * @since 2023/03/08
 * @author 박종협
 * @apiNote lemo_diary_spot table VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiarySpotVO {
    private int spot_no;
    private int arti_no;
    private double spot_longtitude;
    private double spot_lattitude;
    private Point spot_xy;
    private String spot_title;
    private String spot_content;
    private String spot_thumb;
    private String province_name;
    private String spot_addr;

    // 추가
    // @since 2023/03/04
    private String arti_title;
    // @since 2023/03/14
    private String arti_start;
    // @since 2023/03/14
    private String arti_end;
    // @since 2023/03/15
    private String arti_rdate;
    private int arti_like;

}
