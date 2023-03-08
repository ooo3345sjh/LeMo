package kr.co.Lemo.dao;

import kr.co.Lemo.domain.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface adminDAO {
    public List<UserVO> selectUser();
}