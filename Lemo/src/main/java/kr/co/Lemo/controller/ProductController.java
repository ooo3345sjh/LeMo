package kr.co.Lemo.controller;

import kr.co.Lemo.domain.*;
import kr.co.Lemo.domain.search.ProductDetail_SearchVO;
import kr.co.Lemo.domain.search.Product_SearchVO;
import kr.co.Lemo.service.ProductService;
import kr.co.Lemo.utils.PageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @since 2023/03/08
 * @author 이해빈
 * @apiNote 상품 controller
 */

@Slf4j
@Controller
@PropertySource(value = "classpath:title.properties", encoding = "UTF-8")
@RequestMapping("product/")
public class ProductController {

    @Autowired
    private Environment environment;
    private String group = "title.product";

    @Autowired
    private ProductService service;

    /**
     * @since 2023/03/08
     * @param vo (상품 검색 조건 )
     * @Map map (상품 검색 조건)
     */
    @GetMapping("list")
    public String list(Model model,
                       @RequestParam Map map,
                       @ModelAttribute Product_SearchVO vo) throws Exception {
        log.debug("GET list start");

        // 숙박업소 리스트 가져오기
        service.findAllAccommodations(model, map, vo);

        model.addAttribute("title", environment.getProperty(group));

        return "product/list";
    }

    // @since 2023/03/17 상품 보기
    @GetMapping("view")
    public String view(Model model,
                       int acc_id,
                       String checkIn,
                       String checkOut,
                       @AuthenticationPrincipal UserVO myUser
                       ) throws Exception {


        log.debug("Get view start");

        String uid = "";
        if(myUser != null) {
            uid = myUser.getUser_id();
        }

        log.info("myUser : " + myUser);
        log.info("uid : " + uid);


        // 숙소 정보 가져오기
        List<ProductAccommodationVO> rooms = service.findAccommodation(acc_id, checkIn, checkOut);

        String user_id = rooms.get(0).getUser_id();
        
        // 서비스 카테 가져오기
        List<ServiceCateVO> scs = service.findAllServiceCate(acc_id);

        // 판매자 정보 가져오기
        BusinessInfoVO bv = service.findBusinessInfo(user_id);

        // 숙소 찜 여부 가져오기

        int result = 0;

        if(uid != ""){
            result = service.findProductPick(acc_id, uid);
        }

        model.addAttribute("result", result);
        model.addAttribute("uid", uid);
        model.addAttribute("scs", scs);
        model.addAttribute("bv", bv);
        model.addAttribute("rooms", rooms);
        model.addAttribute("checkIn", checkIn);
        model.addAttribute("checkOut", checkOut);

        return "product/view";
    }

    @GetMapping("reservation")
    public String reservation() throws Exception{
        return "product/reservation";
    }

    @GetMapping("result")
    public String result() throws Exception{
        return "product/result";
    }

    @GetMapping("detaildiary")
    public String detailDiary(Model model,
                              @RequestParam Map map,
                              @ModelAttribute ProductDetail_SearchVO vo) {


        log.info("vo : " +vo);

        vo.setMap(map);
        List<ArticleDiaryVO> diaries = service.findAllProductDiaries(vo);

        // 페이징
        int totalCnt = service.getTotalProductDiary(vo); // 전체 게시물 개수
        int totalPage = (int)Math.ceil(totalCnt / (double)vo.getPageSize());  // 전체 페이지의 수
        if(vo.getPage() > totalPage) vo.setPage(totalPage);

        PageHandler pageHandler = new PageHandler(totalCnt, vo);

        model.addAttribute("ph", pageHandler);
        model.addAttribute("diaries", diaries);

        return "product/data/detailDiary";
    }

    @GetMapping("detailreview")
    public String detailReview() {

        return "product/data/detailReview";
    }

    // @since 2023/03/22
    @GetMapping("detailqna")
    public String detailQna(Model model,
                              @RequestParam Map map,
                              @ModelAttribute ProductDetail_SearchVO vo,
                              @AuthenticationPrincipal UserVO myUser) throws Exception {

        String uid = "";
        if(myUser != null) {
            uid = myUser.getUser_id();
        }

        vo.setMap(map);
        List<ProductQnaVO> qnas = service.findAllProductQna(vo);

        // 페이징
        int totalCnt = service.getTotalProductQna(vo); // 전체 게시물 개수
        int totalPage = (int)Math.ceil(totalCnt / (double)vo.getPageSize());  // 전체 페이지의 수
        if(vo.getPage() > totalPage) vo.setPage(totalPage);

        PageHandler pageHandler = new PageHandler(totalCnt, vo);


        model.addAttribute("user_id", uid);
        model.addAttribute("totalCnt", totalCnt);
        model.addAttribute("ph", pageHandler);
        model.addAttribute("qnas", qnas);

        return "product/data/detailQna";
    }

    // @since 2023/03/22
    @ResponseBody
    @PostMapping("rsaveQna")
    public Map<String, Integer> rsaveQna(@RequestBody ProductQnaVO qna, HttpServletRequest req) {

        log.debug("Get rsaveQna start");

        int result = 0;

        qna.setQna_regip(req.getRemoteAddr());

        result = service.rsaveQna(qna);

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("result", result);

        return resultMap;
    }

    // @since 2023/03/24
    @ResponseBody
    @PostMapping("pick")
    public Map<String, Integer> pick(@RequestBody Map map) {

        log.debug("Get pick start");

        int result = 0;

        Boolean sts = (Boolean) map.get("status");

        if(!sts){ // 찜하기가 되어있지 않은 경우
            result = service.saveProductPick(map);
        } else if(sts){ // 찜하기가 된 경우
            result = service.deleteProductPick(map);
        }

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("result", result);
        return resultMap;
    }

    // @since 2023/03/19
    /*
    @GetMapping("loadData")
    public String loadData(Model model,
                           @RequestParam Map map,
                           @ModelAttribute ProductQna_SearchVO vo) throws Exception {

        log.debug("Get loadData start");

        String cate = vo.getCate();
        int acc_id = vo.getAcc_id();

        //log.info("cate : " + cate);

        // 리뷰
        if(cate.equals("review")){

            log.info("here");
            return "product/data/detailReview";

        }else if(cate.equals("diary")) {

            List<ArticleDiaryVO> articles = service.findAllDiary(acc_id);
            model.addAttribute("vos", articles);

            return "product/data/detailDiary";

        }else if(cate.equals("qna")) {
            //List<ProductQnaVO> qnas = service.findAllProductQna(vo);
            return "product/data/detailQna";
        }
        return "product/list";
    }*/

}