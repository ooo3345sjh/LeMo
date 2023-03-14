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
    private String group;

    // 황원진
    private String cs_cate;
    private String cs_type;

    // 이원정
    private Integer searchIsEnabled;
    private Integer searchLevel;
    private Integer searchType;
    private String searchCouponRole;
    private String searchAccName;

    // 이해빈
    private String keyword;
    private double lat; //위도
    private double lng; //경도

    private String accTypes;

    private String sort; // 정렬기준
    private int headcount; // 인원수
    private int maxPrice; // 최대가격
    private int minPrice; // 최소가격
    private String checkIn; // 체크인날짜
    private String checkOut; // 체크아웃날짜






    public String getQueryString(Integer page){
        // ?page=1&pageSize=10&option="T"&keyword="title"
        return getQueryString(page, no, group);
    }

    public String getQueryString(Integer page, Integer no, String group){
        // ?page=1&pageSize=10&option="T"&page=10
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance()
                .queryParam("page", page);

        if (no != null && no != 0)
            builder.queryParam("no", no);

        if(searchField != null && !searchWord.isBlank()){
            builder.queryParam("searchField", searchField)
                    .queryParam("searchWord", searchWord);
        }

        sortGroup(group, builder);

        return builder.toUriString();
    }



   private void sortGroup(String group, UriComponentsBuilder builder) {
        switch (group){
            case "admin":
                getAdminParam(builder);
                break;
            case "product":
                getProductParam(builder);
                break;
            case "cs":
                getCsParam(builder);
                break;
            case "diary":
                getDiaryParam(builder);
                break;
            case "adminCoupon":
                getAdminCouponParam(builder);
                break;
            case "businessCoupon":
                getBusinessCouponParam(builder);
            case "businessAccName":
                getBusinessAccNameParam(builder);
        }
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

    // 황원진
    public void getCsParam(UriComponentsBuilder builder) {
        builder.queryParam("cs_cate", cs_cate)
                .queryParam("cs_type", cs_type);
    }

    // 이원정
    public void getAdminParam(UriComponentsBuilder builder){
        if(searchIsEnabled != null){
            builder.queryParam("searchIsEnabled", searchIsEnabled);
        }else if(searchLevel != null){
            builder.queryParam("searchLevel", searchLevel);
        }else if(searchType != null){
            builder.queryParam("searchType", searchType);
        }else if(searchCouponRole != null){
            builder.queryParam("searchCouponRole", searchCouponRole);
        }
    }

    public void getAdminCouponParam(UriComponentsBuilder builder){
        if(searchCouponRole != null){
            builder.queryParam("searchCouponRole", searchCouponRole);
        }
    }

    public void getBusinessCouponParam(UriComponentsBuilder builder){
        if(searchCouponRole != null){
            builder.queryParam("searchCouponRole", searchCouponRole);
        }
    }

    public void getBusinessAccNameParam(UriComponentsBuilder builder){
        if(searchAccName != null){
            builder.queryParam("searchAccName", searchAccName);
        }
    }

    
    // 이해빈
    public void getProductParam(UriComponentsBuilder builder){

        if(keyword != null){
            builder.queryParam("keyword", keyword);
        }else {
            builder.queryParam("lat", lat)
                    .queryParam("lng", lng);
        }

        if(sort != null) {builder.queryParam("sort", sort);}
        if(headcount > 0) {builder.queryParam("headcount", headcount);}
        if(maxPrice > 0) {builder.queryParam("maxPrice", maxPrice);}
        if(minPrice > 0) {builder.queryParam("minPrice", minPrice);}
        if(checkIn != null) {builder.queryParam("checkIn", checkIn);}
        if(checkOut != null) {builder.queryParam("checkOut", checkOut);}
        if(accTypes != null) {builder.queryParam("accTypes", accTypes);}

    }

    public void getDiaryParam(UriComponentsBuilder builder){

    }
}