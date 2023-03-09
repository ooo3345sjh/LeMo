package kr.co.Lemo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="lemo_member_userinfo")
public class UserEntity {

    @Id
    @Column(name="user_id", insertable = false, updatable = false)
    private String user_id;
    private String hp;
    private String nick;
    private String photo;
    private int type;
    private String role;
    private int level;
    private int point;
    private String regip;
    private int isEnabled;
    private int isLocked;
    private int isPassNonExpired;
    private int isNoticeEnabled;
    private int isLocationEnabled;
    private int isPrivacySelected;
    private String memo;
    private String udate;
    private String rdate;
    private String wdate;

}
