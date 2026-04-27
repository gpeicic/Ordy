package com.example.eureka.merInvoices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class FirstSyncEventListener {

    private static final Logger log = LoggerFactory.getLogger(FirstSyncEventListener.class);

    private final MerInvoiceService merInvoiceService;

    public FirstSyncEventListener(MerInvoiceService merInvoiceService) {
        this.merInvoiceService = merInvoiceService;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCompanyRegistered(CompanyRegisteredEvent event) {
        log.info("First sync pokrenut async nakon registracije — companyId: {}", event.companyId());
        try {
            merInvoiceService.firstSync(event.companyId());
        } catch (Exception ex) {
            log.error("First sync nije uspio — companyId: {}", event.companyId(), ex);
        }
    }
}

