package com.example.eureka.orders;

import com.example.eureka.orders.dto.OrderSummary;
import com.example.eureka.orders.dto.OrderWithSupplierNameDTO;
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


    @Select("""
    SELECT o.id,
            o.company_id,
            o.supplier_id,
            o.user_id,
            o.created_at,
            o.status,
            s.name AS supplier_name
    FROM orders o
    LEFT JOIN suppliers s ON o.supplier_id = s.id
    WHERE o.company_id = #{companyId}
    ORDER BY o.created_at DESC, o.id DESC
    LIMIT 1
""")
    @Results(id= "orderWithSupplierNameResultMap", value ={
            @Result(property="id", column="id"),
            @Result(property="companyId", column="company_id"),
            @Result(property="supplierId", column="supplier_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property="createdAt", column="created_at"),
            @Result(property="status", column="status"),
            @Result(property="supplierName", column="supplier_name")
    })
    OrderWithSupplierNameDTO findLatestByCompanyId(@Param("companyId") Long companyId);

    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "supplierName", column = "supplier_name"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "status", column = "status")
    })
    @Select("""
    SELECT o.id, s.name AS supplier_name, o.created_at, o.status
    FROM orders o
    JOIN suppliers s ON o.supplier_id = s.id
    WHERE o.company_id = #{companyId}
    ORDER BY o.created_at DESC
""")
    List<OrderSummary> findSummariesByCompanyId(@Param("companyId") Long companyId);

    @Select("SELECT * FROM orders WHERE company_id = #{companyId}")
    List<Order> findByCompanyId(@Param("companyId") Long companyId);

    @Update("UPDATE orders SET status = #{status} WHERE id = #{id}")
    void updateStatus(@Param("id") Long id, @Param("status") String status);
}
