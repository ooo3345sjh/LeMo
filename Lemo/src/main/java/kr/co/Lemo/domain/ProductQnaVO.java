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

    private int qna_no;
    private int acc_id;
    private String user_id;
    private String qna_title;
    private String qna_content;
    private String qna_regip;
    private String qna_rdate;
    private String qna_udate;
    private String qna_reply;
    private int qna_secret;

}
