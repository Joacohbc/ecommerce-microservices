package com.microecommerce.gatewayserver.filter;

import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Stream;

@Component
public class AuthFilter implements GlobalFilter {

    private final JwtService jwtService;

    public AuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }


    private record AuthPath(String path, List<HttpMethod> methods) {}

    // Auth paths that don't require authentication
    // TODO: Update this list with the correct paths
    // TODO: Use regex to match paths
    private final List<AuthPath> freeAuthPaths =
    List.of(
            new AuthPath("/auth/register", List.of(HttpMethod.POST)),
            new AuthPath("/auth/login", List.of(HttpMethod.POST)),
            new AuthPath("/products", List.of(HttpMethod.GET)),
            new AuthPath("/stores", List.of(HttpMethod.GET)),
            new AuthPath("/shipping/tracking", List.of(HttpMethod.GET))
    );


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // Exclude auth paths from authentication
        boolean isWithoutAuth = freeAuthPaths.stream().anyMatch(authsPaths ->
            exchange.getRequest().getURI().getPath().startsWith(authsPaths.path())
                    && authsPaths.methods().contains(exchange.getRequest().getMethod())
        );

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
