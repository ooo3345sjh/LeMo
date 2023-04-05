package kr.co.Lemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @since 2023/04/04
 * @author 황원진
 * @apiNote Terms VO
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TermVO {

    private int terms_no;
    private int termsType_no;
    private String terms_title;
    private String terms_content;
    private String termsType_type_ko;
}
