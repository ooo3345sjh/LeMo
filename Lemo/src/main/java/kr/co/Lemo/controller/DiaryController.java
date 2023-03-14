package kr.co.Lemo.controller;

import kr.co.Lemo.domain.DiarySpotVO;
import kr.co.Lemo.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@PropertySource(value = "classpath:title.properties", encoding = "UTF-8")
@RequiredArgsConstructor
@RequestMapping("diary/")
public class DiaryController {

    private final Environment environment;
    private String diaryGroup = "title.diary";
    private final DiaryService service;

    @GetMapping("list")
    public String list(Model m){
        m.addAttribute("title", environment.getProperty(diaryGroup));

        Map<Integer, List<DiarySpotVO>> map = service.findDiaryArticle();
        m.addAttribute("map", map);

        return "diary/list";
    }

    @GetMapping("view")
    public String view(Model m){
        m.addAttribute("title", environment.getProperty(diaryGroup));

        return "diary/view";
    }

}
