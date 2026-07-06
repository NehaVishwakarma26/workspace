package com.workspace.workspace.security;

import com.workspace.workspace.dao.EmployeeRepository;
import com.workspace.workspace.model.Employee;
import com.workspace.workspace.model.Status;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    public CustomUserDetailsService(EmployeeRepository employeeRepository) {
        this.employeeRepository=employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Employee employee=employeeRepository.findEmployeeByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("Employee with this email not found"));

        return User.withUsername(employee.getEmail())
                .password(employee.getPassword())
                .roles(employee.getRole().name())
                .disabled(employee.getStatus()== Status.INACTIVE)
                .build();
    }
}
