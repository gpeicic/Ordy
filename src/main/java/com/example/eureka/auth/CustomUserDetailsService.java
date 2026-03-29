package com.example.eureka.auth;

import com.example.eureka.user.User;
import com.example.eureka.user.UserMapper;
import com.example.eureka.user.role.Role;
import com.example.eureka.user.role.RoleMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;

    public CustomUserDetailsService(UserMapper userMapper, RoleMapper roleMapper) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        Role role = roleMapper.findById(user.getRole_id());

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(role.getRoleType().name().replace("ROLE_", ""))
                .build();
    }
}
