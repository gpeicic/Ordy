package com.example.eureka.quickList;

import com.example.eureka.quickList.dto.QuickListItemDetail;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuickListItemMapper {

    @Insert("INSERT INTO quick_list_items (quick_list_id, catalogue_item_id, quantity) VALUES (#{quickListId}, #{catalogueItemId}, #{quantity})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(QuickListItem item);

    @Select("""
    SELECT qli.id, qli.quick_list_id AS quickListId, qli.catalogue_item_id AS catalogueItemId,
           ci.name AS productName, qli.quantity
    FROM quick_list_items qli
    JOIN catalogue_items ci ON qli.catalogue_item_id = ci.id
    WHERE qli.quick_list_id = #{quickListId}
""")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "quickListId", column = "quickListId"),
            @Result(property = "catalogueItemId", column = "catalogueItemId"),
            @Result(property = "productName", column = "productName"),
            @Result(property = "quantity", column = "quantity")
    })
    List<QuickListItemDetail> findByQuickListId(@Param("quickListId") Long quickListId);

    @Delete("DELETE FROM quick_list_items WHERE quick_list_id = #{quickListId}")
    void deleteByQuickListId(@Param("quickListId") Long quickListId);
}