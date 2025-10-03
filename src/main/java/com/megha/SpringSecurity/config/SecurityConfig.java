package com.megha.SpringSecurity.config;

import com.megha.SpringSecurity.service.StudentDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity  // Enables Spring Security and allows custom security configurations to override defaults

public class SecurityConfig {

    @Autowired
    private StudentDetailsService userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;

    // This method returns a SecurityFilterChain bean that defines security behavior
    @Bean //indicates that this method returns an object that should be managed as a Spring bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                // Disable CSRF protection (generally disabled for APIs, especially if using tokens like JWT)
                .csrf(customizer -> customizer.disable())

                // Require authentication for **every request**
                .authorizeHttpRequests(request -> request
                        .requestMatchers("register", "login")
                        .permitAll().anyRequest().authenticated())

                // Enable default login form (for browser-based login)
                //.formLogin(Customizer.withDefaults())

                // Enable HTTP Basic authentication (sends username & password in headers, base64 encoded)
                .httpBasic(Customizer.withDefaults())

                // Make session management stateless — no session will be stored on the server
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

        // No custom rules — this allows all requests, no authentication required

    }

    //    @Bean
//    // to customize the UsernamePasswordAuthenticationFilter filter
//    public UserDetailsService userDetailsService() {
//        UserDetails user1 = User.withDefaultPasswordEncoder()
//                .username("Rishika").password("abcd").roles("ADMIN").build();
//        UserDetails user2 = User.withDefaultPasswordEncoder()
//                .username("Shoumick").password("abcd").roles("USER").build();
//        return new InMemoryUserDetailsManager(user1, user2);
//    }

    /* The authenticationProvider() method creates and returns a bean of type AuthenticationProvider.
        This bean tells Spring Security:
        How to load user details (from database, memory, or another service)
        How to check passwords (with a password encoder)
        How to decide if login is valid or not
        When a user tries to log in, Spring calls this AuthenticationProvider to validate the username and password.*/
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();

    }

}
