package com.example.eureka.company;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserCompaniesMapper {
    @Select("SELECT company_id FROM user_companies WHERE user_id = #{userId} LIMIT 1")
    Long getFirstCompanyIdByUserId(@Param("userId") Long userId);

    @Select("SELECT COUNT(*) FROM user_companies WHERE user_id = #{userId} AND company_id = #{companyId}")
    boolean existsByUserIdAndCompanyId(@Param("userId") Long userId, @Param("companyId") Long companyId);

    @Insert("INSERT INTO user_companies (user_id, company_id) VALUES (#{userId}, #{companyId})")
    void insertUserCompany(@Param("userId") Long userId, @Param("companyId") Long companyId);
}
