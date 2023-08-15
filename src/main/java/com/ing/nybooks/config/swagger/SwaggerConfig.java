package com.ing.nybooks.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * The Swagger configuration class for NYBooks
 */
@Configuration
public class SwaggerConfig {
    /**
     * Configuration for search Api
     *
     * @return GroupedOpenApi
     */
    @Bean
    public GroupedOpenApi searchApi() {
        return GroupedOpenApi.builder()
                .group("nyt books")
                .packagesToScan("com.ing.nybooks.web")
                .build();
    }


    /**
     * Configuration for api end points info
     *
     * @return OpenAPI
     */
    @Bean
    public OpenAPI apiEndPointsInfo() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .description("Service to expose details of books")
                        .version("1.0.0")
                        .title("Search Book on NYT"));
    }

}
