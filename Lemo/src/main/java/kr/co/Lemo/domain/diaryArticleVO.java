package kr.co.Lemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @since 2023/03/08
 * @author 박종협
 * @apiNote my/diary ArticleVO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class diaryArticleVO {
    private int arti_no;
    private int res_no;
    private String arti_title;
    private String artil_thumb;
    private int arti_comment;
    private int arti_hit;
    private int arti_like;
    private String arti_rdate;
    private String arti_update;
    private String arti_regip;
    private String arti_start;
    private String arti_end;
}
