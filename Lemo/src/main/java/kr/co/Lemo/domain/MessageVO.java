package kr.co.Lemo.domain;

import lombok.*;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @since 2023/03/12
 * @author 서정현
 * @apiNote MessageVO
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageVO {
    String to;
    String content;
}
