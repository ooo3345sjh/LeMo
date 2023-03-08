package kr.co.Lemo.service;

import kr.co.Lemo.dao.CsDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @since 2023/03/08
 * @author 황원진
 * @apiNote csService
 */

@Service
public class CsService {

    @Autowired
    private CsDAO dao;

}
