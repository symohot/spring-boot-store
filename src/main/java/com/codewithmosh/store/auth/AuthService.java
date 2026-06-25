package com.codewithmosh.store.auth;

import com.codewithmosh.store.user.User;
import com.codewithmosh.store.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;


    public User getCurrentUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) auth.getPrincipal();
        return userRepository.findById(userId).orElse(null);
    }
}
