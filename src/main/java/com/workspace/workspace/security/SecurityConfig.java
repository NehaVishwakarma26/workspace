package com.workspace.workspace.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {

        UserDetails admin= User
                .withUsername("admin")
                .password("{noop}admin123")
                .roles("ADMIN","EMPLOYEE")
                .build();

        UserDetails employee=User
                .withUsername("employee123")
                .password("{noop}employee123")
                .roles("EMPLOYEE")
                .build();

        return new InMemoryUserDetailsManager(admin,employee);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(auth->
                auth
                        .requestMatchers(
                                "/employee/new",
                                "/employee/saveEmployee",
                                "/employee/update/**",
                                "/employee/saveUpdatedEmployee/**",
                                "/employee/deactivate/**"
                        ).hasRole("ADMIN")
                        .anyRequest().authenticated());

        httpSecurity.formLogin(form->form.permitAll());

        return httpSecurity.build();
    }
}
