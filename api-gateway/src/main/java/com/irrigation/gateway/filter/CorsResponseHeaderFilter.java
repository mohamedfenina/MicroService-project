package com.irrigation.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Global filter to handle CORS preflight requests (OPTIONS)
 * This runs BEFORE routing to backend services
 */
@Component
public class CorsResponseHeaderFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // If this is an OPTIONS request (CORS preflight), respond immediately
        if (HttpMethod.OPTIONS.equals(exchange.getRequest().getMethod())) {
            exchange.getResponse().setStatusCode(HttpStatus.OK);
            return Mono.empty();
        }
        
        // For other requests, continue through the filter chain
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        // High precedence - run early in the filter chain
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
