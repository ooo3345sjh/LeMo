package kr.co.Lemo.dao;

import kr.co.Lemo.domain.CsVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @since 2023/03/08
 * @author 황원진
 * @apiNote csDAO
 */

@Repository
public interface csDAO {
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
