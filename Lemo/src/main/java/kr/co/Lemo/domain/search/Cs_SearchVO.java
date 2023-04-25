package kr.co.Lemo.domain.search;

import kr.co.Lemo.utils.SearchCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @since 2023/03/14
 * @author 황원진
 * @apiNote Cs_SearchVO 고객센터 관련 VO
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cs_SearchVO extends SearchCondition {

    private String cs_cate;
    private String cs_type;
    private String user_id;
    private int termsType_no;

    @Override
    public String getQueryString(Integer page, Integer no){
        UriComponentsBuilder builder = getDefaultBuilder(page, no);

        if(map != null){
            for(String key : map.keySet()){
                String value = map.get(key);

                if(!"page".equals(key) && !"no".equals(key) && !"searchField".equals(key)
                        && !"searchWord".equals(key) && !"cs_no".equals(key))
                {
                    if(value != null && !value.isBlank())
                        builder.queryParam(key, value);
                }
            }
        }

        return builder.toUriString();
    }
}
