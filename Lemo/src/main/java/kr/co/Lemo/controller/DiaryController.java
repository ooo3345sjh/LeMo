package kr.co.Lemo.controller;

import kr.co.Lemo.domain.DiaryCommentVO;
import kr.co.Lemo.domain.DiarySpotVO;
import kr.co.Lemo.service.DiaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * @since 2023/03/14
 * @author 박종협
 * @apiNote DiaryService
 */
@Controller
@Slf4j
@PropertySource(value = "classpath:title.properties", encoding = "UTF-8")
@RequiredArgsConstructor
@RequestMapping("diary/")
public class DiaryController {

    // @since 2023/03/14
    private final Environment environment;
    // @since 2023/03/14
    private String diaryGroup = "title.diary";
    // @since 2023/03/14
    private final DiaryService service;

    // @since 2023/03/14
    @GetMapping("list")
    public String list(Model m){
        log.debug("GET list start");
        m.addAttribute("title", environment.getProperty(diaryGroup));

        Map<Integer, List<DiarySpotVO>> map = service.findDiaryArticle();
        m.addAttribute("map", map);

        return "diary/list";
    }

    // @since 2023/03/14
    @GetMapping("view")
    public String view(Model m, int arti_no){
        log.debug("GET view start");
        m.addAttribute("title", environment.getProperty(diaryGroup));

        List<DiarySpotVO> spotVO = service.findDiarySpot(arti_no);
        m.addAttribute("article", spotVO);

        Map<Integer, List<DiaryCommentVO>> map = service.findDiaryComment(arti_no);

        return "diary/view";
    }

}
