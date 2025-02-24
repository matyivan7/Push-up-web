package com.project.pushup.security;

import com.project.pushup.filter.JsonUsernamePasswordAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig{

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        JsonUsernamePasswordAuthenticationFilter jsonFilter = new JsonUsernamePasswordAuthenticationFilter();
        jsonFilter.setAuthenticationManager(http.getSharedObject(org.springframework.security.authentication.AuthenticationManager.class));
        jsonFilter.setFilterProcessesUrl("/push-up/login");

        http
            .csrf().disable()
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/push-up/register", "/push-up/login").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin().disable()
            .addFilterAt(jsonFilter, UsernamePasswordAuthenticationFilter.class)
            .logout(LogoutConfigurer::permitAll)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
