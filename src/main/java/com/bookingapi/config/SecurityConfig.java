package com.bookingapi.config;

import com.bookingapi.security.JwtAuthenticationFilter;
import com.bookingapi.security.JwtTokenUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtTokenUtils jwtTokenUtils;

    public SecurityConfig(JwtTokenUtils jwtTokenUtils) {
        this.jwtTokenUtils = jwtTokenUtils;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll() // Swagger público
                .requestMatchers("/login").permitAll() // Login público
                .requestMatchers("/api/**").authenticated() // Protegido con JWT
                .requestMatchers("/api/users/**").hasRole("ADMIN") // Solo ADMIN puede acceder a los usuarios
                .requestMatchers("/api/bookings/**").hasAnyRole("USER", "ADMIN") // Usuarios y ADMIN pueden acceder a las reservas
                .anyRequest().permitAll() // El resto público
            )
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenUtils), UsernamePasswordAuthenticationFilter.class) // Ahora inyectamos JwtTokenUtils
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
