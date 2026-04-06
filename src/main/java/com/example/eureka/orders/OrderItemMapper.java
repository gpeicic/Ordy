package com.example.eureka.orders;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderItemMapper {

    @Insert("INSERT INTO order_items (order_id, catalogue_item_id, quantity) " +
            "VALUES (#{orderId}, #{catalogueItemId}, #{quantity})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(OrderItem orderItem);

    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "orderId", column = "order_id"),
            @Result(property = "catalogueItemId", column = "catalogue_item_id"),
            @Result(property = "quantity", column = "quantity")
    })
    @Select("SELECT * FROM order_items WHERE order_id = #{orderId}")
    List<OrderItem> findByOrderId(@Param("orderId") Long orderId);

    @Select("""
    SELECT name 
    FROM catalogue_items 
    WHERE id = #{catalogueItemId}
    """)
    String findProductNameByCatalogueItemId(@Param("catalogueItemId") Long catalogueItemId);

    @Delete("DELETE FROM order_items WHERE id = #{id}")
    void delete(@Param("id") Long id);
}
