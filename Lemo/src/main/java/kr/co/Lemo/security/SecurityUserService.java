package kr.co.Lemo.security;

import kr.co.Lemo.dao.UserDAO;
import kr.co.Lemo.entity.UserInfoEntity;
import kr.co.Lemo.repository.UserInfoRepo;
import kr.co.Lemo.repository.UserRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @since 2023/03/21
 * @author 서정현
 * @apiNote SecurityUserService
 */

@Slf4j
@AllArgsConstructor
@Service
public class SecurityUserService implements UserDetailsService {

	private UserRepo userRepo;


	// @since 2023/03/21
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepo.findById(username).orElseThrow(()-> new UsernameNotFoundException(username));
	}

}
