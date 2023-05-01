package kr.co.Lemo.controller;

import kr.co.Lemo.domain.*;
import kr.co.Lemo.domain.search.My_SearchVO;
import kr.co.Lemo.service.DiaryService;
import kr.co.Lemo.service.MyService;
import kr.co.Lemo.utils.PageHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

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
    @Autowired
    private MyService myService;

    // @since 2023/03/14
    @GetMapping("list")
    public String list(
            Model m,
            @RequestParam Map options,
            @ModelAttribute My_SearchVO vo,
            @AuthenticationPrincipal UserVO myUser
    ){
        log.info("GET list start");
        m.addAttribute("title", environment.getProperty(diaryGroup));
        if(myUser != null) { vo.setUser_id(myUser.getUser_id()); }
        vo.setMap(options);

        List<ArticleDiaryVO> articles = service.findDairyArticles(vo);

        int totalDiary = service.findTotalDiarys(vo);
        int totalDiaryPage = (int)Math.ceil(totalDiary / (double)vo.getPageSize());
        if(vo.getPage() > totalDiaryPage) vo.setPage(totalDiaryPage);

        PageHandler qnaPageHandler = new PageHandler(totalDiary, vo);

        m.addAttribute("articles", articles);
        m.addAttribute("ph", qnaPageHandler);

        return "diary/list";
    }

    // @since 2023/03/14
    @GetMapping("view")
    public String view(Model m,
                       @RequestParam(defaultValue = "0") int arti_no,
                       @RequestParam(defaultValue = "0") long res_no
    ){
        log.info("GET view start");
        m.addAttribute("title", environment.getProperty(diaryGroup));

        if(arti_no == 0) { return "redirect:/diary/list"; }
        m.addAttribute("arti_no", arti_no);

        List<DiarySpotVO> spotVO = service.findDiarySpot(arti_no);
        m.addAttribute("article", spotVO);

        Map<Integer, List<DiaryCommentVO>> map = service.findDiaryComment(arti_no);
        m.addAttribute("map", map);

        ProductAccommodationVO accommo = myService.findeDiaryXY(res_no);
        m.addAttribute("accommo", accommo);

        int mapTotal = 0;
        for(int total : map.keySet()) { mapTotal += map.get(total).size(); }
        m.addAttribute("total", mapTotal);

        return "diary/view";
    }

    // @since 2023/03/20
    @ResponseBody
    @PostMapping("{commentType}")
    public ResponseEntity<Map<String, String>> rsaveOriComment(
            @PathVariable("commentType") String commentType,
            @RequestBody DiaryCommentVO commentVO,
            @AuthenticationPrincipal UserVO myUser,
            HttpServletRequest req
    ){
        log.info("POST "+ commentType +" start");
        commentVO.setUser_id( myUser.getUser_id() );
        commentVO.setCom_replyId( myUser.getUser_id() );
        commentVO.setCom_regip(req.getRemoteAddr());

        Map<String, String> map = new HashMap<>();

        if(commentType.equals("oriComment")) {
            int result = service.rsaveOriComment(commentVO);

            map.put("result", Integer.toString(result));
            map.put("nick", myUser.getNick());
            map.put("com_no", Integer.toString(commentVO.getCom_no()));
            map.put("photo", myUser.getPhoto());

            return ResponseEntity.ok(map);

        }else if(commentType.equals("comment")) {

            if(commentVO.getCom_pno() != 0) {
                UserVO userVO = service.findCommentNick(commentVO.getCom_no());
                commentVO.setCom_replyId(userVO.getUser_id());

                int result = service.rsaveComment(commentVO);

                map.put("user_id", myUser.getUser_id());
                map.put("nick", myUser.getNick());
                map.put("com_nick", userVO.getNick());
                map.put("result", Integer.toString(result));
                map.put("com_pno", Integer.toString(commentVO.getCom_no()));
                map.put("photo", myUser.getPhoto());
            }

            return ResponseEntity.ok(map);
        }else {
            map.put("result", "thereIsNoCommentType");
        }

        return ResponseEntity.ok(map);

    }

    // @since 2023/03/17
    @ResponseBody
    @DeleteMapping("{commentType}")
    public ResponseEntity<Integer> removeComment(@RequestBody DiaryCommentVO commentVO) {
        log.info("DELETE comment start");

        int result = service.removeComment(commentVO.getCom_no());

        return ResponseEntity.ok(result);
    }

    // @since 2023/03/20
    @ResponseBody
    @PatchMapping("{commentType}")
    public ResponseEntity<Integer> usaveComment(
            @PathVariable("commentType") String commentType,
            @RequestBody DiaryCommentVO commentVO,
            HttpServletRequest req
    ) {
        log.info("PATCH "+commentType+" start");

        if(commentType.equals("comment")) {

            commentVO.setCom_regip(req.getRemoteAddr());
            int result = service.usaveComment(commentVO);

            return ResponseEntity.ok(result);

        }else if(commentType.equals("oriComment")) {

            commentVO.setCom_regip(req.getRemoteAddr());

            int result = service.usaveOriComment(commentVO);

            return ResponseEntity.ok(result);
        }

        return ResponseEntity.ok(404);

    }

    // @since 2023/03/30
    @ResponseBody
    @PatchMapping("like")
    public int usaveDiaryPick(
            @RequestBody ArticleDiaryVO diaryVO,
            @AuthenticationPrincipal UserVO myUser
    ) {
        log.info("PATCH like start");

        int arti_no = diaryVO.getArti_no();
        Boolean status = diaryVO.getStatus();
        String user_id = myUser.getUser_id();

        if(status) {
            int total = service.findDiaryLike(arti_no, user_id);

            if(total > 0) {
                service.usaveRemoveDiary(arti_no, user_id);
                return 1;
            }

        }else {
            int total = service.findDiaryLike(arti_no, user_id);

            if(total == 0) {
                service.rsaveUsaveDiary(arti_no, user_id);
                return 1;
            }else {
                return 0;
            }
        }

        return 1;
    }

    @ResponseBody
    @DeleteMapping("article")
    public int removeDiary(
            @RequestBody ArticleDiaryVO diaryVO,
            @AuthenticationPrincipal UserVO myUser
    ) {
        log.info("PATCH article start");

        int arti_no = diaryVO.getArti_no();

        int result = service.removeDiary(arti_no);

        return result;
    }
}
