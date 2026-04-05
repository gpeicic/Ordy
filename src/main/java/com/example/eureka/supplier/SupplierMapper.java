package com.example.eureka.supplier;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

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

}
