package com.example.eureka.merInvoices;

import com.example.eureka.exception.UnauthorizedException;
import com.example.eureka.merInvoices.dto.DownloadRequest;
import com.example.eureka.merInvoices.dto.InvoiceSummary;
import com.example.eureka.merInvoices.dto.SearchReceivedRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class MerInvoiceClient {

    private final WebClient webClient;

    public MerInvoiceClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<InvoiceSummary> searchReceived(String accessToken, SearchReceivedRequest request) {
        try {
            return webClient.post()
                    .uri("https://api2.moj-eracun.hr/view/Document/SearchReceived")
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Accept", "application/json, text/plain, */*")
                    .bodyValue(request)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError(),
                            res -> Mono.error(new UnauthorizedException("MER token istekao ili neispravan")))
                    .onStatus(status -> status.is5xxServerError(),
                            res -> Mono.error(new RuntimeException("MER servis nedostupan")))
                    .bodyToFlux(InvoiceSummary.class)
                    .collectList()
                    .block();
        } catch (UnauthorizedException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Greška pri dohvaćanju računa s MER-a: " + e.getMessage());
        }
    }

    public byte[] downloadXml(String accessToken, Long documentId) {
        try {
            DownloadRequest request = new DownloadRequest(documentId, 1);
            return webClient.post()
                    .uri("https://api2.moj-eracun.hr/view/Document/Download")
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Accept", "application/xml")
                    .bodyValue(request)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError(),
                            res -> Mono.error(new UnauthorizedException("MER token istekao ili neispravan")))
                    .onStatus(status -> status.is5xxServerError(),
                            res -> Mono.error(new RuntimeException("MER servis nedostupan")))
                    .bodyToMono(byte[].class)
                    .block();
        } catch (UnauthorizedException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Greška pri preuzimanju XML-a s MER-a: " + e.getMessage());
        }
    }
}