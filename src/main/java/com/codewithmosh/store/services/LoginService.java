package com.codewithmosh.store.services;

import com.codewithmosh.store.entities.User;
import com.codewithmosh.store.exceptions.EmailNotFoundException;
import com.codewithmosh.store.exceptions.PasswordMissMatchException;
import com.codewithmosh.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void login(String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) throw new EmailNotFoundException();
        if (!passwordEncoder.matches(password, user.getPassword())) throw new PasswordMissMatchException();
    }
    public User findUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(EmailNotFoundException::new);

    }
}
