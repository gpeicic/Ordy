package com.example.eureka.supplier;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

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

    @Insert("""
        INSERT INTO suppliers (oib, name)
        VALUES (#{oib}, #{name})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Supplier supplier);

}
