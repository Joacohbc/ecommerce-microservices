package com.microecommerce.gatewayserver.filter;

import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class AuthFilter implements GlobalFilter {

    private final JwtService jwtService;

    public AuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    // Auth paths that don't require authentication
    // TODO: Update this list with the correct paths
    public final List<String> freeAuthPaths = List.of(
            "/auth/register",
            "/auth/login",
            "/products",
            "/stores",
            "/shipping/tracking");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // TODO: Apply authentication logic here

        // Exclude auth paths from authentication
        boolean isWithoutAuth = freeAuthPaths.stream().anyMatch(uri -> exchange.getRequest().getURI().getPath().startsWith(uri));

        if(isWithoutAuth) {
            return chain.filter(exchange);
        }

        boolean haveAuthHeader = exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION);
        if(!haveAuthHeader) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = Optional.ofNullable(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION))
                .orElse(List.of(""))
                .get(0);

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
