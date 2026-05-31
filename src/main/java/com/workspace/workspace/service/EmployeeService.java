package com.workspace.workspace.service;

import com.workspace.workspace.model.Department;
import com.workspace.workspace.model.Employee;
import com.workspace.workspace.model.Role;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

     Employee createEmployee(Employee employee);

     Optional<Employee> getEmployeeById(Long id);

     Employee getEmployeeByEmployeeId(String employeeId);

     Optional<Employee> getEmployeeByEmail(String email);

     List<Employee> getAllEmployees( Department department,Role role);

     Employee updateEmployee(String employeeId,Employee updatedEmployee);

     void deactivateEmployee(String employeeId);

}
