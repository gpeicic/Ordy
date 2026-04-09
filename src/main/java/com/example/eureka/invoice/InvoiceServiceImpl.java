package com.example.eureka.invoice;

import com.example.eureka.supplier.CompanySuppliersMapper;
import com.example.eureka.merInvoices.parsedInvoice.dto.ParsedInvoice;
import com.example.eureka.merInvoices.parsedInvoice.dto.ParsedInvoiceItem;
import com.example.eureka.products.ProductService;
import com.example.eureka.supplier.Supplier;
import com.example.eureka.supplier.SupplierMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class InvoiceServiceImpl implements  InvoiceService {
    private final SupplierMapper supplierMapper;
    private final InvoiceMapper invoiceMapper;
    private final InvoiceItemMapper itemMapper;
    private final ProductService productService;
    private final CompanySuppliersMapper companySuppliersMapper;


    public InvoiceServiceImpl(SupplierMapper supplierMapper,
                              InvoiceMapper invoiceMapper,
                              InvoiceItemMapper itemMapper,
                              ProductService productService,
                              CompanySuppliersMapper companySuppliersMapper) {
        this.supplierMapper = supplierMapper;
        this.invoiceMapper = invoiceMapper;
        this.itemMapper = itemMapper;
        this.productService = productService;
        this.companySuppliersMapper = companySuppliersMapper;
    }


    @Transactional
    @Override
    public void saveParsedInvoice(ParsedInvoice parsed, Long externalDocumentId, Long companyId) {
        if (parsed == null) {
            return;
        }

        Supplier supplier = findOrCreateSupplier(parsed);

        if (invoiceAlreadyExists(supplier.getId(), parsed.getInterniBroj())) {
            return;
        }

        linkSupplierToCompanyIfMissing(companyId, supplier.getId());

        Invoice invoice = createAndSaveInvoice(parsed, externalDocumentId, companyId, supplier.getId());

        saveInvoiceItems(parsed.getItems(), invoice.getId());
    }

    @Override
    public BigDecimal getMonthlySpending(Long companyId) {
        return itemMapper.getMonthlySpending(companyId);
    }
    @Override
    public Long getTopSupplierId(Long companyId) {
        return invoiceMapper.findTopSupplierBySpending(companyId);
    }

    private Supplier findOrCreateSupplier(ParsedInvoice parsed) {
        Supplier supplier = supplierMapper.findByOib(parsed.getOib());
        return supplier != null ? supplier : createSupplier(parsed);
    }

    private Supplier createSupplier(ParsedInvoice parsed) {
        Supplier supplier = new Supplier();
        supplier.setOib(parsed.getOib());
        supplier.setName(parsed.getSupplierName());
        supplierMapper.insert(supplier);
        return supplier;
    }

    private boolean invoiceAlreadyExists(Long supplierId, String invoiceNumber) {
        return invoiceMapper.findExisting(supplierId, invoiceNumber) != null;
    }

    private void linkSupplierToCompanyIfMissing(Long companyId, Long supplierId) {
        boolean exists = companySuppliersMapper.countExists(companyId, supplierId) > 0;
        if (!exists) {
            companySuppliersMapper.insert(companyId, supplierId);
        }
    }

    private Invoice createAndSaveInvoice(ParsedInvoice parsed, Long externalDocumentId, Long companyId, Long supplierId) {
        Invoice invoice = new Invoice();
        invoice.setSupplierId(supplierId);
        invoice.setInvoiceNumber(parsed.getInterniBroj());
        invoice.setInvoiceDatetime(parsed.getInvoiceDateTime());
        invoice.setExternalDocumentId(externalDocumentId);
        invoice.setCompanyId(companyId);
        invoiceMapper.insert(invoice);
        return invoice;
    }

    private void saveInvoiceItems(List<ParsedInvoiceItem> parsedItems, Long invoiceId) {
        for (ParsedInvoiceItem parsedItem : parsedItems) {
            InvoiceItem item = buildInvoiceItem(parsedItem, invoiceId);
            itemMapper.insert(item);
        }
    }

    private InvoiceItem buildInvoiceItem(ParsedInvoiceItem parsedItem, Long invoiceId) {
        Long productId = productService.resolveProductId(parsedItem.getProductName());

        InvoiceItem item = new InvoiceItem();
        item.setInvoiceId(invoiceId);
        item.setProductName(parsedItem.getProductName());
        item.setProductId(productId);
        item.setUnitPrice(parsedItem.getUnitPrice());
        item.setDiscount(parsedItem.getDiscount());
        item.setAmount(parsedItem.getAmount());
        return item;
    }
}
