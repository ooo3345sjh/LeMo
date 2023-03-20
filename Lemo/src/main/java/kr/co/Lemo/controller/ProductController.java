package kr.co.Lemo.controller;

import kr.co.Lemo.domain.BusinessInfoVO;
import kr.co.Lemo.domain.ProductAccommodationVO;
import kr.co.Lemo.domain.search.Product_SearchVO;
import kr.co.Lemo.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
                       String checkOut) throws Exception {

        log.debug("Get view start");

        // 숙소 정보 가져오기
        List<ProductAccommodationVO> rooms = service.findAccommodation(acc_id, checkIn, checkOut);

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

    // @since 2023/03/19
    @GetMapping("getview")
    public String getview(Model model,
                          int acc_id ,
                          String cate) throws Exception {

        log.debug("Get getview start");

        log.info("cate" + cate);
        log.info("acc_id" + acc_id);

        //List<ServiceCateVO> scs = service.findAllServiceCate(acc_id);
        //model.addAttribute("scs", scs);

        String user_id = "1000hyeok0819@naver.com";

        BusinessInfoVO bv = service.findBusinessInfo(user_id);


        log.info("bv : " + bv);

        return "product/data";
    }

}