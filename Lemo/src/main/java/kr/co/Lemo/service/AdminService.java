package kr.co.Lemo.service;

import kr.co.Lemo.dao.AdminDAO;
import kr.co.Lemo.domain.CsVO;
import kr.co.Lemo.domain.UserVO;
import kr.co.Lemo.repository.AdminRepo;
import kr.co.Lemo.utils.PageHandler;
import kr.co.Lemo.utils.SearchCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Service
public class AdminService {

    @Autowired
    private AdminDAO dao;

    @Autowired
    private AdminRepo repo;

    public List<UserVO> selectUser(Model model, SearchCondition sc){
        log.info("here1");
        int totalCnt = dao.countUser(sc); // 전체 게시물 개수
        log.info("here2");
        int totalPage = (int)Math.ceil(totalCnt / (double)sc.getPageSize());  // 전체 페이지의 수

        log.info("here3");
        if(sc.getPage() > totalPage) sc.setPage(totalPage);

        log.info("here4");
        PageHandler pageHandler = new PageHandler(totalCnt, sc); // 페이징 처리

        log.info("here: "+sc.toString());

        log.info("here5");
        List<UserVO> users = dao.selectUser(sc);

        log.info("here: "+sc.toString());

        log.info("here6");
        model.addAttribute("users", users);
        model.addAttribute("ph", pageHandler);

        log.info("here7");
        return users;
    }











}
