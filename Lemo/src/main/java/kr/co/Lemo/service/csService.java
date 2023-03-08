package kr.co.Lemo.service;

import kr.co.Lemo.dao.csDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @since 2023/03/08
 * @author 황원진
 * @apiNote csService
 */

@Service
public class csService {

    @Autowired
    private csDAO dao;

}
