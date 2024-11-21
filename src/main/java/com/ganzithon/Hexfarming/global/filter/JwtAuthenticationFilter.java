package com.ganzithon.Hexfarming.global.filter;

import com.ganzithon.Hexfarming.domain.user.util.CustomUserDetails;
import com.ganzithon.Hexfarming.domain.user.util.CustomUserDetailsService;
import com.ganzithon.Hexfarming.global.enumeration.ExceptionMessage;
import com.ganzithon.Hexfarming.global.utility.JwtManager;
import com.ganzithon.Hexfarming.global.utility.NoTokenNeededEndpointParser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        String rawBearerToken = request.getHeader("Authorization");
        if (rawBearerToken == null || !rawBearerToken.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.valueOf(401), ExceptionMessage.INVALID_TOKEN.getMessage());
        }
        String accessToken = rawBearerToken.substring(7);

        int userId = jwtManager.validateToken(accessToken);
        CustomUserDetails customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUserId(userId);
        Authentication authentication =jwtManager.getAuthentication(customUserDetails);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return NoTokenNeededEndpointParser.parsePath(path);
    }
}
