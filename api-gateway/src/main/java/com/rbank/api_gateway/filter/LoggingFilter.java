package com.rbank.api_gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {
     private static final Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class);
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Get the incoming request
        ServerHttpRequest request = exchange.getRequest();
        // Log request details
        System.out.println("=== Incoming Request ===");
        LOGGER.info("Method: {}, Path: {}, Headers: {}, Query Params: {}, Remote Address: {}", request.getMethod(),request.getPath(),request.getHeaders(),request.getQueryParams(),Objects.requireNonNull(request.getRemoteAddress()).getAddress());
        System.out.println("=========================");

        // Continue the filter chain
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        // Set the order of the filter (lower values have higher priority)
        return Ordered.LOWEST_PRECEDENCE;
    }
}