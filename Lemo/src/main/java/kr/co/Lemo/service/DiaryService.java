package kr.co.Lemo.service;

import kr.co.Lemo.dao.DiaryDAO;
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

    @Autowired
    private DiaryDAO dao;

    public Map<Integer, List<DiarySpotVO>> findDiaryArticle(){
        List<DiarySpotVO> spotVO = dao.selectDiaryArticle();

        Map<Integer, List<DiarySpotVO>> map = spotVO.stream().collect(Collectors.groupingBy(DiarySpotVO::getArti_no));

        return map;
    }

}
