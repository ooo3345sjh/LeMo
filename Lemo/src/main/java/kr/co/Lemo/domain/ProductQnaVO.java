package kr.co.Lemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @since 2023/03/08
 * @author 황원진
 * @apiNote productQnaVO
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductQnaVO {

    private int pQna_no;
    private int acc_id;
    private String userId_id;
    private String pQna_title;
    private String pQna_content;
    private String pQna_regip;
    private String pQna_rdate;
    private String pQna_udate;
    private String pQna_reply;

}
