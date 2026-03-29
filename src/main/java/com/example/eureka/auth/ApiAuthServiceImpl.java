package com.example.eureka.auth;

import com.example.eureka.company.Company;
import com.example.eureka.company.CompanyMapper;
import com.example.eureka.company.UserCompaniesMapper;
import com.example.eureka.user.User;
import com.example.eureka.user.UserMapper;
import com.example.eureka.user.role.Role;
import com.example.eureka.user.role.RoleMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ApiAuthServiceImpl implements ApiAuthService {

    private final UserMapper userMapper;
    private final CompanyMapper companyMapper;
    private final UserCompaniesMapper userCompaniesMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RoleMapper roleMapper;

    public ApiAuthServiceImpl(UserMapper userMapper, CompanyMapper companyMapper, UserCompaniesMapper userCompaniesMapper, PasswordEncoder passwordEncoder, JwtService jwtService, RoleMapper roleMapper) {
        this.userMapper = userMapper;
        this.companyMapper = companyMapper;
        this.userCompaniesMapper = userCompaniesMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.roleMapper = roleMapper;
    }

    @Override
    public ApiLoginResponse login(ApiLoginRequest request) {
        User user = userMapper.findByUsername(request.getUsername());

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        Role role = roleMapper.findById(user.getRole_id());
        String token = jwtService.generateToken(user, role);

        return new ApiLoginResponse(token);
    }

    @Override
    public ApiRegisterResponse register(ApiRegisterRequest request) {
        Company company = new Company();
        company.setName(request.getCompanyName());
        company.setMerEmail(request.getMerEmail());
        company.setMerPassword(request.getMerPassword());
        companyMapper.insert(company);

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole_id(2L); // ROLE_USER
        userMapper.insert(user);

        userCompaniesMapper.insertUserCompany(user.getId(), company.getId());

        Role role = roleMapper.findById(user.getRole_id());
        String token = jwtService.generateToken(user, role);

        return new ApiRegisterResponse(token);
    }
}
