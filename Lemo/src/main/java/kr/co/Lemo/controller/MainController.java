package kr.co.Lemo.controller;

import kr.co.Lemo.domain.UserVO;
import kr.co.Lemo.entity.VisitorsLogEntity;
import kr.co.Lemo.repository.VisitorsLogRepo;
import kr.co.Lemo.service.MainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

/**
 * @since 2023/03/19
 * @author 서정현
 * @apiNote 메인 controller
 */
@Slf4j
@Controller
@PropertySource(value = "classpath:title.properties", encoding = "UTF-8")
@RequiredArgsConstructor
public class MainController {

    private final Environment environment;
    private String group = "title.main";
    private final MainService mainService;
    private final VisitorsLogRepo visitorslogRepo;

    // @since 2023/03/05
    @GetMapping(value = {"/", "/index"})
    public String index(Model m, Map map) throws Exception {

        mainService.findMain(map);
        m.addAttribute("title", environment.getProperty(group));
        m.addAttribute("map", map);
        return "index";
    }

    // @since 2023/03/23
    @GetMapping("/access/denied")
    public String accessDenied(Model m){
        m.addAttribute("status", 403);
        return "/error/4xx";
    }

    /**
     * @since 2023/03/19
     * @return 로그인한 회원정보 객체
     */
    @ResponseBody
    @GetMapping("auth")
    public String auth(){
        return SecurityContextHolder.getContext().getAuthentication().toString();
    }

}
