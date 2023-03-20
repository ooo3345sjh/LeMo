package kr.co.Lemo.test;

import kr.co.Lemo.domain.CsVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 *  2023/03/18
 *  황원진
 *  dropzone Test DAO
 * **/


@Mapper
@Repository
public interface testDAO {

    public int insertTestArticle(CsVO vo);
}
