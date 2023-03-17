package kr.co.Lemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @since 2023/03/17
 * @author 서정현
 * @apiNote lemo_member_businessinfo (비지니스정보) VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessVO {
    private String user_id;
    private String bis_company;
    private String bis_ceo;
    private String bis_openDate;
    private String bis_bizRegNum;
    private String bis_zip;
    private String bis_addr;
    private String bis_addrDetail;
}
