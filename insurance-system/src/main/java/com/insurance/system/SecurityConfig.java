package com.insurance.system;
import com.insurance.system.security.JwtAuthenticationFilter;
import com.insurance.system.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.List;
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationFilter jwtFilter;
    
    @Autowired
    private CustomUserDetailsService userDetailsService;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .cors().configurationSource(corsConfigurationSource())
            .and()
            .authorizeHttpRequests()
                .requestMatchers("/api/auth/**").permitAll()
                // Policy viewing should be accessible without authentication
                .requestMatchers(HttpMethod.GET, "/api/policies", "/api/policies/**").permitAll()
                // Explicitly allow POST/PUT/DELETE for policies only for ADMIN role
                .requestMatchers(HttpMethod.POST, "/api/policies").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/policies/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/policies/**").hasRole("ADMIN")
                // Allow all authenticated users to purchase policies and access their data
                .requestMatchers("/api/user-policies/purchase").authenticated()
                .requestMatchers("/api/user-policies/user/**").authenticated()
                // Admin only
                .requestMatchers("/api/user-policies/all").hasRole("ADMIN")
                .requestMatchers("/api/users").hasRole("ADMIN")
                .requestMatchers("/api/users/{id}").hasRole("ADMIN")
                .requestMatchers("/api/users/me").authenticated()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/agent/**").hasAnyRole("AGENT", "ADMIN")
                .requestMatchers("/api/customer/**").hasAnyRole("CUSTOMER", "ADMIN")
                // Claims endpoints
                .requestMatchers("/api/claims/submit").authenticated()
                .requestMatchers("/api/claims/user/**").authenticated()
                .requestMatchers("/api/claims/all").hasAnyRole("ADMIN", "AGENT")
                .requestMatchers("/api/claims/{claimId}/status").hasAnyRole("ADMIN", "AGENT")
                .anyRequest().authenticated()
            .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Cache-Control"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}