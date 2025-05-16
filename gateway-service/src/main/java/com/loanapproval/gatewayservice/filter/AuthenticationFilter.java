package com.loanapproval.gatewayservice.filter;

import com.loanapproval.gatewayservice.util.JwtUtil; // Adjust import if needed
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod; // Import HttpMethod
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    public static final List<String> publicPaths = List.of(
            "/auth/login",
            "/auth/register"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        logger.info("AUTHENTICATION FILTER INVOKED FOR PATH: {}", request.getURI().getPath()); // High-level log

        // **NEW: Explicitly allow OPTIONS preflight requests to pass through**
        if (request.getMethod() == HttpMethod.OPTIONS) {
            logger.trace("OPTIONS request for path: {}, allowing to pass for CORS handling.", request.getURI().getPath());
            // The CorsWebFilter should handle adding necessary headers for OPTIONS response.
            // Spring Security's permitAll for OPTIONS should also ensure it's not blocked later.
            return chain.filter(exchange);
        }

        // Check if the request path is public (existing logic)
        if (isPublicPath(request)) {
            logger.trace("Path is public: {}, skipping authentication for this filter.", request.getURI().getPath());
            return chain.filter(exchange);
        }

        logger.trace("Path requires authentication by this filter: {}", request.getURI().getPath());

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(authHeader)) {
            logger.warn("Missing Authorization header for path: {}", request.getURI().getPath());
            return unauthorized(exchange, "Missing Authorization header");
        }

        if (!authHeader.startsWith("Bearer ")) {
            logger.warn("Invalid Authorization header format for path: {}", request.getURI().getPath());
            return unauthorized(exchange, "Invalid Authorization header format, expecting Bearer token");
        }

        String token = authHeader.substring(7);

        logger.debug("Incoming token: {}", token);

        try {
            logger.debug("is token valid: {}", jwtUtil.validateToken(token));

            if (!jwtUtil.validateToken(token)) {
                logger.warn("Invalid or expired JWT token by jwtUtil for path: {}", request.getURI().getPath());
                return unauthorized(exchange, "Invalid or expired JWT token");
            }

            Claims claims = jwtUtil.getAllClaimsFromToken(token);
            logger.debug("extracted claims: {}", claims);
            String username = claims.getSubject();
            List<String> rolesFromToken = claims.get("roles", List.class);
            logger.debug("Extracted roles from token for user {}: {}", username, rolesFromToken); // LOG THIS
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();

            if (rolesFromToken != null) {
                authorities = rolesFromToken.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
                logger.debug("User {} authenticated with roles: {}", username, authorities);
            } else {
                logger.warn("No 'roles' claim found in JWT for user: {}", username);
            }

            Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);

            ServerHttpRequest modifiedRequest = request.mutate()
                    .header("X-Authenticated-User-Id", username)
                    .header("X-Authenticated-User-Roles", String.join(",", rolesFromToken != null ? rolesFromToken : List.of()))
                    .build();

            logger.debug("Authentication object: {}", authentication);

            return chain.filter(exchange.mutate().request(modifiedRequest).build())
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));

        } catch (Exception e) {
            logger.error("Error processing JWT token in filter for path {}: {}", request.getURI().getPath(), e.getMessage(), e);
            return unauthorized(exchange, "Error processing token: " + e.getMessage());
        }
    }

    private boolean isPublicPath(ServerHttpRequest request) {
        String path = request.getURI().getPath();
        return publicPaths.stream().anyMatch(publicPath -> path.startsWith(publicPath));
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        // The CorsWebFilter should ideally add Access-Control-Allow-Origin for this error response too,
        // but the main goal is to let the OPTIONS request succeed first.
        logger.debug("Responding with UNAUTHORIZED from AuthenticationFilter: {}", message);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return -100; // Run before most other filters but after some very early ones like CORS if possible
    }
}
