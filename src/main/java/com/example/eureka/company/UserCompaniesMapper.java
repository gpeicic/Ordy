package com.example.eureka.company;

import com.example.eureka.company.dto.CompanyResponseDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserCompaniesMapper {
    @Select("SELECT company_id FROM user_companies WHERE user_id = #{userId} LIMIT 1")
    Long getFirstCompanyIdByUserId(@Param("userId") Long userId);

    @Select("SELECT COUNT(*) FROM user_companies WHERE user_id = #{userId} AND company_id = #{companyId}")
    boolean existsByUserIdAndCompanyId(@Param("userId") Long userId, @Param("companyId") Long companyId);

    @Insert("INSERT INTO user_companies (user_id, company_id) VALUES (#{userId}, #{companyId})")
    void insertUserCompany(@Param("userId") Long userId, @Param("companyId") Long companyId);

    @Select("""
    SELECT c.id, c.name
    FROM companies c
    INNER JOIN user_companies uc ON c.id = uc.company_id
    WHERE uc.user_id = #{userId}
""")
    List<CompanyResponseDTO> findCompaniesByUserId(@Param("userId") Long userId);

}