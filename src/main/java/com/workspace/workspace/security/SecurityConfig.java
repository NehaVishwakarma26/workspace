package com.workspace.workspace.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private final CustomAuthenticationSuccessHandler successHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            CustomAuthenticationSuccessHandler successHandler,
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) {
        this.successHandler=successHandler;
        this.jwtAuthenticationFilter=jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrf->csrf.ignoringRequestMatchers("/api/**"));
        httpSecurity.authorizeHttpRequests(auth->
                auth
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/**").hasAnyRole("EMPLOYEE","ADMIN","SUPER_ADMIN")
                        .requestMatchers("/login").permitAll()
                        .requestMatchers(
                                "/employee/new",
                                "/employee/saveEmployee",
                                "/employee/saveEmployee",
                                "/employee/update",
                                "/employee/saveUpdatedEmployee/**",
                                "/employee/deactivate/**",
                                "/employee/list",
                                "/employee/detail/**",
                                "/team/new",
                                "/team/saveTeam",
                                "/team/list"
                        ).hasAnyRole("ADMIN","SUPER_ADMIN")
                        .requestMatchers("/employee/me","/employee/update","/employee/saveUpdatedEmployee/**")
                        .hasAnyRole("EMPLOYEE","ADMIN","SUPER_ADMIN")
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
        httpSecurity.addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
        );
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
