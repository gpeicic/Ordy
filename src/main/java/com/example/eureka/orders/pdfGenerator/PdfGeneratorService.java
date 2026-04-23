package com.example.eureka.orders.pdfGenerator;

import com.example.eureka.catalogue.CatalogueItem;
import com.example.eureka.catalogue.CatalogueItemMapper;
import com.example.eureka.company.Company;
import com.example.eureka.company.CompanyMapper;
import com.example.eureka.exception.ResourceNotFoundException;
import com.example.eureka.exception.ValidationException;
import com.example.eureka.invoice.InvoiceItemMapper;
import com.example.eureka.orders.Order;
import com.example.eureka.orders.OrderItem;
import com.example.eureka.orders.OrderItemMapper;
import com.example.eureka.supplier.Supplier;
import com.example.eureka.supplier.SupplierMapper;
import com.example.eureka.venue.Venue;
import com.example.eureka.venue.VenueMapper;
import com.itextpdf.io.font.PdfEncodings;
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
    private final CatalogueItemMapper catalogueItemMapper;
    private final VenueMapper venueMapper;

    public PdfGeneratorService(CompanyMapper companyMapper, SupplierMapper supplierMapper,
                               OrderItemMapper orderItemMapper, CatalogueItemMapper catalogueItemMapper,
                               VenueMapper venueMapper) {
        this.companyMapper = companyMapper;
        this.supplierMapper = supplierMapper;
        this.orderItemMapper = orderItemMapper;
        this.catalogueItemMapper = catalogueItemMapper;
        this.venueMapper = venueMapper;

    }

    public byte[] generateOrderPdf(Order order, boolean hideCompanyName) throws IOException {
        if (order == null) {
            throw new ValidationException("Order je null");
        }
        Company company = companyMapper.findById(order.getCompanyId());
        if (company == null) {
            throw new ResourceNotFoundException("Kompanija nije pronađena: " + order.getCompanyId());
        }
        Supplier supplier = supplierMapper.findById(order.getSupplierId());
        if (supplier == null) {
            throw new ResourceNotFoundException("Dobavljač nije pronađen: " + order.getSupplierId());
        }
        Venue venue = venueMapper.findById(order.getVenueId());
        List<OrderItem> items = orderItemMapper.findByOrderId(order.getId());
        if (items == null || items.isEmpty()) {
            throw new ValidationException("Narudžba nema artikala: " + order.getId());
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument, PageSize.A4);
        document.setMargins(40, 40, 40, 40);

        PdfFont bold = PdfFontFactory.createFont(
                "src/main/resources/fonts/DejaVuSans-Bold.ttf",
                PdfEncodings.IDENTITY_H
        );
        PdfFont regular = PdfFontFactory.createFont(
                "src/main/resources/fonts/DejaVuSans.ttf",
                PdfEncodings.IDENTITY_H
        );

        document.add(buildHeaderTable(order, supplier, bold, regular));
        document.add(new Paragraph("\n"));
        document.add(buildCompanyTable(company, venue, bold, hideCompanyName));
        document.add(new Paragraph("\n"));
        document.add(buildProductTable(order, items, bold, regular));

        document.close();

        return baos.toByteArray();
    }


    private Table buildHeaderTable(Order order, Supplier supplier, PdfFont bold, PdfFont regular) {
        Table headerTable = new Table(UnitValue.createPercentArray(new float[]{50, 50}))
                .setWidth(UnitValue.createPercentValue(100));

        Cell titleCell = new Cell().setBorder(Border.NO_BORDER);
        titleCell.add(new Paragraph("NARUDŽBENICA").setFont(bold).setFontSize(20));
        titleCell.add(new Paragraph("Broj narudžbe: #" + order.getId()).setFont(regular).setFontSize(10).setFontColor(ColorConstants.GRAY));
        titleCell.add(new Paragraph("Datum: " + order.getCreatedAt().toLocalDate()).setFont(regular).setFontSize(10).setFontColor(ColorConstants.GRAY));
        headerTable.addCell(titleCell);

        Cell supplierCell = new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT);
        supplierCell.add(new Paragraph("Dobavljač").setFont(bold).setFontSize(10).setFontColor(ColorConstants.GRAY));
        supplierCell.add(new Paragraph(supplier.getName()).setFont(bold).setFontSize(12));
        supplierCell.add(new Paragraph("OIB: " + supplier.getOib()).setFont(regular).setFontSize(10));
        headerTable.addCell(supplierCell);

        return headerTable;
    }

    private Table buildCompanyTable(Company company, Venue venue, PdfFont bold, boolean hideCompanyName) {
        Table companyTable = new Table(UnitValue.createPercentArray(new float[]{100}))
                .setWidth(UnitValue.createPercentValue(50));

        Cell companyCell = new Cell()
                .setBackgroundColor(new DeviceRgb(245, 245, 245))
                .setBorder(Border.NO_BORDER)
                .setPadding(10);
        companyCell.add(new Paragraph("Naručitelj").setFont(bold).setFontSize(10).setFontColor(ColorConstants.GRAY));
        System.out.println("pdf " + hideCompanyName);
        if (!hideCompanyName) {
            companyCell.add(new Paragraph(company.getName()).setFont(bold).setFontSize(12));
            companyCell.add(new Paragraph("OIB: " + company.getOib()).setFont(bold).setFontSize(12));
            companyCell.add(
                    new Paragraph(company.getAddress() + " " + company.getPostalCode() + " " + company.getCity())
                            .setFont(bold)
                            .setFontSize(12)
            );
            if(venue != null) {
                companyCell.add(new Paragraph(" ").setMarginBottom(10));
                companyCell.add(new Paragraph(venue.getName()).setFont(bold).setFontSize(10));
            }
        }
        if (venue != null) {
            companyCell.add(new Paragraph(venue.getAddress()).setFont(bold).setFontSize(10).setFontColor(ColorConstants.GRAY));
            companyCell.add(new Paragraph(venue.getPostalCode() + " " + venue.getCity()).setFont(bold).setFontSize(10).setFontColor(ColorConstants.GRAY));
        }

        companyTable.addCell(companyCell);
        return companyTable;
    }

    private Table buildProductTable(Order order, List<OrderItem> items, PdfFont bold, PdfFont regular) {
        Table productTable = new Table(UnitValue.createPercentArray(new float[]{50, 30, 20}))
                .setWidth(UnitValue.createPercentValue(100));

        Stream.of("Naziv proizvoda", "Šifra", "Količina").forEach(col -> productTable.addHeaderCell(
                new Cell()
                        .setBackgroundColor(new DeviceRgb(50, 50, 50))
                        .add(new Paragraph(col).setFont(bold).setFontSize(10).setFontColor(ColorConstants.WHITE))
        ));

        for (int i = 0; i < items.size(); i++) {
            addProductRow(productTable, items.get(i), i, regular);
        }

        return productTable;
    }

    private void addProductRow(Table productTable, OrderItem item, int index, PdfFont regular) {
        CatalogueItem catalogueItem = catalogueItemMapper.findNameCodeById(item.getCatalogueItemId());

        String name = item.getProductName();
        String code = "N/A";
        if(item.getSource().equals("CATALOGUE")){
            code = catalogueItem != null ? catalogueItem.getCode() : "N/A";
        }




        DeviceRgb rowColor = index % 2 == 0
                ? new DeviceRgb(255, 255, 255)
                : new DeviceRgb(250, 250, 250);

        productTable.addCell(new Cell()
                .setBackgroundColor(rowColor)
                .add(new Paragraph(name)
                        .setFont(regular)
                        .setFontSize(10)));

        productTable.addCell(new Cell()
                .setBackgroundColor(rowColor)
                .add(new Paragraph(code)
                        .setFont(regular)
                        .setFontSize(10)));

        productTable.addCell(new Cell()
                .setBackgroundColor(rowColor)
                .add(new Paragraph(item.getQuantity().stripTrailingZeros().toPlainString())
                        .setFont(regular)
                        .setFontSize(10)));
    }
}
