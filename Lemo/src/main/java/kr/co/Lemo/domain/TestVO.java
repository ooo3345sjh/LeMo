package kr.co.Lemo.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestVO {
    private Integer isEnabled;
    private Integer level;
    private Integer type;

}
