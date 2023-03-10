package kr.co.Lemo.dao;

import kr.co.Lemo.domain.ProductAccommodationVO;
import kr.co.Lemo.utils.SearchCondition;
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
    public int countTotal(SearchCondition sc);


    // @since 2022/03/09
    public List<ProductAccommodationVO> selectAccommodations(SearchCondition sc);

    // update

    // delete


}
