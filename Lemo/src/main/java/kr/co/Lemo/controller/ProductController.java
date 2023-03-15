package kr.co.Lemo.controller;

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
     */
    @GetMapping("list")
    public String list(Model model, @RequestParam Map map, @ModelAttribute Product_SearchVO vo) throws Exception {

        // 숙박업소 리스트 가져오기
        service.findAllAccommodations(model, map, vo);

        model.addAttribute("title", environment.getProperty(group));

        return "product/list";
    }

    @GetMapping("view")
    public String view() throws Exception{
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
}