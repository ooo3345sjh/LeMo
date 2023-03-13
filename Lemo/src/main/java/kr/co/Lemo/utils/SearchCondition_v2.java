package kr.co.Lemo.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchCondition_v2 {
    private Integer page = 1;
    private Integer pageSize = 10;
    private Integer no = 0;



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

}