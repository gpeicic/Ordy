package com.example.eureka.orders;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {
    @Insert("INSERT INTO orders (company_id, supplier_id, user_id, created_at, status) " +
            "VALUES (#{companyId}, #{supplierId}, #{userId}, NOW(), #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Order order);

    @Results(id = "orderResultMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "companyId", column = "company_id"),
            @Result(property = "supplierId", column = "supplier_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "status", column = "status")
    })
    @Select("SELECT * FROM orders WHERE id = #{id}")
    Order findById(@Param("id") Long id);

    @Select("SELECT * FROM orders WHERE user_id = #{userId}")
    List<Order> findByUserId(@Param("userId") Long userId);

    @Select("SELECT * FROM orders WHERE company_id = #{companyId}")
    List<Order> findByCompanyId(@Param("companyId") Long companyId);

    @Update("UPDATE orders SET status = #{status} WHERE id = #{id}")
    void updateStatus(@Param("id") Long id, @Param("status") String status);
}
