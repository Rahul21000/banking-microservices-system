package com.rbank.api_gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private final WebClient.Builder webClientBuilder;
    private final Logger LOGGER = LoggerFactory.getLogger(AuthFilter.class);

    public AuthFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String token = extractToken(request);

            if (token == null) {
                return onError(exchange, "Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
            }
            LOGGER.info("Calling AuthService URL: {}", config.getAuthServiceUrl() + "/validate?token=" + token);

            // Call Auth Service to validate the token
            return webClientBuilder.build()
                    .get()
                    .uri(config.getAuthServiceUrl() + "/validate?token=" + token)
                    .retrieve()
                    .bodyToMono(TokenValidationResponse.class)
                    .flatMap(response -> {
                        if (response.isValid()) {
                            // Add roles to headers for further authorization in microservices
                            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                                    .header("X-User-Role", String.join(",", response.getRoles()))
                                    .build();
                            return chain.filter(exchange.mutate().request(modifiedRequest).build());
                        } else {
                            return onError(exchange, "Invalid Token", HttpStatus.UNAUTHORIZED);
                        }
                    })
                    .onErrorResume(error -> {
                        LOGGER.error("AuthService call failed: {}", error.getMessage()); // Log error
                        return onError(exchange, "AuthService unavailable", HttpStatus.SERVICE_UNAVAILABLE);
                    });

        };
    }

    private String extractToken(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        return (authHeader != null && authHeader.startsWith("Bearer ")) ? authHeader.substring(7) : null;
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        return exchange.getResponse().setComplete();
    }

    public static class Config {
        private String authServiceUrl = "http://localhost:8081/auth";

        public String getAuthServiceUrl() {
            return authServiceUrl;
        }

        public void setAuthServiceUrl(String authServiceUrl) {
            this.authServiceUrl = authServiceUrl;
        }
    }
    public static class TokenValidationResponse {
        private boolean valid;
        private String[] roles;

        public boolean isValid() {
            return valid;
        }

        public String[] getRoles() {
            return roles;
        }
    }
}
