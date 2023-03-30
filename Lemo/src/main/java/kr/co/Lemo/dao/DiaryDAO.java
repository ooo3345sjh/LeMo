package kr.co.Lemo.dao;

import kr.co.Lemo.domain.DiaryCommentVO;
import kr.co.Lemo.domain.DiarySpotVO;
import kr.co.Lemo.domain.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;
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

    // @since 2023/03/20
    public int insertDiaryOriComment(DiaryCommentVO commentVO);

    // @since 2023/03/16
    public int insertDiaryComment(DiaryCommentVO commentVO);

    // @since 2023/03/17
    public UserVO selectCommentNick(int com_no);

    // @since 2023/03/17
    public int deleteComment(int com_no);

    // @since 2023/03/20
    public int updateComment(DiaryCommentVO commentVO);

    // @since 2023/03/22
    public int updateOriComment(DiaryCommentVO commentVO);

    // @since 2023/03/30
    public int insertDiaryLike(@Param("arti_no") int arti_no, @Param("user_id") String user_id);
    public int updateDiaryLikePlus(int arti_no);
    public int updateDiaryMinusPlus(int arti_no);
    public int selectDiaryLike(@Param("arti_no") int arti_no, @Param("user_id") String user_id);
    public int deleteDiaryLike(@Param("arti_no") int arti_no, @Param("user_id") String user_id);
    public int updateDiaryHit(int arti_no);

}
