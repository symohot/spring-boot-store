package com.codewithmosh.store.auth;

import com.codewithmosh.store.user.UserDto;
import com.codewithmosh.store.user.User;
import com.codewithmosh.store.user.UserMapper;
import com.codewithmosh.store.user.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final LoginService loginService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtConfig jwtConfig;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest loginRequest,
                                             HttpServletResponse response) {
        /*loginService.login(loginRequest.getEmail(), loginRequest.getPassword());*/ //First solution

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()));

        User user = loginService.findUser(loginRequest.getEmail());

        /*String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);*/
        Jwt accessToken = jwtService.generateAccessToken(user);
        Jwt refreshToken = jwtService.generateRefreshToken(user);

        Cookie cookie = new Cookie("RefreshToken", refreshToken.toString());
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/auth/refresh");

        response.addCookie(cookie);
        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
    }

/*    @PostMapping("/validate")
    public boolean validate(@RequestHeader(name = "Authorization") String authHeader) {
        System.out.println("Methode called");
        String token = authHeader.replace("Bearer ", "");
        return jwtService.validateToken(token);
    }*/

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@CookieValue(value = "RefreshToken") String refreshToken){

        /*if (!jwtService.validateToken(refreshToken)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }*/
        Jwt jwt = jwtService.parseToken(refreshToken);
        if(jwt == null || jwt.isExpired()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        /*Long userId = jwtService.getUserId(refreshToken);*/
        Long userId = jwt.getUserId();
        User user = userRepository.findById(userId).orElse(null);
        Jwt newAccessToken = jwtService.generateAccessToken(user);

        return ResponseEntity.ok(new JwtResponse(newAccessToken.toString()));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me() {
        User user = authService.getCurrentUser();
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    /*@ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEmailNotFound(EmailNotFoundException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credential"));
    }

    @ExceptionHandler(PasswordMissMatchException.class)
    public ResponseEntity<Map<String, String>> handlePasswordMissMatch(PasswordMissMatchException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credential"));
    }*/

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadCredentialsException(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
