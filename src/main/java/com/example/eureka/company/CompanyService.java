package com.example.eureka.company;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CompanyService {
    List<Company> findCompaniesByUserId(Long userId);
}
