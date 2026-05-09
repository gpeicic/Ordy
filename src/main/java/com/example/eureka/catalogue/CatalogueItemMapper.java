package com.example.eureka.catalogue;

import com.example.eureka.catalogue.dto.SearchItemForOrderDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CatalogueItemMapper {

    @Insert("""
        INSERT INTO catalogue_items (supplier_id, code, name, price)
        VALUES (#{supplierId}, #{code}, #{name}, #{price})
        ON DUPLICATE KEY UPDATE
            name = VALUES(name),
            price = VALUES(price),
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

    @Select("SELECT name, code FROM catalogue_items WHERE id = #{id}")
    CatalogueItem findNameCodeById(Long id);

    @Select("""
    SELECT id, name, code, 'CATALOGUE' as source
                FROM catalogue_items
                WHERE supplier_id = #{supplierId}
                  AND (
                    name LIKE CONCAT(#{name}, '%')
                    OR name LIKE CONCAT('%', #{name}, '%')
                    OR code LIKE CONCAT(#{name}, '%')
                  )
                ORDER BY
                    CASE WHEN name LIKE CONCAT(#{name}, '%') THEN 0 ELSE 1 END,
                    name
                LIMIT 10
""")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "code", column = "code"),
    })
    List<SearchItemForOrderDTO> fuzzySearchByName(
            @Param("supplierId") Long supplierId,
            @Param("name") String name
    );

    @Select("""
                SELECT *
                FROM catalogue_items
                WHERE supplier_id = #{supplierId}
                ORDER BY name
                LIMIT #{limit}
                OFFSET #{offset}
            """)
    @Results(value = {
            @Result(property = "supplierId", column = "supplier_id"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at")
    })
    List<CatalogueItem> findBySupplierIdPaged(
            Long supplierId,
            int limit,
            int offset
    );
    @Select("""
                SELECT *
                FROM catalogue_items
                WHERE supplier_id = #{supplierId}
                  AND (
                      name LIKE CONCAT('%', #{search}, '%')
                      OR code LIKE CONCAT('%', #{search}, '%')
                  )
                ORDER BY name
                LIMIT #{limit}
                OFFSET #{offset}
            """)
    @Results(value = {
            @Result(property = "supplierId", column = "supplier_id"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at")
    })
    List<CatalogueItem> searchBySupplierPaged(
            Long supplierId,
            String search,
            int limit,
            int offset
    );
    @Delete("DELETE FROM catalogue_items WHERE id = #{id}")
    void deleteById(Long id);
}