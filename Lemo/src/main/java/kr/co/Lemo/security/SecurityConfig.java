package kr.co.Lemo.security;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 * @since 2023/03/21
 * @author 서정현
 * @apiNote SecurityConfig
 */
@EnableWebSecurity(debug = false)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {

    private ResourceLoader resourceLoader;
    private HomeLoginSuccessHandler homeLoginSuccessHandler;
    private SocialLoginSuccessHandler socialLoginSuccessHandler;
    private LoginFailureHandler loginFailureHandler;

    // @since 2023/03/21
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // 사이트 위변조 요청 방지
//			.csrf().disable()

                // 인가(접근권한) 설정
                .authorizeHttpRequests(req -> req
                                .mvcMatchers("/**").permitAll()
//                        .mvcMatchers("/", "/index").permitAll()
//                        .antMatchers("/my/**").authenticated()
                )

                // 로그인 설정
                .formLogin(login ->
                        login.loginPage("/user/login")           // 로그인 페이지 경로 설정
						     .loginProcessingUrl("/user/login")  // POST로 로그인 정보를 보낼 시 경로
                             .successHandler(homeLoginSuccessHandler)
                             .failureHandler(loginFailureHandler)
				)
                // sns 로그인 설정
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(socialLoginSuccessHandler)
                        .loginPage("/user/lgoin")
                )
                // 로그아웃 설정
                .logout(logout -> logout
                        .invalidateHttpSession(true)
                        .logoutUrl("/user/logout")
                        .logoutSuccessHandler((req, resp, auth) -> {
                            resp.sendRedirect(req.getHeader("Referer"));
                        })
                );

        return http.build();
    }

    // @since 2023/03/21
    @Bean
    public PasswordEncoder PasswordEncoder () {
        return new BCryptPasswordEncoder();
    }

    // @since 2023/03/21
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    // @since 2023/03/21
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**")
                .addResourceLocations(resourceLoader.getResource("file:img/"));
    }


}
