package kr.co.Lemo.dao;

import kr.co.Lemo.domain.ArticleDiaryVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @since 2023/03/11
 * @author 박종협
 * @apiNote MyDAO
 */
@Repository
@Mapper
public interface MyDAO {

    // @since 2023/03/11
    public int insertDiaryArticle(ArticleDiaryVO diaryVO);

    // @since 2023/03/11
    public void insertDiarySpot();
}