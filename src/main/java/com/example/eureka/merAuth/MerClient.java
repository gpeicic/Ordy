package com.example.eureka.merAuth;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class MerClient {

    private final WebClient webClient;

    public MerClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public MerLoginResponse login(MerLoginRequest request) {

        return webClient.post()
                .uri("https://api2.moj-eracun.hr/apis/Authorization/SignIn")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(MerLoginResponse.class)
                .block();
    }
}