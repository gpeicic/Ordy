package com.example.eureka.invoice;

import com.example.eureka.company.CompanyMapper;
import com.example.eureka.merInvoices.MerInvoiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class InvoiceSyncScheduler {

    private static final Logger log = LoggerFactory.getLogger(InvoiceSyncScheduler.class);

    private final CompanyMapper companyMapper;
    private final MerInvoiceService merInvoiceService;

    public InvoiceSyncScheduler(CompanyMapper companyMapper,
                                MerInvoiceService merInvoiceService) {
        this.companyMapper = companyMapper;
        this.merInvoiceService = merInvoiceService;
    }

    @Scheduled(fixedDelay = 2 * 60 * 60 * 1000, initialDelay = 2 * 60 * 1000)
    public void syncAllCompanies() {
        List<Long> companyIds = companyMapper.findAllIds();
        log.info("Scheduled sync pokrenut — {} kompanija", companyIds.size());

        for (Long companyId : companyIds) {
            try {
                merInvoiceService.syncNewInvoices(companyId);
            } catch (Exception ex) {
                log.error("Sync pao za companyId: {}", companyId, ex);
            }
        }
    }
}