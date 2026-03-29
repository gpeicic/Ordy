package com.example.eureka.supplier;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BlockedSupplierMapper {

    @Select("SELECT EXISTS (SELECT 1 FROM blocked_suppliers WHERE oib = #{oib})")
    boolean existsByOib(@Param("oib") String oib);

    @Insert("""
        INSERT INTO blocked_suppliers (oib, name)
        VALUES (#{oib}, #{name})
        """)
    void insert(@Param("oib") String oib,
                @Param("name") String name);
}
