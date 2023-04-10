package kr.co.Lemo.repository;


import kr.co.Lemo.entity.WithdrawLogEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @since 2023/04/11
 * @author 서정현
 * @apiNote withdrawLog repository
 */
public interface WithdrawLogRepo extends MongoRepository<WithdrawLogEntity, String> {

}
