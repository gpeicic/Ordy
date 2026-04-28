package com.example.eureka.invoice;

import org.apache.ibatis.annotations.*;

@Mapper
public interface InvoiceMapper {

    @Select("""
        SELECT * FROM invoices
        WHERE supplier_id = #{supplierId}
        AND invoice_number = #{invoiceNumber}
    """)
    Invoice findExisting(
            Long supplierId,
            String invoiceNumber
    );

    @Insert("""
        INSERT INTO invoices
        (supplier_id, invoice_number, invoice_datetime, external_document_id, company_id)
        VALUES
        (#{supplierId}, #{invoiceNumber}, #{invoiceDatetime}, #{externalDocumentId}, #{companyId})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Invoice invoice);

    @Select("""
    SELECT i.supplier_id
    FROM invoices i
    JOIN invoice_items ii ON ii.invoice_id = i.id
    WHERE i.company_id = #{companyId}
    GROUP BY i.supplier_id
    ORDER BY SUM(ii.amount * ii.unit_price) DESC
    LIMIT 1
""")
    Long findTopSupplierBySpending(@Param("companyId") Long companyId);

    @Select("""
    SELECT MAX(external_document_id) FROM invoices
    WHERE company_id = #{companyId}
""")
    Long findMaxExternalDocumentId(@Param("companyId") Long companyId);

}