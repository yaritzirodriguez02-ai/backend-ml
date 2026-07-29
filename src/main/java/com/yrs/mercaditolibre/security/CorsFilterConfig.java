package com.yrs.mercaditolibre.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsFilterConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE) // Se ejecuta ANTES que Spring Security
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        // Permite credenciales y los dominios necesarios
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(Arrays.asList(
            "http://localhost:5173",
            "http://mercaditoy.2.24.105.6.sslip.io",
            "http://*.sslip.io"
        ));
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}