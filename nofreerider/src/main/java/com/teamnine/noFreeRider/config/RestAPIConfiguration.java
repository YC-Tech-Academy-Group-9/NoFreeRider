package com.teamnine.noFreeRider.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "SNS API",
                version = "1.0",
                description = "SNS API"
        )
)

@Configuration
public class RestAPIConfiguration {
}
