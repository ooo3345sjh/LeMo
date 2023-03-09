package kr.co.Lemo.service;

import kr.co.Lemo.dao.AdminDAO;
import kr.co.Lemo.domain.UserVO;
import kr.co.Lemo.repository.AdminRepo;
import kr.co.Lemo.utils.PageHandler;
import kr.co.Lemo.utils.SearchCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminDAO dao;

    @Autowired
    private AdminRepo repo;

    public List<UserVO> selectUser(Model model, SearchCondition sc){

        int totalCnt = dao.countUser(sc); // 전체 게시물 개수
        int totalPage = (int)Math.ceil(totalCnt / (double)sc.getPageSize());  // 전체 페이지의 수

        if(sc.getPage() > totalPage) sc.setPage(totalPage);
        PageHandler pageHandler = new PageHandler(totalCnt, sc); // 페이징 처리

        List<UserVO> users = dao.selectUser(sc);

        model.addAttribute("users", users);
        model.addAttribute("ph", pageHandler);

        return users;
    }

//    public Page<UserEntity> findAllAndSortByRole(String role, Pageable pageable, Sort sort){
//        return repo.findAllAndSortByRole(role, pageable, sort);
//    }

}
