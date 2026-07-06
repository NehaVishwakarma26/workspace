package com.workspace.workspace.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private final CustomAuthenticationSuccessHandler successHandler;

    public SecurityConfig(
            CustomAuthenticationSuccessHandler successHandler
    ) {
        this.successHandler=successHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(auth->
                auth
                        .requestMatchers("/login","/employee/new","/employee/saveEmployee").permitAll()
                        .requestMatchers(
                                "/employee/new",
                                "/employee/saveEmployee",
                                "/employee/saveEmployee",
                                "/employee/update",
                                "/employee/saveUpdatedEmployee/**",
                                "/employee/deactivate/**",
                                "/employee/list",
                                "/employee/detail/**"
                        ).hasRole("ADMIN")
                        .requestMatchers("/employee/me","/employee/update","/employee/saveUpdatedEmployee/**")
                        .hasAnyRole("EMPLOYEE","ADMIN")
                        .anyRequest().authenticated())
                ;

        httpSecurity.formLogin(form->
                form
                        .loginPage("/login")
                        .successHandler(successHandler)
                        .failureUrl("/login?error")
                        .permitAll());

        httpSecurity.logout(
                logout->logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
        );

        return httpSecurity.build();
    }
}
