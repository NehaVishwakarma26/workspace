package com.workspace.workspace.service;

import com.workspace.workspace.dao.EmployeeRepository;
import com.workspace.workspace.dto.EmployeeAdminUpdateRequest;
import com.workspace.workspace.dto.EmployeeSelfUpdateRequest;
import com.workspace.workspace.model.Department;
import com.workspace.workspace.model.Employee;
import com.workspace.workspace.model.Role;
import com.workspace.workspace.model.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Employee createEmployee(Employee employee) {

        if(employeeRepository.existsByEmail(employee.getEmail()))
            throw new RuntimeException("Email already exists");

        if(employeeRepository.existsByEmployeeId(employee.getEmployeeId()))
            throw new RuntimeException("Employee ID already exists");

        employee.setJoiningDate(LocalDate.now());

        employee.setStatus(Status.ACTIVE);

        employee.setPassword(passwordEncoder.encode(employee.getPassword()));

        employeeRepository.save(employee);

        employee.setEmployeeId("EMP"+employee.getId());

        employeeRepository.save(employee);

        return employee;
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id) {

        return employeeRepository.findById(id);
    }

    @Override
    public Employee getEmployeeByEmployeeId(String employeeId) {

        Employee employee=employeeRepository.findEmployeeByEmployeeId(employeeId)
                .orElseThrow(()->new RuntimeException("employee not found"));
        return employee;
    }

    @Override
    public Optional<Employee> getEmployeeByEmail(String email) {
        return employeeRepository.findEmployeeByEmail(email);
    }

    @Override
    public List<Employee> getAllEmployees(Department department, Role role) {

        if(department!=null && role!=null) {
           return employeeRepository.findEmployeesByRoleAndDepartment(role, department);
        }

        else if(department!=null) {
           return employeeRepository.findEmployeesByDepartment(department);
        }

        else if(role!=null) {
           return employeeRepository.findEmployeesByRole(role);
        }

        return employeeRepository.findAll();
    }

    @Override
    public void deactivateEmployee(String employeeId) {

        Employee employee=employeeRepository.findEmployeeByEmployeeId(employeeId)
                .orElseThrow(()->new RuntimeException("Employee not found"));

        System.out.println("Employee "+employeeId+" deactivated");

        employee.setStatus(Status.INACTIVE);

        employeeRepository.save(employee);

    }

    @Override
    public Employee updateOwnProfile(String email, EmployeeSelfUpdateRequest updateRequest) {

        Employee employee=employeeRepository.findEmployeeByEmail(email)
                .orElseThrow(()->new RuntimeException("Employee with this employee id not found."));

        employee.setFirstName(updateRequest.getFirstName());
        employee.setLastName(updateRequest.getLastName());
//        employee.setEmail(updateRequest.getEmail());

        return employeeRepository.save(employee);

    }

    @Override
    public Employee updateEmployeeByAdmin(String employeeId, EmployeeAdminUpdateRequest updateRequest) {
        Employee employee=employeeRepository.findEmployeeByEmployeeId(employeeId)
                .orElseThrow(()->new RuntimeException("Employee with this employee id not found."));

        employee.setFirstName(updateRequest.getFirstName());
        employee.setLastName(updateRequest.getLastName());
        employee.setEmail(updateRequest.getEmail());
        employee.setRole(updateRequest.getRole());
        employee.setDesignation(updateRequest.getDesignation());
        employee.setDepartment(updateRequest.getDepartment());

        return employeeRepository.save(employee);
    }
}
