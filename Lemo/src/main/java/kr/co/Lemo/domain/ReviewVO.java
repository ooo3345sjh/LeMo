package kr.co.Lemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @since 2023/03/14
 * @author 이원정
 * @apiNote lemo_product_review (상품 리뷰 ) VO
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewVO {

    private int revi_id;                // 리뷰아이디
    private Long res_no;                // 예약번호
    private int revi_rate;              // 리뷰점수
    private String revi_title;          // 리뷰제목
    private String revi_content;        // 리뷰내용
    private String revi_reply;          // 리뷰답변
    private String revi_regip;          // 작성 ip
    private String revi_rdate;          // 작성일
    private String revi_thumb;          // 객실사진

    // 추가 - 리뷰
    private String user_id;
    private String acc_name;
    private String room_name;

}
