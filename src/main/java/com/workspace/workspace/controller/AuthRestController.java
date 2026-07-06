package com.workspace.workspace.controller;

import com.workspace.workspace.dto.LoginRequest;
import com.workspace.workspace.dto.LoginResponse;
import com.workspace.workspace.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthRestController(
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.authenticationManager=authenticationManager;
        this.jwtService=jwtService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {


        Authentication authentication=
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getEmail(),
                                loginRequest.getPassword()
                        )
                );

        UserDetails userDetails=(UserDetails) authentication.getPrincipal();

        String token=jwtService.generateToken(userDetails);

        return new LoginResponse(token);
    }


}
