package com.workspace.workspace.service;

import com.workspace.workspace.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

     Employee createEmployee(Employee employee);

     Optional<Employee> getEmployeeById(Long id);

     Optional<Employee> getEmployeeByEmployeeId(String employeeId);

     Optional<Employee> getEmployeeByEmail(String email);

     List<Employee> getAllEmployees();

     Employee updateEmployee(String employeeId,Employee updatedEmployee);

     void deactivateEmployee(String employeeId);

}
