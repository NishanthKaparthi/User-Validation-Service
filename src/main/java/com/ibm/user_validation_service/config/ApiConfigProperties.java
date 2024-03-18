package com.ibm.user_validation_service.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@Getter
public class ApiConfigProperties {
    @Value("${ip.api-url}")
    private String ipApiUrl;

    @Bean
    public RestTemplate restTemplateBean() {
        return new RestTemplate();
    }
}
