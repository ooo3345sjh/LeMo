package kr.co.Lemo.security;

import kr.co.Lemo.dao.UserDAO;
import kr.co.Lemo.entity.UserInfoEntity;
import kr.co.Lemo.repository.UserInfoRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class SecurityUserService implements UserDetailsService {

	private UserInfoRepo userInfoRepo;
	private UserDAO userDAO;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userInfoRepo.findById(username).orElseThrow(()-> new UsernameNotFoundException(username));
	}

	public UserInfoEntity load(String username) {
		UserInfoEntity userInfo = null;
		try {
			userInfo = userDAO.findByEmail(username);
		} catch (Exception e){
			log.error(e.getMessage());
		}
		return userInfo;
	}

}
