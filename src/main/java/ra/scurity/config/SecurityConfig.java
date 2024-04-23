package ra.scurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import ra.scurity.constants.RoleName;
import ra.scurity.securrity.jwt.AccessDenied;
import ra.scurity.securrity.jwt.JwtEntrypoint;
import ra.scurity.securrity.user_principal.UserDetailServiceCustom;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private JwtEntrypoint jwtEntrypoint;
    @Autowired
    private AccessDenied accessDenied;
    @Autowired
    private UserDetailServiceCustom userDetailServiceCustom;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // Cấu hình Cross-Origin Resource Sharing (CORS) để cho phép truy cập từ các nguồn khác nhau
                .cors(auth -> auth.configurationSource(request -> {
                    // Tạo một cấu hình CORS mới
                    CorsConfiguration configuration = new CorsConfiguration();
                    // Cho phép truy cập từ nguồn http://localhost:8080/
                    configuration.setAllowedOrigins(List.of("http://localhost:8080/"));
                    // Cho phép tất cả các header
                    configuration.setAllowedHeaders(List.of("*"));
                    // Cho phép sử dụng các thông tin xác thực (credentials) trong CORS request
                    configuration.setAllowCredentials(true);
                    // Cho phép sử dụng tất cả các phương thức HTTP
                    configuration.setAllowedMethods(List.of("*"));
                    // Expose header "Authorization" để có thể truy cập từ client-side
                    configuration.setExposedHeaders(List.of("Authorization"));
                    // Trả về cấu hình CORS đã được thiết lập
                    return configuration;
                }))
                // Tắt Cross-Site Request Forgery (CSRF) protection vì chúng ta không sử dụng nó
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/admin/**").hasAnyAuthority(String.valueOf(RoleName.ROLE_ADMIN))
                        .requestMatchers("/user/**").hasAnyAuthority(String.valueOf(RoleName.ROLE_USER))
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtEntrypoint)
                        .accessDeniedHandler(accessDenied)
                )
                .authenticationProvider(authenticationProvider())
                .build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailServiceCustom);
        return provider;
    }
}
