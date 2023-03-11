package kr.co.Lemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Id;
import java.util.List;

/**
 * @since 2023/03/08
 * @author 박종협
 * @apiNote my/diary ArticleVO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleDiaryVO {

    private int arti_no;
    private int res_no;
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
    // @since 2023/03/10
    private List<MultipartFile> thumb;
}
