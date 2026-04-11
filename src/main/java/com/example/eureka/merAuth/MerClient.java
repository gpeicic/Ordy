package com.example.eureka.merAuth;

import com.example.eureka.exception.ValidationException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class MerClient {

    private final WebClient webClient;

    public MerClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public MerLoginResponse login(MerLoginRequest request) {
        try {
            return webClient.post()
                    .uri("https://api2.moj-eracun.hr/apis/Authorization/SignIn")
                    .bodyValue(request)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError(),
                            res -> Mono.error(new ValidationException("MER login failed — krivi kredencijali")))
                    .onStatus(status -> status.is5xxServerError(),
                            res -> Mono.error(new RuntimeException("MER servis nedostupan")))
                    .bodyToMono(MerLoginResponse.class)
                    .block();
        } catch (Exception e) {
            throw new RuntimeException("Greška pri spajanju na MER: " + e.getMessage());
        }
    }
}