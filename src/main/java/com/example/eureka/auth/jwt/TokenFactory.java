package com.example.eureka.auth.jwt;

import com.example.eureka.company.UserCompaniesMapper;
import com.example.eureka.exception.ResourceNotFoundException;
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
        if (role == null) {
            throw new ResourceNotFoundException("Rola nije pronađena za usera: " + user.getId());
        }
        Long companyId = userCompaniesMapper.getFirstCompanyIdByUserId(user.getId());
        if (companyId == null) {
            throw new ResourceNotFoundException("User nema kompaniju: " + user.getId());
        }
        return jwtService.generateToken(user, role, companyId);
    }

    public String generateFor(User user, Long companyId) {
        Role role = roleMapper.findById(user.getRole_id());
        if (role == null) {
            throw new ResourceNotFoundException("Rola nije pronađena za usera: " + user.getId());
        }
        return jwtService.generateToken(user, role, companyId);
    }
}
