package com.microecommerce.gatewayserver.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.stream.Stream;

@Component
public class AuthFilter implements GlobalFilter {

    private final JwtService jwtService;

    public AuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // TODO: Apply authentication logic here

        // Exlcude auth paths from authentication
        // TODO: This is a temporary solutions, shoud add to this list the free-auth paths on other microservices
        boolean goToAuthPaths = Stream.of("/auth/register", "/auth/login")
                                    .anyMatch(uri -> exchange.getRequest().getURI().getPath().contains(uri));
        if(goToAuthPaths) {
            return chain.filter(exchange);
        }

        if(!exchange.getRequest().getHeaders().containsKey("Authorization")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = exchange.getRequest().getHeaders().get("Authorization").get(0);
        if(!token.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        try {
            jwtService.validateToken(token.substring(7));
        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }
}
