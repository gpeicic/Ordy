package com.example.eureka.priceComparison;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PriceComparisonMapper {
    @Select("""
    SELECT
        p.canonical_name AS canonicalName,
        s.name AS supplierName,
        SUM(ii.amount) AS totalAmount,
        (
            SELECT ii2.unit_price
            FROM invoice_items ii2
            JOIN invoices i2 ON ii2.invoice_id = i2.id
            WHERE ii2.product_id = ii.product_id
              AND i2.company_id = #{companyId}
              AND i2.supplier_id = #{supplierId}
            ORDER BY i2.invoice_datetime DESC
            LIMIT 1
        ) AS latestPrice,
        (
            SELECT ii3.unit_price
            FROM invoice_items ii3
            JOIN invoices i3 ON ii3.invoice_id = i3.id
            WHERE ii3.product_id = ii.product_id
              AND i3.company_id = #{companyId}
              AND i3.supplier_id = #{supplierId}
              AND i3.invoice_datetime <= NOW() - INTERVAL '14 days'
            ORDER BY i3.invoice_datetime DESC
            LIMIT 1
        ) AS previousPrice
    FROM invoice_items ii
    JOIN invoices i ON ii.invoice_id = i.id
    JOIN products p ON ii.product_id = p.id
    JOIN suppliers s ON i.supplier_id = s.id
    WHERE i.company_id = #{companyId}
      AND i.supplier_id = #{supplierId}
    GROUP BY p.id, p.canonical_name, s.name, ii.product_id
    ORDER BY totalAmount DESC
    LIMIT 5
""")
    @Results({
            @Result(property = "canonicalName", column = "canonicalName"),
            @Result(property = "supplierName", column = "supplierName"),
            @Result(property = "totalAmount", column = "totalAmount"),
            @Result(property = "latestPrice", column = "latestPrice"),
            @Result(property = "previousPrice", column = "previousPrice")
    })
    List<PriceComparisonItem> getPriceComparison(
            @Param("companyId") Long companyId,
            @Param("supplierId") Long supplierId
    );

    @Select("""
    SELECT
        p.canonical_name AS canonicalName,
        s.name AS supplierName,
        SUM(ii.amount) AS totalAmount,
        (
            SELECT ii2.unit_price
            FROM invoice_items ii2
            JOIN invoices i2 ON ii2.invoice_id = i2.id
            WHERE ii2.product_id = ii.product_id
              AND i2.supplier_id = i.supplier_id
            ORDER BY i2.invoice_datetime DESC
            LIMIT 1
        ) AS latestPrice,
        (
            SELECT ii3.unit_price
            FROM invoice_items ii3
            JOIN invoices i3 ON ii3.invoice_id = i3.id
            WHERE ii3.product_id = ii.product_id
              AND i3.supplier_id = i.supplier_id
              AND i3.invoice_datetime <= NOW() - INTERVAL '7 days'
            ORDER BY i3.invoice_datetime DESC
            LIMIT 1
        ) AS previousPrice
    FROM invoice_items ii
    JOIN invoices i ON ii.invoice_id = i.id
    JOIN products p ON ii.product_id = p.id
    JOIN suppliers s ON i.supplier_id = s.id
    WHERE ii.product_id = #{productId}
    GROUP BY p.id, p.canonical_name, i.supplier_id, s.name, ii.product_id
    ORDER BY latestPrice ASC
""")
    List<PriceComparisonItem> getProductPriceAcrossSuppliers(@Param("productId") Long productId);
}