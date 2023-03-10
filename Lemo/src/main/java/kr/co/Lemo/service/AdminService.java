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

    /**
     * 관리자 회원 - 회원 목록
     * @param model
     * @param sc
     */
    public List<UserVO> selectUser(Model model, SearchCondition sc){
        int totalCnt = dao.countUser(sc); // 전체 게시물 개수
        int totalPage = (int)Math.ceil(totalCnt / (double)sc.getPageSize());  // 전체 페이지의 수
        if(sc.getPage() > totalPage) sc.setPage(totalPage);

        PageHandler pageHandler = new PageHandler(totalCnt, sc); // 페이징 처리

        log.info("here: "+sc.toString());

        List<UserVO> users = dao.selectUser(sc);

        model.addAttribute("users", users);
        model.addAttribute("ph", pageHandler);

        return users;
    }

    /**
     * 관리자 회원 - 메모 입력
     * @param memo
     * @param user_id
     */
    public int updateMemo(String memo, String user_id) {
        return  dao.updateMemo(memo, user_id);
    }

    /**
     * 관리자 회원 - 회원 차단
     * @param user_id
     */
    public int updateIsLocked(String user_id) {
        return  dao.updateIsLocked(user_id);
    }

    /**
     * 관리자 회원 - 회원 삭제
     * @param user_id
     */
    public int updateIsEnabled(String user_id){
        return dao.updateIsEnabled(user_id);
    }








}
