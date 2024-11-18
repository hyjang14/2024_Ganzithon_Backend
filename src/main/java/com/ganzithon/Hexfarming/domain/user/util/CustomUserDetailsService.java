package com.ganzithon.Hexfarming.domain.user.util;

import com.ganzithon.Hexfarming.domain.user.User;
import com.ganzithon.Hexfarming.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(401), "존재하지 않는 이메일입니다."));
        return new CustomUserDetails(user);
    }

    public UserDetails loadUserByUserId(int userId) throws UsernameNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(401), "존재하지 않는 유저id입니다."));
        return new CustomUserDetails(user);
    }

    public CustomUserDetails getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof  CustomUserDetails) {
            return (CustomUserDetails) authentication.getPrincipal();
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(401), "잘못된 유저 정보입니다.");
        }
    }
}
