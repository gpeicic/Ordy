package com.example.eureka.auth;

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
            throw new RuntimeException("User not found");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }
}
