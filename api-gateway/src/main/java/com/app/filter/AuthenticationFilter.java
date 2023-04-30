package com.app.filter;

import com.app.utils.JwtUtil;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;
    @Autowired
    private JwtUtil jwtUtil;
    AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {
            // what endpoints to validate
            if(validator.isSecured.test(exchange.getRequest())) {
                // contain token in header
                if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new JwtException("Missing authorization header");
                }
                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if(authHeader == null || !authHeader.startsWith("Bearer ")) {
                    throw  new JwtException("JWT malformed");
                }
                authHeader = authHeader.split(" ")[1];


                try {
                    // validate token
                    jwtUtil.validateToken(authHeader);
                } catch (JwtException | IllegalArgumentException exception) {
                    throw new JwtException(exception.getMessage());
                }
            }

            return chain.filter(exchange);
        }));
    }

    public static class Config{}
}
