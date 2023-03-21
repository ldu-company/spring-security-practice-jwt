package com.practice.spring.security.jwt;

import com.practice.spring.security.mapper.UserMapper;
import com.practice.spring.security.model.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * JWT를 이용한 인증
 * 1. Cookie에서 JWT Token을 구한다.
 * 2. JWT Token을 파싱하여 username을 구한다.
 * 3. username으로 User를 구하고 Authentication을 생성
 * 4. 생성된 Authentication을 SecurityContext에 넣는다.
 * 5. Exception이 발생하면 응답의 쿠키를 null로 변경
 */
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    //private final UserRepository userRepository;

    private final UserMapper userMapper;

    public JwtAuthorizationFilter(
            UserMapper userMapper
    ) {
        this.userMapper = userMapper;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        String token = null;
        try {
            // 1. cookie 에서 JWT token을 가져옵니다.
            token = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals(JwtProperties.COOKIE_NAME)).findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        } catch (Exception ignored) {}

        if (token != null) {
            try {
                //4. 생성된 Authentication을 SecurityContext에 넣는다.
                Authentication authentication = getUsernamePasswordAuthenticationToken(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                //실패시 쿠키 초기화
                Cookie cookie = new Cookie(JwtProperties.COOKIE_NAME, null);
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
        chain.doFilter(request, response);
    }

    /**
     * JWT 토큰으로 User를 찾아서 UsernamePasswordAuthenticationToken를 만들어서 반환한다.
     * User가 없다면 null
     */
    private Authentication getUsernamePasswordAuthenticationToken(String token) {
        String userName = JwtUtils.getUsername(token);
        if (userName != null) {
            UserVO user = userMapper.selectUsername(userName); // 유저를 유저명으로 찾습니다.
            return new UsernamePasswordAuthenticationToken(
                    user, // principal
                    null,
                    user.getAuthorities()
            );
        }
        return null; // 유저가 없으면 NULL
    }
}