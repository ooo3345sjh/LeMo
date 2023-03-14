package kr.co.Lemo.domain.search;


import kr.co.Lemo.utils.SearchCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchconditionTestVO_sjh extends SearchCondition {
    private String termsType_no;
    private String termsType_type_ko;
    private String termsType_type_en;
}
