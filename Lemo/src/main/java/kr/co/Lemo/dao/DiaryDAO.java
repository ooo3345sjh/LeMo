package kr.co.Lemo.dao;

import kr.co.Lemo.domain.DiarySpotVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @since 2023/03/14
 * @author 박종협
 * @apiNote DiaryDAO
 */

@Mapper
@Repository
public interface DiaryDAO {

    public List<DiarySpotVO> selectDiaryArticle();

}
