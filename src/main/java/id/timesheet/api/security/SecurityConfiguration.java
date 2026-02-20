package id.timesheet.api.security;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthEntryPoint customAuthEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(cors -> cors.configurationSource(corsFilter()))
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(config -> {
                    config.accessDeniedHandler(customAccessDeniedHandler);
                    config.authenticationEntryPoint(customAuthEntryPoint);
                })
                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req ->
                                req.dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/assets/**", "/api/v1/employee").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/department/**").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/department/**").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/employee/**").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/employee/**").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/role/**").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/role/**").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/status/**").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/status/**").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/user/**").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/user/**").permitAll()
                                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    @Bean
    public CorsConfigurationSource corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:5175");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
