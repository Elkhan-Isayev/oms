package com.oms.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.server.WebFilter;

import com.oms.gateway.filter.AuthenticationFilter;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    // List of paths that should bypass security completely
    private static final String[] SWAGGER_WHITELIST = {
        "/swagger-ui.html", 
        "/swagger-ui/**", 
        "/v3/api-docs",
        "/v3/api-docs/**",
        "/api/swagger/**"
    };

    @Autowired
    private AuthenticationFilter authenticationFilter;

    /**
     * Primary security filter chain - This completely excludes Swagger paths
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .securityMatcher(exchange -> {
                    // Skip security for Swagger paths completely
                    ServerHttpRequest request = exchange.getRequest();
                    String path = request.getURI().getPath();
                    
                    // Check if path matches any pattern in our Swagger whitelist
                    for (String pattern : SWAGGER_WHITELIST) {
                        if (pattern.endsWith("/**")) {
                            String prefix = pattern.substring(0, pattern.length() - 3);
                            if (path.startsWith(prefix)) {
                                return org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher.MatchResult.notMatch();
                            }
                        } else if (path.equals(pattern)) {
                            return org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher.MatchResult.notMatch();
                        }
                    }
                    return org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher.MatchResult.match();
                })
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable())
                .authorizeExchange(exchanges -> exchanges
                        // Auth endpoints
                        .pathMatchers("/api/auth/login", "/api/auth/register", "/api/auth/refresh-token").permitAll()
                        // Public and health endpoints
                        .pathMatchers("/api/public/**").permitAll()
                        .pathMatchers("/actuator/**", "/actuator/health").permitAll()
                        // Require authentication for everything else
                        .anyExchange().authenticated()
                )
                .build();
    }
    
    /**
     * Separate open security filter chain specifically for Swagger paths
     */
    @Bean
    public SecurityWebFilterChain swaggerSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .securityMatcher(exchange -> {
                    // Only apply this chain to Swagger paths
                    ServerHttpRequest request = exchange.getRequest();
                    String path = request.getURI().getPath();
                    boolean isSwaggerPath = (path.equals("/swagger-ui.html") || 
                            path.startsWith("/swagger-ui/") || 
                            path.equals("/v3/api-docs") || 
                            path.startsWith("/v3/api-docs/") ||
                            path.startsWith("/api/swagger/"));
                    
                    return isSwaggerPath 
                        ? org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher.MatchResult.match()
                        : org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher.MatchResult.notMatch();
                })
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                    .anyExchange().permitAll()
                )
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable())
                .build();
    }
}