package com.example.eureka.company;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CompanyMapper {

    @Select("""
        SELECT id,
               name,
               mer_email AS merEmail,
               mer_password AS merPassword,
               address
        
        FROM companies
        WHERE id = #{id}
    """)
    Company findById(Long id);

    @Select("""
        SELECT id,
               name,
               mer_email AS merEmail,
               mer_password AS merPassword,
               address
        FROM companies
    """)
    List<Company> findAll();

    @Insert("""
        INSERT INTO companies(name, mer_email, mer_password, address)
        VALUES(#{name}, #{merEmail}, #{merPassword}, #{address})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Company company);

    @Update("""
        UPDATE companies
        SET name = #{name},
            mer_email = #{merEmail},
            mer_password = #{merPassword},
            address = #{address}
        WHERE id = #{id}
    """)
    void update(Company company);

}
