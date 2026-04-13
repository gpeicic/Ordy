package com.example.eureka.supplier;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SupplierMapper {

    @Select("""
        SELECT * FROM suppliers
        WHERE oib = #{oib}
    """)
    Supplier findByOib(String oib);

    @Select("""
        SELECT * FROM suppliers
        WHERE id = #{id}
    """)
    Supplier findById(Long id);
    @Select("""
        SELECT * FROM suppliers
        """)
    List<Supplier> findAll();

    @Insert("""
        INSERT INTO suppliers (oib, name)
        VALUES (#{oib}, #{name})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Supplier supplier);

    @Select("""
    SELECT DISTINCT s.*
        FROM suppliers s
        JOIN company_suppliers cs ON cs.supplier_id = s.id
        WHERE cs.company_id = #{companyId}
          AND EXISTS (
              SELECT 1
              FROM catalogue_items ci
              WHERE ci.supplier_id = s.id
          )
    """)
    List<Supplier> findSuppliersWithCatalogue(Long companyId);
}
