package kr.co.Lemo.dao;

import kr.co.Lemo.domain.ProductAccommodationVO;
import kr.co.Lemo.domain.ProductSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @since 2023/03/09
 * @author 이해빈
 * @apiNote product dao
 */

@Mapper
@Repository
public interface ProductDAO {

    // insert

    // select
    public List<ProductAccommodationVO> selectAccommodations(ProductSearchVO vo);

    // update

    // delete


}
