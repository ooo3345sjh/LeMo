package kr.co.Lemo.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @since 2023/03/08
 * @author 황원진
 * @apiNote csDAO
 */

@Mapper
@Repository
public interface CsDAO {
    /** select **/
    public void selectEventArticles();
    public void selectFaqArticles();
    public void selectNoticeArticles();
    public void selectQnaArticles();
    public void selectTermsArticles();


    /** insert **/



    /** updare **/


    /** delete **/
}
