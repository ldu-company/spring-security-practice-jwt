package com.practice.spring.security.config;

import com.practice.spring.security.jwt.JwtAuthenticationFilter;
import com.practice.spring.security.jwt.JwtAuthorizationFilter;
import com.practice.spring.security.jwt.JwtProperties;
import com.practice.spring.security.mapper.UserMapper;
import com.practice.spring.security.model.vo.UserVO;
import lombok.RequiredArgsConstructor;
import com.practice.spring.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Security 설정 Config
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
//    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //커스텀 필터 적용
//        http.addFilterBefore(
//          new TesterAuthenticationFilter(this.authenticationManager()),
//                UsernamePasswordAuthenticationFilter.class
//        );

        /* basic authentication
         * ch03.06 BasicAuthenticationFilter
         * 활성화 : http.httpBasic();
         */
        http.httpBasic().disable(); // basic authentication filter 비활성화
        //http.httpBasic();

        // csrf
        // jwt 사용으로 비활성화
        //http.csrf();
        http.csrf().disable();

        // remember-me
        // 로그인 유지하기
        // jwt 사용으로 비활성화
        //http.rememberMe();
        http.rememberMe().disable();

        /* stateless
         * 세션을 미사용 하게 변경
         */
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // jwt filter
        http.addFilterBefore(
                new JwtAuthenticationFilter(authenticationManager()),
                UsernamePasswordAuthenticationFilter.class
        ).addFilterBefore(
                new JwtAuthorizationFilter(userMapper),
                BasicAuthenticationFilter.class
        );

        /* authorization
         * 페이지 권한 설정
         */
        http.authorizeRequests()
                // /와 /home은 모두에게 허용
                .antMatchers("/", "/home", "/signup").permitAll()
                // hello 페이지는 USER 롤을 가진 유저에게만 허용
                .antMatchers("/note").hasRole("USER")
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/notice").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/notice").hasRole("ADMIN")
                .anyRequest().authenticated();

        /* login
         * 폼 로그인의 로그인 페이지를 지덩하고 로그인에 성공했을때 이동하는 Url 지정
         */
        http.formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .permitAll(); // 모두 허용

        /* logout
         * 로그아웃 Url을 지정하고 로그아웃에 성공했을때 이동하는 Url 지정
         */
        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies(JwtProperties.COOKIE_NAME);
    }

    @Override
    public void configure(WebSecurity web) {

        /* 정적 리소스 spring security 대상에서 제외
         * 필터를 통과 하기 않기 때문에 permitAll보다 빠르다.
         * css, javascript, images, web jars, favicon 제외
         * web.ignoring().antMatchers("/images/**", "/css/**"); // 아래 코드와 같은 코드입니다.
         */
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
        web.ignoring().antMatchers("/h2-console/**");
    }

    /**
     * UserDetailsService 구현
     *
     * @return UserDetailsService
     */
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return username -> {
            UserVO user = userMapper.selectUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException(username);
            }
            return user;
        };
    }
}
