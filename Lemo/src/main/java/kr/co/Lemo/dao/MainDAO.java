package kr.co.Lemo.dao;

import kr.co.Lemo.domain.*;
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
    List<CsVO> selectNotice() throws Exception;
    List<CsVO> selectEvent() throws Exception;



    // update

    // delete


}
