package daisy.community_be.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.disable())    // CORS 비활성화
                .csrf(csrf -> csrf.disable())    // CSRF 비활성화
                .authorizeHttpRequests(auth -> auth
                        // 모든 요청을 인증 없이 허용
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form.disable())       // Form Login 비활성화
                .httpBasic(httpBasic -> httpBasic.disable()); // HTTP Basic 비활성화

        return http.build();
    }
}