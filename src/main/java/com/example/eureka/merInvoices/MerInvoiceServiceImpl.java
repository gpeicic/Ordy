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
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
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
    private final MerTokenProvider merTokenProvider;

    public MerInvoiceServiceImpl(SessionTokenMapper sessionTokenMapper,
                                 MerInvoiceClient merInvoiceClient,
                                 MerUblinvoiceParser ublInvoiceParser,
                                 InvoiceService invoiceService,
                                 TextEncryptor encryptor,
                                 MerAuthService merAuthService,
                                 MerTokenProvider merTokenProvider) {
        this.sessionTokenMapper = sessionTokenMapper;
        this.merInvoiceClient = merInvoiceClient;
        this.ublInvoiceParser = ublInvoiceParser;
        this.invoiceService = invoiceService;
        this.encryptor = encryptor;
        this.merAuthService = merAuthService;
        this.merTokenProvider = merTokenProvider;
    }

    @Override
    public List<InvoiceSummary> getReceivedInvoices(Long companyId) {
        log.info("Dohvaćanje računa s MER-a za kompaniju: {}", companyId);
        List<InvoiceSummary> invoices = getReceivedInvoicesPage(companyId, 1, 50);
        log.info("Dohvaćeno {} računa za kompaniju: {}", invoices.size(), companyId);
        return invoices;
    }

    @Override
    public void firstSync(Long companyId) {
        log.info("Pokretanje first sync-a za kompaniju: {}", companyId);
        merAuthService.loginCompany(companyId);

        List<InvoiceSummary> invoices = getAllReceivedInvoices(companyId);
        log.info("First sync — dohvaćeno {} računa za kompaniju: {}", invoices.size(), companyId);
        int imported = 0;
        int skipped = 0;
        int failed = 0;

        for (InvoiceSummary invoiceSummary : invoices) {
            Long documentId = invoiceSummary.getId();
            if (documentId == null) {
                skipped++;
                log.warn("Preskačem invoice bez documentId za kompaniju: {}", companyId);
                continue;
            }

            try {
                downloadXml(companyId, documentId);
                imported++;
            } catch (Exception ex) {
                failed++;
                log.error("Neuspješan import invoice-a — companyId: {}, documentId: {}", companyId, documentId, ex);
            }
        }

        log.info("First sync završen — companyId: {}, ukupno: {}, importano: {}, preskočeno: {}, greške: {}",
                companyId, invoices.size(), imported, skipped, failed);
    }


    @Override
    public String downloadXml(Long companyId, Long documentId) {
        log.info("Download XML računa — companyId: {}, documentId: {}", companyId, documentId);
        SessionToken token = merTokenProvider.getValidToken(companyId);
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

    @Async
    @Override
    public void syncNewInvoices(Long companyId) {
        log.info("Sync novih računa — companyId: {}", companyId);
        Long maxKnownId = invoiceService.getMaxExternalDocumentId(companyId);
        log.info("Sync — max poznati externalDocumentId: {} za companyId: {}", maxKnownId, companyId);

        List<InvoiceSummary> allSummaries = getAllReceivedInvoices(companyId);

        List<Long> toDownload = allSummaries.stream()
                .map(InvoiceSummary::getId)
                .filter(id -> id != null && (maxKnownId == null || id > maxKnownId))
                .toList();

        log.info("Sync — ukupno s MER-a: {}, za download: {}", allSummaries.size(), toDownload.size());

        int imported = 0, failed = 0;
        for (Long documentId : toDownload) {
            try {
                downloadXml(companyId, documentId);
                imported++;
            } catch (Exception ex) {
                failed++;
                log.error("Sync — neuspješan download — companyId: {}, documentId: {}", companyId, documentId, ex);
            }
        }

        log.info("Sync završen — companyId: {}, importano: {}, greške: {}", companyId, imported, failed);
    }

    private List<InvoiceSummary> getAllReceivedInvoices(Long companyId) {
        List<InvoiceSummary> invoices = getReceivedInvoicesPage(companyId, 1, 200);
        log.info("First sync — MER vratio ukupno {} računa za kompaniju: {}", invoices.size(), companyId);
        return invoices;
    }

    private List<InvoiceSummary> getReceivedInvoicesPage(Long companyId, int pageNumber, int pageSize) {
        SessionToken token = merTokenProvider.getValidToken(companyId);
        log.info("Pozivam MER searchReceived — companyId: {}, page: {}", companyId, pageNumber);
        SearchReceivedRequest request = buildDefaultSearchRequest(pageNumber, pageSize);
        List<InvoiceSummary> invoices = merInvoiceClient.searchReceived(token.getAccessToken(), request);
        log.info("MER searchReceived vratio {} računa", invoices == null ? "null" : invoices.size());
        return invoices == null ? List.of() : invoices;
    }

    private SearchReceivedRequest buildDefaultSearchRequest(int pageNumber, int pageSize) {
        SearchReceivedRequest request = new SearchReceivedRequest();
        request.setPageNumber(pageNumber);
        request.setPageSize(pageSize);
        request.setDocumentStatuses(List.of());
        request.setDocumentTypes(List.of());
        request.setSearchText("");
        return request;
    }
}