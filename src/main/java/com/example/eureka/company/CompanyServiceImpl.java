package com.example.eureka.company;

import com.example.eureka.company.dto.CompanyResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {
    private final UserCompaniesMapper userCompaniesMapper;

    public CompanyServiceImpl(UserCompaniesMapper userCompaniesMapper) {
        this.userCompaniesMapper = userCompaniesMapper;
    }

    @Override
    public List<CompanyResponseDTO> findCompaniesByUserId(Long userId) {
        return userCompaniesMapper.findCompaniesByUserId(userId);
    }
}
