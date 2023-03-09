package kr.co.Lemo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity(debug = false)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig implements WebMvcConfigurer {

    @Autowired
    private ResourceLoader resourceLoader;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // 사이트 위변조 요청 방지
			.csrf().disable()

                // 인가(접근권한) 설정
                .authorizeHttpRequests(req ->
                        req.mvcMatchers("/**").permitAll()
                );

//                // 로그인 설정
//                .formLogin(login ->
//                        login.loginPage("/user/login")      // 로그인 페이지 경로 설정
//                                .loginProcessingUrl("/user/login")  // POST로 로그인 정보를 보낼 시 경로
//                );

        return http.build();
    }

//    @Bean
//    public PasswordEncoder PasswordEncoder () {
//        return new BCryptPasswordEncoder();
//    }

    @Bean
	public InMemoryUserDetailsManager userDetailsService() {
		UserDetails[] user = {
				User.withDefaultPasswordEncoder()
						.username("user")
						.password("1234")
						.roles("1")
						.build()
				,
				User.withDefaultPasswordEncoder()
						.username("seller")
						.password("1234")
						.roles("2")
						.build()
				,
				User.withDefaultPasswordEncoder()
						.username("admin")
						.password("1234")
						.roles("3")
						.build()
		};

		return new InMemoryUserDetailsManager(user);
	}


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/file/**")
                .addResourceLocations(resourceLoader.getResource("file:file/"));
    }

}
