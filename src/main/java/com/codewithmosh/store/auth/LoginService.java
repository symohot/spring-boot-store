package com.codewithmosh.store.auth;

import com.codewithmosh.store.user.EmailNotFoundException;
import com.codewithmosh.store.user.User;
import com.codewithmosh.store.user.UserRepository;
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
