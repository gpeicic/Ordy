package com.example.eureka.orders;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderItemMapper {
    @Insert("INSERT INTO order_items (order_id, product_id, quantity) " +
            "VALUES (#{orderId}, #{productId}, #{quantity})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(OrderItem orderItem);

    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "orderId", column = "order_id"),
            @Result(property = "productId", column = "product_id"),
            @Result(property = "quantity", column = "quantity")
    })
    @Select("SELECT * FROM order_items WHERE order_id = #{orderId}")
    List<OrderItem> findByOrderId(@Param("orderId") Long orderId);

    @Select("""
    SELECT DISTINCT ON (ii.product_id) ii.product_name
    FROM invoice_items ii
    JOIN invoices i ON ii.invoice_id = i.id
    WHERE ii.product_id = #{productId}
    ORDER BY ii.product_id, i.invoice_datetime DESC
""")
    String findProductNameByProductId(@Param("productId") Long productId);

    @Delete("DELETE FROM order_items WHERE id = #{id}")
    void delete(@Param("id") Long id);
}
