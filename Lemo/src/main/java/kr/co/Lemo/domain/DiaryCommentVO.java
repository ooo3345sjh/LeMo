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
    // @since 2023/03/18
    private String com_replyId;
    // @since 2023/03/22
    private int com_state;
    // @since 2023/03/23
    private String before24H;
    // @since 2023/03/30
    private String photo;

}
