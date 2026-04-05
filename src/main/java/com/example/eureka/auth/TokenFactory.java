package com.example.eureka.auth;

import com.example.eureka.auth.jwt.JwtService;
import com.example.eureka.company.UserCompaniesMapper;
import com.example.eureka.user.User;
import com.example.eureka.user.role.Role;
import com.example.eureka.user.role.RoleMapper;

import org.springframework.stereotype.Component;

@Component
public class TokenFactory {
    private final JwtService jwtService;
    private final RoleMapper roleMapper;
    private final UserCompaniesMapper userCompaniesMapper;

    public TokenFactory(JwtService jwtService, RoleMapper roleMapper, UserCompaniesMapper userCompaniesMapper) {
        this.jwtService = jwtService;
        this.roleMapper = roleMapper;
        this.userCompaniesMapper = userCompaniesMapper;
    }

    public String generateFor(User user) {
        Role role = roleMapper.findById(user.getRole_id());
        Long companyId = userCompaniesMapper.getFirstCompanyIdByUserId(user.getId());
        return jwtService.generateToken(user, role, companyId);
    }
}
