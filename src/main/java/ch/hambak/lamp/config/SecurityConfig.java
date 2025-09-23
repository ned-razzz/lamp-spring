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
        // 비밀번호 암호화를 위한 BCryptPasswordEncoder를 Bean으로 등록합니다.
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // REST API 환경이므로 CSRF 보호 비활성화
                .csrf(AbstractHttpConfigurer::disable)

                // HTTP Basic 인증 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)

                // HTTP 요청에 대한 인가 규칙 설정
                .authorizeHttpRequests(auth -> auth
                        // "/admin/**" 경로는 "ADMIN" 역할을 가진 사용자만 접근 가능
                        .requestMatchers("/api/*/*/admin/**").hasRole("ADMIN")
                        // 그 외의 모든 요청은 인증 여부와 관계없이 모두 허용
                        .anyRequest().permitAll()
                )

                // 폼 기반 로그인 설정
                .formLogin(form -> form
                        // 로그인 요청을 처리할 URL (별도 컨트롤러 구현 필요 없음)
                        .loginProcessingUrl("/api/login")
                        // 로그인 성공 시 응답 핸들러
                        .successHandler((request, response, authentication) -> {
                            log.info("Login Successful. User: {}", authentication.getName());
                            response.setStatus(HttpStatus.OK.value());
                        })
                        // 로그인 실패 시 응답 핸들러
                        .failureHandler((request, response, exception) -> {
                            log.warn("Login Failed from {}: {}", request.getRemoteAddr(), exception.getMessage());
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        })
                )

                // 로그아웃 설정
                .logout(logout -> logout
                        .logoutUrl("/api/logout")
                        .logoutSuccessHandler(((request, response, authentication) -> {
                            String username = (authentication != null) ? authentication.getName() : "anonymous";
                            log.info("Logout Successful. User: {}", username); // 콘솔에 로그 출력
                            response.setStatus(HttpStatus.OK.value()); // 상태 코드만 전송
                        }))
                )

                // 인증 및 인가 예외 처리
                .exceptionHandling(e -> e
                        // 인증되지 않은 사용자가 보호된 리소스에 접근 시 처리
                        .authenticationEntryPoint((request, response, authException) -> {
                            log.warn("Unauthorized access attempt from {}: {}", request.getRemoteAddr(), authException.getMessage()); // 콘솔에 로그 출력
                            response.setStatus(HttpStatus.UNAUTHORIZED.value()); // 상태 코드만 전송
                        })
                        // 인증은 되었으나 권한이 없는 리소스에 접근 시 처리
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            String username = (request.getUserPrincipal() != null) ? request.getUserPrincipal().getName() : "anonymous";
                            log.warn("Access Denied for user {} to {}: {}", username, request.getRequestURI(), accessDeniedException.getMessage()); // 콘솔에 로그 출력
                            response.setStatus(HttpStatus.FORBIDDEN.value()); // 상태 코드만 전송
                        })
                );

        return http.build();
    }
}