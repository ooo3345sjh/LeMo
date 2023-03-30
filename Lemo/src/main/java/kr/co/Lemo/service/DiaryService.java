package kr.co.Lemo.service;

import kr.co.Lemo.dao.DiaryDAO;
import kr.co.Lemo.domain.DiaryCommentVO;
import kr.co.Lemo.domain.DiarySpotVO;
import kr.co.Lemo.domain.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @since 2023/03/14
 * @author 박종협
 * @apiNote DiaryService
 */

@Slf4j
@Service
@Transactional
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
        dao.updateDiaryHit(arti_no);

        return spotVO;
    }

    // @since 2023/03/15
    public Map<Integer, List<DiaryCommentVO>> findDiaryComment(int arti_no) {

        List<DiaryCommentVO> commentVO = dao.selectDiaryComment(arti_no);

        LocalDateTime now = LocalDateTime.now();

        String nowDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date d1 = null;
        Date d2 = null;

        for(DiaryCommentVO vo : commentVO) {
            try {
                d1 = format.parse(nowDate);
                d2 = format.parse(vo.getCom_rdate());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            int result = d2.compareTo(d1);

            if(result < 0) {
                long diff = d1.getTime() - d2.getTime();

                if(diff > 86400000) {
                    //log.debug("24시간 이후 작성");
                    vo.setBefore24H(vo.getCom_rdate().substring(0,10));
                }else if(diff < 86400000 && diff >= 3600000) {
                    //log.debug("24시간 이전 작성");
                    long diffHours = diff / (60 * 60 * 1000);
                    //log.debug(diffHours  + "시간 전");
                    vo.setBefore24H(diffHours  + "시간 전");
                }else if(diff < 3600000 && diff >= 60000) {
                    //log.debug("1시간 이전 작성");
                    long diffMinutes = diff / (60 * 1000);
                    //log.debug(diffMinutes + "분 전");
                    vo.setBefore24H(diffMinutes + "분 전");
                }else {
                    //log.debug("diff"+diff);
                    vo.setBefore24H("방금 전");
                }
            }
        }

        Map<Integer, List<DiaryCommentVO>> map = commentVO.stream().collect(Collectors.groupingBy(DiaryCommentVO::getCom_pno));
        Map<Integer, List<DiaryCommentVO>> sortedTreeMap = map.entrySet().stream().sorted(Map.Entry.comparingByKey())
        				.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));

        return sortedTreeMap;
    }

    // @since 2023/03/20
    public int rsaveOriComment(DiaryCommentVO commentVO) {
        return dao.insertDiaryOriComment(commentVO);
    }

    // @since 2023/03/16
    public int rsaveComment(DiaryCommentVO commentVO) {

        int result = dao.insertDiaryComment(commentVO);

        return result;
    }

    // @since 2023/03/17
    public UserVO findCommentNick(int com_no) {
        return dao.selectCommentNick(com_no);
    }

    // @since 2023/03/17
    public int removeComment(int com_no) {
        return dao.deleteComment(com_no);
    };

    // @since 2023/03/20
    public int usaveComment(DiaryCommentVO commentVO) {
        return dao.updateComment(commentVO);
    }

    // @since 2023/03/22
    public int usaveOriComment(DiaryCommentVO commentVO) {
        return dao.updateOriComment(commentVO);
    }

    // @since 2023/03/30
    public int rsaveUsaveDiary(int arti_no, String user_id) {
        int result = 0;

        result = dao.insertDiaryLike(arti_no, user_id);

        if(result == 1) { result = dao.updateDiaryLikePlus(arti_no); }

        return result;
    }
    public int findDiaryLike(int arti_no, String user_id) {
        return dao.selectDiaryLike(arti_no, user_id);
    }

    public int usaveRemoveDiary(int arti_no, String user_id) {
        int result = 0;

        result = dao.deleteDiaryLike(arti_no, user_id);

        if(result == 1) { dao.updateDiaryMinusPlus(arti_no); }

        return result;
    }
}
