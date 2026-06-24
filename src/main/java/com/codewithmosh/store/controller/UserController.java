package com.codewithmosh.store.controller;

import com.codewithmosh.store.dto.ChangePasswordRequest;
import com.codewithmosh.store.dto.RegisterUserRequest;
import com.codewithmosh.store.dto.UpdateUserRequest;
import com.codewithmosh.store.dto.UserDto;
import com.codewithmosh.store.entities.Role;
import com.codewithmosh.store.entities.User;
import com.codewithmosh.store.mappers.UserMapper;
import com.codewithmosh.store.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public List<UserDto> getUsers(
            @RequestParam(defaultValue = "name", name = "sort") String sortBy) {

        if (!Set.of("name", "email", "id").contains(sortBy)) sortBy = "name";
        return userRepository.findAll(Sort.by(sortBy).ascending())
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@Valid @RequestBody RegisterUserRequest registerUserRequest,
                                              UriComponentsBuilder uriBuilder) {

        if (userRepository.existsByEmail((registerUserRequest.getEmail())))
            return ResponseEntity.badRequest().body(Map.of("email", "Email exist already"));

        User entity = userMapper.toEntity(registerUserRequest);

        entity.setPassword(passwordEncoder.encode(registerUserRequest.getPassword()));

        entity.setRole(Role.USER);

        User save = userRepository.save(entity);

        UserDto userDto = userMapper.toDto(save);

        return ResponseEntity.created(uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri()).body(userDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update (@RequestBody UpdateUserRequest updateUserRequest,
                                           @PathVariable(name = "id") long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();

        userMapper.updateUser(updateUserRequest, user);

        user = userRepository.save(user);

        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete (@PathVariable(name = "id") long id) {

        User user = userRepository.findById(id).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();

        userRepository.delete(user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(@PathVariable(name = "id") Long id,
                                               @RequestBody ChangePasswordRequest changePasswordRequest){

        User user = userRepository.findById(id).orElse(null);
        if (user == null)
            return ResponseEntity.notFound().build();

        if(!user.getPassword().equals(changePasswordRequest.getOldPassword()))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        user.setPassword(changePasswordRequest.getNewPassword());
        userRepository.save(user);
        return ResponseEntity.noContent().build();
    }
}
