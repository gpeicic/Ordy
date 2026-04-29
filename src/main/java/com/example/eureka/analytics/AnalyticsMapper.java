package com.example.eureka.analytics;

import com.example.eureka.analytics.dto.ProductSpendingDTO;
import com.example.eureka.analytics.dto.SupplierSpendingDTO;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface AnalyticsMapper {
    @Select("""
        SELECT 
            i.supplier_id,
            s.name as supplier_name,
            AVG(monthly_total) as avg_monthly_spending
        FROM (
            SELECT 
                i.supplier_id,
                DATE_TRUNC('month', i.invoice_datetime) as month,
                SUM(ii.amount * ii.unit_price) as monthly_total
            FROM invoices i
            JOIN invoice_items ii ON ii.invoice_id = i.id
            WHERE i.company_id = #{companyId}
            GROUP BY i.supplier_id, DATE_TRUNC('month', i.invoice_datetime)
        ) monthly
        JOIN invoices i ON i.supplier_id = monthly.supplier_id
        JOIN suppliers s ON s.id = monthly.supplier_id
        GROUP BY i.supplier_id, s.name
    """)
    @Results({
            @Result(property = "supplierId", column = "supplier_id"),
            @Result(property = "supplierName", column = "supplier_name"),
            @Result(property = "avgMonthlySpending", column = "avg_monthly_spending")
    })
    List<SupplierSpendingDTO> getAvgMonthlySpendingPerSupplier(@Param("companyId") Long companyId);

    @Select("""
        SELECT 
            SUM(ii.amount * ii.unit_price) as current_month_spending
        FROM invoice_items ii
        JOIN invoices i ON ii.invoice_id = i.id
        WHERE i.company_id = #{companyId}
          AND i.supplier_id = #{supplierId}
          AND DATE_TRUNC('month', i.invoice_datetime) = DATE_TRUNC('month', CURRENT_DATE)
    """)
    @Results({
            @Result(property = "currentMonthSpending", column = "current_month_spending")
    })
    BigDecimal getCurrentMonthSpending(@Param("companyId") Long companyId,
                                       @Param("supplierId") Long supplierId);

    @Select("""
        SELECT 
            ii.product_name,
            SUM(ii.amount) as total_quantity,
            SUM(ii.amount * ii.unit_price) as total_spent
        FROM invoice_items ii
        JOIN invoices i ON ii.invoice_id = i.id
        WHERE i.company_id = #{companyId}
          AND i.supplier_id = #{supplierId}
          AND DATE_TRUNC('month', i.invoice_datetime) = DATE_TRUNC('month', CURRENT_DATE)
        GROUP BY ii.product_name
        ORDER BY total_spent DESC
        LIMIT 10
    """)
    @Results({
            @Result(property = "productName", column = "product_name"),
            @Result(property = "totalQuantity", column = "total_quantity"),
            @Result(property = "totalSpent", column = "total_spent")
    })
    List<ProductSpendingDTO> getProductBreakdownForSupplier(@Param("companyId") Long companyId,
                                                            @Param("supplierId") Long supplierId);

    @Select("""
    SELECT 
        SUM(ii.amount * ii.unit_price) as current_month_spending
    FROM invoice_items ii
    JOIN invoices i ON ii.invoice_id = i.id
    WHERE i.company_id = #{companyId}
      AND DATE_TRUNC('month', i.invoice_datetime) = DATE_TRUNC('month', CURRENT_DATE)
""")
    BigDecimal getCurrentMonthSpendingForCompany(@Param("companyId") Long companyId);
}