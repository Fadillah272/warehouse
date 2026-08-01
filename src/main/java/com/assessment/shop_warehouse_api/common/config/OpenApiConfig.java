package com.assessment.shop_warehouse_api.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI shopWarehouseOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Shop Warehouse Management API")
                .description("REST API for Managing shop warehouse inventory")
                .version("v1.0")
        );
    }

}
