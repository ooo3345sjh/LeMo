package kr.co.Lemo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @since 2023/03/19
 * @author 서정현
 * @apiNote lemo_member_userinfo entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="lemo_member_userinfo")
public class UserInfoEntity{

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
