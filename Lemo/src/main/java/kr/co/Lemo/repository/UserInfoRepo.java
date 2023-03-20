package kr.co.Lemo.repository;


import kr.co.Lemo.entity.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserInfoRepo extends JpaRepository<UserInfoEntity, String> {
}
