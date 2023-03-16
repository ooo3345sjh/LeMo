package kr.co.Lemo.service;

import kr.co.Lemo.dao.DiaryDAO;
import kr.co.Lemo.domain.DiaryCommentVO;
import kr.co.Lemo.domain.DiarySpotVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @since 2023/03/14
 * @author 박종협
 * @apiNote DiaryService
 */

@Slf4j
@Service
public class DiaryService {

    // @since 2023/03/14
    @Autowired
    private DiaryDAO dao;

    // @since 2023/03/14
    public Map<Integer, List<DiarySpotVO>> findDiaryArticle() {
        List<DiarySpotVO> spotVO = dao.selectDiaryArticle();

        Map<Integer, List<DiarySpotVO>> map = spotVO.stream().collect(Collectors.groupingBy(DiarySpotVO::getArti_no));

        return map;
    }

    // @since 2023/03/15
    public List<DiarySpotVO> findDiarySpot(int arti_no) {

        List<DiarySpotVO> spotVO = dao.selectDiarySpot(arti_no);

        return spotVO;
    }

    // @since 2023/03/15
    public Map<Integer, List<DiaryCommentVO>> findDiaryComment(int arti_no) {

        List<DiaryCommentVO> commentVO = dao.selectDiaryComment(arti_no);
        Map<Integer, List<DiaryCommentVO>> map = commentVO.stream().collect(Collectors.groupingBy(DiaryCommentVO::getCom_pno));

        return map;
    }

    // @since 2023/03/16
    public int rsaveComment(DiaryCommentVO commentVO) {

        int result = dao.insertDiaryComment(commentVO);

        return result;
    }

}
