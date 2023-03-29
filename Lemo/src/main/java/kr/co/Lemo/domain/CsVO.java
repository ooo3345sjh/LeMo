package kr.co.Lemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @since 2023/03/08
 * @author 황원진
 * @apiNote csVO
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CsVO {

    private int cs_no;
    private String user_id;
    private String cs_hp;
    private String cs_email;
    private String cs_cate;
    private String cs_type;
    private String cs_type_ko;
    private String cs_title;
    private String cs_content;
    private String cs_regip;
    private String cs_rdate;
    private String cs_reply;
    private String cs_eventbannerImg;
    private String cs_eventViewImg;
    private String cs_eventStart;
    private String cs_eventEnd;
    private int cs_eventState;

    /** 추가 **/
    private String nick;
    private String newName;
    private String cs_reply_date;
    private List<String> checkList;
    private String cs_eventMainBannerImg;

    public String getCs_type_ko(){
        String type = null;
        switch (cs_type) {
            case "event" :
                type = "이벤트";
                break;
            case "payment" :
                type = "예약/결제";
                break;
            case "cancel" :
                type = "취소/환불";
                break;
            case "benefit" :
                type = "혜택받기";
                break;
            case "use" :
                type = "이용문의";
                break;
            case "info" :
                type = "회원정보";
                break;
            case "review" :
                type = "리뷰";
                break;
            case "refund" :
                type = "환불신청";
                break;
            case "point" :
                type = "쿠폰/포인트";
                break;
            case "other" :
                type = "기타";
                break;
        }
        return type;
    };

    public void setCs_no(String cs_no){
        this.cs_no = Integer.parseInt(cs_no);
    }

}
