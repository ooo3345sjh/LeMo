package kr.co.Lemo.security;

import kr.co.Lemo.utils.RemoteAddrHandler;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

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
    private DataSource dataSource;
    private RemembermeService remembermeService;
    private SecurityUserService securityUserService;
    private CustomAuthenticationTokenManager customAuthenticationTokenManager;

    // @since 2023/03/21
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 인가(접근권한) 설정
                .authorizeHttpRequests(req -> req
                                .mvcMatchers("/", "/index").permitAll()
                                .antMatchers("/my/**").authenticated()
                                .antMatchers("/admin/**").hasAnyRole("ADMIN")
                                .antMatchers("/business/**").hasAnyRole("BUSINESS")
//                        .mvcMatchers(
//                                "/user/terms", "/user/login", "/user/error", "/user/join", "/user/hp/auth", "/user/social/**",
//                                "/user/signup", "/user/pw/reset", "/user/email/send"
//                        ).hasRole("ANONYMOUS")
//                        .mvcMatchers("/admin/**").hasRole("ADMIN")
//                        .mvcMatchers("/business/**").hasRole("BUSINESS")
//                        .mvcMatchers("/", "/index", "/auth", "/cs/**", "/diary/**", "/user/**", "/product/**").permitAll()
//                        .anyRequest().authenticated()
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
                // 자동 로그인
                .rememberMe(rem -> rem
                        .rememberMeServices(rememberMeServices())
                )
                // 로그아웃 설정
                .logout(logout -> logout
                        .invalidateHttpSession(true)
                        .logoutUrl("/user/logout")
                        .logoutSuccessHandler((req, resp, auth) -> {
                            String fromUri = req.getHeader("Referer");
                            if(fromUri.contains("reset"))
                                resp.sendRedirect(req.getContextPath()+"/user/login");
                            else
                                resp.sendRedirect(fromUri);
                        })
                        .deleteCookies("JSESSIONID", "remember-me")
                )

                // 권한이 없는 uri 접근시 보여줄 페이지설정
                .exceptionHandling(e->e.accessDeniedPage("/accessDenied"));

                // 커스텀한 AuthenticationProvider를 추가 등록하는 방법
                AuthenticationManagerBuilder amb = http.getSharedObject(AuthenticationManagerBuilder.class);
                amb.authenticationProvider(customAuthenticationTokenManager)
                        .userDetailsService(securityUserService) // 기존에 있는 DaoAuthenticationProvider도 사용하기위해서는 UserDetailsService 등록이 필요
                ;

        return http.build();
    }

    @Bean
    PersistentTokenRepository tokenRepository(){
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
        repository.setDataSource(dataSource);
        try{
            repository.removeUserTokens("1");
        }catch(Exception ex){
            repository.setCreateTableOnStartup(true);
        }
        return repository;
    }

    @Bean
    PersistentTokenBasedRememberMeServices rememberMeServices(){
        PersistentTokenBasedRememberMeServices service =
                new PersistentTokenBasedRememberMeServices("hello", remembermeService, tokenRepository())
                {
                    // 권한이 없는 페이지 방문시 이미 로그인 되어있는데 다시 로그인 페이지로 가는 로직을
                    // accessDeniedPage로 이동시키기위한 로직
                    // RemembermeToken을 CustomAuthenticationToken으로 바꿔서 인증을 진행한다.
                    // 그래서 추가적으로 Provider와 token을 커스텀해서 만들어주어야한다.
                    @Override
                    protected Authentication createSuccessfulAuthentication(HttpServletRequest request, UserDetails user) {
                        CustomAuthenticationToken token = new CustomAuthenticationToken(user.getUsername(), user.getPassword());
                        token.setDetailes(RemoteAddrHandler.getRemoteAddr(request), request.getSession().getId());
                        return token;
                    }
                };

        service.setAlwaysRemember(true); // 항상 자동 로그인
        service.setTokenValiditySeconds(604800); // 일주일 유지

        return service;
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
    
    // @since 2023/03/31 이해빈 / double slash를 허용해주는 설정
//    public void configure(WebSecurity web) throws Exception {
//        web.httpFirewall(defaultHttpFirewall());
//    }
//
//    @Bean
//    public HttpFirewall defaultHttpFirewall() {
//        return new DefaultHttpFirewall();
//    }

}
