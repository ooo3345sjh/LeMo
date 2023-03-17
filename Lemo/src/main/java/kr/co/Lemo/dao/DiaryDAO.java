package kr.co.Lemo.dao;

import kr.co.Lemo.domain.DiaryCommentVO;
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

    // @since 2023/03/14
    public List<DiarySpotVO> selectDiaryArticle();

    // @since 2023/03/15
    public List<DiarySpotVO> selectDiarySpot(int arti_no);

    // @since 2023/03/15
    public List<DiaryCommentVO> selectDiaryComment(int arti_no);

    // @since 2023/03/16
    public int insertDiaryComment(DiaryCommentVO commentVO);

    // @since 2023/03/17
    public String selectCommentNick(int com_no);

    // @since 2023/03/17
    public int deleteComment(int com_no);

}
