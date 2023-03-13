package kr.co.Lemo.domain;

import lombok.*;

import java.time.LocalDateTime;

/**
 * @since 2023/03/12
 * @author 서정현
 * @apiNote SmsResponseVO
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SmsResponseVO {
    String requestId;
    LocalDateTime requestTime;
    String statusCode;
    String statusName;
    Integer code;
}