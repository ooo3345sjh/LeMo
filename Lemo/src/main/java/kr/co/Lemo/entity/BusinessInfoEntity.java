package kr.co.Lemo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @since 2023/03/19
 * @author 서정현
 * @apiNote lemo_member_businessinfo entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="lemo_member_businessinfo")
public class BusinessInfoEntity {
    @Id
    @Column(name="user_id", insertable = false, updatable = false)
    private String user_id;
    private String bis_company;
    private String bis_ceo;
    private String bis_openDate;
    private String bis_bizRegNum;
    private String bis_tel;
    private String bis_zip;
    private String bis_addr;
    private String bis_addrDetail;
}
