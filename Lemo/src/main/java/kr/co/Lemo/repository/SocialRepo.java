package kr.co.Lemo.repository;


import kr.co.Lemo.entity.SocialEntity;
import kr.co.Lemo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SocialRepo extends JpaRepository<SocialEntity, String> {
}
