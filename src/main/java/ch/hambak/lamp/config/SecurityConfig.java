package ch.hambak.lamp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 모든 요청을 허용
        http.authorizeHttpRequests(authorize ->
                authorize.anyRequest().permitAll());

        // CSRF 보호 기능 비활성화
        http.csrf(AbstractHttpConfigurer::disable);

        // HTTP Basic 인증 끄기
        http.httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }
}