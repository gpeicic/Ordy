package com.example.eureka.company;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserCompaniesMapper {
    @Insert("INSERT INTO user_companies (user_id, company_id) VALUES (#{userId}, #{companyId})")
    void insertUserCompany(@Param("userId") Long userId, @Param("companyId") Long companyId);
}
