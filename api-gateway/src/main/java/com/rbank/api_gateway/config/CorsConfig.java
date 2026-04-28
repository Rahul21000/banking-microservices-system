package com.rbank.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    WebFluxConfigurer corsConfigurer(){
        return new WebFluxConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET","PUT","POST","DELETE");
//                WebFluxConfigurer.super.addCorsMappings(registry);
            }
        };
    }
}
