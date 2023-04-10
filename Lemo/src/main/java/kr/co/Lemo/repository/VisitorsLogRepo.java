package kr.co.Lemo.repository;


import kr.co.Lemo.entity.VisitorsLogEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitorsLogRepo extends MongoRepository<VisitorsLogEntity, String> {

    @Query(value = "{'$and' : [{'date' : {'$gte' : ?0, '$lt' : ?1}}, {'acc_id' : ?2}]}", count = true)
    Integer countVisitors(LocalDateTime start, LocalDateTime end, String acc_id);

    @Query(value = "{'$and' : [{'date' : {'$gte' : ?0, '$lt' : ?1}}, {'acc_id' : ?2}]}")
    List<VisitorsLogEntity> selectVisitors(LocalDateTime start, LocalDateTime end, String acc_id);

    @Query(value = "{'$and' : [{'date' : {'$gte' : ?0, '$lt' : ?1}}, {'username' : ?2}, {'acc_id' : ?3}]}")
    List<VisitorsLogEntity> selectUsername(LocalDateTime start, LocalDateTime end, String username, String acc_id);

    @Query(value = "{'$and' : [{'date' : {'$gte' : ?0, '$lt' : ?1}}, {'sessionid' : ?2}, {'acc_id' : ?3}]}")
    List<VisitorsLogEntity> selectSessionId(LocalDateTime start, LocalDateTime end, String sessrionId, String acc_id);

    @Query(value = "{'$and' : [{'date' : {'$gte' : ?0, '$lt' : ?1}}, {'ip' : ?2}, {'acc_id' : ?3}]}")
    List<VisitorsLogEntity> selectIp(LocalDateTime start, LocalDateTime end, String ip, String acc_id);

}
