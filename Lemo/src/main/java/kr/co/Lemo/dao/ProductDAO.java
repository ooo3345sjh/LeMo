package kr.co.Lemo.dao;

import kr.co.Lemo.domain.*;
import kr.co.Lemo.utils.SearchCondition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @since 2023/03/09
 * @author 이해빈
 * @apiNote product dao
 */

@Mapper
@Repository
public interface ProductDAO {

    // insert

    // @since 2023/03/21
    public int insertQna(ProductQnaVO qna);

    // @since 2023/03/24
    public int insertProductPick(Map map);

    // select
    public int countTotal(SearchCondition sc);

    // @since 2023/03/09
    public List<ProductAccommodationVO> selectAccommodations(SearchCondition sc);

    // @since 2023/03/17
    public List<ProductAccommodationVO> selectAccommodation(@Param("acc_id") int acc_id,
                                                      @Param("checkIn")String checkIn, @Param("checkOut") String checkOut);

    // @since 2023/03/19
    public List<ServiceCateVO> selectServiceCates(@Param("acc_id") int acc_id);

    // @since 2023/03/19
    public BusinessInfoVO selectBusinessInfo(@Param("user_id") String user_id);

    // @since 2023/03/20
    public List<ArticleDiaryVO> selectDiaries(@Param("acc_id") int acc_id);

    // @since 2023/03/22
    public List<ProductQnaVO> selectProductQnas(SearchCondition sc);

    // @since 2023/03/22
    public int getTotalProductQna(SearchCondition sc);

    // update

    // delete


}
