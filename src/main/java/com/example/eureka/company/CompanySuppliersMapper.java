package com.example.eureka.company;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CompanySuppliersMapper {

    @Insert("""
        INSERT INTO company_suppliers (company_id, supplier_id)
        VALUES (#{companyId}, #{supplierId})
    """)
    void insert(@Param("companyId") Long companyId,
                @Param("supplierId") Long supplierId);

    @Delete("""
        DELETE FROM company_suppliers
        WHERE company_id = #{companyId}
          AND supplier_id = #{supplierId}
    """)
    void delete(@Param("companyId") Long companyId,
                @Param("supplierId") Long supplierId);

    @Select("""
        SELECT supplier_id
        FROM company_suppliers
        WHERE company_id = #{companyId}
    """)
    List<Long> findSupplierIdsByCompanyId(@Param("companyId") Long companyId);

    @Select("""
        SELECT COUNT(*)
        FROM company_suppliers
        WHERE company_id = #{companyId}
          AND supplier_id = #{supplierId}
    """)
    int countExists(@Param("companyId") Long companyId,
                    @Param("supplierId") Long supplierId);
}
