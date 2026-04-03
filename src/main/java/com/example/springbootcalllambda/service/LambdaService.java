package service;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class LambdaService {

    private final LambdaClient lambdaClient;
    private final ObjectMapper objectMapper;
    private final String functionName;

    public LambdaService(
            LambdaClient lambdaClient,
            ObjectMapper objectMapper,
            @Value("${aws.lambda.function-name}") String functionName) {
        this.lambdaClient = lambdaClient;
        this.objectMapper = objectMapper;
        this.functionName = functionName;
    }

    public String invoke(Map<String, Object> payload) {
        try {
            String json = objectMapper.writeValueAsString(payload);

            InvokeRequest request = InvokeRequest.builder()
                    .functionName(functionName)
                    .payload(SdkBytes.fromString(json, StandardCharsets.UTF_8))
                    .build();

            InvokeResponse response = lambdaClient.invoke(request);

            if (response.functionError() != null) {
                throw new RuntimeException("Lambda returned error: " + response.functionError());
            }

            return response.payload().asUtf8String();
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke Lambda", e);
        }
    }
}
