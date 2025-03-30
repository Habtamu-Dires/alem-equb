package com.ekub.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.Timeout;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


@Configuration
@RequiredArgsConstructor
public class BeanConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}") String jwkSetUri;
    private final AllowedOriginsConfig allowedOriginsConfig;

    // cors filter
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(allowedOriginsConfig.getOrigins());
        config.setAllowCredentials(true);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of(
                HttpHeaders.ORIGIN, HttpHeaders.CONTENT_TYPE,
                HttpHeaders.ACCEPT, HttpHeaders.AUTHORIZATION
        ));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    // auditor aware
    @Bean
    public AuditorAware<String> auditorAware() {
        return new ApplicationAuditAware();
    }


//    // rest template with connection time out
//    @Bean
//    public RestTemplate restTemplate(RestTemplateBuilder builder) {
//        return builder
//                .connectTimeout(Duration.ofSeconds(5)) // 5 second
//                .readTimeout(Duration.ofSeconds(5))  // 5 second
//                .build();
//    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(50);
        connectionManager.setDefaultMaxPerRoute(20);

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.ofSeconds(5)) // Time to get connection from pool
                .setConnectTimeout(Timeout.ofSeconds(5))          // Time to establish connection
                .setResponseTimeout(Timeout.ofSeconds(5))         // Time to receive response
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();

        return new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
    }

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("jwksCache");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(1) // Store only one JWKS key set
                .expireAfterWrite(12, TimeUnit.HOURS)); // Expire after 12 hours
        return cacheManager;
    }


    // jwks call to keycloak using custom rest template
    @Bean
    public JwtDecoder jwtDecoder(RestTemplate restTemplate, CacheManager cacheManager) {
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri)
                .restOperations(restTemplate) // Use the timeout-enabled RestTemplate
                .cache(Objects.requireNonNull(cacheManager.getCache("jwksCache"),
                        "jwksCache not found!")
                )
                .build();
    }


}
