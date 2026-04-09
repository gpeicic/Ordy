package com.example.eureka.supplier;

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
    SELECT s.*
    FROM suppliers s
    INNER JOIN company_suppliers cs ON s.id = cs.supplier_id
    WHERE cs.company_id = #{companyId}
""")
    List<Supplier> findSuppliersFromCompany(Long companyId);

    @Select("""
        SELECT COUNT(*)
        FROM company_suppliers
        WHERE company_id = #{companyId}
          AND supplier_id = #{supplierId}
    """)
    int countExists(@Param("companyId") Long companyId,
                    @Param("supplierId") Long supplierId);
}
