package com.example.springbootcalllambda.controller;


import com.example.springbootcalllambda.service.LambdaHttpService;
import com.example.springbootcalllambda.service.LambdaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/lambda")
public class LambdaController {

    private final LambdaService lambdaService;

    private final LambdaHttpService lambdaHttpService;


    @Value("${aws.lambda.add-user}")
    private String addUserFunction;

    @Value("${aws.lambda.get-user}")

    private String getUserFunction;
    @Value("${aws1.lambda1.get-all-users-url}")
    private String getAllUsersUrl;

    public LambdaController(LambdaService lambdaService, LambdaHttpService lambdaHttpService) {
        this.lambdaService = lambdaService;
        this.lambdaHttpService = lambdaHttpService;
    }

    @PostMapping("/add-user")
    public String addUser(@RequestBody Map<String, Object> payload) {
        return lambdaService.invoke(addUserFunction, payload);
    }

    @GetMapping("/users")
    public String getAllUsers() {
        return lambdaService.invoke(getUserFunction, Map.of());
    }

    @GetMapping("/users-http")
    public String getAllUsersHttp() {
        return lambdaHttpService.getAllUsers(getAllUsersUrl);
    }
}
