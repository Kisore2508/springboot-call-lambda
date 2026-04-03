package com.example.springbootcalllambda.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;

import java.util.Map;

@Service
public class LambdaService {

    private final LambdaClient lambdaClient;
    private final ObjectMapper objectMapper;

    public LambdaService(
            LambdaClient lambdaClient,
            ObjectMapper objectMapper) {
        this.lambdaClient = lambdaClient;
        this.objectMapper = objectMapper;
    }

    public String invoke(String functionName, Map<String, Object> payload) {
        try {
            String json = objectMapper.writeValueAsString(payload);

            InvokeRequest request = InvokeRequest.builder()
                    .functionName(functionName)
                    .payload(SdkBytes.fromUtf8String(json))
                    .build();

            InvokeResponse response = lambdaClient.invoke(request);

            return response.payload().asUtf8String();

        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke Lambda", e);
        }
    }
}
