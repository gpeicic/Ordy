package com.example.eureka.catalogue;

import com.example.eureka.catalogue.dto.SearchItemForOrderDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CatalogueItemMapper {

    @Insert("""
        INSERT INTO catalogue_items (supplier_id, code, category, name, price)
        VALUES (#{supplierId}, #{code}, #{category}, #{name}, #{price})
        ON CONFLICT (supplier_id, code)
        DO UPDATE SET
            category = EXCLUDED.category,
            name = EXCLUDED.name,
            price = EXCLUDED.price,
            updated_at = NOW()
    """)
    void upsert(CatalogueItem item);

    @Select("SELECT * FROM catalogue_items WHERE supplier_id = #{supplierId}")
    @Results(value = {
            @Result(property = "supplierId", column = "supplier_id"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at")

    })
    List<CatalogueItem> findBySupplierId(Long supplierId);

    @Select("""
    SELECT id, name, code
    FROM catalogue_items
    WHERE supplier_id = #{supplierId}
      AND name ILIKE CONCAT('%', #{name}, '%')
    ORDER BY name
    LIMIT 20
""")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "code", column = "code"),
    })
    List<SearchItemForOrderDTO> fuzzySearchByName(Long supplierId, String name);
}