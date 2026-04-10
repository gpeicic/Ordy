package com.example.eureka.venue;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface VenueMapper {

    @Select("SELECT id, name, address, city, postal_code AS postalCode, company_id AS companyId, user_id AS userId FROM venues WHERE id = #{id}")
    Venue findById(@Param("id") Long id);

    @Select("SELECT id, name, address, city, postal_code AS postalCode, company_id AS companyId, user_id AS userId FROM venues WHERE company_id = #{companyId}")
    List<Venue> findByCompanyId(@Param("companyId") Long companyId);

    @Insert("INSERT INTO venues (name, address, city, postal_code, company_id, user_id) VALUES (#{name}, #{address}, #{city}, #{postalCode}, #{companyId}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Venue venue);

    @Update("UPDATE venues SET name = #{name}, address = #{address}, city = #{city}, postal_code = #{postalCode} WHERE id = #{id}")
    void update(Venue venue);

    @Delete("DELETE FROM venues WHERE id = #{id}")
    void delete(@Param("id") Long id);
}