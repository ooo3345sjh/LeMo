package kr.co.Lemo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @since 2023/04/11
 * @author 서정현
 * @apiNote withdraw_reasons_log entity
 */
@Document(collection = "withdraw_log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawLogEntity {
    @Id
    private String id;
    private String username;
    private String ip;
    private String text;
    private Date rdate;
}
