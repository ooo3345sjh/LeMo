package kr.co.Lemo.controller;

import kr.co.Lemo.domain.UserVO;
import kr.co.Lemo.service.MyInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;


/**
 * @since 2023/03/26
 * @author 서정현
 * @apiNote 나의 정보 controller(충돌 방지를 위해 임시로 만듬)
 */

@Slf4j
@Controller
@PropertySource(value = "classpath:title.properties", encoding = "UTF-8")
@RequiredArgsConstructor
@RequestMapping("my/")
public class MyInfoController {
    private final Environment environment;
    private final MyInfoService myService;
    private String myGroup = "title.my";

    // @since 2023/03/27
    @ResponseBody
    @PatchMapping("info/profile")
    public String uploadProfile(
            @RequestPart(value = "profileFile") MultipartFile photo,
            @AuthenticationPrincipal UserVO userVO
    ) throws  Exception {
        log.debug("MyService PATCH uploadProfile start...");
        log.debug(""+photo.toString());
        int result = myService.usaveProfile(photo, userVO);
        return result+"";
    }

    // @since 2023/03/27
    @ResponseBody
    @DeleteMapping("info/profile")
    public Map removeProfile(
            @AuthenticationPrincipal UserVO userVO
    ) {
        log.debug("MyService DELETE deleteProfile start...");
        Map map = new HashMap();
        map.put("result", myService.removeProfile(userVO));
        return map;
    }

}
