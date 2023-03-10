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
    private Integer no = 0;
    private String searchField;
    private String searchWord;

    // 황원진
    private String cs_cate;

    // 이원정
    private Integer searchIsEnabled;
    private Integer searchLevel;
    private Integer searchType;

    public String getQueryString(Integer page){
        // ?page=1&pageSize=10&option="T"&keyword="title"
        return getQueryString(page, no);
    }

    public String getQueryString(Integer page, Integer no){
        // ?page=1&pageSize=10&option="T"&page=10
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance()
                .queryParam("page", page)
                .queryParam("group", group);


        if (no != null && no != 0)
            builder.queryParam("no", no);

        getCsParam(builder);

        if(searchField != null && !searchWord.isBlank()){
            builder.queryParam("searchField", searchField)
                    .queryParam("searchWord", searchWord);
        }

//        sortGroup(group, builder);

//        getAdminParam(builder);

        return builder.toUriString();
    }

    /*
    private void sortGroup(String group, UriComponentsBuilder builder) {
        switch (group){
            case "admin":
                getAdminParam(builder);
                break;
            case "product":
                getAdminParam(builder);
                break;
            case "cs":
                getAdminParam(builder);
                break;
            case "diary":
                getAdminParam(builder);
                break;
        }
    }

     */

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

    // 황원진
    public void getCsParam(UriComponentsBuilder builder) {
        builder.queryParam("cs_cate", cs_cate);
    }

    // 이원정
    public void getAdminParam(UriComponentsBuilder builder){

        if(searchIsEnabled != null || searchLevel != null || searchType != null){
            builder.queryParam("searchIsEnabled", searchIsEnabled)
                    .queryParam("searchLevel", searchLevel)
                    .queryParam("searchType", searchType);
        }


    }

    public void getProductParam(UriComponentsBuilder builder){

    }

    public void getDiaryParam(UriComponentsBuilder builder){

    }
}