package com.example.eureka.company;

import com.example.eureka.company.dto.CompanyResponseDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CompanyService {
    List<Company> getAllCompanies();
    List<CompanyResponseDTO> findCompaniesByUserId(Long userId);
}
