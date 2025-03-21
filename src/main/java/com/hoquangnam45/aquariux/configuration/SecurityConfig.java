package com.hoquangnam45.aquariux.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoquangnam45.aquariux.component.SecurityFilter;
import com.hoquangnam45.aquariux.pojo.ExceptionResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;

import java.time.Instant;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            SecurityFilter securityFilter,
            ObjectMapper objectMapper)
            throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        config -> config.requestMatchers("/market/**").permitAll().anyRequest().authenticated())
                .addFilterAfter(securityFilter, ExceptionTranslationFilter.class)
                .exceptionHandling(
                        config -> {
                            config.accessDeniedHandler(
                                    (request, response, e) -> {
                                        response.setStatus(HttpStatus.FORBIDDEN.value());
                                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                                        objectMapper.writeValue(
                                                response.getWriter(),
                                                new ExceptionResponse(
                                                        String.valueOf(HttpStatus.FORBIDDEN.value()),
                                                        request.getServletPath(),
                                                        e.getMessage(), null, Instant.now()));
                                    });
                            config.authenticationEntryPoint(
                                    (request, response, e) -> {
                                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                                        objectMapper.writeValue(
                                                response.getWriter(),
                                                new ExceptionResponse(
                                                        String.valueOf(HttpStatus.UNAUTHORIZED.value()),
                                                        request.getServletPath(),
                                                        e.getMessage(), null, Instant.now()));
                                    });
                        })
                .build();
    }
}
