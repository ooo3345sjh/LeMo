package kr.co.Lemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @since 2023/03/08
 * @author 이원정
 * @apiNote lemo_member_user (회원) VO
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceCateVO {
    private int sc_no;
    private String sc_name;


    /* 추가필드 */
    private int srg_id;
}
