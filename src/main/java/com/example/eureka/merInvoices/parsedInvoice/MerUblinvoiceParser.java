package com.example.eureka.merInvoices.parsedInvoice;

import com.example.eureka.exception.ValidationException;
import com.example.eureka.merInvoices.parsedInvoice.dto.ParsedInvoice;
import com.example.eureka.merInvoices.parsedInvoice.dto.ParsedInvoiceItem;
import com.example.eureka.supplier.BlockedSupplierKeyword;
import com.example.eureka.supplier.BlockedSupplierMapper;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MerUblinvoiceParser implements InvoiceParser {

    private final BlockedSupplierMapper blockedSupplierMapper;

    public MerUblinvoiceParser(BlockedSupplierMapper blockedSupplierMapper) {
        this.blockedSupplierMapper = blockedSupplierMapper;
    }
    @Override
    public ParsedInvoice parse(byte[] xmlBytes) {
        if (xmlBytes == null || xmlBytes.length == 0) {
            throw new ValidationException("XML bytes su prazni");
        }
        try {
            Document doc = buildDocument(xmlBytes);
            String supplierName = extractSupplierName(doc);

            if (BlockedSupplierKeyword.matches(supplierName)) {
                tryInsertBlockedSupplier(doc, supplierName);
                return null;
            }
            String oib = extractOib(doc);

            if (blockedSupplierMapper.existsByOib(oib)) {
                return null;
            }
            if (oib.length() != 11 || !oib.matches("\\d{11}")) {
                return null;
            }

            LocalDateTime invoiceDateTime = extractInvoiceDateTime(doc);
            String interniBroj = extractInterniBroj(doc);
            List<ParsedInvoiceItem> items = extractItems(doc);

            return new ParsedInvoice(supplierName, invoiceDateTime, items, oib, interniBroj);

        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Greška pri parsiranju XML računa: " + e.getMessage(), e);
        }
    }

    private Document buildDocument(byte[] xmlBytes) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xmlBytes));
        doc.getDocumentElement().normalize();
        return doc;
    }

    private String extractSupplierName(Document doc) {
        Element supplierElement = (Element) doc.getElementsByTagName("cac:AccountingSupplierParty").item(0);
        if (supplierElement == null) {
            throw new RuntimeException("AccountingSupplierParty element missing in XML");
        }

        String supplierName = extractSupplierNameFromParty(supplierElement);

        if (isNullOrEmpty(supplierName)) {
            supplierName = extractNameFromTag(doc, "cac:PartyLegalEntity");
        }
        if (isNullOrEmpty(supplierName)) {
            supplierName = extractNameFromTag(doc, "cac:SellerContact");
        }
        if (isNullOrEmpty(supplierName)) {
            throw new RuntimeException("Supplier name not found in XML");
        }

        return supplierName;
    }

    private String extractSupplierNameFromParty(Element supplierElement) {
        Element party = (Element) supplierElement.getElementsByTagName("cac:Party").item(0);
        if (party == null) return null;
        NodeList partyNames = party.getElementsByTagName("cac:PartyName");
        return getString(null, partyNames);
    }

    private String extractNameFromTag(Document doc, String tagName) {
        NodeList nodes = doc.getElementsByTagName(tagName);
        return getString(null, nodes);
    }

    private String extractOib(Document doc) {
        String oib = extractOibFromLegalEntity(doc);

        if (isNullOrEmpty(oib)) {
            oib = extractOibFromSellerContact(doc);
        }
        if (isNullOrEmpty(oib)) {
            throw new RuntimeException("OIB not found in XML");
        }

        return oib;
    }

    private String extractOibFromLegalEntity(Document doc) {
        NodeList supplierParties = doc.getElementsByTagName("cac:AccountingSupplierParty");
        if (supplierParties.getLength() == 0) return null;

        Element supplierParty = (Element) supplierParties.item(0);
        NodeList parties = supplierParty.getElementsByTagName("cac:Party");
        if (parties.getLength() == 0) return null;

        Element party = (Element) parties.item(0);
        return safeGetFirstText(party, "cbc:EndpointID");
    }

    private String extractOibFromSellerContact(Document doc) {
        NodeList sellerContacts = doc.getElementsByTagName("cac:SellerContact");
        if (sellerContacts.getLength() == 0 || sellerContacts.item(0) == null) return null;
        return safeGetFirstText((Element) sellerContacts.item(0), "cbc:ID");
    }


    private LocalDateTime extractInvoiceDateTime(Document doc) {
        String issueDate = safeGetTextContent(doc, "cbc:IssueDate");
        String issueTime = safeGetTextContent(doc, "cbc:IssueTime");
        assert issueDate != null;
        assert issueTime != null;
        return LocalDateTime.of(LocalDate.parse(issueDate), LocalTime.parse(issueTime));
    }

    private String extractInterniBroj(Document doc) {
        NodeList children = doc.getDocumentElement().getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("cbc:ID")) {
                return node.getTextContent();
            }
        }
        return null;
    }

    private List<ParsedInvoiceItem> extractItems(Document doc) {
        NodeList invoiceLines = doc.getElementsByTagName("cac:InvoiceLine");
        List<ParsedInvoiceItem> items = new ArrayList<>();

        for (int i = 0; i < invoiceLines.getLength(); i++) {
            items.add(parseInvoiceItem((Element) invoiceLines.item(i)));
        }

        return items;
    }

    private ParsedInvoiceItem parseInvoiceItem(Element line) {
        String productName = safeGetFirstText(line, "cbc:Name");
        if (productName == null || productName.isBlank()) {

            NodeList itemNodes = line.getElementsByTagName("cac:Item");
            if (itemNodes.getLength() > 0) {
                productName = safeGetFirstText((Element) itemNodes.item(0), "cbc:Name");
            }
        }
        if (productName == null || productName.isBlank()) {
            throw new ValidationException("Naziv proizvoda nije pronađen u InvoiceLine ID: "
                    + safeGetFirstText(line, "cbc:ID"));
        }
        BigDecimal price = new BigDecimal(safeGetFirstText(line, "cbc:PriceAmount"));
        BigDecimal amount = new BigDecimal(safeGetFirstText(line, "cbc:InvoicedQuantity"));
        BigDecimal discount = extractDiscount(line);
        return new ParsedInvoiceItem(productName, price, discount, amount);
    }

    private BigDecimal extractDiscount(Element line) {
        NodeList allowanceNodes = line.getElementsByTagName("cac:AllowanceCharge");
        if (allowanceNodes.getLength() == 0) return BigDecimal.ZERO;

        Element allowance = (Element) allowanceNodes.item(0);
        Node chargeNode = allowance.getElementsByTagName("cbc:ChargeIndicator").item(0);
        boolean isCharge = chargeNode != null && Boolean.parseBoolean(chargeNode.getTextContent());

        if (isCharge) return BigDecimal.ZERO;

        Node discountNode = allowance.getElementsByTagName("cbc:MultiplierFactorNumeric").item(0);
        return discountNode != null ? new BigDecimal(discountNode.getTextContent()) : BigDecimal.ZERO;
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }

    private String getString(String supplierName, NodeList sellerContacts) {
        if (sellerContacts.getLength() > 0 && sellerContacts.item(0) != null) {
            Element sellerContact = (Element) sellerContacts.item(0);
            Node nameNode = sellerContact.getElementsByTagName("cbc:RegistrationName").item(0);
            if (nameNode != null) {
                supplierName = nameNode.getTextContent().trim();
            }
        }
        return supplierName;
    }

    private static String safeGetFirstText(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() > 0 && nodes.item(0) != null) {
            String text = nodes.item(0).getTextContent();
            return text != null ? text.trim() : null;
        }
        return null;
    }

    private static String safeGetTextContent(Document doc, String tagName) {
        NodeList nodes = doc.getElementsByTagName(tagName);
        if (nodes.getLength() > 0 && nodes.item(0) != null) {
            String text = nodes.item(0).getTextContent();
            return text != null ? text.trim() : null;
        }
        return null;
    }

    private void tryInsertBlockedSupplier(Document doc, String supplierName) {
        try {
            String oib = extractOib(doc);
            if (!isNullOrEmpty(oib) && oib.matches("\\d{11}")) {
                blockedSupplierMapper.insert(oib, supplierName);
            }
        } catch (Exception ignored) {
            // nema OIB-a, ne spremamo
        }
    }
}