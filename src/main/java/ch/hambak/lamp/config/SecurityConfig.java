package ch.hambak.lamp.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Register BCryptPasswordEncoder as a bean for password encryption.
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // Disable CSRF protection
                .csrf(AbstractHttpConfigurer::disable)

                // Disable HTTP Basic authentication
                .httpBasic(AbstractHttpConfigurer::disable)

                // Configure authorization rules for HTTP requests
                .authorizeHttpRequests(auth -> auth
                        // Only users with the "ADMIN" role can access "/api/*/*/admin/**"
                        .requestMatchers("/api/*/*/admin/**").hasRole("ADMIN")
                        // All other requests are permitted regardless of authentication
                        .anyRequest().permitAll()
                )

                // Configure form-based login
                .formLogin(form -> form
                        // URL to process the login request (no separate controller implementation needed)
                        .loginProcessingUrl("/api/login")
                        // Response handler for successful login
                        .successHandler((request, response, authentication) -> {
                            log.info("Login Successful. User: {}", authentication.getName());
                            response.setStatus(HttpStatus.OK.value());
                        })
                        // Response handler for failed login
                        .failureHandler((request, response, exception) -> {
                            log.warn("Login Failed from {}: {}", request.getRemoteAddr(), exception.getMessage());
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        })
                )

                // Configure logout
                .logout(logout -> logout
                        .logoutUrl("/api/logout")
                        .logoutSuccessHandler(((request, response, authentication) -> {
                            String username = (authentication != null) ? authentication.getName() : "anonymous";
                            log.info("Logout Successful. User: {}", username); // Log to console
                            response.setStatus(HttpStatus.OK.value()); // Send only the status code
                        }))
                )

                // Configure exception handling for authentication and authorization
                .exceptionHandling(e -> e
                        // Handles access to protected resources by unauthenticated users
                        .authenticationEntryPoint((request, response, authException) -> {
                            log.warn("Unauthorized access attempt from {}: {}", request.getRemoteAddr(), authException.getMessage()); // Log to console
                            response.setStatus(HttpStatus.UNAUTHORIZED.value()); // Send only the status code
                        })
                        // Handles access to resources by authenticated users without the required permissions
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            String username = (request.getUserPrincipal() != null) ? request.getUserPrincipal().getName() : "anonymous";
                            log.warn("Access Denied for user {} to {}: {}", username, request.getRequestURI(), accessDeniedException.getMessage()); // Log to console
                            response.setStatus(HttpStatus.FORBIDDEN.value()); // Send only the status code
                        })
                );

        return http.build();
    }
}