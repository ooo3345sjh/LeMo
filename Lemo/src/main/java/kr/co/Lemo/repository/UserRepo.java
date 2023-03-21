package kr.co.Lemo.repository;


import kr.co.Lemo.entity.UserEntity;
import kr.co.Lemo.entity.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepo extends JpaRepository<UserEntity, String> {
}
