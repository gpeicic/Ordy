package com.example.eureka.auth.jwt;

import com.example.eureka.exception.ResourceNotFoundException;
import com.example.eureka.exception.ValidationException;
import com.example.eureka.user.User;
import com.example.eureka.user.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserValidator(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public User validateCredentials(String username, String password) {
        User user = userMapper.findByUsername(username);

        if (user == null) {
            throw new ResourceNotFoundException("User nije pronađen");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ValidationException("Pogrešna lozinka");
        }

        return user;
    }
}
