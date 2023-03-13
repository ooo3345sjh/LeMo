package kr.co.Lemo.dao;

import kr.co.Lemo.domain.ArticleDiaryVO;
import kr.co.Lemo.domain.DiarySpotVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    public int insertDiarySpot(DiarySpotVO spotVO);

    // @since 2023/03/13
    public List<ArticleDiaryVO> selectDiaryArticle(String uid);

    // @since 2023/03/13
    public List<DiarySpotVO> selectDiarySpot(int arti_no);
}