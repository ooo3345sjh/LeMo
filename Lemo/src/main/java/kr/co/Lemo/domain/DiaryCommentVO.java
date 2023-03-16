package kr.co.Lemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @since 2023/03/15
 * @author 박종협
 * @apiNote lemo_diary_comment table VO
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiaryCommentVO {
    private int com_no;
    private int arti_no;
    private String user_id;
    private int com_pno;
    private String com_comment;
    private String com_regip;
    private String com_rdate;
    private String com_udate;

    // 추가
    // @since 2023/03/16
    private String nick;


}
