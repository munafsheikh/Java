package com.virtrics.experiment.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfiguration {

    private static final String[] AUTH_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/actuator/**",
            "/authentication/**"
    };
    private final JwtAuthConverter jwtAuthConverter;

    /**
     * Configures Spring Security for the application.
     *
     * @param httpSecurity The HttpSecurity object to configure security settings.
     * @return The configured SecurityFilterChain.
     * @throws Exception If an error occurs during security configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        log.error("\n\n securityFilterChain > URI securityFilterChain \n--> ");

        httpSecurity
                .csrf()
                .disable()
                .authorizeHttpRequests(authorize -> authorize
                        // Allow access to health endpoint
                        //.requestMatchers(EndpointRequest.to(HealthEndpoint.class)).permitAll()
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .anyRequest().authenticated());

        log.info("\n\n securityFilterChain > URI oauth2ResourceServer \n--> ");

        httpSecurity
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(jwtAuthConverter);

        log.info("\n\n securityFilterChain > URI sessionManagement \n--> ");

        httpSecurity
                .sessionManagement()
                .sessionCreationPolicy(STATELESS);

        return httpSecurity.build();
    }
}
