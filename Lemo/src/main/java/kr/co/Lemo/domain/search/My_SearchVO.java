package kr.co.Lemo.domain.search;

import kr.co.Lemo.utils.SearchCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @since 2023/03/27
 * @author 박종협
 * @apiNote my페이지 공통 페이지 핸들러
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class My_SearchVO extends SearchCondition {

    private String user_id;
    private String myCate;

}
