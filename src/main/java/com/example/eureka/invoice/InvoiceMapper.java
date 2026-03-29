package com.example.eureka.invoice;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

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

}