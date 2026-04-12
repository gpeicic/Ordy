package com.example.eureka.merInvoices;

import com.example.eureka.exception.ResourceNotFoundException;
import com.example.eureka.invoice.InvoiceService;
import com.example.eureka.merAuth.MerAuthService;
import com.example.eureka.merInvoices.dto.InvoiceSummary;
import com.example.eureka.merInvoices.dto.SearchReceivedRequest;
import com.example.eureka.merInvoices.parsedInvoice.MerUblinvoiceParser;
import com.example.eureka.merInvoices.parsedInvoice.dto.ParsedInvoice;
import com.example.eureka.sessionToken.SessionToken;
import com.example.eureka.sessionToken.SessionTokenMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MerInvoiceServiceImpl implements MerInvoiceService {

    private static final Logger log = LoggerFactory.getLogger(MerInvoiceServiceImpl.class);

    private final SessionTokenMapper sessionTokenMapper;
    private final MerAuthService merAuthService;
    private final MerInvoiceClient merInvoiceClient;
    private final MerUblinvoiceParser ublInvoiceParser;
    private final InvoiceService invoiceService;
    private final TextEncryptor encryptor;

    public MerInvoiceServiceImpl(SessionTokenMapper sessionTokenMapper,
                                 MerInvoiceClient merInvoiceClient,
                                 MerUblinvoiceParser ublInvoiceParser,
                                 InvoiceService invoiceService,
                                 TextEncryptor encryptor,
                                 MerAuthService merAuthService) {
        this.sessionTokenMapper = sessionTokenMapper;
        this.merInvoiceClient = merInvoiceClient;
        this.ublInvoiceParser = ublInvoiceParser;
        this.invoiceService = invoiceService;
        this.encryptor = encryptor;
        this.merAuthService = merAuthService;
    }

    @Override
    public List<InvoiceSummary> getReceivedInvoices(Long companyId) {
        log.info("Dohvaćanje računa s MER-a za kompaniju: {}", companyId);
        SessionToken token = findTokenOrThrow(companyId);
        SearchReceivedRequest request = buildDefaultSearchRequest();
        List<InvoiceSummary> invoices = merInvoiceClient.searchReceived(token.getAccessToken(), request);
        log.info("Dohvaćeno {} računa za kompaniju: {}", invoices.size(), companyId);
        return invoices;
    }


    @Override
    public String downloadXml(Long companyId, Long documentId) {
        log.info("Download XML računa — companyId: {}, documentId: {}", companyId, documentId);
        SessionToken token = findTokenOrThrow(companyId);
        byte[] xmlBytes = merInvoiceClient.downloadXml(token.getAccessToken(), documentId);
        ParsedInvoice parsed = ublInvoiceParser.parse(xmlBytes);

        if (parsed == null) {
            log.warn("XML parsiran ali invoice skiped (blocked supplier) — documentId: {}", documentId);
        } else {
            invoiceService.saveParsedInvoice(parsed, documentId, companyId);
            log.info("Invoice uspješno spremljen — documentId: {}, supplier: {}", documentId, parsed.getSupplierName());
        }

        return new String(xmlBytes, StandardCharsets.UTF_8);
    }

    private SessionToken findTokenOrThrow(Long companyId) {
        SessionToken token = sessionTokenMapper.findByCompanyId(companyId);
        if (token == null) {
            log.error("MER token nije pronađen za kompaniju: {}", companyId);
            throw new ResourceNotFoundException("MER token nije pronađen za kompaniju: " + companyId);
        }

        if (isTokenExpired(token)) {
            log.info("MER token istekao za kompaniju: {}, refresham...", companyId);
            merAuthService.loginCompany(companyId);
            token = sessionTokenMapper.findByCompanyId(companyId);
            log.info("MER token refreshan za kompaniju: {}", companyId);
        }

        token.setAccessToken(encryptor.decrypt(token.getAccessToken()));
        return token;
    }

    private boolean isTokenExpired(SessionToken token) {
        return token.getExpiration() == null ||
                LocalDateTime.now().isAfter(token.getExpiration().minusMinutes(5));
    }

    private SearchReceivedRequest buildDefaultSearchRequest() {
        SearchReceivedRequest request = new SearchReceivedRequest();
        request.setPageNumber(1);
        request.setPageSize(50);
        request.setDocumentStatuses(List.of());
        request.setDocumentTypes(List.of());
        request.setSearchText("");
        return request;
    }
}