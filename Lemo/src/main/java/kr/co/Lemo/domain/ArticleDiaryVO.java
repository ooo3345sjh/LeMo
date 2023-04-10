package kr.co.Lemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @since 2023/03/08
 * @author 박종협
 * @apiNote lemo_diary_article table VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleDiaryVO {

    private int arti_no;
    private long res_no;
    private String user_id;
    private String arti_title;
    private String arti_thumb;
    private int arti_comment;
    private int arti_hit;
    private int arti_like;
    private String arti_rdate;
    private String arti_update;
    private String arti_regip;
    private String arti_start;
    private String arti_end;

    // 추가

    // @since 2023/03/12
    private List<DiarySpotVO> spotVO;

    // @since 2023/03/13
    private List<String> spot_thumb;
    
    // @since 2023/03/24 @author 이해빈
    private int acc_id;
    private String acc_name;

    /**
     * @since 2023/03/29
     * @author 서정현
     * @apiNote 회원 닉네임, 여행일기 중 하나의 내용
     */
    private String user_nick;
    private String spot_content;
    private Boolean status;
}
