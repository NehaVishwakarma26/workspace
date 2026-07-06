package com.workspace.workspace.service;

import com.workspace.workspace.dto.EmployeeAdminUpdateRequest;
import com.workspace.workspace.dto.EmployeeSelfUpdateRequest;
import com.workspace.workspace.model.Department;
import com.workspace.workspace.model.Employee;
import com.workspace.workspace.model.Role;
import jakarta.validation.constraints.Pattern;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

     Employee createEmployee(Employee employee, String creatorEmail);

     Optional<Employee> getEmployeeById(Long id);

     Employee getEmployeeByEmployeeId(String employeeId);

     Optional<Employee> getEmployeeByEmail(String email);

     List<Employee> getAllEmployees( Department department,Role role);

     void deactivateEmployee(String employeeId);

     Employee updateOwnProfile(String email, EmployeeSelfUpdateRequest updateRequest);

     Employee updateEmployeeByAdmin(String employeeId,String updaterEmail, EmployeeAdminUpdateRequest updateRequest);
}
