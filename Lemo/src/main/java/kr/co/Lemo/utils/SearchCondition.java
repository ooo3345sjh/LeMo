package kr.co.Lemo.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchCondition {
    private Integer page = 1;
    private Integer pageSize = 10;
    private Integer no = 0;
    private String searchField;
    private String searchWord;
    private Map<String, String> map;


    public String getQueryString(Integer page){
        // ?page=1&pageSize=10&option="T"&keyword="title"
        return getQueryString(page, no);
    }

    public String getQueryString(Integer page, Integer no){
        UriComponentsBuilder builder = getDefaultBuilder(page, no);

        if(map != null){
            for(String key : map.keySet()){
                String value = map.get(key);

                if(!"page".equals(key) && !"no".equals(key) && !"searchField".equals(key) && !"searchWord".equals(key)){
                    if(value != null && !value.isBlank())
                        builder.queryParam(key, value);
                }
            }
        }

        return builder.toUriString();
    }

    public String getQueryString(){
        // ?page=1&pageSize=10&option="T"&keyword="title"
        return getQueryString(page);
    }

    public UriComponentsBuilder getDefaultBuilder(Integer page, Integer no){

        // ?page=1&pageSize=10&option="T"&page=10
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance()
                .queryParam("page", page);

        if (no != null && no != 0)
            builder.queryParam("no", no);

        if(searchField != null && !searchField.isBlank()){
            builder.queryParam("searchField", getSearchField())
                    .queryParam("searchWord", getSearchWord());
        }

        return builder;
    }

    public Integer getOffset() {
        return (page-1) * pageSize;
    }

    public void setPage(Integer page) {
        this.page = page == 0 ? 1:page;
    }



}