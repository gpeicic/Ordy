package com.example.eureka.invoice;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface InvoiceItemMapper {

    @Insert("""
        INSERT INTO invoice_items
        (invoice_id, product_name, unit_price, discount, amount, product_id)
        VALUES
        (#{invoiceId}, #{productName}, #{unitPrice}, #{discount}, #{amount}, #{productId})
    """)
    void insert(InvoiceItem item);

    @Select("""
    SELECT COALESCE(SUM(ii.amount * ii.unit_price), 0)
    FROM invoice_items ii
    JOIN invoices i ON ii.invoice_id = i.id
    WHERE i.company_id = #{companyId}
      AND DATE_TRUNC('month', i.created_at) = DATE_TRUNC('month', CURRENT_DATE)
""")
    BigDecimal getMonthlySpending(@Param("companyId") Long companyId);

    @Select("""
        SELECT ii.*
        FROM invoice_items ii
        JOIN invoices i ON i.id = ii.invoice_id
        JOIN company_suppliers cs ON cs.supplier_id = i.supplier_id
        WHERE cs.company_id = #{companyId}
          AND i.supplier_id = #{supplierId}
        ORDER BY ii.amount DESC
        LIMIT 5
    """)
    List<InvoiceItem> findTop5ByAmountForCompanyAndSupplier(@Param("companyId") Long companyId,
                                                            @Param("supplierId") Long supplierId);

    @Select("""
    SELECT DISTINCT ON (product_id) product_name
    FROM invoice_items ii
    JOIN invoices i ON ii.invoice_id = i.id
    WHERE ii.product_id = #{productId}
      AND i.supplier_id = #{supplierId}
    ORDER BY product_id, i.invoice_datetime DESC
""")
    String findLatestProductNameBySupplier(@Param("productId") Long productId,
                                           @Param("supplierId") Long supplierId);
}