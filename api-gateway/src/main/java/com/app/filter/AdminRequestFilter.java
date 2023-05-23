package com.app.filter;

import com.app.exceptions.UserAccessDenied;
import com.app.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
public class AdminRequestFilter extends AbstractGatewayFilterFactory<AdminRequestFilter.Config> {

    @Autowired
    private RouteValidator routeValidator;

    @Autowired
    private JwtUtil jwtUtil;

    AdminRequestFilter() {
        super(Config.class);
    }
    @Override
    public GatewayFilter apply(AdminRequestFilter.Config config) {
        return (exchange, chain) -> {
            if(routeValidator.adminRoutes.test(exchange.getRequest())) {
                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if(authHeader == null || !authHeader.startsWith("Bearer ")) {
                    throw  new JwtException("JWT malformed");
                }
                authHeader = authHeader.split(" ")[1];
                try {
                    Claims tokenBody = jwtUtil.getAllClaimsFromToken(authHeader);
                    // cast into an array
                    Object roles =  tokenBody.get("roles");
                    System.out.println("roles=---" + roles);
                    boolean isAdmin = false;
                    if(roles instanceof List) {
                        List<?> rolesList = (List<?>) roles;
                        System.out.println("role list mapped=-----------" + rolesList);
                        for(Object roleObj : rolesList) {
                            // Check if the role object is a map
                            if (roleObj instanceof Map) {
                                Map<String, Object> roleMap = (Map<String, Object>) roleObj;
                                String roleName = (String) roleMap.get("name");

                                // Compare the role with a certain value
                                if ("ADMIN".equals(roleName)) {
                                    isAdmin = true;
                                    break;
                                }
                            }
                        }
                    }
                    if(!isAdmin) {
                        throw new UserAccessDenied("User doesn't have ADMIN role");
                    }
                }  catch (UserAccessDenied e) {
                    throw new RuntimeException(e);
                }


            }
            return chain.filter(exchange);
        };
    }

    public static class Config {
        //Put the configuration properties for your filter here
    }
}
