package com.khac_dat.identity_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class WebConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // Cho phép gửi Cookie/Token
        config.setAllowCredentials(true);

        // Chỉ đích danh domain của FE (Vite React)
        config.addAllowedOrigin("http://localhost:5173");

        // Cho phép tất cả các Headers (Authorization, Content-Type...)
        config.addAllowedHeader("*");

        // Cho phép tất cả các phương thức (GET, POST, PUT, DELETE, OPTIONS)
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Áp dụng cấu hình này cho mọi API (/**)
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}