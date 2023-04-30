package kr.co.Lemo.service;

import kr.co.Lemo.dao.DiaryDAO;
import kr.co.Lemo.domain.*;
import kr.co.Lemo.utils.SearchCondition;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
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

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    // @since 2023/03/14
    public List<ArticleDiaryVO> findDairyArticles(SearchCondition sc) {
        List<ArticleDiaryVO> articles = dao.selectDiaryArticles(sc);
        List<DiarySpotVO> spots = dao.selectDiarySpots();
        List<ArticleDiaryVO> picks = dao.selectDiaryLikes(sc);

        Map<Integer, List<DiarySpotVO>> map = spots.stream().collect(Collectors.groupingBy(DiarySpotVO::getArti_no));
        Map<Integer, List<ArticleDiaryVO>> maps = picks.stream().collect(Collectors.groupingBy(ArticleDiaryVO::getArti_no));

        for(ArticleDiaryVO artiVO : articles) {
            artiVO.setSpotVO(map.get(artiVO.getArti_no()));
            if(maps.get(artiVO.getArti_no()) != null) {
                artiVO.setUser_id(maps.get(artiVO.getArti_no()).get(0).getUser_id());
            }
        }
        return articles;
    }

    // @since 2023/03/15
    public List<DiarySpotVO> findDiarySpot(int arti_no) {

        List<DiarySpotVO> spotVO = dao.selectDiarySpot(arti_no);
        dao.updateDiaryHit(arti_no);

        return spotVO;
    }

    public List<DiarySpotVO> usaveDiarySpot(int arti_no) {
        List<DiarySpotVO> spotVO = dao.selectDiarySpot(arti_no);
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
                    vo.setBefore24H(vo.getCom_rdate().substring(0,10));
                }else if(diff < 86400000 && diff >= 3600000) {
                    long diffHours = diff / (60 * 60 * 1000);
                    vo.setBefore24H(diffHours  + "시간 전");
                }else if(diff < 3600000 && diff >= 60000) {
                    long diffMinutes = diff / (60 * 1000);
                    vo.setBefore24H(diffMinutes + "분 전");
                }else {
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
        dao.updateArticleCommentPlus(commentVO);
        return dao.insertDiaryOriComment(commentVO);
    }

    // @since 2023/03/16
    public int rsaveComment(DiaryCommentVO commentVO) {
        dao.updateArticleCommentPlus(commentVO);
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

    public int removeDiary(int arti_no) {
        dao.deleteDiarySpot(arti_no);
        dao.deleteDiaryComments(arti_no);
        dao.deleteDiaryLikes(arti_no);
        int result = dao.deleteDiaryArticle(arti_no);
        dao.updateArticleCommentMinus(arti_no);

        String path = new File(uploadPath+"diary/"+arti_no).getAbsolutePath();

        File deleteFolder = new File(path);

        try {
            FileUtils.cleanDirectory(deleteFolder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        deleteFolder.delete();

        return result;
    }

    // @since 2023/04/18
    public int findTotalDiarys(SearchCondition sc) {
        return dao.selectTotalDiarys(sc);
    }
}
