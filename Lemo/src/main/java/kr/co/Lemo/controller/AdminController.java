package kr.co.Lemo.controller;

import kr.co.Lemo.domain.CsVO;
import kr.co.Lemo.repository.AdminRepo;
import kr.co.Lemo.service.AdminService;
import kr.co.Lemo.service.CsService;
import kr.co.Lemo.utils.SearchCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @since 2023/03/07
 * @author 이원정
 * @apiNote 관리자 controller
 */

@Slf4j
@Controller
@RequestMapping("admin/")
public class AdminController {

    @Autowired
    private AdminService service;

    @Autowired
    private AdminRepo repo;
    
    @Autowired
    private CsService csService;

    @GetMapping("index_admin")
    public String index_admin() {
        return "admin/index_admin";
    }

    @GetMapping("index_business")
    public String index_business() {
        return "admin/index_business";
    }

    @GetMapping("stats")
    public String stats() {
        return "admin/stats";
    }

    @GetMapping("user")
    public String user(Model model, SearchCondition sc) {

        service.selectUser(model, sc);

        return "admin/user";
    }

    @ResponseBody
    @PostMapping("updateMemo")
    public Map<String, Integer> updateMemo(String memo, String user_id) throws Exception {

        log.warn("here2");

        int result = service.updateMemo(memo, user_id);

        log.warn("here3: "+result);

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("result", result);

        log.warn("here4");

        return resultMap;
    }

    @ResponseBody
    @PostMapping("updateIsLocked")
    public Map<String, Integer> updateIsLocked(String user_id) throws Exception {
        int result = service.updateIsLocked(user_id);

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("result", result);

        return resultMap;
    }

    @ResponseBody
    @PostMapping("updateIsEnabled")
    public Map<String, Integer> updateIsEnabled(String user_id) throws Exception {
        int result = service.updateIsEnabled(user_id);

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("result", result);

        return resultMap;
    }


    @GetMapping("coupon/insertCoupon")
    public String insertCoupon() {
        return "admin/coupon/insertCoupon";
    }

    @GetMapping("coupon/manageCoupon")
    public String manageCoupon() {
        return "admin/coupon/manageCoupon";
    }


    @GetMapping("cs/event/list")
    public String event_list(){
        return "admin/cs/event/list";
    }

    @GetMapping("cs/event/modify")
    public String event_modify(){
        return "admin/cs/event/modify";
    }

    @GetMapping("cs/event/view")
    public String event_view(){
        return "admin/cs/event/view";
    }

    @GetMapping("cs/event/write")
    public String event_write(CsVO vo){

        return "admin/cs/event/write";
    }


    @GetMapping("cs/faq/list")
    public String faq_list(){
        return "admin/cs/faq/list";
    }

    @GetMapping("cs/faq/modify")
    public String faq_modify(){
        return "admin/cs/faq/modify";
    }

    @GetMapping("cs/faq/write")
    public String faq_write(){
        return "admin/cs/faq/write";
    }


    @GetMapping("cs/notice/list")
    public String notice_list(){
        return "admin/cs/notice/list";
    }

    @GetMapping("cs/notice/modify")
    public String notice_modify(){
        return "admin/cs/notice/modify";
    }

    @GetMapping("cs/notice/view")
    public String notice_view(){
        return "admin/cs/notice/view";
    }

    @GetMapping("cs/notice/write")
    public String notice_write(){
        return "admin/cs/notice/write";
    }


    @GetMapping("cs/qna/list")
    public String qna_list(){
        return "admin/cs/qna/list";
    }

    @GetMapping("cs/qna/view")
    public String qna_view(){
        return "admin/cs/qna/view";
    }

    @GetMapping("cs/terms/test")
    public String terms_list(){
        return "admin/cs/terms/test";
    }

    @GetMapping("cs/terms/write")
    public String terms_write(){
        return "admin/cs/terms/write";
    }


    @GetMapping("info/list")
    public String info_list(){
        return "admin/info/list";
    }

    @GetMapping("info/modify")
    public String info_modify(){
        return "admin/info/modify";
    }

    @GetMapping("info/view")
    public String info_view(){
        return "admin/info/view";
    }

    @GetMapping("info/write")
    public String info_write(){
        return "admin/info/write";
    }


    @GetMapping("reservation/list")
    public String reservation_list(){
        return "admin/reservation/list";
    }

    @GetMapping("reservation/timeline")
    public String reservation_timeline(){
        return "admin/reservation/timeline";
    }


    @GetMapping("review/list")
    public String review_list(){
        return "admin/review/list";
    }

    @GetMapping("review/view")
    public String review_view(){
        return "admin/review/view";
    }

    @GetMapping("roomInfo/list")
    public String roomInfo_list(){
        return "admin/roomInfo/list";
    }

    @GetMapping("roomInfo/modify")
    public String roomInfo_modify(){
        return "admin/roomInfo/modify";
    }

    @GetMapping("roomInfo/view")
    public String roomInfo_view(){
        return "admin/roomInfo/view";
    }

    @GetMapping("roomInfo/write")
    public String roomInfo_write(){
        return "admin/roomInfo/write";
    }




}
