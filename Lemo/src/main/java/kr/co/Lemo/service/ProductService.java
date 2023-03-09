package kr.co.Lemo.service;

import kr.co.Lemo.dao.ProductDAO;
import kr.co.Lemo.domain.ProductAccommodationVO;
import kr.co.Lemo.domain.ProductSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @since 2023/03/09
 * @author 이해빈
 * @apiNote product service
 */
@Service
public class ProductService {

    @Autowired
    private ProductDAO dao;

    // insert

    // select
    public List<ProductAccommodationVO> selectAccommodations(ProductSearchVO vo){
        return dao.selectAccommodations(vo);
    };

    // update

    // delete



}
