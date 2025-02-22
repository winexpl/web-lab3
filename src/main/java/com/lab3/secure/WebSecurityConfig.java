package com.lab3.secure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;



@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {

    @Value("${security.logins}")
    private String[] logins;

    @Value("${security.passwords}")
    private String[] passwords;

    @Value("${security.roles}")
    private String[] roles;
    
    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // отключение защиты от csrf
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/", "/login").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/api/projects", true)
                .permitAll())
            .logout((logout) -> logout.permitAll());
        return http.build();
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager userDetailsService = new InMemoryUserDetailsManager();
        for (int i = 0; i < logins.length; i++) {
            UserDetails user = User.builder()
                .username(logins[i])
                .password(passwordEncoder().encode(passwords[i]))
                .roles(roles[i])
                .build();
            userDetailsService.createUser(user);
        }
        return userDetailsService;
    }
}