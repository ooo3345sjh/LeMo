package kr.co.Lemo.domain;


import kr.co.Lemo.utils.SearchCondition_v2;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Field;
import java.util.Map;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchconditionTestVO_sjh extends SearchCondition_v2 {
    private String[] termsType_no;
    private String termsType_type_ko;
    private String termsType_type_en;
    private Map<String, String> map;

    @Override
    public String getQueryString(Integer page, Integer no) {

        UriComponentsBuilder builder = getDefaultBuilder(page, no);

        for(String key : map.keySet()){
            String value = map.get(key);

            if(!"page".equals(key) || !"no".equals(key)){
                if(value != null && !value.isBlank())
                    builder.queryParam(key, value);
            }
        }

        return builder.toUriString();
    }

    public String getTermsType_no() {
        return String.join(",", termsType_no);
    }
}
