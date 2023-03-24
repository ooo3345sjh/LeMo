package kr.co.Lemo.security;

import kr.co.Lemo.dao.UserDAO;
import kr.co.Lemo.entity.SocialEntity;
import kr.co.Lemo.entity.UserEntity;
import kr.co.Lemo.entity.UserInfoEntity;
import kr.co.Lemo.repository.SocialRepo;
import kr.co.Lemo.repository.UserRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @since 2023/03/24
 * @author 서정현
 * @apiNote RemembermeService
 */
@Slf4j
@AllArgsConstructor
@Service
public class RemembermeService implements UserDetailsService {

	private UserRepo userRepo;
	private SocialRepo socialRepo;


	// @since 2023/03/24
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity = userRepo.findById(username).orElse(null);

		if(userEntity == null){
			SocialEntity socialEntity = socialRepo.findById(username).orElseThrow(()-> new UsernameNotFoundException(username));
			return socialEntity;
		}

		return userEntity;
	}


}
