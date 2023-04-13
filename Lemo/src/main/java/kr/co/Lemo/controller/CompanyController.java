package kr.co.Lemo.controller;

import kr.co.Lemo.repository.VisitorsLogRepo;
import kr.co.Lemo.service.MainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @since 2023/03/19
 * @author 서정현
 * @apiNote 회사 controller
 */
@Slf4j
@PropertySource(value = "classpath:title.properties", encoding = "UTF-8")
@RequiredArgsConstructor
//@RequestMapping("/corp")
@Controller
public class CompanyController {

    private final Environment environment;
    private String group = "title.main";

    @GetMapping("/corp")
    public String index(){
        return "vue/index";
    }
}