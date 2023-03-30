package kr.co.Lemo.controller;

import kr.co.Lemo.domain.ArticleDiaryVO;
import kr.co.Lemo.domain.DiaryCommentVO;
import kr.co.Lemo.domain.DiarySpotVO;
import kr.co.Lemo.domain.UserVO;
import kr.co.Lemo.service.DiaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    // @since 2023/03/14
    @GetMapping("list")
    public String list(
            Model m,
            @RequestParam Map options
    ){
        log.debug("GET list start");
        m.addAttribute("title", environment.getProperty(diaryGroup));

        Map<Integer, List<DiarySpotVO>> map = service.findDiaryArticle(options);
        m.addAttribute("map", map);

        return "diary/list";
    }

    // @since 2023/03/14
    @GetMapping("view")
    public String view(Model m,
                       @RequestParam(defaultValue = "0") int arti_no
    ){
        log.debug("GET view start");
        m.addAttribute("title", environment.getProperty(diaryGroup));

        if(arti_no == 0) { return "redirect:/diary/list"; }
        m.addAttribute("arti_no", arti_no);

        List<DiarySpotVO> spotVO = service.findDiarySpot(arti_no);
        m.addAttribute("article", spotVO);

        Map<Integer, List<DiaryCommentVO>> map = service.findDiaryComment(arti_no);
        m.addAttribute("map", map);

        int mapTotal = 0;
        for(int total : map.keySet()) { mapTotal += map.get(total).size(); }
        m.addAttribute("total", mapTotal);

        return "diary/view";
    }

    // @since 2023/03/20
    @ResponseBody
    @PostMapping("oriComment")
    public ResponseEntity<Map<String, String>> rsaveOriComment(
            @RequestBody DiaryCommentVO commentVO,
            @AuthenticationPrincipal UserVO myUser,
            HttpServletRequest req
    ){
        log.debug("POST oriComment start");
        commentVO.setUser_id( myUser.getUser_id() );
        commentVO.setCom_replyId( myUser.getUser_id() );
        commentVO.setCom_regip(req.getRemoteAddr());

        int result = service.rsaveOriComment(commentVO);

        Map<String, String> map = new HashMap<>();
        map.put("result", Integer.toString(result));
        map.put("nick", myUser.getNick());
        map.put("com_no", Integer.toString(commentVO.getCom_no()));
        map.put("photo", myUser.getPhoto());

        return ResponseEntity.ok(map);
    }

    // @since 2023/03/16
    @ResponseBody
    @PostMapping("comment")
    public ResponseEntity<Map<String, String>> rsaveComment(
            @RequestBody DiaryCommentVO commentVO,
            @AuthenticationPrincipal UserVO myUser,
            HttpServletRequest req
    ) {
        log.debug("POST comment start");

        commentVO.setUser_id(myUser.getUser_id());
        commentVO.setCom_regip(req.getRemoteAddr());

        Map<String, String> map = new HashMap<>();

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
    }

    // @since 2023/03/17
    @ResponseBody
    @DeleteMapping("comment")
    public ResponseEntity<Integer> removeComment(@RequestBody DiaryCommentVO commentVO) {
        log.debug("DELETE comment start");

        int result = service.removeComment(commentVO.getCom_no());

        return ResponseEntity.ok(result);
    }

    // @since 2023/03/20
    @ResponseBody
    @PatchMapping("comment")
    public ResponseEntity<Integer> usaveComment(@RequestBody DiaryCommentVO commentVO, HttpServletRequest req) {
        log.debug("PATCH comment start");

        commentVO.setCom_regip(req.getRemoteAddr());
        int result = service.usaveComment(commentVO);

        return ResponseEntity.ok(result);
    }

    // @since 2023/0322
    @ResponseBody
    @PatchMapping("oriComment")
    public ResponseEntity<Integer> usaveOriComment(@RequestBody DiaryCommentVO commentVO, HttpServletRequest req) {
        log.debug("PATCH oriComment start");

        commentVO.setCom_regip(req.getRemoteAddr());

        int result = service.usaveOriComment(commentVO);

        return ResponseEntity.ok(result);
    }

    // @since 2023/03/30
    @ResponseBody
    @PatchMapping("like")
    public int usaveDiaryPick(
            @RequestBody ArticleDiaryVO diaryVO,
            @AuthenticationPrincipal UserVO myUser
    ) {
        log.debug("arti_no"+diaryVO.getArti_no());
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
        int arti_no = diaryVO.getArti_no();

        int result = service.removeDiary(arti_no);

        return result;
    }

    // @since 2023/03/31
    @ResponseBody
    @PostMapping("search")
    public void searchDiary(@RequestBody Map options) {
        log.debug("options : " + options);
        log.debug("keyword : " + options.get("keyword"));
        log.debug("orderBySearchType : " + options.get("orderBySearchType"));
        log.debug("orderByOption : " + options.get("orderByOption"));

        Map<Integer, List<DiarySpotVO>> map = service.findDiaryArticle(options);
        log.debug("map : " + map);

    }
}
