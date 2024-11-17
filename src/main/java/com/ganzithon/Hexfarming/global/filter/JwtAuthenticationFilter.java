package com.ganzithon.Hexfarming.global.filter;

import com.ganzithon.Hexfarming.domain.user.CustomUserDetails;
import com.ganzithon.Hexfarming.domain.user.CustomUserDetailsService;
import com.ganzithon.Hexfarming.utility.JwtManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtManager jwtManager;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public JwtAuthenticationFilter(JwtManager jwtManager, CustomUserDetailsService customUserDetailsService) {
        this.jwtManager = jwtManager;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String rawBearerToken = request.getHeader("Authorization"); // Authorization 헤더의 값을 가져옴
        if (rawBearerToken == null || !rawBearerToken.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.valueOf(401), "유효하지 않은 토큰입니다.");
        }
        String accessToken = rawBearerToken.substring(7); // 액세스 토큰 추출

        int userId = jwtManager.validateToken(accessToken); // 토큰으로부터 userId를 받아옴
        CustomUserDetails customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUserId(userId);
        Authentication authentication =jwtManager.getAuthentication(customUserDetails);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // Jwt 필터를 적용하지 않을 Endpoint를 작성
        String path = request.getRequestURI();
        return path.startsWith("/favicon.ico")
                || path.startsWith("/user/login")
                || path.startsWith("/user/signup");
    }
}
