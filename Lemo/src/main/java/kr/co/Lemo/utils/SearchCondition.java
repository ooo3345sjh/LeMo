package kr.co.Lemo.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.util.UriComponentsBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchCondition {
    private Integer page = 1;
    private Integer pageSize = 10;
    private String cs_cate;
    private Integer no = 0;
    private String searchField;
    private String searchWord;
    private int offset;

    public String getQueryString(Integer page){
        // ?page=1&pageSize=10&option="T"&keyword="title"
        return getQueryString(page, no);
    }

    public String getQueryString(Integer page, Integer no){
        // ?page=1&pageSize=10&option="T"&page=10
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance()
                .queryParam("page", page);


        if (no != null && no != 0)
            builder.queryParam("no", no);

        getCsParam(builder);

        if(searchField != null && !searchWord.isBlank()){
            builder.queryParam("searchField", searchField)
                    .queryParam("searchWord", searchWord);
        }



        return builder.toUriString();
    }

    public String getQueryString(){
        // ?page=1&pageSize=10&option="T"&keyword="title"
        return getQueryString(page);
    }

    public Integer getOffset() {
        return (page-1) * pageSize;
    }

    public void setPage(Integer page) {
        this.page = page == 0 ? 1:page;
    }

    public void getCsParam(UriComponentsBuilder builder){
        builder.queryParam("cs_cate", cs_cate);
    }
}