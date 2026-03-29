package com.example.eureka.merInvoices;

import com.example.eureka.merInvoices.dto.DownloadRequest;
import com.example.eureka.merInvoices.dto.InvoiceSummary;
import com.example.eureka.merInvoices.dto.SearchReceivedRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class MerInvoiceClient {

    private final WebClient webClient;

    public MerInvoiceClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<InvoiceSummary> searchReceived(String accessToken, SearchReceivedRequest request) {
        return webClient.post()
                .uri("https://api2.moj-eracun.hr/view/Document/SearchReceived")
                .header("Authorization", "Bearer " + accessToken)
                .header("Accept", "application/json, text/plain, */*")
                .bodyValue(request)
                .retrieve()
                .bodyToFlux(InvoiceSummary.class) // -> Flux jer je array
                .collectList()
                .block();
    }

    public byte[] downloadXml(String accessToken, Long documentId) {

        DownloadRequest request = new DownloadRequest(documentId, 1);

        return webClient.post()
                .uri("https://api2.moj-eracun.hr/view/Document/Download")
                .header("Authorization", "Bearer " + accessToken)
                .header("Accept", "application/xml")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(byte[].class)
                .block();
    }
}