package com.yrs.mercaditolibre.security;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {
    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(JwtTokenProvider tokenProvider, CustomUserDetailsService customUserDetailsService) {
        this.tokenProvider = tokenProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(tokenProvider, customUserDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // 1. PERMITIR TODAS LAS PETICIONES PREFLIGHT (OPTIONS)
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // 2. ENDPOINTS PÚBLICOS (Incluimos explícitamente las rutas de lectura)
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/productos", "/api/v1/productos/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/categorias", "/api/v1/categorias/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/proveedores", "/api/v1/proveedores/**").permitAll()

                // 3. RUTAS PROTEGIDAS PARA ADMINISTRADOR
                .requestMatchers(HttpMethod.POST, "/api/v1/productos/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/productos/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/productos/**").hasAuthority("ROLE_ADMIN")

                .requestMatchers(HttpMethod.POST, "/api/v1/categorias/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/categorias/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/categorias/**").hasAuthority("ROLE_ADMIN")

                .requestMatchers(HttpMethod.POST, "/api/v1/proveedores/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/proveedores/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/proveedores/**").hasAuthority("ROLE_ADMIN")

                // 3.1 GESTIÓN DE USUARIOS (crear admins/clientes desde el panel) — solo Admin
                .requestMatchers("/api/v1/usuarios/**").hasAuthority("ROLE_ADMIN")

                // 3.2 GESTIÓN DE CLIENTES (listar/crear/editar/eliminar) — solo Admin
                .requestMatchers(HttpMethod.GET, "/api/v1/clientes/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/clientes/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/clientes/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/clientes/**").hasAuthority("ROLE_ADMIN")

                // 4. VENTAS Y PAGOS
                .requestMatchers(HttpMethod.POST, "/api/v1/ventas", "/api/v1/ventas/**").hasAnyAuthority("ROLE_CLIENTE", "ROLE_ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/v1/ventas").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/v1/ventas/mis-compras").hasAuthority("ROLE_CLIENTE")
                .requestMatchers("/api/v1/pagos/**").hasAnyAuthority("ROLE_CLIENTE", "ROLE_ADMIN")

                .anyRequest().authenticated()
            );

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Habilitamos patrones de orígenes para entorno local y Coolify
        configuration.setAllowedOriginPatterns(Arrays.asList(
            "http://localhost:5173",
            "http://mercaditoy.2.24.105.6.sslip.io",
            "http://*.sslip.io"
        ));

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        configuration.setAllowedHeaders(Arrays.asList("*")); // Permite Authorization, Content-Type, etc.
        configuration.setExposedHeaders(Collections.singletonList("Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}