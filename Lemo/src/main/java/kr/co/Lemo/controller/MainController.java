package kr.co.Lemo.controller;

import kr.co.Lemo.domain.BusinessInfoVO;
import kr.co.Lemo.domain.MessageVO;
import kr.co.Lemo.domain.SmsResponseVO;
import kr.co.Lemo.domain.UserVO;
import kr.co.Lemo.domain.search.SearchconditionTestVO_sjh;
import kr.co.Lemo.service.SmsService;
import kr.co.Lemo.service.UserService;
import kr.co.Lemo.utils.SearchCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

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

    @GetMapping(value = {"/", "/index"})
    public String index(Model m){
        m.addAttribute("title", environment.getProperty(group));
        return "index";
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
