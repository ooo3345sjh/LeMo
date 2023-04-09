package kr.co.Lemo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Document(collection = "product_visitors_log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitorslogEntity {
    @Id
    private String id;
    private String acc_id;
    private String username;
    private String ip;
    private Date date;
    private String sessionid;
    private String device;
}
