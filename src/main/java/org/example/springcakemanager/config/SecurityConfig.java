package org.example.springcakemanager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${app.security.admin.username}")
    private String adminUsername;

    @Value("${app.security.admin.password}")
    private String adminPassword;

    @Value("${app.security.user.username}")
    private String userUsername;

    @Value("${app.security.user.password}")
    private String userPassword;

    /**
     * Configures an in-memory user store for authentication.
     * Two users are defined: an admin and a regular user, with roles ADMIN and USER respectively.
     *
     * @return UserDetailsService managing the in-memory users
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
                User.withUsername(adminUsername)
                        .password("{noop}" + adminPassword) // {noop} indicates no password encoding
                        .roles("ADMIN") // Assign ADMIN role to the user
                        .build(),
                User.withUsername(userUsername)
                        .password("{noop}" + userPassword)
                        .roles("USER") // Assign USER role to the user
                        .build()
        );
    }

    /**
     * Configures the security filter chain that defines HTTP security settings such as CORS, CSRF, session management, and route-level access control.
     *
     * @param http HttpSecurity to be configured
     * @return SecurityFilterChain configured with the defined security settings
     * @throws Exception in case of any configuration error
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disables CSRF protection since it's unnecessary for stateless APIs
                .csrf(csrf -> csrf.disable())
                // Configures Cross-Origin Resource Sharing (CORS) with bean defined bellow
                .cors(Customizer.withDefaults())
                // Configures stateless session management, which prevents the creation of HTTP sessions
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configures HTTP headers for enhanced security
                .headers(headers -> headers
                        .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'; script-src 'self'; object-src 'none'"))
                        .frameOptions(frame -> frame.sameOrigin())
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/v1/cakes").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/v1/cakes/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/actuator/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                // Enables Basic Authentication for all authenticated requests
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    /**
     * Configures a CORS policy for the application.
     * This policy allows requests from specific origins and allows certain HTTP methods and headers.
     *
     * @return CorsConfigurationSource for defining the CORS policy
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
