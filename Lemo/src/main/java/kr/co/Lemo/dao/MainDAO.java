package kr.co.Lemo.dao;

import kr.co.Lemo.domain.ArticleDiaryVO;
import kr.co.Lemo.domain.BusinessInfoVO;
import kr.co.Lemo.domain.ProductAccommodationVO;
import kr.co.Lemo.domain.UserVO;
import kr.co.Lemo.entity.UserInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @since 2023/03/29
 * @author 서정현
 * @apiNote main dao
 */

@Mapper
@Repository
public interface MainDAO {

    // insert



    // select
    // @since 2023/03/29
    List<ProductAccommodationVO> selectRevisit() throws Exception;
    List<ProductAccommodationVO> selectBest() throws Exception;
    List<ArticleDiaryVO> selectBestDiary() throws Exception;



    // update

    // delete


}
