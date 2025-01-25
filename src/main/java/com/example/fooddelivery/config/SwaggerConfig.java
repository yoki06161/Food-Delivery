package com.example.fooddelivery.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    // 기본 API 그룹 설정
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("food-delivery-public")
                .pathsToMatch("/api/v1/public/**")
                .build();
    }

    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("food-delivery-auth")
                .pathsToMatch("/api/v1/auth/**")
                .build();
    }

    @Bean
    public GroupedOpenApi menuApi() {
        return GroupedOpenApi.builder()
                .group("food-delivery-menu")
                .pathsToMatch("/api/v1/menu/**")
                .build();
    }

    @Bean
    public GroupedOpenApi orderApi() {
        return GroupedOpenApi.builder()
                .group("food-delivery-orders")
                .pathsToMatch("/api/v1/orders/**")
                .build();
    }

    @Bean
    public GroupedOpenApi storeApi() {
        return GroupedOpenApi.builder()
                .group("food-delivery-store")
                .pathsToMatch("/api/v1/store/**")
                .build();
    }

    @Bean
    public GroupedOpenApi deliveryApi() {
        return GroupedOpenApi.builder()
                .group("food-delivery-delivery")
                .pathsToMatch("/api/v1/delivery/**")
                .build();
    }
}
