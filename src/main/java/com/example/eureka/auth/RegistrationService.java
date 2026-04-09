package com.example.eureka.auth;

import com.example.eureka.auth.dto.ApiRegisterRequest;
import com.example.eureka.company.Company;
import com.example.eureka.company.CompanyMapper;
import com.example.eureka.company.UserCompaniesMapper;
import com.example.eureka.user.User;
import com.example.eureka.user.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class RegistrationService {
    private final UserMapper userMapper;
    private final CompanyMapper companyMapper;
    private final UserCompaniesMapper userCompaniesMapper;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(UserMapper userMapper, CompanyMapper companyMapper,
                               UserCompaniesMapper userCompaniesMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.companyMapper = companyMapper;
        this.userCompaniesMapper = userCompaniesMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(ApiRegisterRequest request) {
        Company company = createCompany(request);
        User user = createUser(request, company);
        return user;
    }

    private Company createCompany(ApiRegisterRequest request) {
        Company company = new Company();
        company.setName(request.getCompanyName());
        company.setMerEmail(request.getMerEmail());
        company.setMerPassword(request.getMerPassword());
        company.setAddress(request.getAddress());
        companyMapper.insert(company);
        return company;
    }

    private User createUser(ApiRegisterRequest request, Company company) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole_id(2L);
        userMapper.insert(user);
        userCompaniesMapper.insertUserCompany(user.getId(), company.getId());
        return user;
    }
}
