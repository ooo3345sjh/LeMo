package kr.co.Lemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @since 2023/03/08
 * @author 황원진
 * @apiNote csVO
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CsVO {

    private int cs_no;
    private String userld_id;
    private String cs_hp;
    private String cs_email;
    private String cs_cate;
    private String cs_type;
    private String cs_title;
    private String cs_content;
    private String cs_regip;
    private String cs_rdate;
    private String cs_reply;
    private String cs_eventbannerImg;
    private String cs_eventViewImg;
    private String cs_eventStart;
    private String cs_eventEnd;
    private int cs_eventState;

}
