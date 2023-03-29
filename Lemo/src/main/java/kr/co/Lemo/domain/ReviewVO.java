package kr.co.Lemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private String res_checkIn;
    private String res_checkOut;
    private int acc_id;
    private int acc_review;
    private int acc_rate;
    private String revi_reply_rdate;
    private String revi_dateBF; // 리뷰 n일전 작성
    private String photo; // 프로필 사진

    /**
     * @since 2023/03/27
     * @author 박종협
     */
    private String acc_thumbs;
    private int province_no;

    // @since 2023/03/29
    private List<String> thumbs;

}
