package com.example.springbootcalllambda.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class LambdaHttpService {

    private final WebClient webClient;

    public LambdaHttpService(WebClient webClient) {
        this.webClient = webClient;
    }

    public String getAllUsers(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .block(); // blocking for simplicity
    }
}
