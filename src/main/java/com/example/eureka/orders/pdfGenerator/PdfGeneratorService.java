package com.example.eureka.orders.pdfGenerator;

import com.example.eureka.company.Company;
import com.example.eureka.company.CompanyMapper;
import com.example.eureka.invoice.InvoiceItemMapper;
import com.example.eureka.orders.Order;
import com.example.eureka.orders.OrderItem;
import com.example.eureka.orders.OrderItemMapper;
import com.example.eureka.supplier.Supplier;
import com.example.eureka.supplier.SupplierMapper;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;
import com.itextpdf.layout.Document;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@Service
public class PdfGeneratorService {

    private final CompanyMapper companyMapper;
    private final SupplierMapper supplierMapper;
    private final OrderItemMapper orderItemMapper;
    private final InvoiceItemMapper itemMapper;

    public PdfGeneratorService(CompanyMapper companyMapper, SupplierMapper supplierMapper,
                               OrderItemMapper orderItemMapper, InvoiceItemMapper itemMapper) {
        this.companyMapper = companyMapper;
        this.supplierMapper = supplierMapper;
        this.orderItemMapper = orderItemMapper;
        this.itemMapper = itemMapper;
    }


    public byte[] generateOrderPdf(Order order) throws IOException {
        Company company = companyMapper.findById(order.getCompanyId());
        Supplier supplier = supplierMapper.findById(order.getSupplierId());
        List<OrderItem> items = orderItemMapper.findByOrderId(order.getId());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);
        document.setMargins(40, 40, 40, 40);

        // font
        PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont regular = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        // --- SUPPLIER INFO (gore desno) ---
        Table headerTable = new Table(UnitValue.createPercentArray(new float[]{50, 50}))
                .setWidth(UnitValue.createPercentValue(100));

        // lijevo prazno ili naziv dokumenta
        Cell titleCell = new Cell().setBorder(Border.NO_BORDER);
        titleCell.add(new Paragraph("NARUDŽBENICA")
                .setFont(bold)
                .setFontSize(20));
        titleCell.add(new Paragraph("Broj narudžbe: #" + order.getId())
                .setFont(regular)
                .setFontSize(10)
                .setFontColor(ColorConstants.GRAY));
        titleCell.add(new Paragraph("Datum: " + order.getCreatedAt().toLocalDate())
                .setFont(regular)
                .setFontSize(10)
                .setFontColor(ColorConstants.GRAY));
        headerTable.addCell(titleCell);

        // desno supplier info
        Cell supplierCell = new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT);
        supplierCell.add(new Paragraph("Dobavljac").setFont(bold).setFontSize(10).setFontColor(ColorConstants.GRAY));
        supplierCell.add(new Paragraph(supplier.getName()).setFont(bold).setFontSize(12));
        supplierCell.add(new Paragraph("OIB: " + supplier.getOib()).setFont(regular).setFontSize(10));
        headerTable.addCell(supplierCell);

        document.add(headerTable);
        document.add(new Paragraph("\n"));

        // --- COMPANY INFO ---
        Table companyTable = new Table(UnitValue.createPercentArray(new float[]{100}))
                .setWidth(UnitValue.createPercentValue(50));

        Cell companyCell = new Cell()
                .setBackgroundColor(new DeviceRgb(245, 245, 245))
                .setBorder(Border.NO_BORDER)
                .setPadding(10);
        companyCell.add(new Paragraph("Narucitelj").setFont(bold).setFontSize(10).setFontColor(ColorConstants.GRAY));
        companyCell.add(new Paragraph(company.getName()).setFont(bold).setFontSize(12));
        companyTable.addCell(companyCell);

        document.add(companyTable);
        document.add(new Paragraph("\n"));

     /*   // --- TABLICA PROIZVODA ---
        Table productTable = new Table(UnitValue.createPercentArray(new float[]{50, 25, 25}))
                .setWidth(UnitValue.createPercentValue(100));

        // header
        Stream.of("Naziv proizvoda", "Interna šifra", "Kolicina").forEach(col -> productTable.addHeaderCell(
                new Cell()
                        .setBackgroundColor(new DeviceRgb(50, 50, 50))
                        .add(new Paragraph(col)
                                .setFont(bold)
                                .setFontSize(10)
                                .setFontColor(ColorConstants.WHITE))
        ));
*/
        Table productTable = new Table(UnitValue.createPercentArray(new float[]{75, 25}))
                .setWidth(UnitValue.createPercentValue(100));

        Stream.of("Naziv proizvoda", "Kolicina").forEach(col -> productTable.addHeaderCell(
                new Cell()
                        .setBackgroundColor(new DeviceRgb(50, 50, 50))
                        .add(new Paragraph(col)
                                .setFont(bold)
                                .setFontSize(10)
                                .setFontColor(ColorConstants.WHITE))
        ));

        for (int i = 0; i < items.size(); i++) {
            OrderItem item = items.get(i);
            String productName = itemMapper.findLatestProductNameBySupplier(
                    item.getProductId(),
                    order.getSupplierId()
            );
            DeviceRgb rowColor = i % 2 == 0
                    ? new DeviceRgb(255, 255, 255)
                    : new DeviceRgb(250, 250, 250);

            productTable.addCell(new Cell()
                    .setBackgroundColor(rowColor)
                    .add(new Paragraph(productName != null ? productName : "N/A")
                            .setFont(regular).setFontSize(10)));
/*
            productTable.addCell(new Cell()
                    .setBackgroundColor(rowColor)
                    .add(new Paragraph(product != null && product.getInternalCode() != null ? product.getInternalCode() : "-")
                            .setFont(regular).setFontSize(10)));
*/
            productTable.addCell(new Cell()
                    .setBackgroundColor(rowColor)
                    .add(new Paragraph(item.getQuantity().stripTrailingZeros().toPlainString())
                            .setFont(regular).setFontSize(10)));
        }

        document.add(productTable);
        document.close();

        return baos.toByteArray();
    }
}
