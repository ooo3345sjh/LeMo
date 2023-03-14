package kr.co.Lemo.utils;


import lombok.Data;

@Data
public class PageHandler {

    private SearchCondition sc;
    private int totalCnt;   // 총 게시물 갯수
    private int naviSize = 10;   // 페이지 내비게이션의 크기
    private int totalPage;  // 전체 페이지의 수
    private int beginPage;  // 내비게이션의 첫번째 페이지
    private int endPage;    // 내비게이션의 마지막 페이지
    private boolean showPrev; // 이전 페이지로 이동하는 링크를 보여줄 것인지의 여부
    private boolean showNext; // 다음 페이지로 이동하는 링크를 보여줄 것인지의 여부

    public PageHandler(int totalCnt, SearchCondition sc){
        this.totalCnt = totalCnt;
        this.sc = sc;
        doPaging(totalCnt, sc);
    }

    public void doPaging(int totalCnt, SearchCondition sc){
        this.totalCnt = totalCnt;

        totalPage = (int)Math.ceil(totalCnt / (double)sc.getPageSize()); // Math.ceil(255/10.0) = 26.0  -> (int) 캐스팅 = 26
        beginPage =  (sc.getPage() -1) / naviSize * naviSize + 1;
        endPage = Math.min(beginPage + naviSize - 1, totalPage);
        showPrev =  beginPage != 1;
        showNext = endPage != totalPage;
    }

    public void print(){
        System.out.println("page = " + sc.getPage());
        System.out.println("beginpage = " + beginPage);
        System.out.println("endpage = " + endPage);
        System.out.println(showPrev ? "[PREV] " : "");
        for(int i = beginPage; i <= endPage; i++){
            System.out.println(i + " ");
        }
        System.out.println(showNext ? "[NEXT]" : "");
    }

}