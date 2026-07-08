package com.workspace.workspace.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        boolean isSuperAdmin=authentication
                .getAuthorities()
                .stream()
                .anyMatch(authority->
                        authority.getAuthority().equals("ROLE_SUPER_ADMIN"));

        boolean isAdmin=authentication
                .getAuthorities()
                .stream()
                .anyMatch(authority->
                        authority.getAuthority().equals("ROLE_ADMIN"));

        if(isSuperAdmin || isAdmin) {
            System.out.println("SUccess login");
            response.sendRedirect("/employee/list");
        }
        else {
            System.out.println("SUccess login");
            response.sendRedirect("/employee/me");
        }

    }
}
