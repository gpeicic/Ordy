package com.example.eureka.quickList;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuickListMapper {

    @Insert("INSERT INTO quick_lists (name, company_id, user_id, supplier_id) VALUES (#{name}, #{companyId}, #{userId}, #{supplierId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(QuickList quickList);

    @Select("SELECT id, name, company_id AS companyId, user_id AS userId, supplier_id AS supplierId FROM quick_lists WHERE company_id = #{companyId}")
    List<QuickList> findByCompanyId(@Param("companyId") Long companyId);

    @Select("SELECT id, name, company_id AS companyId, user_id AS userId, supplier_id AS supplierId FROM quick_lists WHERE id = #{id}")
    QuickList findById(@Param("id") Long id);

    @Delete("DELETE FROM quick_lists WHERE id = #{id}")
    void delete(@Param("id") Long id);

    @Update("UPDATE quick_lists SET name = #{name} WHERE id = #{id}")
    void update(QuickList quickList);
}