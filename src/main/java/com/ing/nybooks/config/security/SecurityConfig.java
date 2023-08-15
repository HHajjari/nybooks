package com.ing.nybooks.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing.nybooks.config.properties.SecurityProperties;
import com.ing.nybooks.config.exception.ErrorResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.PrintWriter;

import static com.ing.nybooks.model.Const.ME_BOOKS_LIST;

/**
 * Security configuration class for configuring authentication and authorization.
 */
@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private SecurityProperties securityProperties;

    /**
     * Configures HTTP security settings.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .disable()
                .authorizeRequests()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/v3/api-docs/**").permitAll()
                .antMatchers("/v3/api-docs.yaml").permitAll()
                .antMatchers("/actuator/**").authenticated()
                .antMatchers("/actuator/**").hasAnyRole("admin")
                .antMatchers(ME_BOOKS_LIST).authenticated() // Secure ME_BOOKS_LIST endpoint
                .antMatchers(ME_BOOKS_LIST).hasAnyRole("operator1", "admin")
                .anyRequest().authenticated() // All other requests require authentication
                .and()
                .httpBasic() // Use HTTP Basic authentication
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless session management
                .and()
                .headers() // Configure headers for security
                .addHeaderWriter((request, response) -> {
                    // Prevents content type sniffing by browsers
                    response.setHeader("X-Content-Type-Options", "nosniff");

                    // Enforces HTTPS and secure connections
                    response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");

                    // Prevents content from being framed or iframed by other websites
                    response.setHeader("X-Frame-Options", "DENY");

                    // Enables XSS (cross-site scripting) protection in browsers
                    response.setHeader("X-XSS-Protection", "1; mode=block");

                    // Specifies a basic content security policy to prevent XSS and other attacks
                    response.setHeader("Content-Security-Policy", "default-src 'self'");

                    // Disables caching to prevent sensitive data from being cached
                    response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");

                    // Prevents caching in older browsers
                    response.setHeader("Pragma", "no-cache");
                })
                .and()
                .exceptionHandling() // Handle authentication errors
                .authenticationEntryPoint((request, response, authException) -> {
                    ErrorResponse errorResponse = new ErrorResponse("Invalid credential", "You don't have proper credential to access this resource");
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setContentType("application/json");
                    PrintWriter out = response.getWriter();
                    out.print(new ObjectMapper().writeValueAsString(errorResponse));
                    out.flush();
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    ErrorResponse errorResponse = new ErrorResponse("Access Denied", "You do not have permission to access this resource.");
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.setContentType("application/json");
                    PrintWriter out = response.getWriter();
                    out.print(new ObjectMapper().writeValueAsString(errorResponse));
                    out.flush();
                });
    }

    /**
     * Configures authentication manager to use in-memory user details.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(username -> {
            SecurityProperties.UserDetails userDetails = securityProperties.getCredentials().stream()
                    .filter(user -> user.getUsername().equals(username))
                    .findFirst()
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            return User.withUsername(userDetails.getUsername())
                    .password(passwordEncoder().encode(userDetails.getPassword()))
                    .roles(userDetails.getRoles().split(","))
                    .build();
        });
    }

    /**
     * Creates a NoOpPasswordEncoder bean for secure password storage.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
