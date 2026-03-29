package com.example.eureka.merInvoices;

import com.example.eureka.invoice.InvoiceService;
import com.example.eureka.merInvoices.dto.InvoiceSummary;
import com.example.eureka.merInvoices.dto.SearchReceivedRequest;
import com.example.eureka.merInvoices.parsedInvoice.MerUblinvoiceParser;
import com.example.eureka.merInvoices.parsedInvoice.dto.ParsedInvoice;
import com.example.eureka.sessionToken.SessionToken;
import com.example.eureka.sessionToken.SessionTokenMapper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class MerInvoiceServiceImpl implements MerInvoiceService {

    private final SessionTokenMapper sessionTokenMapper;
    private final MerInvoiceClient merInvoiceClient;
    private final MerUblinvoiceParser ublInvoiceParser;
    private final InvoiceService invoiceService;

    public MerInvoiceServiceImpl(SessionTokenMapper sessionTokenMapper,
                                 MerInvoiceClient merInvoiceClient,
                                 MerUblinvoiceParser ublInvoiceParser,
                                 InvoiceService invoiceService) {
        this.sessionTokenMapper = sessionTokenMapper;
        this.merInvoiceClient = merInvoiceClient;
        this.ublInvoiceParser = ublInvoiceParser;
        this.invoiceService = invoiceService;
    }

    @Override
    public List<InvoiceSummary> getReceivedInvoices(Long companyId) {
        SessionToken token = findTokenOrThrow(companyId);
        SearchReceivedRequest request = buildDefaultSearchRequest();
        return merInvoiceClient.searchReceived(token.getAccessToken(), request);
    }

    @Override
    public String downloadXml(Long companyId, Long documentId) {
        SessionToken token = findTokenOrThrow(companyId);
        byte[] xmlBytes = merInvoiceClient.downloadXml(token.getAccessToken(), documentId);
        ParsedInvoice parsed = ublInvoiceParser.parse(xmlBytes);
        invoiceService.saveParsedInvoice(parsed, documentId, companyId);
        return new String(xmlBytes, StandardCharsets.UTF_8);
    }

    private SessionToken findTokenOrThrow(Long companyId) {
        SessionToken token = sessionTokenMapper.findByCompanyId(companyId);
        if (token == null) {
            throw new RuntimeException("MER token not found for company " + companyId);
        }
        return token;
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