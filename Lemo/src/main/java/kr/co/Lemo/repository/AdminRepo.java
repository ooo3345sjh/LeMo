package kr.co.Lemo.repository;

import kr.co.Lemo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepo extends JpaRepository<UserEntity, Integer> {

    // 회원 권한 구분에 따른 정렬 (일반, 비즈니스, 관리자)
    //Page<UserEntity> findAllAndSortByRole(String role, Pageable pageable, Sort sort);
}
