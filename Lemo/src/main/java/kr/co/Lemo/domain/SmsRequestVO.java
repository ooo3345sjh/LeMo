package kr.co.Lemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @since 2023/03/12
 * @author 서정현
 * @apiNote SmsRequestVO
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SmsRequestVO {
    String type;
    String contentType;
    String countryCode;
    String from;
    String content;
    List<MessageVO> messages;
}
