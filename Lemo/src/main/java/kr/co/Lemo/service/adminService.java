package kr.co.Lemo.service;

import kr.co.Lemo.dao.adminDAO;
import kr.co.Lemo.domain.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class adminService {

    @Autowired
    private adminDAO dao;

    public List<UserVO> selectUser(){
        return dao.selectUser();
    }

}
