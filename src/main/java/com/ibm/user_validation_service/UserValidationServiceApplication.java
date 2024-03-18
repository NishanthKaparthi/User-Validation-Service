package com.ibm.user_validation_service;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.ibm.user_validation_service")
@OpenAPIDefinition(info = @Info(title = "User Validation API", version = "1.0"))
public class UserValidationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserValidationServiceApplication.class, args);
    }

}
